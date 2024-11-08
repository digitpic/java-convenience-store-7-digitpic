package store.model.order;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static store.constants.ExceptionMessage.NOT_ALLOWED_INPUT_TYPE;

class OrdersTest {
    @Test
    void 입력에_쉼표가_포함_되지_않은_경우_예외가_발생한다() {
        // given
        String input = "[콜라-10][사이다-3]]";

        // when & then
        assertThatIllegalArgumentException()
                .isThrownBy(() -> new Orders(input))
                .withMessage(NOT_ALLOWED_INPUT_TYPE.getMessage());
    }
}