package seedu.address.model.property;

import static java.util.Objects.requireNonNull;

import java.util.List;
import java.util.function.Predicate;

import seedu.address.commons.util.StringUtil;
import seedu.address.commons.util.ToStringBuilder;

/**
 * Tests that a {@code Property}'s {@code PropertyType} matches any of the keywords given.
 */
public class PropertyTypeContainsKeywordsPredicate implements Predicate<Property> {
    private final List<String> keywords;

    /**
         * Constructs a {@code PropertyTypeContainsKeywordsPredicate} with the given keywords.
     */
    public PropertyTypeContainsKeywordsPredicate(List<String> keywords) {
        requireNonNull(keywords);
        this.keywords = keywords;
    }

    @Override
    public boolean test(Property property) {
        if (property.getPropertyType() == null) {
            return false;
        }
        return keywords.stream()
                .anyMatch(keyword -> StringUtil.containsWordIgnoreCase(property.getPropertyType().toString(), keyword));
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof PropertyTypeContainsKeywordsPredicate)) {
            return false;
        }

        PropertyTypeContainsKeywordsPredicate otherPredicate = (PropertyTypeContainsKeywordsPredicate) other;
        return keywords.equals(otherPredicate.keywords);
    }

    @Override
    public String toString() {

        return new ToStringBuilder(this)
                .add("keywords", keywords)
                .toString();
    }
}

