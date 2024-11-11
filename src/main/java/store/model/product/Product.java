package store.model.product;

import store.model.order.Order;
import store.model.promotion.Promotion;
import store.model.promotion.Promotions;

import java.util.Arrays;
import java.util.List;

import static store.constants.ExceptionMessage.INPUT_CANNOT_HAVE_FIRST_LAST_BLANK;
import static store.constants.ExceptionMessage.INPUT_MUST_CONTAIN_COMMA;
import static store.constants.ExceptionMessage.INPUT_VALUE_MUST_BE_NUMERIC;
import static store.constants.ExceptionMessage.INVALID_PRODUCT_INFORMATION_COUNT;

public class Product {
    private static final int NAME_INDEX = 0;
    private static final int PRICE_INDEX = 1;
    private static final int QUANTITY_INDEX = 2;
    private static final int PROMOTION_INDEX = 3;
    private static final int INFORMATION_COUNT = 4;

    private final String name;
    private final int price;
    private int quantity;
    private String promotion;

    public Product(final String information) {
        validateComma(information);
        List<String> separated = separateByComma(information);
        this.name = separated.get(NAME_INDEX);
        this.price = Integer.parseInt(separated.get(PRICE_INDEX));
        this.quantity = Integer.parseInt(separated.get(QUANTITY_INDEX));
        this.promotion = separated.get(PROMOTION_INDEX);
    }

    @Override
    public String toString() {
        String quantityDisplay = makeQuantityDisplay(quantity);
        String promotionDisplay = makePromotionDisplay(promotion);
        return String.format("- %s %,d원 %s %s\n", name, price, quantityDisplay, promotionDisplay);
    }

    public void decreaseQuantity(final Promotion promotion, int stock, final boolean more) {
        if (more) {
            stock += promotion.getGetCount();
        }
        if (stock > quantity) {
            int groupCount = quantity / (promotion.getBuyCount() + promotion.getGetCount());
            int coveredCount = groupCount * (promotion.getBuyCount() + promotion.getGetCount());
            this.quantity -= coveredCount;
        }
        if (stock <= quantity) {
            this.quantity -= stock;
        }
    }

    public String makeCsv() {
        if (quantity == 0 && promotion.equals("null")) {
            return "";
        }
        return String.format("%s,%s,%s,%s\n", name, price, quantity, promotion);
    }

    public String getName() {
        return name;
    }

    public int getPrice() {
        return price;
    }

    public int getQuantity() {
        return quantity;
    }

    public String getPromotion() {
        return promotion;
    }

    private String makeQuantityDisplay(final int quantity) {
        String quantityDisplay = quantity + "개";
        if (quantity == 0) {
            quantityDisplay = "재고 없음";
        }
        return quantityDisplay;
    }

    private String makePromotionDisplay(final String promotion) {
        String promotionDisplay = promotion;
        if (promotion.equals("null")) {
            promotionDisplay = "";
        }
        return promotionDisplay;
    }

    private void validateComma(final String information) {
        if (information.contains(",")) {
            return;
        }
        throw new IllegalArgumentException(INPUT_MUST_CONTAIN_COMMA.getMessage());
    }

    private List<String> separateByComma(final String information) {
        List<String> separated = Arrays.stream(information.split(",")).toList();
        validateAfterSeparate(separated);
        return separated;
    }

    private void validateAfterSeparate(final List<String> separated) {
        validateCount(separated);
        validateStrip(separated);
        validateNumeric(separated.get(PRICE_INDEX));
        validateNumeric(separated.get(QUANTITY_INDEX));
    }

    private void validateCount(final List<String> separated) {
        if (separated.size() == INFORMATION_COUNT) {
            return;
        }
        throw new IllegalArgumentException(INVALID_PRODUCT_INFORMATION_COUNT.getMessage());
    }

    private void validateStrip(final List<String> separated) {
        separated.forEach((element) -> {
            String stripped = element.strip();
            if (element.equals(stripped)) {
                return;
            }
            throw new IllegalArgumentException(INPUT_CANNOT_HAVE_FIRST_LAST_BLANK.getMessage());
        });
    }

    private void validateNumeric(final String input) {
        try {
            Integer.parseInt(input);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException(INPUT_VALUE_MUST_BE_NUMERIC.getMessage());
        }
    }
}
