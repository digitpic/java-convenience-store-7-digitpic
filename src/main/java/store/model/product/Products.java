package store.model.product;

import store.model.order.Order;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static store.constants.ExceptionMessage.ERROR_WITH_OPENING_CSV_FILE;
import static store.constants.ExceptionMessage.NOT_FOUND_PRODUCT_NAME;

public class Products {
    private List<Product> products;

    public Products(final List<String> productsInformation) {
        validateNull(productsInformation);
        this.products = parseToPromotion(productsInformation);
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

    public Product findByName(final String name) {
        return products.stream()
                .filter((product) -> product.getName().equals(name))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException(NOT_FOUND_PRODUCT_NAME.getMessage()));
    }

    public String makeCsv() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("name,price,quantity,promotion");
        for (Product product : products) {
            stringBuilder.append(product.makeCsv());
        }
        return stringBuilder.toString();
    }

    private void validateNull(final List<String> productsInformation) {
        if (productsInformation == null) {
            throw new IllegalArgumentException(ERROR_WITH_OPENING_CSV_FILE.getMessage());
        }
    }

    private List<Product> parseToPromotion(final List<String> productsInformation) {
        productsInformation.removeFirst();
        List<String> updatedProductInformation = new ArrayList<>();
        Set<String> processedProductNames = new HashSet<>();

        productsInformation.forEach(line -> handleProductLine(line, updatedProductInformation, processedProductNames, productsInformation));
        return mapToProducts(updatedProductInformation);
    }

    private void handleProductLine(final String line, final List<String> updatedProductInformation,
                                   Set<String> processedProductNames, List<String> productsInformation) {
        String[] productData = line.split(",");
        String name = productData[0], price = productData[1], promotion = productData[3];

        if (shouldAddDefaultStock(name, price, promotion, productsInformation, processedProductNames)) {
            updatedProductInformation.add(line);
            updatedProductInformation.add(String.join(",", name, price, "0", "null"));
            processedProductNames.add(name);
            return;
        }
        updatedProductInformation.add(line);
    }

    private boolean shouldAddDefaultStock(final String name, final String price, final String promotion,
                                          final List<String> productsInformation, final Set<String> processedProductNames) {
        return !promotion.equals("null") && !processedProductNames.contains(name) && !hasDefaultStock(name, price, productsInformation);
    }

    private boolean hasDefaultStock(final String name, final String price, final List<String> productsInformation) {
        return productsInformation.stream()
                .filter(product -> product.startsWith(name + "," + price))
                .count() > 1;
    }

    private List<Product> mapToProducts(final List<String> updatedProductInformation) {
        List<Product> products = new ArrayList<>();
        updatedProductInformation.forEach(line -> products.add(new Product(line)));
        return products;
    }
}
