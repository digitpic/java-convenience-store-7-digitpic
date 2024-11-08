package store.model.order;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static store.constants.ExceptionMessage.NOT_ALLOWED_INPUT_TYPE;

public class Orders {
    private final List<Order> orders;

    public Orders(final String rawOrders) {
        validateComma(rawOrders);
        this.orders = parseToOrders(rawOrders);
    }

    public List<Order> getOrders() {
        return Collections.unmodifiableList(orders);
    }

    private void validateComma(final String rawOrders) {
        if (rawOrders.contains(",")) {
            return;
        }
        throw new IllegalArgumentException(NOT_ALLOWED_INPUT_TYPE.getMessage());
    }

    private List<Order> parseToOrders(String rawOrders) {
        List<Order> orders = new ArrayList<>();
        List<String> separated = Arrays.stream(rawOrders.split(","))
                .toList();
        for (String order : separated) {
            orders.add(new Order(order));
        }
        return orders;
    }
}
