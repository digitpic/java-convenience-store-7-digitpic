package store.controller;

import store.model.Membership;
import store.model.order.Order;
import store.model.order.Orders;
import store.model.product.Products;
import store.model.promotion.Promotions;
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
        updateStockStatus(products, orders);
        Membership membership = requestMembership();
        String rawRestartConfirmation = requestRestartConfirmation();
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

    private void updateStockStatus(Products products, Orders orders) {
        for (Order order : orders.getOrders()) {
            products.updateStockStatus(order);
        }
    }

    private Membership requestMembership() {
        String rawMembership = inputView.requestMembership();
        return new Membership(rawMembership);
    }

    private String requestRestartConfirmation() {
        return inputView.requestRestartConfirmation();
    }
}