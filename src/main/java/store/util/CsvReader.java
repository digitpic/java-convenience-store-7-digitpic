package store.util;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import static store.constants.ExceptionMessage.ERROR_WITH_OPENING_CSV_FILE;

public class CsvReader {
    public static final String PROMOTIONS_FILE_NAME = "promotions.md";
    private static final String PARENT_DIRECTORY_PATH = "src/main/resources/";
    private static final String PRODUCTS_FILE_NAME = "products.md";

    public static List<String> getProductsData() {
        Path productsPath = Paths.get(PARENT_DIRECTORY_PATH + PRODUCTS_FILE_NAME);
        try {
            return Files.readAllLines(productsPath);
        } catch (IOException e) {
            System.err.println(ERROR_WITH_OPENING_CSV_FILE.getMessage());
            return null;
        }
    }

    public static List<String> getPromotionsData() {
        Path promotionsPath = Paths.get(PARENT_DIRECTORY_PATH + PROMOTIONS_FILE_NAME);
        try {
            return Files.readAllLines(promotionsPath);
        } catch (IOException e) {
            System.err.println(ERROR_WITH_OPENING_CSV_FILE.getMessage());
            return null;
        }
    }
}
