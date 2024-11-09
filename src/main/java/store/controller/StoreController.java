package store.controller;

import store.model.Membership;
import store.model.order.Order;
import store.model.order.Orders;
import store.model.product.Product;
import store.model.product.Products;
import store.model.promotion.Promotion;
import store.model.promotion.Promotions;
import store.util.CsvReader;
import store.util.CsvWriter;
import store.view.InputView;
import store.view.OutputView;

import java.util.List;
import java.util.function.Supplier;

import static store.constants.ExceptionMessage.NOT_ALLOWED_OVER_QUANTITY;

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
        while (true) {
            printStockStatus(products.toString());
            Orders orders = requestOrders(products, promotions);
            Membership membership = requestMembership();
            printReceipt(orders.makeReceipt(products, promotions, membership));
            updateStockStatus(products, orders);
            String rawRestartConfirmation = requestRestartConfirmation();
            if (rawRestartConfirmation.equals("N")) {
                // writeStockStatus(products);
                break;
            }
        }
    }

    private void printReceipt(final String message) {
        outputView.printReceipt(message);
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

    private Orders requestOrders(final Products products, final Promotions promotions) {
        return getValidInput(() -> {
            String rawOrders = inputView.requestOrder();
            Orders orders = new Orders(rawOrders);
            checkQuantityForPromotion(products, promotions, orders);
            checkNoPromotionStock(products, promotions, orders);
            checkValidCount(products, orders);
            return orders;
        });
    }

    private void checkQuantityForPromotion(final Products products, final Promotions promotions, final Orders orders) {
        // 프로모션 적용이 가능한 상품에 대해 고객이 해당 수량보다 적게 가져온 경우, 그 수량만큼 추가 여부를 입력받는다.
        for (Order order : orders.getOrders()) {
            Product product = products.findByName(order.getName());
            Promotion promotion = promotions.findByName(product.getPromotion());
            if (promotion != null && promotion.getBuyCount() > order.getQuantity()) {
                String answer = inputView.requestAddPromotionProduct(order.getName());
                if (answer.equals("Y")) {
                    order.increaseQuantity();
                }
            }
        }
    }

    private void checkNoPromotionStock(final Products products, final Promotions promotions, final Orders orders) {
        // 프로모션 재고가 부족하여 일부 수량을 프로모션 혜택 없이 결제해야 하는 경우, 일부 수량에 대해 정가로 결제할지 여부를 입력받는다.

    }

    private void checkValidCount(final Products products, final Orders orders) {
        for (Order order : orders.getOrders()) {
            Product product = products.findByName(order.getName());
            if (product.getQuantity() < order.getQuantity()) {
                throw new IllegalArgumentException(NOT_ALLOWED_OVER_QUANTITY.getMessage());
            }
        }
    }

    private void updateStockStatus(final Products products, final Orders orders) {
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

    private <T> T getValidInput(final Supplier<T> inputSupplier) {
        while (true) {
            try {
                return inputSupplier.get();
            } catch (IllegalArgumentException e) {
                outputView.printErrorMessage(e.getMessage());
            }
        }
    }

    private void writeStockStatus(final Products products) {
        CsvWriter.writeCurrentStockStatus(products.makeCsv());
    }
}
