package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.logic.commands.AddPropertyCommand.MESSAGE_INVALID_PERSON_INDEX;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandFailure;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.address.testutil.Assert.assertThrows;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_PERSON;
import static seedu.address.testutil.TypicalPersons.getTypicalAddressBook;

import java.util.List;

import org.junit.jupiter.api.Test;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.person.Person;
import seedu.address.model.property.Price;
import seedu.address.model.property.Property;
import seedu.address.model.property.PropertyAddress;
import seedu.address.model.property.Size;

public class AddPropertyCommandTest {
    private Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());

    private Property validProperty = new Property(
            new PropertyAddress("311 Clementi Ave 2, #02-25"),
            new Price("1200000"),
            new Size("1200")
    );

    @Test
    public void constructor_nullIndex_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> new AddPropertyCommand(null, validProperty));
    }

    @Test
    public void constructor_nullProperty_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> new AddPropertyCommand(List.of(INDEX_FIRST_PERSON), null));
    }

    @Test
    public void execute_validIndexUnfilteredList_success() throws CommandException {
        Person personToEdit = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());

        AddPropertyCommand addCommand = new AddPropertyCommand(List.of(INDEX_FIRST_PERSON), validProperty);

        Person editedPerson = personToEdit.addProperty(validProperty);

        String expectedMessage = String.format(
                AddPropertyCommand.MESSAGE_SUCCESS,
                personToEdit.getName(),
                validProperty
        );

        Model expectedModel = new ModelManager(model.getAddressBook(), new UserPrefs());
        expectedModel.setPerson(personToEdit, editedPerson);

        assertCommandSuccess(addCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_invalidIndexUnfilteredList_throwsCommandException() {
        Index outOfBoundIndex = Index.fromOneBased(model.getFilteredPersonList().size() + 1);
        AddPropertyCommand addCommand = new AddPropertyCommand(List.of(outOfBoundIndex), validProperty);

        assertCommandFailure(addCommand, model, MESSAGE_INVALID_PERSON_INDEX);
    }

    @Test
    public void execute_duplicateProperty_throwsCommandException() throws CommandException {
        AddPropertyCommand addCommand = new AddPropertyCommand(List.of(INDEX_FIRST_PERSON), validProperty);
        addCommand.execute(model);

        AddPropertyCommand duplicateCommand = new AddPropertyCommand(List.of(INDEX_FIRST_PERSON), validProperty);

        assertCommandFailure(duplicateCommand, model, AddPropertyCommand.MESSAGE_DUPLICATE_PROPERTY);
    }

    @Test
    public void execute_noPersons_throwsCommandException() {
        Model emptyModel = new ModelManager();

        AddPropertyCommand addCommand = new AddPropertyCommand(List.of(INDEX_FIRST_PERSON), validProperty);

        assertCommandFailure(addCommand, emptyModel, AddPropertyCommand.MESSAGE_NO_PERSONS);
    }

    @Test
    public void execute_multiplePersons_success() {
        Model testModel = new ModelManager(getTypicalAddressBook(), new UserPrefs());

        AddPropertyCommand addCommand = new AddPropertyCommand(
                List.of(INDEX_FIRST_PERSON, Index.fromOneBased(2)), validProperty);

        Person firstPersonToEdit = testModel.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());
        Person secondPersonToEdit = testModel.getFilteredPersonList().get(Index.fromOneBased(2).getZeroBased());

        Person firstEditedPerson = firstPersonToEdit.addProperty(validProperty);
        Person secondEditedPerson = secondPersonToEdit.addProperty(validProperty);

        String expectedMessage = String.format(
                AddPropertyCommand.MESSAGE_SUCCESS,
                firstPersonToEdit.getName() + ", " + secondPersonToEdit.getName(),
                validProperty
        );

        Model expectedModel = new ModelManager(testModel.getAddressBook(), new UserPrefs());
        expectedModel.setPerson(firstPersonToEdit, firstEditedPerson);
        expectedModel.setPerson(secondPersonToEdit, secondEditedPerson);

        assertCommandSuccess(addCommand, testModel, expectedMessage, expectedModel);
    }

    @Test
    public void equals() {
        AddPropertyCommand firstCommand = new AddPropertyCommand(List.of(INDEX_FIRST_PERSON), validProperty);
        AddPropertyCommand secondCommand = new AddPropertyCommand(List.of(INDEX_FIRST_PERSON),
                new Property(new PropertyAddress("123 Street"), new Price("500000"), new Size("1000")));

        AddPropertyCommand firstCommandCopy = new AddPropertyCommand(List.of(INDEX_FIRST_PERSON), validProperty);

        assertTrue(firstCommand.equals(firstCommand));
        assertTrue(firstCommand.equals(firstCommandCopy));
        assertFalse(firstCommand.equals(null));
        assertFalse(firstCommand.equals(1));
        assertFalse(firstCommand.equals(secondCommand));
    }

    @Test
    public void toStringMethod() {
        AddPropertyCommand command = new AddPropertyCommand(List.of(INDEX_FIRST_PERSON), validProperty);
        String expected = AddPropertyCommand.class.getCanonicalName()
                + "{targetIndices=" + List.of(INDEX_FIRST_PERSON) + ", property=" + validProperty + "}";
        assertEquals(expected, command.toString());
    }
}
