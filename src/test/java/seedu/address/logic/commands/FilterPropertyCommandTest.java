package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.logic.Messages.MESSAGE_PROPERTIES_LISTED_OVERVIEW;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_PERSON;
import static seedu.address.testutil.TypicalIndexes.INDEX_SECOND_PERSON;
import static seedu.address.testutil.TypicalPersons.getTypicalAddressBook;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import seedu.address.commons.core.index.Index;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.property.Price;
import seedu.address.model.property.Property;
import seedu.address.model.property.PropertyAddress;
import seedu.address.model.property.PropertyAddressContainsKeywordsPredicate;
import seedu.address.model.property.Size;

/**
 * Contains integration tests (interaction with the Model) for {@code FilterPropertyCommand}.
 */
public class FilterPropertyCommandTest {
    private Model model;
    private Model expectedModel;

    @BeforeEach
    public void setUp() throws Exception {
        model = new ModelManager(getTypicalAddressBook(), new UserPrefs());
        expectedModel = new ModelManager(getTypicalAddressBook(), new UserPrefs());

        addPropertyToModel(model, INDEX_FIRST_PERSON, "311 Clementi Ave 2", "1200000", "1200");
        addPropertyToModel(model, INDEX_FIRST_PERSON, "50 Jurong East St 24", "850000", "1000");
        addPropertyToModel(model, INDEX_SECOND_PERSON, "10 Punggol Walk", "950000", "1100");

        addPropertyToModel(expectedModel, INDEX_FIRST_PERSON, "311 Clementi Ave 2", "1200000", "1200");
        addPropertyToModel(expectedModel, INDEX_FIRST_PERSON, "50 Jurong East St 24", "850000", "1000");
        addPropertyToModel(expectedModel, INDEX_SECOND_PERSON, "10 Punggol Walk", "950000", "1100");
    }

    @Test
    public void equals() {
        PropertyAddressContainsKeywordsPredicate firstPredicate =
                new PropertyAddressContainsKeywordsPredicate(Collections.singletonList("first"));
        PropertyAddressContainsKeywordsPredicate secondPredicate =
                new PropertyAddressContainsKeywordsPredicate(Collections.singletonList("second"));

        FilterPropertyCommand findFirstCommand = new FilterPropertyCommand(firstPredicate);
        FilterPropertyCommand findSecondCommand = new FilterPropertyCommand(secondPredicate);

        // same object -> returns true
        assertTrue(findFirstCommand.equals(findFirstCommand));

        // same values -> returns true
        FilterPropertyCommand findFirstCommandCopy = new FilterPropertyCommand(firstPredicate);
        assertTrue(findFirstCommand.equals(findFirstCommandCopy));

        // different types -> returns false
        assertFalse(findFirstCommand.equals(1));

        // null -> returns false
        assertFalse(findFirstCommand.equals(null));

        // different predicate -> returns false
        assertFalse(findFirstCommand.equals(findSecondCommand));
    }

    @Test
    public void execute_zeroKeywords_noPropertyFound() {
        String expectedMessage = String.format(MESSAGE_PROPERTIES_LISTED_OVERVIEW, 0);
        PropertyAddressContainsKeywordsPredicate predicate = preparePredicate(" ");
        FilterPropertyCommand command = new FilterPropertyCommand(predicate);
        expectedModel.updateFilteredPropertyList(predicate);
        expectedModel.updateFilteredPersonList(person -> expectedModel.getFilteredPropertyList().stream()
                .anyMatch(property -> person.getProperties().contains(property)));

        assertCommandSuccess(command, model, expectedMessage, expectedModel);
        assertEquals(Collections.emptyList(), model.getFilteredPropertyList());
        assertEquals(Collections.emptyList(), model.getFilteredPersonList());
    }

    @Test
    public void execute_multipleKeywords_multiplePropertiesAndOwnersFound() {
        String expectedMessage = String.format(MESSAGE_PROPERTIES_LISTED_OVERVIEW, 2);
        PropertyAddressContainsKeywordsPredicate predicate = preparePredicate("Clementi Punggol");
        FilterPropertyCommand command = new FilterPropertyCommand(predicate);
        expectedModel.updateFilteredPropertyList(predicate);
        expectedModel.updateFilteredPersonList(person -> expectedModel.getFilteredPropertyList().stream()
                .anyMatch(property -> person.getProperties().contains(property)));

        assertCommandSuccess(command, model, expectedMessage, expectedModel);
        assertEquals(2, model.getFilteredPropertyList().size());
        assertEquals(2, model.getFilteredPersonList().size());
        assertTrue(model.getFilteredPersonList().stream()
                .allMatch(person -> model.getFilteredPropertyList().stream()
                        .anyMatch(property -> person.getProperties().contains(property))));
    }

    @Test
    public void toStringMethod() {
        PropertyAddressContainsKeywordsPredicate predicate =
                new PropertyAddressContainsKeywordsPredicate(Arrays.asList("keyword"));
        FilterPropertyCommand filterPropertyCommand = new FilterPropertyCommand(predicate);
        String expected = FilterPropertyCommand.class.getCanonicalName() + "{predicate=" + predicate + "}";
        assertEquals(expected, filterPropertyCommand.toString());
    }

    /**
     * Parses {@code userInput} into a {@code PropertyAddressContainsKeywordsPredicate}.
     */
    private PropertyAddressContainsKeywordsPredicate preparePredicate(String userInput) {
        return new PropertyAddressContainsKeywordsPredicate(Arrays.asList(userInput.split("\\s+")));
    }

    private void addPropertyToModel(Model model, Index index, String address, String price, String size)
            throws Exception {
        Property property = new Property(new PropertyAddress(address), new Price(price), new Size(size));
        new AddPropertyCommand(List.of(index), property).execute(model);
    }
}
