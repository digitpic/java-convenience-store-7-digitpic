package store.model;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static store.constants.ExceptionMessage.COMMON_EXCEPTION_MESSAGE;

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
                .withMessage(COMMON_EXCEPTION_MESSAGE.getMessage());
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
                .withMessage(COMMON_EXCEPTION_MESSAGE.getMessage());
    }

    @Test
    void Y_또는_N_이_입력_되지_않는_경우_예외가_발생한다() {
        // given
        String rawMembership = "A";

        // when & then
        assertThatIllegalArgumentException()
                .isThrownBy(() -> new Membership(rawMembership))
                .withMessage(COMMON_EXCEPTION_MESSAGE.getMessage());
    }

    @Test
    void boolean_정보에_대한_getter_가_정상_동작한다_true(){
        // given
        String rawMembership = "Y";
        Membership membership = new Membership(rawMembership);

        // when
        boolean real = membership.isMember();

        // then
        boolean expected = true;
        assertThat(real).isEqualTo(expected);
    }

    @Test
    void boolean_정보에_대한_getter_가_정상_동작한다_false(){
        // given
        String rawMembership = "N";
        Membership membership = new Membership(rawMembership);

        // when
        boolean real = membership.isMember();

        // then
        boolean expected = false;
        assertThat(real).isEqualTo(expected);
    }
}
