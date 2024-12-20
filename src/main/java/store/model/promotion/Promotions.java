package store.model.promotion;

import java.util.ArrayList;
import java.util.List;

import static store.constants.ExceptionMessage.ERROR_WITH_OPENING_CSV_FILE;

public class Promotions {
    private final List<Promotion> promotions;

    public Promotions(final List<String> promotionsInformation) {
        validate(promotionsInformation);
        this.promotions = parseToPromotion(promotionsInformation);
    }

    public Promotion findByName(final String name) {
        return promotions.stream()
                .filter((promotion) -> promotion.getName().equals(name))
                .findFirst()
                .orElse(null);
    }

    private void validate(final List<String> promotionsInformation) {
        validateNull(promotionsInformation);
    }

    private void validateNull(final List<String> promotionsInformation) {
        if (promotionsInformation == null) {
            throw new IllegalArgumentException(ERROR_WITH_OPENING_CSV_FILE.getMessage());
        }
    }

    private List<Promotion> parseToPromotion(final List<String> promotionsInformation) {
        promotionsInformation.removeFirst();
        List<Promotion> promotions = new ArrayList<>();
        for (String promotionInformation : promotionsInformation) {
            promotions.add(new Promotion(promotionInformation));
        }
        return promotions;
    }
}
