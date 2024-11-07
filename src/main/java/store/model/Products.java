package store.model;

import java.util.ArrayList;
import java.util.List;

import static store.constants.ExceptionMessage.ERROR_WITH_OPENING_CSV_FILE;

public class Products {
    private List<Product> products;

    public Products(final List<String> productsInformation) {
        validate(productsInformation);
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

    private void validate(final List<String> productsInformation) {
        validateNull(productsInformation);
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
