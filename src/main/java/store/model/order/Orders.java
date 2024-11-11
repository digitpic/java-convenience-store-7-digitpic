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
    public static final int MAX_MEMBERSHIP_DISCOUNT = 8000;
    public static final double MEMBERSHIP_DISCOUNT_RATE = 0.3;
    private final List<Order> orders;

    public Orders(final String rawOrders) {
        this.orders = parseToOrders(rawOrders);
    }

    public int calculateTotalPrice(final Products products, final Promotions promotions) {
        int total = 0;
        LocalDateTime now = DateTimes.now();
        for (Order order : orders) {
            Product product = products.findByName(order.getName());
            Promotion promotion = promotions.findByName(product.getPromotion());
            total += product.getPrice() * order.getQuantity();
            if (promotion != null && now.isAfter(promotion.getStartDate()) && now.isBefore(promotion.getEndDate())) {
                if (order.more && order.getQuantity() >= promotion.getBuyCount()) {
                    total += product.getPrice() * promotion.getGetCount();
                }
            }
        }
        return total;
    }

    public int calculatePromotionDiscount(final Products products, final Promotions promotions) {
        int discount = 0;
        LocalDateTime now = DateTimes.now();
        for (Order order : orders) {
            Product product = products.findByName(order.getName());
            Promotion promotion = promotions.findByName(product.getPromotion());
            if (promotion != null && now.isAfter(promotion.getStartDate()) && now.isBefore(promotion.getEndDate())) {
                if (promotion.getBuyCount() == 1 && order.getQuantity() >= 2) {
                    discount += order.getQuantity() / 2 * product.getPrice();
                }
                if (promotion.getBuyCount() == 2 && order.getQuantity() >= 3) {
                    discount += order.getQuantity() / 3 * product.getPrice();
                }
            }
        }
        return discount;
    }

    public int calculateMembershipDiscount(final Products products, final Promotions promotions, final Membership membership) {
        int commonStockTotal = 0;
        for (Order order : orders) {
            Product product = products.findByName(order.getName());
            Promotion promotion = promotions.findByName(product.getPromotion());
            if (promotion == null) {
                commonStockTotal += product.getPrice() * order.getQuantity();
            }
        }
        int discount = 0;
        if (membership.isMember()) {
            discount = (int) (commonStockTotal * MEMBERSHIP_DISCOUNT_RATE);
        }
        if (discount > MAX_MEMBERSHIP_DISCOUNT) {
            discount = MAX_MEMBERSHIP_DISCOUNT;
        }
        return discount;
    }

    public String makeReceipt(final Products products, final Promotions promotions, final Membership membership) {
        LocalDateTime now = DateTimes.now();
        int totalPrice = calculateTotalPrice(products, promotions);
        int promotionDiscount = calculatePromotionDiscount(products, promotions);
        int totalCount = 0;
        for (Order order : orders) {
            totalCount += order.getQuantity();
            Product product = products.findByName(order.getName());
            Promotion promotion = promotions.findByName(product.getPromotion());
            if (promotion != null && now.isAfter(promotion.getStartDate()) && now.isBefore(promotion.getEndDate())) {
                if (promotion.getBuyCount() == 1 && order.getQuantity() >= 2 && order.getQuantity() >= promotion.getBuyCount()) {
                    totalCount += order.getQuantity() / 2;
                }
                if (promotion.getBuyCount() == 2 && order.getQuantity() >= 3 && order.getQuantity() >= promotion.getBuyCount()) {
                    totalCount += order.getQuantity() / 3;
                }
            }
        }
        int membershipDiscount = calculateMembershipDiscount(products, promotions, membership);
        int payment = totalPrice - promotionDiscount - membershipDiscount;
        return makeString(products, promotions, totalPrice, totalCount, promotionDiscount, membershipDiscount, payment);
    }

    private String makeString(final Products products, final Promotions promotions, final int totalPrice, final int totalCount, final int promotionDiscount, final int membershipDiscount, final int payment) {
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
        stringBuilder.append(String.format("총구매액\t\t%d\t%,d\n", totalCount, totalPrice));
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
