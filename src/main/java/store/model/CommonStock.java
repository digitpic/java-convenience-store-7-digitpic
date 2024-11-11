package store.model;

import static store.constants.ExceptionMessage.COMMON_EXCEPTION_MESSAGE;

public class CommonStock {
    private boolean commonStock;

    public CommonStock(final String commonStock) {
        validate(commonStock);
        setField(commonStock);
    }

    public boolean getCommonStock() {
        return commonStock;
    }

    private void validate(final String commonStock) {
        validateStrip(commonStock);
        validateUpperCase(commonStock);
        validateYesOrNo(commonStock);
        setField(commonStock);
    }

    private void validateStrip(final String commonStock) {
        String stripped = commonStock.strip();
        if (commonStock.equals(stripped)) {
            return;
        }
        throw new IllegalArgumentException(COMMON_EXCEPTION_MESSAGE.getMessage());
    }

    private void validateUpperCase(final String commonStock) {
        String upperCase = commonStock.toUpperCase();
        if (commonStock.equals(upperCase)) {
            return;
        }
        throw new IllegalArgumentException(COMMON_EXCEPTION_MESSAGE.getMessage());
    }

    private void validateYesOrNo(final String commonStock) {
        if (isYesOrNo(commonStock)) {
            return;
        }
        throw new IllegalArgumentException(COMMON_EXCEPTION_MESSAGE.getMessage());
    }

    private boolean isYesOrNo(final String membership) {
        return membership.equals("Y") || membership.equals("N");
    }

    private void setField(final String commonStock) {
        if (commonStock.equals("Y")) {
            this.commonStock = true;
        }
        if (commonStock.equals("N")) {
            this.commonStock = false;
        }
    }
}
