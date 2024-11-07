package store.util;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class CsvReaderTest {
    @Test
    void 상품_정보를_가져올_수_있다() {
        // given
        List<String> productsData = null;

        // when
        productsData = CsvReader.getProductsData();

        // then
        assertThat(productsData).isNotEqualTo(null);
    }

    @Test
    void 프로모션_정보를_가져올_수_있다() {
        // given
        List<String> promotionsData = null;

        // when
        promotionsData = CsvReader.getPromotionsData();

        // then
        assertThat(promotionsData).isNotEqualTo(null);
    }
}