package seedu.address.model.property;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.AppUtil.checkArgument;

/**
 * Represents the address of a property.
 */
public class PropertyAddress {

    public static final String MESSAGE_CONSTRAINTS =
            "Property address should not be blank, should be at most 100 characters long, "
                    + "and should only contain letters, numbers, spaces, and , . # -";

    public static final String VALIDATION_REGEX = "[A-Za-z0-9 ,.#\\-]+";

    public final String value;

    /**
     * Constructs a {@code PropertyAddress}.
     *
     * @param address A valid property address.
     */
    public PropertyAddress(String address) {
        requireNonNull(address);
        String trimmedAddress = address.trim();
        checkArgument(isValidPropertyAddress(trimmedAddress), MESSAGE_CONSTRAINTS);
        this.value = trimmedAddress;
    }

    /**
     * Returns true if a given string is a valid property address.
     */
    public static boolean isValidPropertyAddress(String test) {
        return !test.isBlank()
                && test.length() <= 100
                && test.matches(VALIDATION_REGEX);
    }

    @Override
    public String toString() {
        return value;
    }

    @Override
    public boolean equals(Object other) {
        return other == this
                || (other instanceof PropertyAddress
                && value.equals(((PropertyAddress) other).value));
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }
}
