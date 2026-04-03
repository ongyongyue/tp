package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.logic.commands.AddPropertyCommand.MESSAGE_INVALID_PERSON_INDEX;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandFailure;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.address.testutil.Assert.assertThrows;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_PERSON;
import static seedu.address.testutil.TypicalIndexes.INDEX_SECOND_PERSON;
import static seedu.address.testutil.TypicalPersons.getTypicalAddressBook;

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

/**
 * Contains integration tests (interaction with the Model) for {@code AddPropertyCommand}.
 */
public class AddPropertyCommandTest {
    private Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());

    private Property validProperty = new Property(
            new PropertyAddress("311 Clementi Ave 2, #02-25"),
            new Price("1200000"),
            new Size("1200")
    );

    private final Property anotherValidProperty = new Property(
            new PropertyAddress("123 Clementi Ave 3"),
            new Price("1000000"),
            new Size("1200")
    );

    @Test
    public void constructor_nullIndex_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> new AddPropertyCommand(null, validProperty));
    }

    @Test
    public void constructor_nullProperty_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> new AddPropertyCommand(INDEX_FIRST_PERSON, null));
    }

    @Test
    public void execute_validIndexUnfilteredList_success() throws CommandException {
        Person personToEdit = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());

        AddPropertyCommand addCommand = new AddPropertyCommand(INDEX_FIRST_PERSON, validProperty);

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
        AddPropertyCommand addCommand = new AddPropertyCommand(outOfBoundIndex, validProperty);

        assertCommandFailure(addCommand, model, MESSAGE_INVALID_PERSON_INDEX);
    }

    @Test
    public void execute_duplicateProperty_throwsCommandException() throws CommandException {
        AddPropertyCommand addCommand = new AddPropertyCommand(INDEX_FIRST_PERSON, validProperty);
        addCommand.execute(model);

        AddPropertyCommand duplicateCommand = new AddPropertyCommand(INDEX_FIRST_PERSON, validProperty);

        assertCommandFailure(duplicateCommand, model, AddPropertyCommand.MESSAGE_DUPLICATE_PROPERTY);
    }

    @Test
    public void execute_propertyAlreadyOwnedByAnotherClient_throwsCommandException() {
        Person firstPerson = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());
        Person updatedFirstPerson = firstPerson.addProperty(anotherValidProperty);
        model.setPerson(firstPerson, updatedFirstPerson);

        AddPropertyCommand command = new AddPropertyCommand(INDEX_SECOND_PERSON, anotherValidProperty);

        assertCommandFailure(command, model, AddPropertyCommand.MESSAGE_PROPERTY_ALREADY_OWNED);
    }

    @Test
    public void execute_duplicatePropertySameClient_failure() {
        Person firstPerson = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());
        Person updatedFirstPerson = firstPerson.addProperty(anotherValidProperty);
        model.setPerson(firstPerson, updatedFirstPerson);

        AddPropertyCommand command = new AddPropertyCommand(INDEX_FIRST_PERSON, anotherValidProperty);

        assertCommandFailure(command, model, AddPropertyCommand.MESSAGE_DUPLICATE_PROPERTY);
    }

    @Test
    public void execute_noPersons_throwsCommandException() {
        Model emptyModel = new ModelManager();

        AddPropertyCommand addCommand = new AddPropertyCommand(INDEX_FIRST_PERSON, validProperty);

        assertCommandFailure(addCommand, emptyModel, AddPropertyCommand.MESSAGE_NO_PERSONS);
    }

    @Test
    public void equals() {
        AddPropertyCommand firstCommand = new AddPropertyCommand(INDEX_FIRST_PERSON, validProperty);
        AddPropertyCommand secondCommand = new AddPropertyCommand(INDEX_FIRST_PERSON,
                new Property(new PropertyAddress("123 Street"), new Price("500000"), new Size("1000")));

        AddPropertyCommand firstCommandCopy = new AddPropertyCommand(INDEX_FIRST_PERSON, validProperty);

        assertTrue(firstCommand.equals(firstCommand));
        assertTrue(firstCommand.equals(firstCommandCopy));
        assertFalse(firstCommand.equals(null));
        assertFalse(firstCommand.equals(1));
        assertFalse(firstCommand.equals(secondCommand));
        assertFalse(firstCommand.equals(new AddPropertyCommand(INDEX_SECOND_PERSON, validProperty)));
    }

    @Test
    public void toStringMethod() {
        AddPropertyCommand command = new AddPropertyCommand(INDEX_FIRST_PERSON, validProperty);
        String expected = AddPropertyCommand.class.getCanonicalName()
                + "{targetIndex=" + INDEX_FIRST_PERSON + ", property=" + validProperty + "}";
        assertEquals(expected, command.toString());
    }
}
