package store.model;

import static store.constants.ExceptionMessage.INPUT_CANNOT_HAVE_FIRST_LAST_BLANK;
import static store.constants.ExceptionMessage.INPUT_MUST_BE_UPPER_CASE;
import static store.constants.ExceptionMessage.INPUT_MUST_BE_YES_OR_NO;

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
        throw new IllegalArgumentException(INPUT_CANNOT_HAVE_FIRST_LAST_BLANK.getMessage());
    }

    private void validateUpperCase(final String membership) {
        String upperCase = membership.toUpperCase();
        if (membership.equals(upperCase)) {
            return;
        }
        throw new IllegalArgumentException(INPUT_MUST_BE_UPPER_CASE.getMessage());
    }

    private void validateYesOrNo(final String membership) {
        if (isYesOrNo(membership)) {
            return;
        }
        throw new IllegalArgumentException(INPUT_MUST_BE_YES_OR_NO.getMessage());
    }

    private boolean isYesOrNo(final String membership) {
        return membership.equals("Y") || membership.equals("N");
    }
}
