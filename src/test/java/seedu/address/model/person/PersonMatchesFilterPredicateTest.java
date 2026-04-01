package seedu.address.model.person;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.Test;

import seedu.address.testutil.PersonBuilder;

public class PersonMatchesFilterPredicateTest {

    @Test
    public void equals() {
        List<String> firstNameKeywordList = Collections.singletonList("first");
        List<String> secondNameKeywordList = Arrays.asList("first", "second");
        List<String> firstTagKeywordList = Collections.singletonList("friends");
        List<String> secondTagKeywordList = Arrays.asList("friends", "owesMoney");

        PersonMatchesFilterPredicate firstPredicate =
            new PersonMatchesFilterPredicate(firstNameKeywordList, firstTagKeywordList);
        PersonMatchesFilterPredicate secondPredicate =
            new PersonMatchesFilterPredicate(secondNameKeywordList, secondTagKeywordList);
        PersonMatchesFilterPredicate thirdPredicate =
            new PersonMatchesFilterPredicate(firstNameKeywordList, secondTagKeywordList);

        // same object -> returns true
        assertTrue(firstPredicate.equals(firstPredicate));

        // same values -> returns true
        PersonMatchesFilterPredicate firstPredicateCopy =
            new PersonMatchesFilterPredicate(firstNameKeywordList, firstTagKeywordList);
        assertTrue(firstPredicate.equals(firstPredicateCopy));

        // different types -> returns false
        assertFalse(firstPredicate.equals(1));

        // null -> returns false
        assertFalse(firstPredicate.equals(null));

        // different name keywords -> returns false
        assertFalse(firstPredicate.equals(secondPredicate));

        // same name keywords but different tag keywords -> returns false
        assertFalse(firstPredicate.equals(thirdPredicate));
    }

    @Test
    public void test_nameContainsKeywords_returnsTrue() {
        // One keyword
        PersonMatchesFilterPredicate predicate = new PersonMatchesFilterPredicate(
                Collections.singletonList("Alice"), Collections.emptyList());
        assertTrue(predicate.test(new PersonBuilder().withName("Alice Bob").build()));

        // Multiple keywords
        predicate = new PersonMatchesFilterPredicate(Arrays.asList("Alice", "Bob"), Collections.emptyList());
        assertTrue(predicate.test(new PersonBuilder().withName("Alice Bob").build()));

        // Only one matching keyword
        predicate = new PersonMatchesFilterPredicate(Arrays.asList("Bob", "Carol"), Collections.emptyList());
        assertTrue(predicate.test(new PersonBuilder().withName("Alice Carol").build()));

        // Mixed-case keywords
        predicate = new PersonMatchesFilterPredicate(Arrays.asList("aLIce", "bOB"), Collections.emptyList());
        assertTrue(predicate.test(new PersonBuilder().withName("Alice Bob").build()));
    }

    @Test
    public void test_nameDoesNotContainKeywords_returnsFalse() {
        // Zero keywords with no tag filter matches all persons
        PersonMatchesFilterPredicate predicate = new PersonMatchesFilterPredicate(
            Collections.emptyList(), Collections.emptyList());
        assertTrue(predicate.test(new PersonBuilder().withName("Alice").build()));

        // Non-matching keyword
        predicate = new PersonMatchesFilterPredicate(Arrays.asList("Carol"), Collections.emptyList());
        assertFalse(predicate.test(new PersonBuilder().withName("Alice Bob").build()));

        // Keywords match phone, email and address, but does not match name
        predicate = new PersonMatchesFilterPredicate(
            Arrays.asList("12345", "alice@email.com", "Main", "Street"), Collections.emptyList());
        assertFalse(predicate.test(new PersonBuilder().withName("Alice").withPhone("12345")
                .withEmail("alice@email.com").build()));
    }

    @Test
    public void test_tagContainsKeywords_returnsTrue() {
        PersonMatchesFilterPredicate predicate = new PersonMatchesFilterPredicate(
                Collections.emptyList(), Collections.singletonList("owesMoney"));
        assertTrue(predicate.test(new PersonBuilder().withTags("owesMoney", "friends").build()));

        predicate = new PersonMatchesFilterPredicate(
                Collections.emptyList(), Arrays.asList("friends", "vip"));
        assertTrue(predicate.test(new PersonBuilder().withTags("FRIENDS").build()));
    }

    @Test
    public void test_tagDoesNotContainKeywords_returnsFalse() {
        PersonMatchesFilterPredicate predicate = new PersonMatchesFilterPredicate(
                Collections.emptyList(), Collections.singletonList("owesMoney"));
        assertFalse(predicate.test(new PersonBuilder().withTags("friends").build()));
    }

    @Test
    public void test_nameAndTagKeywords_bothMustMatch() {
        PersonMatchesFilterPredicate predicate = new PersonMatchesFilterPredicate(
                Collections.singletonList("Alice"), Collections.singletonList("friends"));
        assertTrue(predicate.test(new PersonBuilder().withName("Alice Bob").withTags("friends").build()));

        assertFalse(predicate.test(new PersonBuilder().withName("Alice Bob").withTags("owesMoney").build()));
        assertFalse(predicate.test(new PersonBuilder().withName("Carol Dan").withTags("friends").build()));
    }

    @Test
    public void toStringMethod() {
        List<String> nameKeywords = List.of("keyword1", "keyword2");
        List<String> tagKeywords = List.of("friends");
        PersonMatchesFilterPredicate predicate = new PersonMatchesFilterPredicate(nameKeywords, tagKeywords);

        String expected = PersonMatchesFilterPredicate.class.getCanonicalName()
                + "{nameKeywords=" + nameKeywords + ", tagKeywords=" + tagKeywords + "}";
        assertEquals(expected, predicate.toString());
    }
}
