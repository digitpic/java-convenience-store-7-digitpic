package store.model.order;

import camp.nextstep.edu.missionutils.DateTimes;
import store.model.Membership;
import store.model.product.Product;
import store.model.product.Products;
import store.model.promotion.Promotion;
import store.model.promotion.Promotions;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class Orders {
    private final List<Order> orders;

    public Orders(final String rawOrders) {
        this.orders = parseToOrders(rawOrders);
    }

    public int calculateOriginalPrice(final Products products, final Promotions promotions) {
        int total = 0;
        LocalDateTime now = DateTimes.now();
        for (Order order : orders) {
            Product product = products.findByName(order.getName());
            Promotion promotion = promotions.findByName(product.getPromotion());
            total += product.getPrice() * order.getQuantity();
            if (promotion != null && order.more && now.isAfter(promotion.getStartDate()) && now.isBefore(promotion.getEndDate())
                    && order.getQuantity() % (promotion.getBuyCount() + promotion.getGetCount()) != 0
                    && order.getQuantity() > (promotion.getBuyCount() + promotion.getGetCount())) {
                total += product.getPrice() * promotion.getGetCount();
            }
        }
        return total;
    }

    public int calculatePromotionDiscount(final Products products, final Promotions promotions) {
        int discount = 0;
        for (Order order : orders) {
            Product product = products.findByName(order.getName());
            Promotion promotion = promotions.findByName(product.getPromotion());
            LocalDateTime now = DateTimes.now();
            if (promotion != null && order.more && now.isAfter(promotion.getStartDate()) && now.isBefore(promotion.getEndDate())) {
                int repeat = order.getQuantity() / (promotion.getBuyCount() + promotion.getGetCount());
                for (int i = 0; i < repeat; i++) {
                    discount += product.getPrice() * promotion.getGetCount();
                }
            }
            if (promotion == null) {
                discount += 0;
            }
        }
        return discount;
    }

    public int calculateMembershipDiscount(final Products products, final Promotions promotions, final Membership membership) {
        int total = 0;
        for (Order order : orders) {
            Product product = products.findByName(order.getName());
            Promotion promotion = promotions.findByName(product.getPromotion());
            if (promotion == null) {
                total += product.getPrice() * order.getQuantity();
            }
        }
        int discount = 0;
        if (membership.isMember()) {
            discount = (int) (total * 0.3);
        }
        if (discount > 8000) {
            discount = 8000;
        }
        return discount;
    }

    public String makeReceipt(final Products products, final Promotions promotions, final Membership membership) {
        int originalPrice = calculateOriginalPrice(products, promotions);
        int promotionDiscount = calculatePromotionDiscount(products, promotions);
        int totalCount = 0;
        for (Order order : orders) {
            totalCount += order.getQuantity();
            Product product = products.findByName(order.getName());
            Promotion promotion = promotions.findByName(product.getPromotion());
            if (promotion != null && order.more && order.getQuantity() % (promotion.getBuyCount() + promotion.getGetCount()) != 0
                    && order.getQuantity() > (promotion.getBuyCount() + promotion.getGetCount())) {
                totalCount += promotion.getGetCount();
            }
            if  (order.more) {
                totalCount += 1;
            }
        }
        int membershipDiscount = 0;
        if (membership.isMember()) {
            membershipDiscount = calculateMembershipDiscount(products, promotions, membership);
        }
        int payment = originalPrice - promotionDiscount - membershipDiscount;

        return makeString(products, promotions, originalPrice, totalCount, promotionDiscount, membershipDiscount, payment);
    }

    private String makeString(final Products products, final Promotions promotions, final int originalPrice, final int totalCount, final int promotionDiscount, final int membershipDiscount, final int payment) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("==============W 편의점================\n");
        stringBuilder.append("상품명\t\t수량\t금액\n");
        for (Order order : orders) {
            Product product = products.findByName(order.getName());
            Promotion promotion = promotions.findByName(product.getPromotion());
            if (promotion != null && order.more && order.getQuantity() % (promotion.getBuyCount() + promotion.getGetCount()) != 0
                    && order.getQuantity() > (promotion.getBuyCount() + promotion.getGetCount())) {
                stringBuilder.append(String.format("%s\t\t%d\t%,d\n", order.getName(), order.getQuantity() + promotion.getGetCount(),
                        (order.getQuantity() + promotion.getGetCount()) * product.getPrice()));
            }
            if (promotion != null && order.more && order.getQuantity() % (promotion.getBuyCount() + promotion.getGetCount()) != 0
                    && order.getQuantity() < (promotion.getBuyCount() + promotion.getGetCount())) {
                if (promotion.getBuyCount() == order.getQuantity()) {
                    stringBuilder.append(String.format("%s\t\t%d\t%,d\n", order.getName(), order.getQuantity() + promotion.getGetCount(),
                            (order.getQuantity() + promotion.getGetCount()) * product.getPrice()));
                }
                if (promotion.getBuyCount() != order.getQuantity()) {
                    stringBuilder.append(String.format("%s\t\t%d\t%,d\n", order.getName(), order.getQuantity(), order.getQuantity() * product.getPrice()));
                }
            }
            if (promotion != null && order.more && order.getQuantity() % (promotion.getBuyCount() + promotion.getGetCount()) == 0) {
                stringBuilder.append(String.format("%s\t\t%d\t%,d\n", order.getName(), order.getQuantity(),
                        order.getQuantity() * product.getPrice()));
            }
            if (promotion != null && !order.more) {
                stringBuilder.append(String.format("%s\t\t%d\t%,d\n", order.getName(), order.getQuantity(),
                        order.getQuantity() * product.getPrice()));
            }
            if (promotion == null) {
                stringBuilder.append(String.format("%s\t\t%d\t%,d\n", order.getName(), order.getQuantity(),
                        order.getQuantity() * product.getPrice()));
            }
        }
        stringBuilder.append("=============증\t정===============\n");
        for (Order order : orders) {
            Product product = products.findByName(order.getName());
            Promotion promotion = promotions.findByName(product.getPromotion());
            if (promotion != null && order.more && order.getQuantity() % (promotion.getBuyCount() + promotion.getGetCount()) != 0
                    && order.getQuantity() < (promotion.getBuyCount() + promotion.getGetCount())) {
                if (promotion.getBuyCount() == order.getQuantity()) {
                    int repeat = order.getQuantity() / promotion.getBuyCount();
                    stringBuilder.append(order.getName()).append("\t\t").append(promotion.getGetCount() * repeat)
                            .append("\t").append("\n");
                }
            }
            if (promotion != null && order.more && order.getQuantity() % (promotion.getBuyCount() + promotion.getGetCount()) == 0) {
                int repeat = order.getQuantity() / (promotion.getBuyCount() + promotion.getGetCount());
                stringBuilder.append(order.getName()).append("\t\t").append(repeat)
                        .append("\t").append("\n");
            }
        }
        stringBuilder.append("====================================\n");
        stringBuilder.append(String.format("총구매액\t\t%d\t%,d\n", totalCount, originalPrice));
        stringBuilder.append(String.format("행사할인\t\t \t%,d\n", -promotionDiscount));
        stringBuilder.append(String.format("멤버십할인\t\t \t%,d\n", -membershipDiscount));
        stringBuilder.append(String.format("내실돈\t\t \t%,d", payment));
        return stringBuilder.toString();
    }

    public List<Order> getOrders() {
        return Collections.unmodifiableList(orders);
    }

    private List<Order> parseToOrders(final String rawOrders) {
        List<Order> orders = new ArrayList<>();
        List<String> separated = Arrays.stream(rawOrders.split(","))
                .toList();
        for (String order : separated) {
            orders.add(new Order(order));
        }
        return orders;
    }
}
