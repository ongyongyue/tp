package seedu.address.model.property;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Arrays;
import java.util.Collections;

import org.junit.jupiter.api.Test;

public class PropertyTypeContainsKeywordsPredicateTest {

    private final PropertyAddress validAddress = new PropertyAddress("123 Main Street");
    private final Price validPrice = new Price("500000");
    private final Size validSize = new Size("1200");

    @Test
    public void test_propertyTypeContainsKeywords_returnsTrue() {
        // One keyword
        PropertyTypeContainsKeywordsPredicate predicate =
                new PropertyTypeContainsKeywordsPredicate(Collections.singletonList("HDB"));
        assertTrue(predicate.test(new Property(validAddress, validPrice, validSize, new PropertyType("HDB"))));

        // Multiple keywords
        predicate = new PropertyTypeContainsKeywordsPredicate(Arrays.asList("HDB", "Condo"));
        assertTrue(predicate.test(new Property(validAddress, validPrice, validSize, new PropertyType("HDB"))));

        // Only one matching keyword
        predicate = new PropertyTypeContainsKeywordsPredicate(Arrays.asList("HDB", "Condo"));
        assertTrue(predicate.test(new Property(validAddress, validPrice, validSize, new PropertyType("Condo"))));

        // Mixed case keyword
        predicate = new PropertyTypeContainsKeywordsPredicate(Arrays.asList("hDb"));
        assertTrue(predicate.test(new Property(validAddress, validPrice, validSize, new PropertyType("HDB"))));
    }

    @Test
    public void test_propertyTypeDoesNotContainKeywords_returnsFalse() {
        // Zero keywords
        PropertyTypeContainsKeywordsPredicate predicate =
                new PropertyTypeContainsKeywordsPredicate(Collections.emptyList());
        assertFalse(predicate.test(new Property(validAddress, validPrice, validSize, new PropertyType("HDB"))));

        // Non-matching keyword
        predicate = new PropertyTypeContainsKeywordsPredicate(Arrays.asList("Condo"));
        assertFalse(predicate.test(new Property(validAddress, validPrice, validSize, new PropertyType("HDB"))));

        // Keywords match name, but does not match property type
        predicate = new PropertyTypeContainsKeywordsPredicate(Arrays.asList("Main", "Street"));
        assertFalse(predicate.test(new Property(validAddress, validPrice, validSize, new PropertyType("HDB"))));
    }

    @Test
    public void test_propertyTypeIsNull_returnsFalse() {
        PropertyTypeContainsKeywordsPredicate predicate =
                new PropertyTypeContainsKeywordsPredicate(Arrays.asList("HDB"));
        assertFalse(predicate.test(new Property(validAddress, validPrice, validSize)));
    }

    @Test
    public void equals() {
        PropertyTypeContainsKeywordsPredicate firstPredicate =
                new PropertyTypeContainsKeywordsPredicate(Collections.singletonList("first"));
        PropertyTypeContainsKeywordsPredicate secondPredicate =
                new PropertyTypeContainsKeywordsPredicate(Collections.singletonList("second"));

        // same object -> returns true
        assertTrue(firstPredicate.equals(firstPredicate));

        // same values -> returns true
        PropertyTypeContainsKeywordsPredicate firstPredicateCopy =
                new PropertyTypeContainsKeywordsPredicate(Collections.singletonList("first"));
        assertTrue(firstPredicate.equals(firstPredicateCopy));

        // different types -> returns false
        assertFalse(firstPredicate.equals(1));

        // null -> returns false
        assertFalse(firstPredicate.equals(null));

        // different predicate -> returns false
        assertFalse(firstPredicate.equals(secondPredicate));
    }
}
