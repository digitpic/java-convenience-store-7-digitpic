package store.controller;

import store.model.CommonStock;
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
        startStore(products, promotions);
    }

    private Products getProductsInformation() {
        List<String> productsInformation = CsvReader.getProductsData();
        return new Products(productsInformation);
    }

    private Promotions getPromotionsInformation() {
        List<String> promotionsInformation = CsvReader.getPromotionsData();
        return new Promotions(promotionsInformation);
    }

    private void startStore(final Products products, final Promotions promotions) {
        while (true) {
            Orders orders = getOrders(products, promotions);
            Membership membership = requestMembership();
            printReceipt(orders.makeReceipt(products, promotions, membership));
            updateStockStatus(products, promotions, orders);
            Restart restart = requestRestartConfirmation();
            if (!restart.getIsRestart()) {
                // writeStockStatus(products);
                break;
            }
        }
    }

    private Orders getOrders(final Products products, final Promotions promotions) {
        printStockStatus(products.toString());
        return requestOrders(products, promotions);
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
        if (promotion != null) {
            if (promotion.getBuyCount() == 1 && order.getQuantity() % 2 != 0) {
                confirm(order);
            }
            if (promotion.getBuyCount() == 2 && (order.getQuantity() - 2) % 3 == 0 && order.getQuantity() >= 2) {
                confirm(order);
            }
        }
    }

    private void confirm(final Order order) {
        More more = requestAddPromotionProductConfirmation(order);
        order.more = more.getMore();
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
                checkPromotionStock(products, promotion, order, promotionStockRequired, product);
            }
        }
    }

    private int calculateRequiredPromotionStock(final Order order, final Promotion promotion) {
        int stock = order.getQuantity();
        if (order.more) {
            stock += promotion.getGetCount();
        }
        return stock;
    }

    private void checkPromotionStock(final Products products, final Promotion promotion, final Order order, final int promotionStockRequired, final Product product) {
        if (promotionStockRequired > product.getQuantity()) {
            int groupCount = product.getQuantity() / (promotion.getBuyCount() + promotion.getGetCount());
            int coveredCount = groupCount * (promotion.getBuyCount() + promotion.getGetCount());
            int insufficientQuantity = promotionStockRequired - coveredCount;
            processInsufficientStockCase(products, order, insufficientQuantity);
        }
    }

    private void processInsufficientStockCase(final Products products, final Order order, final int insufficientQuantity) {
        boolean canReplace = checkCommonStock(products, order);
        if (canReplace) {
            CommonStock commonStock = getValidInput(() -> {
                String rawCommonStock = inputView.requestFullPricePayment(order.getName(), insufficientQuantity);
                return new CommonStock(rawCommonStock);
            });
            if (commonStock.getCommonStock()) {
                addFullPriceQuantity(order, insufficientQuantity);
            }
        }
    }

    private boolean checkCommonStock(final Products products, final Order order) {
        Product second = products.findByNameSecond(order.getName());
        return second.getQuantity() >= order.getQuantity();
    }

    private void addFullPriceQuantity(Order order, int insufficientQuantity) {
        for (int i = 0; i < insufficientQuantity; i++) {
            // todo: 일반 재고 구매 처리
        }
    }

    private void checkValidCount(final Products products, final Orders orders) {
        for (Order order : orders.getOrders()) {
            Product product = products.findByName(order.getName());
            Product second = products.findByNameSecond(order.getName());
            if (product.getQuantity() + second.getQuantity() < order.getQuantity()) {
                throw new IllegalArgumentException(NOT_ALLOWED_OVER_QUANTITY.getMessage());
            }
        }
    }

    private Membership requestMembership() {
        return getValidInput(() -> {
            String rawMembership = inputView.requestMembership();
            return new Membership(rawMembership);
        });
    }

    private void printReceipt(final String message) {
        outputView.printReceipt(message);
    }

    private void updateStockStatus(final Products products, final Promotions promotions, final Orders orders) {
        for (Order order : orders.getOrders()) {
            products.updateStockStatus(promotions, order);
        }
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
