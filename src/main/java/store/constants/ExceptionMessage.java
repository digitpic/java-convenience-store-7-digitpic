package store.constants;

public enum ExceptionMessage {
    ERROR_WITH_OPENING_CSV_FILE("CSV 파일을 여는 중 처리할 수 없는 예외가 발생했습니다.");

    private static final String PREFIX = "[ERROR] ";

    private final String message;

    ExceptionMessage(String message) {
        this.message = PREFIX + message;
    }

    public String getMessage() {
        return message;
    }
}
