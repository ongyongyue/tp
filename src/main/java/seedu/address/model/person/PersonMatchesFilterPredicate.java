package seedu.address.model.person;

import static java.util.Objects.requireNonNull;

import java.util.List;
import java.util.function.Predicate;

import seedu.address.commons.util.StringUtil;
import seedu.address.commons.util.ToStringBuilder;

/**
 * Tests that a {@code Person}'s {@code Name} or {@code Tag} matches any of the keywords given.
 */
public class PersonMatchesFilterPredicate implements Predicate<Person> {
    private final List<String> nameKeywords;
    private final List<String> tagKeywords;

    /**
     * Creates a PersonMatchesFilterPredicate with the given name keywords and tag keywords.
     * @param nameKeywords the list of keywords to match against the person's name
     * @param tagKeywords the list of keywords to match against the person's tags
     */
    public PersonMatchesFilterPredicate(List<String> nameKeywords, List<String> tagKeywords) {
        this.nameKeywords = nameKeywords;
        this.tagKeywords = tagKeywords;
    }

    @Override
    public boolean test(Person person) {
        requireNonNull(person);
        boolean matchesName = nameKeywords.isEmpty() || nameKeywords.stream()
                .anyMatch(keyword -> StringUtil.containsWordIgnoreCase(person.getName().fullName, keyword));
        boolean matchesTag = tagKeywords.isEmpty() || person.getTags().stream()
                .anyMatch(tag -> tagKeywords.stream().anyMatch(keyword -> tag.tagName.equalsIgnoreCase(keyword)));
        return matchesName && matchesTag;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof PersonMatchesFilterPredicate)) {
            return false;
        }

        PersonMatchesFilterPredicate otherPersonMatchesFilterPredicate = (PersonMatchesFilterPredicate) other;
        return nameKeywords.equals(otherPersonMatchesFilterPredicate.nameKeywords)
            && tagKeywords.equals(otherPersonMatchesFilterPredicate.tagKeywords);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
            .add("nameKeywords", nameKeywords)
            .add("tagKeywords", tagKeywords)
            .toString();
    }
}
