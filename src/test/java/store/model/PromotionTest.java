package store.model;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static store.constants.ExceptionMessage.ERROR_WITH_OPENING_CSV_FILE;
import static store.constants.ExceptionMessage.INPUT_VALUE_MUST_BE_NUMERIC;
import static store.constants.ExceptionMessage.INVALID_PROMOTION_INFORMATION_COUNT;
import static store.constants.ExceptionMessage.PROMOTION_CANNOT_HAVE_FIRST_LAST_BLANK_IN_NAME;

class PromotionTest {
    @Test
    void 행사_정보의_값이_null_인_경우_예외가_발생한다(){
        // given
        List<String> promotionsInformation = null;

        // when & then
        assertThatIllegalArgumentException()
                .isThrownBy(() -> new Promotions(promotionsInformation))
                .withMessage(ERROR_WITH_OPENING_CSV_FILE.getMessage());
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "반짝할인,1,1,2024-11-01",
            "반짝할인,1,1,2024-11-01,2024-11-30,test"
    })
    void 행사_정보의_개수가_5개가_아닌_경우_예외가_발생한다(String input) {
        // when & then
        assertThatIllegalArgumentException()
                .isThrownBy(() -> new Promotion(input))
                .withMessage(INVALID_PROMOTION_INFORMATION_COUNT.getMessage());
    }

    @ParameterizedTest
    @ValueSource(strings = {
            " 반짝할인,1,1,2024-11-01,2024-11-30",
            "반짝할인 ,1,1,2024-11-01,2024-11-30",
            "반짝할인, 1,1,2024-11-01,2024-11-30",
            "반짝할인,1 ,1,2024-11-01,2024-11-30",
            "반짝할인,1, 1,2024-11-01,2024-11-30",
            "반짝할인,1,1 ,2024-11-01,2024-11-30",
            "반짝할인,1,1, 2024-11-01,2024-11-30",
            "반짝할인,1,1,2024-11-01 ,2024-11-30",
            "반짝할인,1,1,2024-11-01, 2024-11-30",
            "반짝할인,1,1,2024-11-01,2024-11-30 "
    })
    void 각_요소의_맨_앞_맨_뒤에_공백이_포함_되는_경우_예외가_발생한다(String input){
        // when & then
        assertThatIllegalArgumentException()
                .isThrownBy(() -> new Promotion(input))
                .withMessage(PROMOTION_CANNOT_HAVE_FIRST_LAST_BLANK_IN_NAME.getMessage());
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "반짝할인,1개,1,2024-11-01,2024-11-30",
            "반짝할인,1,1개,2024-11-01,2024-11-30"
    })
    void 구매와_증정_개수에_정수가_입력_되지_않은_경우_예외가_발생한다(String input){
        // when & then
        assertThatIllegalArgumentException()
                .isThrownBy(() -> new Promotion(input))
                .withMessage(INPUT_VALUE_MUST_BE_NUMERIC.getMessage());
    }
}