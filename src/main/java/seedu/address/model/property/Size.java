package seedu.address.model.property;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.AppUtil.checkArgument;

/**
 * Represents the size of a property.
 */
public class Size {
    public static final String MESSAGE_CONSTRAINTS =
            "Size should only contain digits and be at most 10 digits long.";

    public static final String VALIDATION_REGEX = "\\d+";

    public final String value;

    /**
     * Constructs a {@code Size}.
     *
     * @param size A valid size.
     */
    public Size(String size) {
        requireNonNull(size);
        String trimmedSize = size.trim();
        checkArgument(isValidSize(trimmedSize), MESSAGE_CONSTRAINTS);
        this.value = String.valueOf(Integer.parseInt(trimmedSize));
    }

    /**
     * Returns true if a given string is a valid size.
     */
    public static boolean isValidSize(String test) {
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
                || (other instanceof Size
                && value.equals(((Size) other).value));
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }
}
