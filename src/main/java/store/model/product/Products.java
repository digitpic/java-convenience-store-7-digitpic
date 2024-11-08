package store.model.product;

import store.model.order.Order;

import java.util.ArrayList;
import java.util.List;

import static store.constants.ExceptionMessage.ERROR_WITH_OPENING_CSV_FILE;
import static store.constants.ExceptionMessage.NOT_FOUND_PRODUCT_NAME;

public class Products {
    private List<Product> products;

    public Products(final List<String> productsInformation) {
        validateNull(productsInformation);
        this.products = parseToProducts(productsInformation);
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        for (Product product : products) {
            stringBuilder.append(product.toString());
        }
        return stringBuilder.toString();
    }

    public void updateStockStatus(final Order order) {
        Product product = findByName(order.getName());
        product.decreaseQuantity(order.getQuantity());
    }

    private Product findByName(final String name) {
        return products.stream()
                .filter((product) -> product.getName().equals(name))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException(NOT_FOUND_PRODUCT_NAME.getMessage()));
    }

    private void validateNull(final List<String> productsInformation) {
        if (productsInformation == null) {
            throw new IllegalArgumentException(ERROR_WITH_OPENING_CSV_FILE.getMessage());
        }
    }

    private List<Product> parseToProducts(final List<String> productsInformation) {
        removeHeader(productsInformation);
        List<Product> products = new ArrayList<>();
        for (String productInformation : productsInformation) {
            products.add(new Product(productInformation));
        }
        return products;
    }

    private void removeHeader(List<String> productsInformation) {
        productsInformation.removeFirst();
    }
}