package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.address.logic.commands.FilterTypeCommand.MESSAGE_PROPERTIES_LISTED;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_PERSON;
import static seedu.address.testutil.TypicalIndexes.INDEX_SECOND_PERSON;
import static seedu.address.testutil.TypicalIndexes.INDEX_THIRD_PERSON;
import static seedu.address.testutil.TypicalPersons.getTypicalAddressBook;

import java.util.Arrays;
import java.util.Collections;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import seedu.address.commons.core.index.Index;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.property.Price;
import seedu.address.model.property.Property;
import seedu.address.model.property.PropertyAddress;
import seedu.address.model.property.PropertyType;
import seedu.address.model.property.PropertyTypeContainsKeywordsPredicate;
import seedu.address.model.property.Size;

/**
 * Contains integration tests (interaction with the Model) for {@code FilterTypeCommand}.
 */
public class FilterTypeCommandTest {
    private Model model;
    private Model expectedModel;

    @BeforeEach
    public void setUp() throws Exception {
        model = new ModelManager(getTypicalAddressBook(), new UserPrefs());
        expectedModel = new ModelManager(getTypicalAddressBook(), new UserPrefs());

        // Add properties with different types
        addPropertyWithTypeToModel(model, INDEX_FIRST_PERSON, "311 Clementi Ave 2", "1200000", "1200", "HDB");
        addPropertyWithTypeToModel(model, INDEX_FIRST_PERSON, "50 Jurong East St 24", "8500", "1000", "Condo");
        addPropertyWithTypeToModel(model, INDEX_SECOND_PERSON, "10 Punggol Walk", "950000", "1100", "HDB");
        addPropertyWithTypeToModel(model, INDEX_THIRD_PERSON, "20 Orchard Road", "2000000", "800", "Condo");

        addPropertyWithTypeToModel(expectedModel, INDEX_FIRST_PERSON, "311 Clementi Ave 2", "1200000", "1200", "HDB");
        addPropertyWithTypeToModel(expectedModel, INDEX_FIRST_PERSON, "50 Jurong East St 24", "8500", "1000", "Condo");
        addPropertyWithTypeToModel(expectedModel, INDEX_SECOND_PERSON, "10 Punggol Walk", "950000", "1100", "HDB");
        addPropertyWithTypeToModel(expectedModel, INDEX_THIRD_PERSON, "20 Orchard Road", "2000000", "800", "Condo");
    }

    @Test
    public void execute_singleTypeKeyword_multiplePropertiesFound() {
        String expectedMessage = String.format(MESSAGE_PROPERTIES_LISTED, 2);
        PropertyTypeContainsKeywordsPredicate predicate = preparePredicate("HDB");
        FilterTypeCommand command = new FilterTypeCommand(predicate);
        expectedModel.updateFilteredPropertyList(predicate);
        expectedModel.updateFilteredPersonList(person -> expectedModel.getFilteredPropertyList().stream()
                .anyMatch(property -> person.getProperties().contains(property)));

        assertCommandSuccess(command, model, expectedMessage, expectedModel);
        assertEquals(2, model.getFilteredPropertyList().size());
        assertEquals(2, model.getFilteredPersonList().size());
    }

    @Test
    public void execute_multipleTypeKeywords_multiplePropertiesFound() {
        String expectedMessage = String.format(MESSAGE_PROPERTIES_LISTED, 4);
        PropertyTypeContainsKeywordsPredicate predicate = preparePredicate("HDB Condo");
        FilterTypeCommand command = new FilterTypeCommand(predicate);
        expectedModel.updateFilteredPropertyList(predicate);
        expectedModel.updateFilteredPersonList(person -> expectedModel.getFilteredPropertyList().stream()
                .anyMatch(property -> person.getProperties().contains(property)));

        assertCommandSuccess(command, model, expectedMessage, expectedModel);
        assertEquals(4, model.getFilteredPropertyList().size());
        assertEquals(3, model.getFilteredPersonList().size());
    }

    @Test
    public void execute_caseInsensitiveKeywords_multiplePropertiesFound() {
        String expectedMessage = String.format(MESSAGE_PROPERTIES_LISTED, 2);
        PropertyTypeContainsKeywordsPredicate predicate = preparePredicate("hdb");
        FilterTypeCommand command = new FilterTypeCommand(predicate);
        expectedModel.updateFilteredPropertyList(predicate);
        expectedModel.updateFilteredPersonList(person -> expectedModel.getFilteredPropertyList().stream()
                .anyMatch(property -> person.getProperties().contains(property)));

        assertCommandSuccess(command, model, expectedMessage, expectedModel);
        assertEquals(2, model.getFilteredPropertyList().size());
        assertEquals(2, model.getFilteredPersonList().size());
    }

    @Test
    public void execute_noMatchingType_noPropertiesFound() {
        String expectedMessage = String.format(MESSAGE_PROPERTIES_LISTED, 0);
        PropertyTypeContainsKeywordsPredicate predicate = preparePredicate("Apartment");
        FilterTypeCommand command = new FilterTypeCommand(predicate);
        expectedModel.updateFilteredPropertyList(predicate);
        expectedModel.updateFilteredPersonList(person -> expectedModel.getFilteredPropertyList().stream()
                .anyMatch(property -> person.getProperties().contains(property)));

        assertCommandSuccess(command, model, expectedMessage, expectedModel);
        assertEquals(0, model.getFilteredPropertyList().size());
        assertEquals(0, model.getFilteredPersonList().size());
    }

    @Test
    public void equals() {
        PropertyTypeContainsKeywordsPredicate firstPredicate =
                new PropertyTypeContainsKeywordsPredicate(Collections.singletonList("first"));
        PropertyTypeContainsKeywordsPredicate secondPredicate =
                new PropertyTypeContainsKeywordsPredicate(Collections.singletonList("second"));

        FilterTypeCommand findFirstCommand = new FilterTypeCommand(firstPredicate);
        FilterTypeCommand findSecondCommand = new FilterTypeCommand(secondPredicate);

        // same object -> returns true
        assertTrue(findFirstCommand.equals(findFirstCommand));

        // same values -> returns true
        FilterTypeCommand findFirstCommandCopy = new FilterTypeCommand(firstPredicate);
        assertTrue(findFirstCommand.equals(findFirstCommandCopy));

        // different types -> returns false
        assertFalse(findFirstCommand.equals(1));

        // null -> returns false
        assertFalse(findFirstCommand.equals(null));

        // different predicate -> returns false
        assertFalse(findFirstCommand.equals(findSecondCommand));
    }

    @Test
    public void toStringMethod() {
        PropertyTypeContainsKeywordsPredicate predicate =
                new PropertyTypeContainsKeywordsPredicate(Arrays.asList("HDB"));
        FilterTypeCommand filterTypeCommand = new FilterTypeCommand(predicate);
        String expected = FilterTypeCommand.class.getCanonicalName() + "{predicate=" + predicate + "}";
        assertEquals(expected, filterTypeCommand.toString());
    }

    /**
     * Parses {@code userInput} into a {@code PropertyTypeContainsKeywordsPredicate}.
     */
    private PropertyTypeContainsKeywordsPredicate preparePredicate(String userInput) {
        return new PropertyTypeContainsKeywordsPredicate(Arrays.asList(userInput.split("\\s+")));
    }

    private void addPropertyWithTypeToModel(Model model, Index index, String address, String price,
                                           String size, String type) throws Exception {
        Property property = new Property(new PropertyAddress(address), new Price(price),
                                       new Size(size), new PropertyType(type));
        new AddPropertyCommand(index, property).execute(model);
    }
}




