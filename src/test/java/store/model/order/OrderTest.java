package store.model.order;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static store.constants.ExceptionMessage.NOT_ALLOWED_INPUT_TYPE;

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
                .withMessage(NOT_ALLOWED_INPUT_TYPE.getMessage());
    }

    @Test
    void 하이픈으로_구분_되지_않은_경우_예외가_발생한다() {
        // given
        String order = "[콜라10]";

        // when & then
        assertThatIllegalArgumentException()
                .isThrownBy(() -> new Order(order))
                .withMessage(NOT_ALLOWED_INPUT_TYPE.getMessage());
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
                .withMessage(NOT_ALLOWED_INPUT_TYPE.getMessage());
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
                .withMessage(NOT_ALLOWED_INPUT_TYPE.getMessage());
    }
}