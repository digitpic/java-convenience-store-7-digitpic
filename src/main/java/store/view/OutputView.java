package store.view;

public class OutputView {
    private static final String GREETING_MESSAGE = "안녕하세요. W편의점입니다.";
    private static final String STOCK_STATUS_MESSAGE = "현재 보유하고 있는 상품입니다.";
    private static final String LINE_BREAK = "\n";

    public void printGreetingMessage() {
        System.out.println(LINE_BREAK + GREETING_MESSAGE);
    }

    public void printStockStatusHeader() {
        System.out.println(STOCK_STATUS_MESSAGE);
    }

    public void printStockStatus(final String stockStatus) {
        System.out.println(LINE_BREAK + stockStatus);
    }

    public void printErrorMessage(final String message) {
        System.out.println(message + LINE_BREAK);
    }

    public void printReceipt(final String message) {
        System.out.println(LINE_BREAK + message);
    }
}
