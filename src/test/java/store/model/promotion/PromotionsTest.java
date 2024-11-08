package store.model.promotion;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static store.constants.ExceptionMessage.INPUT_MUST_CONTAIN_COMMA;

class PromotionsTest {
    @Test
    void 행사_정보에_쉼표가_포함_되지_않은_경우_예외가_발생한다() {
        // given
        String information = "반짝할인112024-11-012024-11-30";

        // when & then
        assertThatIllegalArgumentException()
                .isThrownBy(() -> new Promotion(information))
                .withMessage(INPUT_MUST_CONTAIN_COMMA.getMessage());
    }
}