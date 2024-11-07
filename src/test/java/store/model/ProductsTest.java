package store.model;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static store.constants.ExceptionMessage.ERROR_WITH_OPENING_CSV_FILE;

class ProductsTest {
    @Test
    void 상품_정보의_값이_null_인_경우_예외가_발생한다() {
        // given
        List<String> productsData = null;

        // when & then
        assertThatIllegalArgumentException()
                .isThrownBy(() -> new Products(productsData))
                .withMessage(ERROR_WITH_OPENING_CSV_FILE.getMessage());
    }
}