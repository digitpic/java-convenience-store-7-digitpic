package store.constants;

public enum ExceptionMessage {
    ERROR_WITH_OPENING_CSV_FILE("CSV 파일을 여는 중 처리할 수 없는 예외가 발생했습니다."),
    PRODUCT_INFORMATION_MUST_CONTAIN_COMMA("상품 정보는 쉼표(,)를 통해 구분 되어야 합니다."),
    PROMOTION_INFORMATION_MUST_CONTAIN_COMMA("행사 정보는 쉼표(,)를 통해 구분 되어야 합니다."),
    INVALID_PRODUCT_INFORMATION_COUNT("상품 정보는 4개의 정보가 있어야 합니다."),
    INVALID_PROMOTION_INFORMATION_COUNT("행사 정보는 5개의 정보가 있어야 합니다."),
    PRODUCT_CANNOT_HAVE_FIRST_LAST_BLANK_IN_NAME("상품 정보의 맨 앞, 맨 뒤에는 공백이 포함 되지 않아야 합니다."),
    PROMOTION_CANNOT_HAVE_FIRST_LAST_BLANK_IN_NAME("행사 정보의 맨 앞, 맨 뒤에는 공백이 포함 되지 않아야 합니다."),
    INPUT_VALUE_MUST_BE_NUMERIC("정수를 입력 해야 합니다.");

    private static final String PREFIX = "[ERROR] ";

    private final String message;

    ExceptionMessage(final String message) {
        this.message = PREFIX + message;
    }

    public String getMessage() {
        return message;
    }
}
