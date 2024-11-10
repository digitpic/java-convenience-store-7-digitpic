package store.controller;

import store.model.Membership;
import store.model.More;
import store.model.Restart;
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
            Restart restart = requestRestartConfirmation();
            if (!restart.getIsRestart()) {
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
        for (Order order : orders.getOrders()) {
            Product product = products.findByName(order.getName());
            Promotion promotion = promotions.findByName(product.getPromotion());
            confirmAddPromotionProduct(promotion, order);
        }
    }

    private void confirmAddPromotionProduct(final Promotion promotion, final Order order) {
        if (promotion != null && promotion.getBuyCount() == order.getQuantity()) {
            More more = requestAddPromotionProductConfirmation(order);
            if (!more.getMore()) {
                order.more = false;
            }
        }
    }

    private More requestAddPromotionProductConfirmation(final Order order) {
        return getValidInput(() -> {
            String rawMore = inputView.requestAddPromotionProduct(order.getName());
            return new More(rawMore);
        });
    }

    private void checkNoPromotionStock(final Products products, final Promotions promotions, final Orders orders) {
        for (Order order : orders.getOrders()) {
            Product product = products.findByName(order.getName());
            Promotion promotion = promotions.findByName(product.getPromotion());
            if (promotion != null) {
                int promotionStockRequired = calculateRequiredPromotionStock(order, promotion);
                checkPromotionStock(order, promotionStockRequired, product);
            }
        }
    }

    private int calculateRequiredPromotionStock(Order order, Promotion promotion) {
        if (order.getQuantity() % (promotion.getBuyCount() + promotion.getGetCount()) == 0) {
            return 0;
        }
        return (order.getQuantity() / promotion.getBuyCount()) * promotion.getGetCount();
    }

    private void checkPromotionStock(final Order order, final int promotionStockRequired, final Product product) {
        if (order.getQuantity() + promotionStockRequired > product.getQuantity()) {
            int insufficientQuantity = Math.abs(promotionStockRequired - product.getQuantity());
            processInsufficientStockCase(order, insufficientQuantity);
        }
    }

    private void processInsufficientStockCase(Order order, int insufficientQuantity) {
        String answer = inputView.requestFullPricePayment(order.getName(), insufficientQuantity);
        if (answer.equals("Y")) {
            addFullPriceQuantity(order, insufficientQuantity);
        }
    }

    private void addFullPriceQuantity(Order order, int insufficientQuantity) {
        for (int i = 0; i < insufficientQuantity; i++) {
            order.increaseQuantity();
        }
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

    private Restart requestRestartConfirmation() {
        return getValidInput(() -> {
            String rawRestart = inputView.requestRestartConfirmation();
            return new Restart(rawRestart);
        });
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
