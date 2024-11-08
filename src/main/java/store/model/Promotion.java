package store.model;

import java.util.Arrays;
import java.util.List;

import static store.constants.ExceptionMessage.INPUT_CANNOT_HAVE_FIRST_LAST_BLANK;
import static store.constants.ExceptionMessage.INPUT_MUST_CONTAIN_COMMA;
import static store.constants.ExceptionMessage.INPUT_VALUE_MUST_BE_NUMERIC;
import static store.constants.ExceptionMessage.INVALID_PROMOTION_INFORMATION_COUNT;

public class Promotion {
    private static final int NAME_INDEX = 0;
    private static final int BUY_COUNT_INDEX = 1;
    private static final int GET_COUNT_INDEX = 2;
    private static final int EVENT_START_DATE_INDEX = 3;
    private static final int EVENT_END_DATE_INDEX = 4;
    private static final int INFORMATION_COUNT = 5;

    private final String name;
    private final int buyCount;
    private final int getCount;
    private final String eventStartDate;
    private final String eventEndDate;

    public Promotion(String information) {
        validate(information);
        List<String> separated = separateByComma(information);
        this.name = separated.get(NAME_INDEX);
        this.buyCount = Integer.parseInt(separated.get(BUY_COUNT_INDEX));
        this.getCount = Integer.parseInt(separated.get(GET_COUNT_INDEX));
        this.eventStartDate = separated.get(EVENT_START_DATE_INDEX);
        this.eventEndDate = separated.get(EVENT_END_DATE_INDEX);
    }

    private void validate(final String information) {
        validateComma(information);
    }

    private void validateComma(final String information) {
        if (information.contains(",")) {
            return;
        }
        throw new IllegalArgumentException(INPUT_MUST_CONTAIN_COMMA.getMessage());
    }

    private List<String> separateByComma(final String information) {
        List<String> separated = Arrays.stream(information.split(","))
                .toList();
        validateAfterSeparate(separated);
        return separated;
    }

    private void validateAfterSeparate(final List<String> separated) {
        validateCount(separated);
        validateStrip(separated);
        validateNumeric(separated.get(BUY_COUNT_INDEX));
        validateNumeric(separated.get(GET_COUNT_INDEX));
    }

    private void validateCount(final List<String> separated) {
        if (separated.size() == INFORMATION_COUNT) {
            return;
        }
        throw new IllegalArgumentException(INVALID_PROMOTION_INFORMATION_COUNT.getMessage());
    }

    private void validateStrip(final List<String> separated) {
        separated.forEach((element) -> {
            String stripped = element.strip();
            if (element.equals(stripped)) {
                return;
            }
            throw new IllegalArgumentException(INPUT_CANNOT_HAVE_FIRST_LAST_BLANK.getMessage());
        });
    }

    private void validateNumeric(final String input) {
        try {
            Integer.parseInt(input);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException(INPUT_VALUE_MUST_BE_NUMERIC.getMessage());
        }
    }
}
