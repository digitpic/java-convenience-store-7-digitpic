package store.view;

import camp.nextstep.edu.missionutils.Console;

public class InputView {
    private static final String REQUEST_ORDER_MESSAGE = "구매하실 상품명과 수량을 입력해 주세요. (예: [사이다-2],[감자칩-1])";
    private static final String REQUEST_MEMBERSHIP_MESSAGE = "멤버십 할인을 받으시겠습니까? (Y/N)";

    public String requestOrder() {
        System.out.println(REQUEST_ORDER_MESSAGE);
        return Console.readLine();
    }

    public String requestMembership() {
        System.out.println(REQUEST_MEMBERSHIP_MESSAGE);
        return Console.readLine();
    }
}
