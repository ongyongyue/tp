package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.testutil.Assert.assertThrows;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_PERSON;
import static seedu.address.testutil.TypicalIndexes.INDEX_SECOND_PERSON;
import static seedu.address.testutil.TypicalIndexes.INDEX_THIRD_PERSON;
import static seedu.address.testutil.TypicalPersons.getTypicalAddressBook;

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
import seedu.address.model.property.PropertyType;
import seedu.address.model.property.Size;

public class ViewPropertyCommandTest {

    private Model createModelWithProperties() throws CommandException {
        Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());

        Property propertyA = new Property(
                new PropertyAddress("123 Clementi Ave 3"),
                new Price("1000000"),
                new Size("121"),
                new PropertyType("HDB")
        );
        Property propertyB = new Property(
                new PropertyAddress("456 Jurong West St 42"),
                new Price("900000"),
                new Size("99")
        );
        Property propertyC = new Property(
                new PropertyAddress("789 Ang Mo Kio Ave 5"),
                new Price("850000"),
                new Size("110")
        );

        // Add properties with single ownership
        new AddPropertyCommand(INDEX_FIRST_PERSON, propertyA).execute(model);
        new AddPropertyCommand(INDEX_SECOND_PERSON, propertyB).execute(model);
        new AddPropertyCommand(INDEX_THIRD_PERSON, propertyC).execute(model);

        return model;
    }

    @Test
    public void execute_validIndex_success() throws Exception {
        Model model = createModelWithProperties();
        ViewPropertyCommand command = new ViewPropertyCommand(Index.fromZeroBased(0));

        CommandResult result = command.execute(model);

        // With single ownership, viewing a property shows only its one owner
        assertEquals(String.format(Messages.MESSAGE_PERSONS_LISTED_OVERVIEW, 1), result.getFeedbackToUser());
        assertEquals(1, model.getFilteredPropertyList().size());
        assertEquals(1, model.getFilteredPersonList().size());
        assertTrue(model.getFilteredPersonList().stream()
                .anyMatch(person -> person.getName().fullName.equals("Alice Pauline")));
    }

    @Test
    public void execute_viewSecondProperty_showsCorrectOwner() throws Exception {
        Model model = createModelWithProperties();
        ViewPropertyCommand command = new ViewPropertyCommand(Index.fromZeroBased(1));

        CommandResult result = command.execute(model);

        assertEquals(String.format(Messages.MESSAGE_PERSONS_LISTED_OVERVIEW, 1), result.getFeedbackToUser());
        assertEquals(1, model.getFilteredPropertyList().size());
        assertEquals(1, model.getFilteredPersonList().size());
        assertTrue(model.getFilteredPersonList().stream()
                .anyMatch(person -> person.getName().fullName.equals("Benson Meier")));
    }

    @Test
    public void execute_viewThirdProperty_showsCorrectOwner() throws Exception {
        Model model = createModelWithProperties();
        ViewPropertyCommand command = new ViewPropertyCommand(Index.fromZeroBased(2));

        CommandResult result = command.execute(model);

        assertEquals(String.format(Messages.MESSAGE_PERSONS_LISTED_OVERVIEW, 1), result.getFeedbackToUser());
        assertEquals(1, model.getFilteredPropertyList().size());
        assertEquals(1, model.getFilteredPersonList().size());
        assertTrue(model.getFilteredPersonList().stream()
                .anyMatch(person -> person.getName().fullName.equals("Carl Kurz")));
    }

    @Test
    public void execute_invalidIndex_throwsCommandException() throws Exception {
        Model model = createModelWithProperties();
        ViewPropertyCommand command = new ViewPropertyCommand(Index.fromZeroBased(10));

        assertThrows(CommandException.class,
                Messages.MESSAGE_INVALID_PROPERTY_DISPLAYED_INDEX, () -> command.execute(model)
        );
    }

    @Test
    public void execute_propertyWithNoOwner_filtersPersonListToEmpty() throws Exception {
        // This test covers the else branch (lines 54-56) where no owner is found
        Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());

        Property propertyA = new Property(
                new PropertyAddress("123 Clementi Ave 3"),
                new Price("1000000"),
                new Size("121")
        );

        // Add property to a person
        new AddPropertyCommand(INDEX_FIRST_PERSON, propertyA).execute(model);

        // Filter out all persons, making the property "orphaned" from the current filtered list
        model.updateFilteredPersonList(person -> false);

        ViewPropertyCommand command = new ViewPropertyCommand(Index.fromZeroBased(0));
        CommandResult result = command.execute(model);

        // Should return 0 persons since all persons are filtered out (no owner in current view)
        assertEquals(String.format(Messages.MESSAGE_PERSONS_LISTED_OVERVIEW, 0), result.getFeedbackToUser());
        assertEquals(1, model.getFilteredPropertyList().size());
        assertEquals(0, model.getFilteredPersonList().size());
    }

    @Test
    public void equals() {
        ViewPropertyCommand viewFirstCommand = new ViewPropertyCommand(INDEX_FIRST_PERSON);
        ViewPropertyCommand viewSecondCommand = new ViewPropertyCommand(INDEX_SECOND_PERSON);

        // same object -> returns true
        assertTrue(viewFirstCommand.equals(viewFirstCommand));

        // same values -> returns true
        ViewPropertyCommand viewFirstCommandCopy = new ViewPropertyCommand(INDEX_FIRST_PERSON);
        assertTrue(viewFirstCommand.equals(viewFirstCommandCopy));

        // different types -> returns false
        assertFalse(viewFirstCommand.equals(1));

        // null -> returns false
        assertFalse(viewFirstCommand.equals(null));

        // different property -> returns false
        assertFalse(viewFirstCommand.equals(viewSecondCommand));
    }
}
