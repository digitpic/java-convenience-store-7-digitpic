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
import java.util.function.Supplier;

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
        Membership membership = requestMembership();
        updateStockStatus(products, orders);
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
        return getValidInput(() -> {
            String rawOrders = inputView.requestOrder();
            return new Orders(rawOrders);
        });
    }

    private void updateStockStatus(Products products, Orders orders) {
        for (Order order : orders.getOrders()) {
            products.updateStockStatus(order);
        }
    }

    private Membership requestMembership() {
        return getValidInput(() -> {
            String rawMembership = inputView.requestMembership();
            return new Membership(rawMembership);
        });
    }

    private String requestRestartConfirmation() {
        return inputView.requestRestartConfirmation();
    }

    private <T> T getValidInput(Supplier<T> inputSupplier) {
        while (true) {
            try {
                return inputSupplier.get();
            } catch (IllegalArgumentException e) {
                outputView.printErrorMessage(e.getMessage());
            }
        }
    }
}
