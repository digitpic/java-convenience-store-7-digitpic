package store.model;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static store.constants.ExceptionMessage.INPUT_CANNOT_HAVE_FIRST_LAST_BLANK;
import static store.constants.ExceptionMessage.INPUT_MUST_BE_UPPER_CASE;
import static store.constants.ExceptionMessage.INPUT_MUST_BE_YES_OR_NO;

class MembershipTest {
    @ParameterizedTest
    @ValueSource(strings = {
            " Y",
            "Y "
    })
    void 문장_맨_앞_맨_뒤에_공백이_포함_되는_경우_예외가_발생한다(final String rawMembership) {
        // when & then
        assertThatIllegalArgumentException()
                .isThrownBy(() -> new Membership(rawMembership))
                .withMessage(INPUT_CANNOT_HAVE_FIRST_LAST_BLANK.getMessage());
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "y",
            "n"
    })
    void 소문자가_입력_되는_경우_예외가_발생한다(final String rawMembership) {
        // when & then
        assertThatIllegalArgumentException()
                .isThrownBy(() -> new Membership(rawMembership))
                .withMessage(INPUT_MUST_BE_UPPER_CASE.getMessage());
    }

    @Test
    void Y_또는_N_이_입력_되지_않는_경우_예외가_발생한다() {
        // given
        String rawMembership = "A";

        // when & then
        assertThatIllegalArgumentException()
                .isThrownBy(() -> new Membership(rawMembership))
                .withMessage(INPUT_MUST_BE_YES_OR_NO.getMessage());
    }
}