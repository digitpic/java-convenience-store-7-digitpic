package store.util;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public class CsvReader {
    private static final String PARENT_DIRECTORY_PATH = "src/main/resources/";
    private static final String PRODUCTS_FILE_NAME = "products.md";
    private static final String PROMOTIONS_FILE_NAME = "promotions.md";

    public static List<String> getProductsData() {
        Path productsPath = Paths.get(PARENT_DIRECTORY_PATH + PRODUCTS_FILE_NAME);
        try {
            return Files.readAllLines(productsPath);
        } catch (IOException e) {
            return null;
        }
    }

    public static List<String> getPromotionsData() {
        Path promotionsPath = Paths.get(PARENT_DIRECTORY_PATH + PROMOTIONS_FILE_NAME);
        try {
            return Files.readAllLines(promotionsPath);
        } catch (IOException e) {
            return null;
        }
    }
}
