package store.model;

import java.util.Arrays;
import java.util.List;

import static store.constants.ExceptionMessage.INPUT_MUST_CONTAIN_COMMA;
import static store.constants.ExceptionMessage.INPUT_VALUE_MUST_BE_NUMERIC;
import static store.constants.ExceptionMessage.INVALID_PRODUCT_INFORMATION_COUNT;
import static store.constants.ExceptionMessage.PRODUCT_CANNOT_HAVE_FIRST_LAST_BLANK_IN_NAME;

public class Product {
    private static final int NAME_INDEX = 0;
    private static final int PRICE_INDEX = 1;
    private static final int QUANTITY_INDEX = 2;
    private static final int PROMOTION_INDEX = 3;
    private static final int INFORMATION_COUNT = 4;

    private final String name;
    private final int price;
    private final int quantity;
    private String promotion;

    public Product(final String information) {
        validate(information);
        List<String> separated = separateByComma(information);
        this.name = separated.get(NAME_INDEX);
        this.price = Integer.parseInt(separated.get(PRICE_INDEX));
        this.quantity = Integer.parseInt(separated.get(QUANTITY_INDEX));
        this.promotion = separated.get(PROMOTION_INDEX);
    }

    @Override
    public String toString() {
        if (promotion.equals("null")) {
            promotion = "";
        }
        return String.format("- %s %,d원 %d개 %s\n", name, price, quantity, promotion);
    }

    private void validate(final String information) {
        validateComma(information);
    }

    private void validateComma(final String information) {
        if (information.contains(",")) {
            return;
        }
        throw new IllegalArgumentException(INPUT_MUST_CONTAIN_COMMA.getMessage());
    }

    private List<String> separateByComma(final String information) {
        List<String> separated = Arrays.stream(information.split(","))
                .toList();
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
            throw new IllegalArgumentException(PRODUCT_CANNOT_HAVE_FIRST_LAST_BLANK_IN_NAME.getMessage());
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
