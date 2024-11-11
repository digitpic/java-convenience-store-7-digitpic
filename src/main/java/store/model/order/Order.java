package store.model.order;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static store.constants.ExceptionMessage.NOT_ALLOWED_INPUT_TYPE;

public class Order {
    private static final int NAME_INDEX = 0;
    private static final int QUANTITY_INDEX = 1;

    private final String name;
    public boolean more = true;
    private int quantity;

    public Order(final String order) {
        validate(order);
        List<String> separated = separateByHyphen(order);
        this.name = separated.get(NAME_INDEX);
        this.quantity = Integer.parseInt(separated.get(QUANTITY_INDEX));
    }

    public void increaseQuantity() {
        quantity++;
    }

    public String getName() {
        return name;
    }

    public int getQuantity() {
        return quantity;
    }

    private void validate(final String order) {
        validateSquareBrackets(order);
        validateHyphen(order);
    }

    private void validateSquareBrackets(final String order) {
        List<String> separated = Arrays.stream(order.split(",")).toList();
        separated.forEach((element) -> {
            if (hasSquareBrackets(element)) {
                return;
            }
            throw new IllegalArgumentException(NOT_ALLOWED_INPUT_TYPE.getMessage());
        });
    }

    private boolean hasSquareBrackets(final String order) {
        return order.startsWith("[") && order.endsWith("]");
    }

    private void validateHyphen(final String order) {
        List<String> separated = Arrays.stream(order.split(",")).toList();
        separated.forEach((element) -> {
            if (element.contains("-")) {
                return;
            }
            throw new IllegalArgumentException(NOT_ALLOWED_INPUT_TYPE.getMessage());
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
            element = element.replace("[", "").replace("]", "");
            separated.set(i, element);
        }
    }

    private void validateAfterSeparate(final List<String> separated) {
        validateStrip(separated);
        validateNumeric(separated.get(QUANTITY_INDEX));
        validateZero(separated.get(QUANTITY_INDEX));
    }

    private void validateStrip(final List<String> separated) {
        separated.forEach((element) -> {
            String stripped = element.strip();
            if (element.equals(stripped)) {
                return;
            }
            throw new IllegalArgumentException(NOT_ALLOWED_INPUT_TYPE.getMessage());
        });
    }

    private void validateNumeric(final String input) {
        try {
            Integer.parseInt(input);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException(NOT_ALLOWED_INPUT_TYPE.getMessage());
        }
    }

    private void validateZero(final String s) {
        int quantity = Integer.parseInt(s);
        if (quantity == 0) {
            throw new IllegalArgumentException(NOT_ALLOWED_INPUT_TYPE.getMessage());
        }
    }
}
