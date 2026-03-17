package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.logic.Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandFailure;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.address.logic.commands.EditClientCommand.MESSAGE_DUPLICATE_PERSON;
import static seedu.address.logic.commands.EditClientCommand.MESSAGE_EDIT_CLIENT_SUCCESS;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_PERSON;
import static seedu.address.testutil.TypicalIndexes.INDEX_SECOND_PERSON;
import static seedu.address.testutil.TypicalPersons.getTypicalAddressBook;

import org.junit.jupiter.api.Test;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.EditClientCommand.EditClientDescriptor;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.person.Email;
import seedu.address.model.person.Name;
import seedu.address.model.person.Person;
import seedu.address.model.person.Phone;
import seedu.address.testutil.PersonBuilder;

public class EditClientCommandTest {
    private final Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());

    @Test
    public void execute_allFieldsSpecifiedUnfilteredList_success() {
        Person personToEdit = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());

        Person editedPerson = new PersonBuilder(personToEdit)
                .withName("Alice Tan")
                .withPhone("91234567")
                .withEmail("alice@example.com")
                .build();

        EditClientDescriptor descriptor = new EditClientDescriptor();
        descriptor.setName(new Name("Alice Tan"));
        descriptor.setPhone(new Phone("91234567"));
        descriptor.setEmail(new Email("alice@example.com"));

        EditClientCommand editCommand = new EditClientCommand(INDEX_FIRST_PERSON, descriptor);

        String expectedMessage = String.format(MESSAGE_EDIT_CLIENT_SUCCESS, editedPerson);

        Model expectedModel = new ModelManager(model.getAddressBook(), new UserPrefs());
        expectedModel.setPerson(personToEdit, editedPerson);

        assertCommandSuccess(editCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_someFieldsSpecifiedUnfilteredList_success() {
        Index indexLastPerson = INDEX_SECOND_PERSON;
        Person lastPerson = model.getFilteredPersonList().get(indexLastPerson.getZeroBased());

        Person editedPerson = new PersonBuilder(lastPerson)
                .withPhone("91234567")
                .withEmail("new@email.com")
                .build();

        EditClientDescriptor descriptor = new EditClientDescriptor();
        descriptor.setPhone(new Phone("91234567"));
        descriptor.setEmail(new Email("new@email.com"));

        EditClientCommand editCommand = new EditClientCommand(indexLastPerson, descriptor);

        String expectedMessage = String.format(MESSAGE_EDIT_CLIENT_SUCCESS, editedPerson);

        Model expectedModel = new ModelManager(model.getAddressBook(), new UserPrefs());
        expectedModel.setPerson(lastPerson, editedPerson);

        assertCommandSuccess(editCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_duplicatePersonUnfilteredList_failure() {
        Person firstPerson = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());

        EditClientDescriptor descriptor = new EditClientDescriptor();
        descriptor.setName(firstPerson.getName());
        descriptor.setPhone(firstPerson.getPhone());
        descriptor.setEmail(firstPerson.getEmail());

        EditClientCommand editCommand = new EditClientCommand(INDEX_SECOND_PERSON, descriptor);

        assertCommandFailure(editCommand, model, MESSAGE_DUPLICATE_PERSON);
    }

    @Test
    public void execute_invalidPersonIndexUnfilteredList_failure() {
        Index outOfBoundIndex = Index.fromOneBased(model.getFilteredPersonList().size() + 1);

        EditClientDescriptor descriptor = new EditClientDescriptor();
        descriptor.setName(new Name("Alice"));

        EditClientCommand editCommand = new EditClientCommand(outOfBoundIndex, descriptor);

        assertCommandFailure(editCommand, model, MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
    }

    @Test
    public void equals() {
        EditClientDescriptor descriptor = new EditClientDescriptor();
        descriptor.setName(new Name("Alice"));

        EditClientDescriptor sameDescriptor = new EditClientDescriptor();
        sameDescriptor.setName(new Name("Alice"));

        EditClientDescriptor differentDescriptor = new EditClientDescriptor();
        differentDescriptor.setPhone(new Phone("91234567"));

        EditClientCommand editFirstCommand = new EditClientCommand(INDEX_FIRST_PERSON, descriptor);
        EditClientCommand editSecondCommand = new EditClientCommand(INDEX_SECOND_PERSON, differentDescriptor);

        assertTrue(editFirstCommand.equals(editFirstCommand));
        assertTrue(editFirstCommand.equals(new EditClientCommand(INDEX_FIRST_PERSON, sameDescriptor)));
        assertFalse(editFirstCommand.equals(editSecondCommand));
        assertFalse(editFirstCommand.equals(1));
        assertFalse(editFirstCommand.equals(null));
    }
}
