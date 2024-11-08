package store.constants;

public enum ExceptionMessage {
    ERROR_WITH_OPENING_CSV_FILE("CSV 파일을 여는 중 처리할 수 없는 예외가 발생했습니다."),
    INPUT_MUST_CONTAIN_COMMA("입력 정보는 쉼표(,)를 통해 구분 되어야 합니다."),
    INVALID_PRODUCT_INFORMATION_COUNT("상품 정보는 4개의 정보가 있어야 합니다."),
    INVALID_PROMOTION_INFORMATION_COUNT("행사 정보는 5개의 정보가 있어야 합니다."),
    INPUT_CANNOT_HAVE_FIRST_LAST_BLANK("입력 정보의 맨 앞, 맨 뒤에는 공백이 포함 되지 않아야 합니다."),
    INPUT_VALUE_MUST_BE_NUMERIC("정수를 입력 해야 합니다."),
    NOT_FOUND_PRODUCT_NAME("존재하지 않는 상품입니다. 다시 입력해 주세요."),
    NOT_ALLOWED_INPUT_TYPE("올바르지 않은 형식으로 입력했습니다. 다시 입력해 주세요."),
    COMMON_EXCEPTION_MESSAGE("잘못된 입력입니다. 다시 입력해 주세요."),
    NOT_ALLOWED_OVER_QUANTITY("재고 수량을 초과하여 구매할 수 없습니다. 다시 입력해 주세요.");

    private static final String PREFIX = "[ERROR] ";

    private final String message;

    ExceptionMessage(final String message) {
        this.message = PREFIX + message;
    }

    public String getMessage() {
        return message;
    }
}
