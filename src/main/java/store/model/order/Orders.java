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

    public int calculateOriginalPrice(final Products products) {
        int total = 0;
        for (Order order : orders) {
            Product product = products.findByName(order.getName());
            total += product.getPrice() * order.getQuantity();
        }
        return total;
    }

    public int calculatePromotionDiscount(final Products products, final Promotions promotions) {
        int discount = 0;
        for (Order order : orders) {
            Product product = products.findByName(order.getName());
            Promotion promotion = promotions.findByName(product.getPromotion());
            LocalDateTime now = DateTimes.now();
            if (promotion != null) {
                discount += product.getPrice() * promotion.getGetCount();
            }
            if (promotion == null) {
                discount = 0;
                continue;
            }
            if (promotion.getStartDate().isBefore(now) || promotion.getEndDate().isAfter(now)) {
                discount = 0;
            }
        }
        return discount;
    }

    public int calculateMembershipDiscount(final Products products, final Membership membership) {
        int total = calculateOriginalPrice(products);
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
        int originalPrice = calculateOriginalPrice(products);
        int promotionDiscount = calculatePromotionDiscount(products, promotions);
        int membershipDiscount = 0;
        if (membership.isMember()) {
            membershipDiscount = calculateMembershipDiscount(products, membership);
        }
        int payment = originalPrice - promotionDiscount - membershipDiscount;

        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("==============W 편의점================\n");
        stringBuilder.append("상품명\t\t수량\t금액\n");
        stringBuilder.append("=============증\t정===============\n");
        stringBuilder.append("====================================\n");
        stringBuilder.append(String.format("총구매액\t\t%d\t%d\n", totalOrderCount(), originalPrice));
        stringBuilder.append(String.format("행사할인\t\t \t%,d\n", -Math.abs(promotionDiscount)));
        stringBuilder.append(String.format("멤버십할인\t\t \t%,d\n", -Math.abs(membershipDiscount)));
        stringBuilder.append(String.format("내실돈\t\t \t%,d", payment));
        return stringBuilder.toString();
    }

    public List<Order> getOrders() {
        return Collections.unmodifiableList(orders);
    }

    private int totalOrderCount() {
        int total = 0;
        for (Order order : orders) {
            total += order.getQuantity();
        }
        return total;
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
