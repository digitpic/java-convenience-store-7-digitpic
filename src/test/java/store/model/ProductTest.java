package store.model;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static store.constants.ExceptionMessage.INPUT_VALUE_MUST_BE_NUMERIC;
import static store.constants.ExceptionMessage.INVALID_PRODUCT_INFORMATION_COUNT;
import static store.constants.ExceptionMessage.PRODUCT_CANNOT_HAVE_FIRST_LAST_BLANK_IN_NAME;
import static store.constants.ExceptionMessage.PRODUCT_INFORMATION_MUST_CONTAIN_COMMA;

class ProductTest {
    @Test
    void toString_메서드_오버라이딩_테스트() {
        // given
        String input = "콜라,1000,10,null";
        Product product = new Product(input);

        // when
        String real = product.toString();

        // then
        assertThat(real).contains("-")
                .contains("콜라")
                .contains("1,000원")
                .contains("10개");
    }

    @Test
    void 값에_쉼표가_포함_되지_않는_경우_예외가_발생한다() {
        // given
        String input = "콜라100010null";

        // when & then
        assertThatIllegalArgumentException()
                .isThrownBy(() -> new Product(input))
                .withMessage(PRODUCT_INFORMATION_MUST_CONTAIN_COMMA.getMessage());
    }

    @ParameterizedTest
    @ValueSource(strings = { "콜라,1000,10", "콜라,1000,10,null,null" })
    void 값이_네_개가_아닌_경우_예외가_발생한다(String input) {
        // when & then
        assertThatIllegalArgumentException()
                .isThrownBy(() -> new Product(input))
                .withMessage(INVALID_PRODUCT_INFORMATION_COUNT.getMessage());
    }

    @ParameterizedTest
    @ValueSource(strings = {
            " 콜라,1000,10,null",
            "콜라 ,1000,10,null",
            "콜라, 1000,10,null",
            "콜라,1000 ,10,null",
            "콜라,1000, 10,null",
            "콜라,1000,10 ,null",
            "콜라,1000,10, null",
            "콜라,1000,10,null "
    })
    void 각_요소의_맨_앞_맨_뒤에_공백이_있는_경우_예외가_발생한다(String input) {
        // when & then
        assertThatIllegalArgumentException()
                .isThrownBy(() -> new Product(input))
                .withMessage(PRODUCT_CANNOT_HAVE_FIRST_LAST_BLANK_IN_NAME.getMessage());
    }

    @Test
    void 가격_정보가_정수로_입력_되지_않은_경우_예외가_발생한다() {
        // given
        String input = "콜라,천원,10,null";

        // when & then
        assertThatIllegalArgumentException()
                .isThrownBy(() -> new Product(input))
                .withMessage(INPUT_VALUE_MUST_BE_NUMERIC.getMessage());
    }

    @Test
    void 개수_정보가_정수로_입력_되지_않은_경우_예외가_발생한다() {
        // given
        String input = "콜라,1000,열개,null";

        // when & then
        assertThatIllegalArgumentException()
                .isThrownBy(() -> new Product(input))
                .withMessage(INPUT_VALUE_MUST_BE_NUMERIC.getMessage());
    }
}