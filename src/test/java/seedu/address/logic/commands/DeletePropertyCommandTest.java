package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandFailure;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_PROPERTY;
import static seedu.address.testutil.TypicalIndexes.INDEX_SECOND_PROPERTY;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.Messages;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.property.Price;
import seedu.address.model.property.Property;
import seedu.address.model.property.PropertyAddress;
import seedu.address.model.property.Size;
import seedu.address.testutil.TypicalIndexes;
import seedu.address.testutil.TypicalPersons;

/**
 * Contains integration tests (interaction with the Model) and unit tests for
 * {@code DeletePropertyCommand}.
 */
public class DeletePropertyCommandTest {

    private Model model;

    @BeforeEach
    public void setUp() {
        model = new ModelManager(TypicalPersons.getTypicalAddressBook(), new UserPrefs());
    }

    @Test
    public void execute_validIndexUnfilteredList_success() throws CommandException {
        // Add a property to test
        Property testProperty = new Property(new PropertyAddress("311 Clementi Ave 2, #02-25"),
                new Price("1200000"), new Size("1200"));
        AddPropertyCommand addPropertyCommand = new AddPropertyCommand(List.of(TypicalIndexes.INDEX_FIRST_PERSON),
                testProperty);
        addPropertyCommand.execute(model);

        // Get the property from the list
        List<Property> propertyList = model.getFilteredPropertyList();
        assertTrue(!propertyList.isEmpty(), "Property should be added");

        int initialPropertyCount = model.getFilteredPropertyList().size();

        DeletePropertyCommand deleteCommand = new DeletePropertyCommand(INDEX_FIRST_PROPERTY);
        String expectedMessage = String.format(DeletePropertyCommand.MESSAGE_SUCCESS, testProperty);
        CommandResult result = deleteCommand.execute(model);

        assertEquals(expectedMessage, result.getFeedbackToUser());
        assertEquals(initialPropertyCount - 1, model.getFilteredPropertyList().size());
    }

    @Test
    public void execute_invalidIndexUnfilteredList_throwsCommandException() {
        // Add a property first
        Property testProperty = new Property(new PropertyAddress("311 Clementi Ave 2, #02-25"),
                new Price("1200000"), new Size("1200"));
        try {
            AddPropertyCommand addPropertyCommand = new AddPropertyCommand(List.of(TypicalIndexes.INDEX_FIRST_PERSON),
                    testProperty);
            addPropertyCommand.execute(model);
        } catch (CommandException e) {
            // Ignore
        }

        Index outOfBoundIndex = Index.fromOneBased(model.getFilteredPropertyList().size() + 1);
        DeletePropertyCommand deleteCommand = new DeletePropertyCommand(outOfBoundIndex);

        assertCommandFailure(deleteCommand, model, Messages.MESSAGE_INVALID_PROPERTY_DISPLAYED_INDEX);
    }

    @Test
    public void execute_noProperties_throwsCommandException() {
        // Model has no properties by default
        DeletePropertyCommand deleteCommand = new DeletePropertyCommand(INDEX_FIRST_PROPERTY);

        assertCommandFailure(deleteCommand, model, DeletePropertyCommand.MESSAGE_NO_PROPERTIES);
    }

    @Test
    public void execute_deleteMultipleProperties_success() throws CommandException {
        // Add two properties
        Property property1 = new Property(new PropertyAddress("311 Clementi Ave 2, #02-25"),
                new Price("1200000"), new Size("1200"));
        Property property2 = new Property(new PropertyAddress("412 Clementi Ave 3, #03-25"),
                new Price("1500000"), new Size("1500"));

        AddPropertyCommand addPropertyCommand1 = new AddPropertyCommand(List.of(TypicalIndexes.INDEX_FIRST_PERSON),
                property1);
        addPropertyCommand1.execute(model);

        AddPropertyCommand addPropertyCommand2 = new AddPropertyCommand(List.of(TypicalIndexes.INDEX_SECOND_PERSON),
                property2);
        addPropertyCommand2.execute(model);

        assertEquals(2, model.getFilteredPropertyList().size());

        // Delete first property
        DeletePropertyCommand deleteCommand = new DeletePropertyCommand(INDEX_FIRST_PROPERTY);
        String expectedMessage = String.format(DeletePropertyCommand.MESSAGE_SUCCESS, property1);
        CommandResult result = deleteCommand.execute(model);

        assertEquals(expectedMessage, result.getFeedbackToUser());
        assertEquals(1, model.getFilteredPropertyList().size());
    }

    @Test
    public void equals() {
        DeletePropertyCommand deleteFirstCommand = new DeletePropertyCommand(INDEX_FIRST_PROPERTY);
        DeletePropertyCommand deleteSecondCommand = new DeletePropertyCommand(INDEX_SECOND_PROPERTY);

        // same object -> returns true
        assertTrue(deleteFirstCommand.equals(deleteFirstCommand));

        // same values -> returns true
        DeletePropertyCommand deleteFirstCommandCopy = new DeletePropertyCommand(INDEX_FIRST_PROPERTY);
        assertTrue(deleteFirstCommand.equals(deleteFirstCommandCopy));

        // different types -> returns false
        assertFalse(deleteFirstCommand.equals(1));

        // null -> returns false
        assertFalse(deleteFirstCommand.equals(null));

        // different property -> returns false
        assertFalse(deleteFirstCommand.equals(deleteSecondCommand));
    }

    @Test
    public void toStringMethod() {
        Index targetIndex = Index.fromOneBased(1);
        DeletePropertyCommand deleteCommand = new DeletePropertyCommand(targetIndex);
        String expected = DeletePropertyCommand.class.getSimpleName() + "{index=" + targetIndex + "}";
        assertEquals(expected, deleteCommand.toString());
    }
}

