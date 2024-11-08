package store.model;

import static store.constants.ExceptionMessage.COMMON_EXCEPTION_MESSAGE;

public class Membership {
    public Membership(final String membership) {
        validate(membership);
    }

    private void validate(final String membership) {
        validateStrip(membership);
        validateUpperCase(membership);
        validateYesOrNo(membership);
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
