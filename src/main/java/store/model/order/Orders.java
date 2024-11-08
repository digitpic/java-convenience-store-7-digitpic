package store.model.order;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static store.constants.ExceptionMessage.INPUT_MUST_CONTAIN_COMMA;

public class Orders {
    private final List<Order> orders;

    public Orders(final String rawOrders) {
        validateComma(rawOrders);
        this.orders = parseToOrders(rawOrders);
    }

    private void validateComma(final String rawOrders) {
        if (rawOrders.contains(",")) {
            return;
        }
        throw new IllegalArgumentException(INPUT_MUST_CONTAIN_COMMA.getMessage());
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
