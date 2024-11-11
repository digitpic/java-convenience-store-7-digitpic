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

    public int calculatePromotionDiscount(final Products products, final Promotions promotions) {
        int discount = 0;
        LocalDateTime now = DateTimes.now();
        for (Order order : orders) {
            Product product = products.findByName(order.getName());
            Promotion promotion = promotions.findByName(product.getPromotion());
            if (promotion != null && now.isAfter(promotion.getStartDate()) && now.isBefore(promotion.getEndDate())) {
                discount = getDiscount(order, promotion, discount, product);
            }
        }
        return discount;
    }

    private static int getDiscount(final Order order, final Promotion promotion, int discount, final Product product) {
        if (promotion.getBuyCount() == 1 && order.getQuantity() >= 1) {
            discount += order.getQuantity() / 2 * product.getPrice();
            if (discount == 0 && order.more) {
                discount += product.getPrice() * promotion.getGetCount();
            }
        }
        if (promotion.getBuyCount() == 2 && order.getQuantity() >= 2) {
            discount += order.getQuantity() / 3 * product.getPrice();
            if (discount == 0 && order.more) {
                discount += product.getPrice() * promotion.getGetCount();
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
        int promotionDiscount = calculatePromotionDiscount(products, promotions);
        int membershipDiscount = calculateMembershipDiscount(products, promotions, membership);
        return makeString(products, promotions, promotionDiscount, membershipDiscount);
    }

    private String makeString(final Products products, final Promotions promotions, final int promotionDiscount, final int membershipDiscount) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("==============W 편의점================\n");
        stringBuilder.append("상품명\t\t수량\t금액\n");
        int totalCount = 0;
        int totalPrice = 0;
        for (Order order : orders) {
            Product product = products.findByName(order.getName());
            Promotion promotion = promotions.findByName(product.getPromotion());
            if (order.more) {
                int orderPrice = product.getPrice() * (order.getQuantity() + promotion.getGetCount());
                totalPrice += orderPrice;
                totalCount += order.getQuantity() + promotion.getGetCount();
                stringBuilder.append(String.format("%s\t\t%d\t%,d\n", order.getName(), order.getQuantity() + promotion.getGetCount(), orderPrice));
            }
            if (!order.more) {
                int orderPrice = product.getPrice() * order.getQuantity();
                totalPrice += orderPrice;
                totalCount += order.getQuantity();
                stringBuilder.append(String.format("%s\t\t%d\t%,d\n", order.getName(), order.getQuantity(), orderPrice));
            }
        }
        stringBuilder.append("=============증\t정===============\n");
        for (Order order : orders) {
            Product product = products.findByName(order.getName());
            Promotion promotion = promotions.findByName(product.getPromotion());
            if (promotion != null && DateTimes.now().isAfter(promotion.getStartDate()) && DateTimes.now().isBefore(promotion.getEndDate())) {
                int freeProductCount = 0;
                makePromotionString(order, promotion, freeProductCount, stringBuilder);
            }
        }
        makeLastString(promotionDiscount, membershipDiscount, totalPrice, stringBuilder, totalCount);
        return stringBuilder.toString();
    }

    private static void makePromotionString(final Order order, final Promotion promotion, int freeProductCount, final StringBuilder stringBuilder) {
        freeProductCount = getFreeProductCount(order, promotion, freeProductCount);
        if (freeProductCount > 0) {
            stringBuilder.append(order.getName()).append("\t\t").append(freeProductCount).append("\t").append("\n");
        }
    }

    private static int getFreeProductCount(final Order order, final Promotion promotion, int freeProductCount) {
        if (promotion.getBuyCount() == 1 && order.getQuantity() >= 1 && order.getQuantity() >= promotion.getBuyCount()) {
            freeProductCount += order.getQuantity() / 2;
            freeProductCount = getFreeProductCount(order, freeProductCount);
        }
        if (promotion.getBuyCount() == 2 && order.getQuantity() >= 2 && order.getQuantity() >= promotion.getBuyCount()) {
            freeProductCount += order.getQuantity() / 3;
            freeProductCount = getFreeProductCount(order, freeProductCount);
        }
        return freeProductCount;
    }

    private static int getFreeProductCount(final Order order, int freeProductCount) {
        if (freeProductCount == 0 && order.more) {
            freeProductCount++;
        }
        return freeProductCount;
    }

    private static void makeLastString(final int promotionDiscount, final int membershipDiscount, final int totalPrice, final StringBuilder stringBuilder, final int totalCount) {
        int payment = totalPrice - promotionDiscount - membershipDiscount;
        stringBuilder.append("====================================\n");
        stringBuilder.append(String.format("총구매액\t\t%d\t%,d\n", totalCount, totalPrice));
        stringBuilder.append(String.format("행사할인\t\t \t-%,d\n", promotionDiscount));
        stringBuilder.append(String.format("멤버십할인\t\t \t-%,d\n", membershipDiscount));
        stringBuilder.append(String.format("내실돈\t\t \t%,d", payment));
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
