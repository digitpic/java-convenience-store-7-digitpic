package store.view;

import camp.nextstep.edu.missionutils.Console;

public class InputView {
    private static final String LINE_BREAK = "\n";
    private static final String REQUEST_ORDER_MESSAGE = "구매하실 상품명과 수량을 입력해 주세요. (예: [사이다-2],[감자칩-1])";
    private static final String REQUEST_MEMBERSHIP_MESSAGE = "멤버십 할인을 받으시겠습니까? (Y/N)";
    private static final String REQUEST_RESTART_CONFIRMATION_MESSAGE = "감사합니다. 구매하고 싶은 다른 상품이 있나요? (Y/N)";
    private static final String REQUEST_ADD_PROMOTION_PRODUCT = "현재 %s은(는) 1개를 무료로 더 받을 수 있습니다. 추가하시겠습니까? (Y/N)";

    public String requestOrder() {
        System.out.println(REQUEST_ORDER_MESSAGE);
        return Console.readLine();
    }

    public String requestMembership() {
        System.out.println(LINE_BREAK + REQUEST_MEMBERSHIP_MESSAGE);
        return Console.readLine();
    }

    public String requestRestartConfirmation() {
        System.out.println(LINE_BREAK + REQUEST_RESTART_CONFIRMATION_MESSAGE);
        return Console.readLine();
    }

    public String requestAddPromotionProduct(String name) {
        System.out.printf((REQUEST_ADD_PROMOTION_PRODUCT) + "%n", name);
        return Console.readLine();
    }
}
