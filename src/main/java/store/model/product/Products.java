package store.model.product;

import store.model.order.Order;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static store.constants.ExceptionMessage.ERROR_WITH_OPENING_CSV_FILE;

public class Products {
    private static final int NAME_INDEX = 0;
    private static final int PRICE_INDEX = 1;
    private static final int PROMOTION_INDEX = 3;

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
        List<Product> selected = products.stream()
                .filter((product) -> product.getName().equals(name))
                .toList();
        return selected.getFirst();
    }

    public Product findByNameSecond(final String name) {
        List<Product> selected = products.stream()
                .filter((product) -> product.getName().equals(name))
                .toList();
        return selected.getLast();
    }

    public String makeCsv() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("name,price,quantity,promotion\n");
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
        String name = productData[NAME_INDEX];
        String price = productData[PRICE_INDEX];
        String promotion = productData[PROMOTION_INDEX];

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
