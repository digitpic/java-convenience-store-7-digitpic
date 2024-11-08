package store.model.order;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static store.constants.ExceptionMessage.INPUT_VALUE_MUST_BE_NUMERIC;
import static store.constants.ExceptionMessage.ORDER_ELEMENT_MUST_SPLIT_BY_HYPHEN;
import static store.constants.ExceptionMessage.ORDER_MUST_CONTAIN_SQUARE_BRACKETS;
import static store.constants.ExceptionMessage.INPUT_CANNOT_HAVE_FIRST_LAST_BLANK;

class OrderTest {
    @ParameterizedTest
    @ValueSource(strings = {
            "콜라-10]",
            "[콜라-10",
    })
    void 대괄호가_입력_되지_않은_경우_예외가_발생한다(final String order) {
        // when & then
        assertThatIllegalArgumentException()
                .isThrownBy(() -> new Order(order))
                .withMessage(ORDER_MUST_CONTAIN_SQUARE_BRACKETS.getMessage());
    }

    @Test
    void 하이픈으로_구분_되지_않은_경우_예외가_발생한다() {
        // given
        String order = "[콜라10]";

        // when & then
        assertThatIllegalArgumentException()
                .isThrownBy(() -> new Order(order))
                .withMessage(ORDER_ELEMENT_MUST_SPLIT_BY_HYPHEN.getMessage());
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "[ 콜라-10]",
            "[콜라 -10]",
            "[콜라- 10]",
            "[콜라-10 ]"
    })
    void 각_요소의_맨_앞_맨_뒤에_공백이_있는_경우_예외가_발생한다(final String order) {
        // when & then
        assertThatIllegalArgumentException()
                .isThrownBy(() -> new Order(order))
                .withMessage(INPUT_CANNOT_HAVE_FIRST_LAST_BLANK.getMessage());
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "[콜라-10개]",
            "[콜라-n개]",
    })
    void 상품_개수에_정수가_입력_되지_않은_경우_예외가_발생한다(final String order) {
        // when & then
        assertThatIllegalArgumentException()
                .isThrownBy(() -> new Order(order))
                .withMessage(INPUT_VALUE_MUST_BE_NUMERIC.getMessage());
    }
}