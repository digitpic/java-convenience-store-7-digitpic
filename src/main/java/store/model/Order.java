package store.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static store.constants.ExceptionMessage.INPUT_VALUE_MUST_BE_NUMERIC;
import static store.constants.ExceptionMessage.ORDER_ELEMENT_MUST_SPLIT_BY_HYPHEN;
import static store.constants.ExceptionMessage.ORDER_MUST_CONTAIN_SQUARE_BRACKETS;
import static store.constants.ExceptionMessage.PRODUCT_CANNOT_HAVE_FIRST_LAST_BLANK_IN_NAME;

public class Order {
    private static final int NAME_INDEX = 0;
    private static final int QUANTITY_INDEX = 1;

    private final String name;
    private final int quantity;

    public Order(final String order) {
        validate(order);
        List<String> separated = separateByHyphen(order);
        this.name = separated.get(NAME_INDEX);
        this.quantity = Integer.parseInt(separated.get(QUANTITY_INDEX));
    }

    private void validate(final String order) {
        validateSquareBrackets(order);
        validateHyphen(order);
    }

    private void validateSquareBrackets(final String order) {
        List<String> separated = Arrays.stream(order.split(","))
                .toList();
        separated.forEach((element) -> {
            if (hasSquareBrackets(element)) {
                return;
            }
            throw new IllegalArgumentException(ORDER_MUST_CONTAIN_SQUARE_BRACKETS.getMessage());
        });
    }

    private boolean hasSquareBrackets(final String order) {
        return order.startsWith("[") && order.endsWith("]");
    }

    private void validateHyphen(final String order) {
        List<String> separated = Arrays.stream(order.split(","))
                .toList();
        separated.forEach((element) -> {
            if (element.contains("-")) {
                return;
            }
            throw new IllegalArgumentException(ORDER_ELEMENT_MUST_SPLIT_BY_HYPHEN.getMessage());
        });
    }

    private List<String> separateByHyphen(final String order) {
        List<String> separated = new ArrayList<>(Arrays.asList(order.split("-")));
        removeSquareBracket(separated);
        validateAfterSeparate(separated);
        return separated;
    }

    private void removeSquareBracket(final List<String> separated) {
        for (int i = 0; i < separated.size(); i++) {
            String element = separated.get(i);
            element = element.replace("[", "")
                    .replace("]", "");
            separated.set(i, element);
        }
    }

    private void validateAfterSeparate(final List<String> separated) {
        validateStrip(separated);
        validateNumeric(separated.get(QUANTITY_INDEX));
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
