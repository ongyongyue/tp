package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandFailure;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.address.testutil.TypicalPersons.getTypicalAddressBook;

import java.util.LinkedHashSet;
import java.util.Set;

import org.junit.jupiter.api.Test;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.Messages;
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

        Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());
        model.addPerson(personToEdit);
        model.updateFilteredPropertyList(p -> true);

        Property propertyToEdit = model.getFilteredPropertyList()
                .get(model.getFilteredPropertyList().size() - 1);

        EditPropertyDescriptor descriptor = new EditPropertyDescriptor();
        descriptor.setAddress(new PropertyAddress("999 New Address"));

        Index targetIndex = Index.fromOneBased(model.getFilteredPropertyList().size());
        EditPropertyCommand command = new EditPropertyCommand(targetIndex, descriptor);

        Property editedProperty = new Property(
                new PropertyAddress("999 New Address"),
                propertyToEdit.getPrice(),
                propertyToEdit.getSize(),
                propertyToEdit.getPropertyType()
        );

        String expectedMessage =
                String.format(EditPropertyCommand.MESSAGE_EDIT_PROPERTY_SUCCESS, editedProperty);

        Model expectedModel = new ModelManager(model.getAddressBook(), new UserPrefs());

        Person owner = expectedModel.getFilteredPersonList().stream()
                .filter(p -> p.getProperties().contains(propertyToEdit))
                .findFirst()
                .orElseThrow();

        Set<Property> updatedProperties = new LinkedHashSet<>(owner.getProperties());
        updatedProperties.remove(propertyToEdit);
        updatedProperties.add(editedProperty);

        Person editedPerson = new Person(
                owner.getName(),
                owner.getPhone(),
                owner.getEmail(),
                owner.getTags(),
                updatedProperties
        );

        expectedModel.setPerson(owner, editedPerson);
        expectedModel.updateFilteredPersonList(p -> p.isSamePerson(editedPerson));
        expectedModel.updateFilteredPropertyList(p -> p.equals(editedProperty));

        assertCommandSuccess(command, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_invalidPropertyIndex_failure() {
        Person personToEdit = new PersonBuilder()
                .withName("Edit Test")
                .withPhone("88888888")
                .withEmail("edit@test.com")
                .withProperty("111 Clementi Ave 1", "1000000", "1000")
                .build();

        Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());
        model.addPerson(personToEdit);
        model.updateFilteredPropertyList(p -> true);

        EditPropertyDescriptor descriptor = new EditPropertyDescriptor();
        descriptor.setAddress(new PropertyAddress("999 New Address"));

        Index invalidIndex = Index.fromOneBased(model.getFilteredPropertyList().size() + 1);

        EditPropertyCommand command = new EditPropertyCommand(invalidIndex, descriptor);

        assertCommandFailure(command, model, Messages.MESSAGE_INVALID_PROPERTY_DISPLAYED_INDEX);
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
                new EditPropertyCommand(Index.fromOneBased(1), firstDescriptor);
        EditPropertyCommand editSecondCommand =
                new EditPropertyCommand(Index.fromOneBased(2), differentDescriptor);

        assertTrue(editFirstCommand.equals(editFirstCommand));
        assertTrue(editFirstCommand.equals(
                new EditPropertyCommand(Index.fromOneBased(1), sameDescriptor)));
        assertFalse(editFirstCommand.equals(editSecondCommand));
        assertFalse(editFirstCommand.equals(1));
        assertFalse(editFirstCommand.equals(null));
    }
}
