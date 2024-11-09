package store.util;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class CsvWriter {
    private static final String PARENT_DIRECTORY_PATH = "src/main/resources/";
    private static final String PRODUCTS_FILE_NAME = "products.md";

    public static void writeCurrentStockStatus(String lines) {
        Path productsPath = Paths.get(PARENT_DIRECTORY_PATH + PRODUCTS_FILE_NAME);
        try {
            Files.writeString(productsPath, lines);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
