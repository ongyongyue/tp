package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.logic.Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandFailure;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_PERSON;
import static seedu.address.testutil.TypicalPersons.getTypicalAddressBook;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.Test;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.EditPropertyCommand.EditPropertyDescriptor;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.person.Person;
import seedu.address.model.property.Price;
import seedu.address.model.property.Property;
import seedu.address.model.property.PropertyAddress;
import seedu.address.testutil.PersonBuilder;

public class EditPropertyCommandTest {

    private final Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());

    @Test
    public void execute_editAddress_success() {
        Person personToEdit = new PersonBuilder()
                .withName("Edit Test")
                .withPhone("88888888")
                .withEmail("edit@test.com")
                .withProperty("111 Clementi Ave 1", "1000000", "1000")
                .build();

        Model editModel = new ModelManager(getTypicalAddressBook(), new UserPrefs());
        editModel.addPerson(personToEdit);

        Index targetPersonIndex = Index.fromOneBased(editModel.getFilteredPersonList().size());

        List<Property> propertyList = new ArrayList<>(personToEdit.getProperties());
        Property propertyToEdit = propertyList.get(0);

        Property editedProperty = new Property(
                new PropertyAddress("999 New Address"),
                propertyToEdit.getPrice(),
                propertyToEdit.getSize()
        );

        EditPropertyDescriptor descriptor = new EditPropertyDescriptor();
        descriptor.setAddress(new PropertyAddress("999 New Address"));

        EditPropertyCommand editCommand =
                new EditPropertyCommand(targetPersonIndex, Index.fromOneBased(1), descriptor);

        propertyList.set(0, editedProperty);
        Set<Property> updatedProperties = new LinkedHashSet<>(propertyList);

        Person editedPerson = new Person(
                personToEdit.getName(),
                personToEdit.getPhone(),
                personToEdit.getEmail(),
                personToEdit.getTags(),
                updatedProperties
        );

        String expectedMessage = String.format(EditPropertyCommand.MESSAGE_EDIT_PROPERTY_SUCCESS, editedProperty);

        Model expectedModel = new ModelManager(editModel.getAddressBook(), new UserPrefs());
        expectedModel.setPerson(personToEdit, editedPerson);
        expectedModel.updateFilteredPersonList(p -> p.isSamePerson(editedPerson));
        expectedModel.updateFilteredPropertyList(p -> p.equals(editedProperty));

        assertCommandSuccess(editCommand, editModel, expectedMessage, expectedModel);
    }

    @Test
    public void execute_invalidClientIndex_failure() {
        Index outOfBoundClientIndex = Index.fromOneBased(model.getFilteredPersonList().size() + 1);

        EditPropertyDescriptor descriptor = new EditPropertyDescriptor();
        descriptor.setAddress(new PropertyAddress("999 New Address"));

        EditPropertyCommand editCommand =
                new EditPropertyCommand(outOfBoundClientIndex, Index.fromOneBased(1), descriptor);

        assertCommandFailure(editCommand, model, MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
    }

    @Test
    public void execute_invalidPropertyIndex_failure() {
        Person personToEdit = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());
        int outOfBoundPropertyIndex = personToEdit.getProperties().size() + 1;

        EditPropertyDescriptor descriptor = new EditPropertyDescriptor();
        descriptor.setAddress(new PropertyAddress("999 New Address"));

        EditPropertyCommand editCommand =
                new EditPropertyCommand(INDEX_FIRST_PERSON, Index.fromOneBased(outOfBoundPropertyIndex), descriptor);

        assertCommandFailure(editCommand, model, EditPropertyCommand.MESSAGE_INVALID_PROPERTY_INDEX);
    }

    @Test
    public void execute_duplicateProperty_failure() {
        Person personToEdit = new PersonBuilder()
                .withName("Duplicate Test")
                .withPhone("88888888")
                .withEmail("duplicate@test.com")
                .withProperty("111 Clementi Ave 1", "1000000", "1000")
                .withProperty("222 Clementi Ave 2", "2000000", "2000")
                .build();

        Model duplicateModel = new ModelManager(getTypicalAddressBook(), new UserPrefs());
        duplicateModel.addPerson(personToEdit);

        Index targetPersonIndex = Index.fromOneBased(duplicateModel.getFilteredPersonList().size());

        EditPropertyDescriptor descriptor = new EditPropertyDescriptor();
        descriptor.setAddress(new PropertyAddress("111 Clementi Ave 1"));

        EditPropertyCommand editCommand =
                new EditPropertyCommand(targetPersonIndex, Index.fromOneBased(2), descriptor);

        assertCommandFailure(editCommand, duplicateModel, EditPropertyCommand.MESSAGE_DUPLICATE_PROPERTY);
    }

    @Test
    public void equals() {
        EditPropertyDescriptor firstDescriptor = new EditPropertyDescriptor();
        firstDescriptor.setAddress(new PropertyAddress("999 New Address"));

        EditPropertyDescriptor sameDescriptor = new EditPropertyDescriptor();
        sameDescriptor.setAddress(new PropertyAddress("999 New Address"));

        EditPropertyDescriptor differentDescriptor = new EditPropertyDescriptor();
        differentDescriptor.setPrice(new Price("123456"));

        EditPropertyCommand editFirstCommand =
                new EditPropertyCommand(INDEX_FIRST_PERSON, Index.fromOneBased(1), firstDescriptor);
        EditPropertyCommand editSecondCommand =
                new EditPropertyCommand(INDEX_FIRST_PERSON, Index.fromOneBased(2), differentDescriptor);

        assertTrue(editFirstCommand.equals(editFirstCommand));
        assertTrue(editFirstCommand.equals(
                new EditPropertyCommand(INDEX_FIRST_PERSON, Index.fromOneBased(1), sameDescriptor)));
        assertFalse(editFirstCommand.equals(editSecondCommand));
        assertFalse(editFirstCommand.equals(1));
        assertFalse(editFirstCommand.equals(null));
    }
}
