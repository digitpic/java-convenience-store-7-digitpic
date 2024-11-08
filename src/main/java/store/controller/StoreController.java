package store.controller;

import store.model.Orders;
import store.model.Products;
import store.model.Promotions;
import store.util.CsvReader;
import store.view.InputView;
import store.view.OutputView;

import java.util.List;

public class StoreController {
    private final OutputView outputView;
    private final InputView inputView;

    public StoreController(final OutputView outputView, final InputView inputView) {
        this.outputView = outputView;
        this.inputView = inputView;
    }

    public void run() {
        Products products = getProductsInformation();
        Promotions promotions = getPromotionsInformation();
        printStockStatus(products.toString());
        Orders orders = requestOrders();
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

    private Orders requestOrders() {
        String rawOrder = inputView.requestOrder();
        return new Orders(rawOrder);
    }
}
