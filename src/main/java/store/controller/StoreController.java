package store.controller;

import store.model.Products;
import store.model.Promotions;
import store.util.CsvReader;
import store.view.OutputView;

import java.util.List;

public class StoreController {
    private final OutputView outputView;

    public StoreController(final OutputView outputView) {
        this.outputView = outputView;
    }

    public void run() {
        Products products = getProductsInformation();
        Promotions promotions = getPromotionsInformation();
        printStockStatus(products.toString());
    }

    private Products getProductsInformation() {
        List<String> productsInformation = CsvReader.getProductsData();
        return new Products(productsInformation);
    }

    private Promotions getPromotionsInformation() {
        List<String> promotionsInformation = CsvReader.getPromotionsData();
        return new Promotions(promotionsInformation);
    }

    private void printStockStatus(final String stockStatus) {
        outputView.printGreetingMessage();
        outputView.printStockStatusHeader();
        outputView.printStockStatus(stockStatus);
    }
}
