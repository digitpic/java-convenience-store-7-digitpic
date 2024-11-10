package store.model;

import static store.constants.ExceptionMessage.COMMON_EXCEPTION_MESSAGE;

public class Restart {
    private boolean isRestart;

    public Restart(String restart) {
        validate(restart);
        if (restart.equals("Y")) {
            this.isRestart = true;
        }
        if (restart.equals("N")) {
            this.isRestart = false;
        }
    }

    private void validate(final String restart) {
        validateStrip(restart);
        validateUpperCase(restart);
        validateYesOrNo(restart);
    }

    public boolean getIsRestart() {
        return isRestart;
    }

    private void validateStrip(final String restart) {
        String stripped = restart.strip();
        if (restart.equals(stripped)) {
            return;
        }
        throw new IllegalArgumentException(COMMON_EXCEPTION_MESSAGE.getMessage());
    }

    private void validateUpperCase(final String restart) {
        String upperCase = restart.toUpperCase();
        if (restart.equals(upperCase)) {
            return;
        }
        throw new IllegalArgumentException(COMMON_EXCEPTION_MESSAGE.getMessage());
    }

    private void validateYesOrNo(final String restart) {
        if (isYesOrNo(restart)) {
            return;
        }
        throw new IllegalArgumentException(COMMON_EXCEPTION_MESSAGE.getMessage());
    }

    private boolean isYesOrNo(final String membership) {
        return membership.equals("Y") || membership.equals("N");
    }
}
