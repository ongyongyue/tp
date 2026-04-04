package seedu.address.model.property;

import static java.util.Objects.requireNonNull;

import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;

import seedu.address.commons.util.StringUtil;
import seedu.address.commons.util.ToStringBuilder;

/**
 * Tests that a {@code Property} matches address keywords and numeric ranges.
 */
public class PropertyMatchesFilterPredicate implements Predicate<Property> {
    private final List<String> addressKeywords;
    private final long minPrice;
    private final long maxPrice;
    private final long minSize;
    private final long maxSize;

    /**
     * Constructs a {@code PropertyMatchesFilterPredicate} with the given address keywords and numeric ranges.
     */
    public PropertyMatchesFilterPredicate(
            List<String> addressKeywords, long minPrice, long maxPrice, long minSize, long maxSize) {
        requireNonNull(addressKeywords);
        this.addressKeywords = List.copyOf(addressKeywords);
        this.minPrice = minPrice;
        this.maxPrice = maxPrice;
        this.minSize = minSize;
        this.maxSize = maxSize;
    }

    @Override
    public boolean test(Property property) {
        boolean matchesAddress = addressKeywords.isEmpty()
                || addressKeywords.stream()
                .anyMatch(keyword -> StringUtil.containsWordIgnoreCase(property.getAddress().value, keyword));

        long propertyPrice = Long.parseLong(property.getPrice().value);
        long propertySize = Long.parseLong(property.getSize().value);

        boolean matchesPrice = propertyPrice >= minPrice && propertyPrice <= maxPrice;
        boolean matchesSize = propertySize >= minSize && propertySize <= maxSize;

        return matchesAddress && matchesPrice && matchesSize;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        if (!(other instanceof PropertyMatchesFilterPredicate)) {
            return false;
        }

        PropertyMatchesFilterPredicate otherPredicate = (PropertyMatchesFilterPredicate) other;
        return addressKeywords.equals(otherPredicate.addressKeywords)
                && minPrice == otherPredicate.minPrice
                && maxPrice == otherPredicate.maxPrice
                && minSize == otherPredicate.minSize
                && maxSize == otherPredicate.maxSize;
    }

    @Override
    public int hashCode() {
        return Objects.hash(addressKeywords, minPrice, maxPrice, minSize, maxSize);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("addressKeywords", addressKeywords)
                .add("minPrice", minPrice)
                .add("maxPrice", maxPrice)
                .add("minSize", minSize)
                .add("maxSize", maxSize)
                .toString();
    }
}
