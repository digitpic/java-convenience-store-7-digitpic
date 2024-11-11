package store.model;

import static store.constants.ExceptionMessage.COMMON_EXCEPTION_MESSAGE;

public class Membership {
    private boolean isMember;

    public Membership(final String membership) {
        validate(membership);
        setField(membership);
    }

    private void validate(final String membership) {
        validateStrip(membership);
        validateUpperCase(membership);
        validateYesOrNo(membership);
    }

    private void setField(final String membership) {
        if (membership.equals("Y")) {
            this.isMember = true;
        }
        if (membership.equals("N")) {
            this.isMember = false;
        }
    }

    public boolean isMember() {
        return this.isMember;
    }

    private void validateStrip(final String membership) {
        String stripped = membership.strip();
        if (membership.equals(stripped)) {
            return;
        }
        throw new IllegalArgumentException(COMMON_EXCEPTION_MESSAGE.getMessage());
    }

    private void validateUpperCase(final String membership) {
        String upperCase = membership.toUpperCase();
        if (membership.equals(upperCase)) {
            return;
        }
        throw new IllegalArgumentException(COMMON_EXCEPTION_MESSAGE.getMessage());
    }

    private void validateYesOrNo(final String membership) {
        if (isYesOrNo(membership)) {
            return;
        }
        throw new IllegalArgumentException(COMMON_EXCEPTION_MESSAGE.getMessage());
    }

    private boolean isYesOrNo(final String membership) {
        return membership.equals("Y") || membership.equals("N");
    }
}
