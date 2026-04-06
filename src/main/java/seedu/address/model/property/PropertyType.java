package seedu.address.model.property;

import static java.util.Objects.requireNonNull;

/**
 * Represents a Property's type in the address book.
 * Examples include HDB and Condo only.
 */
public class PropertyType {

    public static final String MESSAGE_CONSTRAINTS =
            "Property type should be either HDB or Condo, and it should not be blank.";

    public static final String VALIDATION_REGEX = "(?i)^(HDB|Condo)$";

    public final String value;

    /**
     * Constructs a {@code PropertyType}.
     *
     * @param propertyType A valid property type string.
     */
    public PropertyType(String propertyType) {
        requireNonNull(propertyType);
        if (!isValidPropertyType(propertyType)) {
            throw new IllegalArgumentException(MESSAGE_CONSTRAINTS);
        }
        this.value = normalizePropertyType(propertyType);
    }
    /**
    * * Normalizes the input to its canonical form: "HDB" or "Condo", case-insensitively.
    *
    * @param input A valid property type string.
    */
    public static String normalizePropertyType(String input) {
        if (input.equalsIgnoreCase("hdb")) {
            return "HDB";
        } else {
            return "Condo";
        }
    }

    /**
     * Returns true if the given string is a valid property type.
     *
     * @param test The string to validate.
     */
    public static boolean isValidPropertyType(String test) {
        return test.matches(VALIDATION_REGEX);
    }

    @Override
    public String toString() {
        return value;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if (!(other instanceof PropertyType)) {
            return false;
        }
        PropertyType otherType = (PropertyType) other;
        return value.equals(otherType.value);
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }
}
