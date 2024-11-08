package store.constants;

public enum ExceptionMessage {
    ERROR_WITH_OPENING_CSV_FILE("CSV 파일을 여는 중 처리할 수 없는 예외가 발생했습니다."),
    INPUT_MUST_CONTAIN_COMMA("입력 정보는 쉼표(,)를 통해 구분 되어야 합니다."),
    INVALID_PRODUCT_INFORMATION_COUNT("상품 정보는 4개의 정보가 있어야 합니다."),
    INVALID_PROMOTION_INFORMATION_COUNT("행사 정보는 5개의 정보가 있어야 합니다."),
    INPUT_CANNOT_HAVE_FIRST_LAST_BLANK("입력 정보의 맨 앞, 맨 뒤에는 공백이 포함 되지 않아야 합니다."),
    INPUT_VALUE_MUST_BE_NUMERIC("정수를 입력 해야 합니다."),
    ORDER_MUST_CONTAIN_SQUARE_BRACKETS("각 상품과 수량은 대괄호 사이에 입력 되어야 합니다."),
    ORDER_ELEMENT_MUST_SPLIT_BY_HYPHEN("상품과 수량은 하이픈으로 구분 되어야 합니다."),
    INPUT_MUST_BE_UPPER_CASE("대문자로 입력 해야 합니다."),
    INPUT_MUST_BE_YES_OR_NO("Y, 또는 N 이 입력 되어야 합니다");

    private static final String PREFIX = "[ERROR] ";

    private final String message;

    ExceptionMessage(final String message) {
        this.message = PREFIX + message;
    }

    public String getMessage() {
        return message;
    }
}
