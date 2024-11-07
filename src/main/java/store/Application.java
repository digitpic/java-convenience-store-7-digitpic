package store;

import store.controller.StoreController;
import store.view.OutputView;

public class Application {
    public static void main(String[] args) {
        StoreController storeController = new StoreController(new OutputView());
        storeController.run();
    }
}
