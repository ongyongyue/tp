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
import seedu.address.model.property.Size;
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
    public void execute_duplicateProperty_failure() {
        Person person = new PersonBuilder()
                .withName("Edit Test")
                .withPhone("88888888")
                .withEmail("edit@test.com")
                .withProperty("111 Clementi Ave 1", "1000000", "1000")
                .withProperty("222 Clementi Ave 2", "2000000", "2000")
                .build();

        Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());
        model.addPerson(person);
        model.updateFilteredPropertyList(p -> true);

        EditPropertyDescriptor descriptor = new EditPropertyDescriptor();
        descriptor.setAddress(new PropertyAddress("222 Clementi Ave 2"));
        descriptor.setPrice(new Price("2000000"));
        descriptor.setSize(new Size("2000"));

        Index targetIndex = Index.fromOneBased(model.getFilteredPropertyList().size() - 1);
        EditPropertyCommand command = new EditPropertyCommand(targetIndex, descriptor);

        assertCommandFailure(command, model, EditPropertyCommand.MESSAGE_DUPLICATE_PROPERTY);
    }

    @Test
    public void execute_duplicatePropertyAcrossPersons_failure() {
        Person firstPerson = new PersonBuilder()
                .withName("Alice")
                .withPhone("91111111")
                .withEmail("alice@test.com")
                .withProperty("111 Clementi Ave 1", "1000000", "1000")
                .build();

        Person secondPerson = new PersonBuilder()
                .withName("Bob")
                .withPhone("92222222")
                .withEmail("bob@test.com")
                .withProperty("222 Clementi Ave 2", "2000000", "2000")
                .build();

        Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());
        model.addPerson(firstPerson);
        model.addPerson(secondPerson);
        model.updateFilteredPropertyList(p -> true);

        EditPropertyDescriptor descriptor = new EditPropertyDescriptor();
        descriptor.setAddress(new PropertyAddress("111 Clementi Ave 1"));
        descriptor.setPrice(new Price("1000000"));
        descriptor.setSize(new Size("1000"));

        Property secondPersonsProperty = secondPerson.getProperties().iterator().next();
        int index = model.getFilteredPropertyList().indexOf(secondPersonsProperty) + 1;

        EditPropertyCommand command = new EditPropertyCommand(Index.fromOneBased(index), descriptor);

        assertCommandFailure(command, model, EditPropertyCommand.MESSAGE_DUPLICATE_PROPERTY);
    }

    @Test
    public void execute_noProperties_throwsCommandException() {
        Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());
        model.updateFilteredPropertyList(p -> false);
        EditPropertyDescriptor descriptor = new EditPropertyDescriptor();
        descriptor.setAddress(new PropertyAddress("999 New Address"));

        EditPropertyCommand command =
                new EditPropertyCommand(Index.fromOneBased(1), descriptor);

        assertCommandFailure(command, model, EditPropertyCommand.MESSAGE_NO_PROPERTIES);
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

    @Test
    public void execute_editOneOfMultipleProperties_success() {
        Person personToEdit = new PersonBuilder()
                .withName("Edit Test")
                .withPhone("88888888")
                .withEmail("edit@test.com")
                .withProperty("111 Clementi Ave 1", "1000000", "1000")
                .withProperty("222 Clementi Ave 2", "2000000", "2000")
                .build();

        Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());
        model.addPerson(personToEdit);
        model.updateFilteredPropertyList(p -> true);

        Property propertyToEdit = model.getFilteredPropertyList().stream()
                .filter(p -> p.getAddress().toString().equals("111 Clementi Ave 1"))
                .findFirst()
                .orElseThrow();

        int index = model.getFilteredPropertyList().indexOf(propertyToEdit) + 1;

        EditPropertyDescriptor descriptor = new EditPropertyDescriptor();
        descriptor.setAddress(new PropertyAddress("999 New Address"));

        EditPropertyCommand command = new EditPropertyCommand(Index.fromOneBased(index), descriptor);

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

        Set<Property> updatedProperties = new LinkedHashSet<>();
        for (Property p : owner.getProperties()) {
            if (p.equals(propertyToEdit)) {
                updatedProperties.add(editedProperty);
            } else {
                updatedProperties.add(p);
            }
        }

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
    public void equals_nullAndDifferentType_false() {
        EditPropertyDescriptor descriptor = new EditPropertyDescriptor();
        descriptor.setAddress(new PropertyAddress("A"));

        assertFalse(descriptor.equals(null));
        assertFalse(descriptor.equals(1));
    }

    @Test
    public void equals_differentFields_false() {
        EditPropertyDescriptor descriptor = new EditPropertyDescriptor();
        descriptor.setAddress(new PropertyAddress("A"));
        descriptor.setPrice(new Price("100"));

        EditPropertyDescriptor diffAddress = new EditPropertyDescriptor();
        diffAddress.setAddress(new PropertyAddress("B"));
        diffAddress.setPrice(new Price("100"));

        EditPropertyDescriptor diffPrice = new EditPropertyDescriptor();
        diffPrice.setAddress(new PropertyAddress("A"));
        diffPrice.setPrice(new Price("200"));

        assertFalse(descriptor.equals(diffAddress));
        assertFalse(descriptor.equals(diffPrice));
    }

    @Test
    public void equals_sameAddressDifferentPrice_false() {
        EditPropertyDescriptor first = new EditPropertyDescriptor();
        first.setAddress(new PropertyAddress("111 Clementi Ave 1"));
        first.setPrice(new Price("100"));
        first.setSize(new Size("1000"));

        EditPropertyDescriptor second = new EditPropertyDescriptor();
        second.setAddress(new PropertyAddress("111 Clementi Ave 1"));
        second.setPrice(new Price("200"));
        second.setSize(new Size("1000"));

        assertFalse(first.equals(second));
    }

    @Test
    public void equals_sameAddressSamePriceDifferentSize_false() {
        EditPropertyDescriptor first = new EditPropertyDescriptor();
        first.setAddress(new PropertyAddress("111 Clementi Ave 1"));
        first.setPrice(new Price("100"));
        first.setSize(new Size("1000"));

        EditPropertyDescriptor second = new EditPropertyDescriptor();
        second.setAddress(new PropertyAddress("111 Clementi Ave 1"));
        second.setPrice(new Price("100"));
        second.setSize(new Size("2000"));

        assertFalse(first.equals(second));
    }

    @Test
    public void editPropertyDescriptor_equals() {
        EditPropertyDescriptor descriptor = new EditPropertyDescriptor();
        descriptor.setAddress(new PropertyAddress("111 Clementi Ave 1"));
        descriptor.setPrice(new Price("100"));
        descriptor.setSize(new Size("1000"));

        EditPropertyDescriptor sameDescriptor = new EditPropertyDescriptor();
        sameDescriptor.setAddress(new PropertyAddress("111 Clementi Ave 1"));
        sameDescriptor.setPrice(new Price("100"));
        sameDescriptor.setSize(new Size("1000"));

        EditPropertyDescriptor diffAddress = new EditPropertyDescriptor();
        diffAddress.setAddress(new PropertyAddress("222 Clementi Ave 2"));
        diffAddress.setPrice(new Price("100"));
        diffAddress.setSize(new Size("1000"));

        EditPropertyDescriptor diffPrice = new EditPropertyDescriptor();
        diffPrice.setAddress(new PropertyAddress("111 Clementi Ave 1"));
        diffPrice.setPrice(new Price("200"));
        diffPrice.setSize(new Size("1000"));

        EditPropertyDescriptor diffSize = new EditPropertyDescriptor();
        diffSize.setAddress(new PropertyAddress("111 Clementi Ave 1"));
        diffSize.setPrice(new Price("100"));
        diffSize.setSize(new Size("2000"));

        assertTrue(descriptor.equals(descriptor));
        assertTrue(descriptor.equals(sameDescriptor));
        assertFalse(descriptor.equals(null));
        assertFalse(descriptor.equals(1));
        assertFalse(descriptor.equals(diffAddress));
        assertFalse(descriptor.equals(diffPrice));
        assertFalse(descriptor.equals(diffSize));
    }
}
