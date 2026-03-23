package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.testutil.Assert.assertThrows;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_PERSON;
import static seedu.address.testutil.TypicalIndexes.INDEX_SECOND_PERSON;
import static seedu.address.testutil.TypicalIndexes.INDEX_THIRD_PERSON;
import static seedu.address.testutil.TypicalPersons.getTypicalAddressBook;

import java.util.List;

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

public class ViewPropertyCommandTest {

    private Model createModelWithProperties() throws CommandException {
        Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());

        Property propertyA = new Property(
                new PropertyAddress("123 Clementi Ave 3"),
                new Price("1000000"),
                new Size("121")
        );
        Property propertyAWithDifferentDetails = new Property(
                new PropertyAddress("123 Clementi Ave 3"),
                new Price("1100000"),
                new Size("150")
        );
        Property propertyB = new Property(
                new PropertyAddress("456 Jurong West St 42"),
                new Price("900000"),
                new Size("99")
        );

        // Add properties
        new AddPropertyCommand(List.of(INDEX_FIRST_PERSON), propertyA).execute(model);
        new AddPropertyCommand(List.of(INDEX_SECOND_PERSON), propertyAWithDifferentDetails).execute(model);
        new AddPropertyCommand(List.of(INDEX_THIRD_PERSON), propertyB).execute(model);

        return model;
    }

    @Test
    public void execute_validIndex_success() throws Exception {
        Model model = createModelWithProperties();
        ViewPropertyCommand command = new ViewPropertyCommand(Index.fromZeroBased(0));

        CommandResult result = command.execute(model);

        assertEquals(String.format(Messages.MESSAGE_PERSONS_LISTED_OVERVIEW, 2), result.getFeedbackToUser());
        assertEquals(2, model.getFilteredPropertyList().size());
        assertEquals(2, model.getFilteredPersonList().size());
        assertTrue(model.getFilteredPersonList().stream()
                .anyMatch(person -> person.getName().fullName.equals("Alice Pauline")));
        assertTrue(model.getFilteredPersonList().stream()
                .anyMatch(person -> person.getName().fullName.equals("Benson Meier")));
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
