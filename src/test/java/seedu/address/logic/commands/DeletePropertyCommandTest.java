package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandFailure;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.address.logic.commands.CommandTestUtil.showPersonAtIndex;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_PERSON;
import static seedu.address.testutil.TypicalIndexes.INDEX_SECOND_PERSON;
import static seedu.address.testutil.TypicalPersons.getTypicalAddressBook;

import java.util.List;

import org.junit.jupiter.api.Test;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.Messages;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.person.Person;
import seedu.address.model.property.Price;
import seedu.address.model.property.Property;
import seedu.address.model.property.PropertyAddress;
import seedu.address.model.property.Size;

/**
 * Contains integration tests (interaction with the Model) and unit tests for
 * {@code DeletePropertyCommand}.
 */
public class DeletePropertyCommandTest {

    private Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());

    @Test
    public void execute_validIndexUnfilteredList_success() throws CommandException {
        // Add property to the first person
        AddPropertyCommand addPropertyCommand = new AddPropertyCommand(List.of(INDEX_FIRST_PERSON),
                new Property(new PropertyAddress("311 Clementi Ave 2, #02-25"), new Price("1200000"),
                new Size("1200")));
        addPropertyCommand.execute(model);

        Person personToEdit = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());
        DeletePropertyCommand deleteCommand = new DeletePropertyCommand(INDEX_FIRST_PERSON);

        Person editedPerson = new Person(personToEdit.getName(), personToEdit.getPhone(),
                personToEdit.getEmail(), personToEdit.getTags());

        String expectedMessage = String.format(DeletePropertyCommand.MESSAGE_SUCCESS, Messages.format(editedPerson));

        ModelManager expectedModel = new ModelManager(model.getAddressBook(), new UserPrefs());
        expectedModel.setPerson(personToEdit, editedPerson);

        assertCommandSuccess(deleteCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_invalidIndexUnfilteredList_throwsCommandException() {
        Index outOfBoundIndex = Index.fromOneBased(model.getFilteredPersonList().size() + 1);
        DeletePropertyCommand deleteCommand = new DeletePropertyCommand(outOfBoundIndex);

        assertCommandFailure(deleteCommand, model, "The person index provided is invalid.");
    }

    @Test
    public void execute_noProperties_throwsCommandException() {
        DeletePropertyCommand deleteCommand = new DeletePropertyCommand(INDEX_FIRST_PERSON);

        assertCommandFailure(deleteCommand, model, Messages.MESSAGE_INVALID_NO_PROPERTY);
    }

    @Test
    public void execute_validIndexFilteredList_success() throws CommandException {
        showPersonAtIndex(model, INDEX_FIRST_PERSON);

        // Add property to the first person
        AddPropertyCommand addPropertyCommand = new AddPropertyCommand(List.of(INDEX_FIRST_PERSON),
                new Property(new PropertyAddress("311 Clementi Ave 2, #02-25"), new Price("1200000"),
                new Size("1200")));
        addPropertyCommand.execute(model);

        Person personToEdit = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());
        DeletePropertyCommand deleteCommand = new DeletePropertyCommand(INDEX_FIRST_PERSON);

        Person editedPerson = new Person(personToEdit.getName(), personToEdit.getPhone(),
                personToEdit.getEmail(), personToEdit.getTags());

        String expectedMessage = String.format(DeletePropertyCommand.MESSAGE_SUCCESS, Messages.format(editedPerson));

        Model expectedModel = new ModelManager(model.getAddressBook(), new UserPrefs());
        expectedModel.setPerson(personToEdit, editedPerson);
        showPersonAtIndex(expectedModel, INDEX_FIRST_PERSON);

        assertCommandSuccess(deleteCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_invalidIndexFilteredList_throwsCommandException() {
        showPersonAtIndex(model, INDEX_FIRST_PERSON);

        Index outOfBoundIndex = INDEX_SECOND_PERSON;
        // ensures that outOfBoundIndex is still in bounds of address book list
        assertTrue(outOfBoundIndex.getZeroBased() < model.getAddressBook().getPersonList().size());

        DeletePropertyCommand deleteCommand = new DeletePropertyCommand(outOfBoundIndex);

        assertCommandFailure(deleteCommand, model, "The person index provided is invalid.");
    }

    @Test
    public void execute_filteredListNoProperties_throwsCommandException() {
        showPersonAtIndex(model, INDEX_FIRST_PERSON);

        DeletePropertyCommand deleteCommand = new DeletePropertyCommand(INDEX_FIRST_PERSON);

        assertCommandFailure(deleteCommand, model, Messages.MESSAGE_INVALID_NO_PROPERTY);
    }

    @Test
    public void equals() {
        DeletePropertyCommand deleteFirstCommand = new DeletePropertyCommand(INDEX_FIRST_PERSON);
        DeletePropertyCommand deleteSecondCommand = new DeletePropertyCommand(INDEX_SECOND_PERSON);

        // same object -> returns true
        assertTrue(deleteFirstCommand.equals(deleteFirstCommand));

        // same values -> returns true
        DeletePropertyCommand deleteFirstCommandCopy = new DeletePropertyCommand(INDEX_FIRST_PERSON);
        assertTrue(deleteFirstCommand.equals(deleteFirstCommandCopy));

        // different types -> returns false
        assertFalse(deleteFirstCommand.equals(1));

        // null -> returns false
        assertFalse(deleteFirstCommand.equals(null));

        // different person -> returns false
        assertFalse(deleteFirstCommand.equals(deleteSecondCommand));
    }

    @Test
    public void toStringMethod() {
        Index targetIndex = Index.fromOneBased(1);
        DeletePropertyCommand deleteCommand = new DeletePropertyCommand(targetIndex);
        String expected = DeletePropertyCommand.class.getSimpleName() + "{index=" + targetIndex + "}";
        assertEquals(expected, deleteCommand.toString());
    }

    /**
     * Updates {@code model}'s filtered list to show no one.
     */
    private void showNoPerson(Model model) {
        model.updateFilteredPersonList(p -> false);

        assertTrue(model.getFilteredPersonList().isEmpty());
    }
}
