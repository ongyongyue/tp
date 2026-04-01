package seedu.address.model.property;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.AppUtil.checkArgument;

/**
 * Represents the price of a property.
 */
public class Price {
    public static final String MESSAGE_CONSTRAINTS =
            "Price should only contain digits and be at most 10 digits long.";

    public static final String VALIDATION_REGEX = "\\d+";

    public final String value;

    /**
     * Constructs a {@code Price}.
     *
     * @param price A valid price.
     */
    public Price(String price) {
        requireNonNull(price);
        String trimmedPrice = price.trim();
        checkArgument(isValidPrice(trimmedPrice), MESSAGE_CONSTRAINTS);
        this.value = String.valueOf(Integer.parseInt(trimmedPrice));
    }

    /**
     * Returns true if a given string is a valid price.
     */
    public static boolean isValidPrice(String test) {
        return !test.isBlank()
                && test.length() <= 10
                && test.matches(VALIDATION_REGEX);
    }

    @Override
    public String toString() {
        return value;
    }

    @Override
    public boolean equals(Object other) {
        return other == this
                || (other instanceof Price
                && value.equals(((Price) other).value));
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }
}
