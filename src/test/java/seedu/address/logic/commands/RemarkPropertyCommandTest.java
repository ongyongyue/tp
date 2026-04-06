package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandFailure;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_PERSON;
import static seedu.address.testutil.TypicalIndexes.INDEX_SECOND_PERSON;
import static seedu.address.testutil.TypicalPersons.getTypicalAddressBook;

import java.util.HashSet;
import java.util.Set;

import org.junit.jupiter.api.Test;

import seedu.address.logic.Messages;
import seedu.address.model.AddressBook;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.person.Person;
import seedu.address.model.property.Price;
import seedu.address.model.property.Property;
import seedu.address.model.property.PropertyAddress;
import seedu.address.model.property.PropertyType;
import seedu.address.model.property.Size;
import seedu.address.testutil.PersonBuilder;

/**
 * Contains integration tests (interaction with the Model) and unit tests for RemarkPropertyCommand.
 */
public class RemarkPropertyCommandTest {

    private static final String VALID_REMARK = "Near MRT";
    private static final String EMPTY_REMARK = "";

    private static final Property VALID_PROPERTY = createValidProperty();

    private static Property createValidProperty() {
        return new Property(
                new PropertyAddress("123 Main Street"),
                new Price("500000"),
                new Size("1200"),
                new PropertyType("HDB")
        );
    }

    private Model createModelWithPersonWithProperty() {
        AddressBook ab = new AddressBook();
        Set<Property> properties = new HashSet<>();
        properties.add(VALID_PROPERTY);
        Person personWithProperty = new PersonBuilder().withName("Alice Pauline")
                .withPhone("94351253")
                .withEmail("alice@example.com")
                .withTags("friends")
                .withProperties(properties)
                .build();
        ab.addPerson(personWithProperty);
        return new ModelManager(ab, new UserPrefs());
    }

    @Test
    public void execute_validIndexAndRemark_success() {
        Model model = createModelWithPersonWithProperty();
        // filter to the person with a property, as viewProperty would do
        model.updateFilteredPersonList(p -> !p.getProperties().isEmpty());

        RemarkPropertyCommand command = new RemarkPropertyCommand(INDEX_FIRST_PERSON, VALID_REMARK);

        Property updatedProperty = VALID_PROPERTY.withRemarks(VALID_REMARK);
        String expectedMessage = String.format(RemarkPropertyCommand.MESSAGE_SUCCESS, updatedProperty);

        Model expectedModel = createModelWithPersonWithProperty();
        expectedModel.updateFilteredPersonList(p -> !p.getProperties().isEmpty());
        Person currentPerson = expectedModel.getFilteredPersonList().get(0);
        Set<Property> updatedProperties = new HashSet<>();
        updatedProperties.add(updatedProperty);
        Person updatedPerson = new PersonBuilder(currentPerson)
                .withProperties(updatedProperties)
                .build();
        expectedModel.setPerson(currentPerson, updatedPerson);
        expectedModel.updateFilteredPersonList(p -> p.isSamePerson(updatedPerson));

        assertCommandSuccess(command, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_emptyRemark_success() {
        Model model = createModelWithPersonWithProperty();
        model.updateFilteredPersonList(p -> !p.getProperties().isEmpty());

        RemarkPropertyCommand command = new RemarkPropertyCommand(INDEX_FIRST_PERSON, EMPTY_REMARK);

        Property updatedProperty = VALID_PROPERTY.withRemarks(EMPTY_REMARK);
        String expectedMessage = String.format(RemarkPropertyCommand.MESSAGE_SUCCESS, updatedProperty);

        Model expectedModel = createModelWithPersonWithProperty();
        expectedModel.updateFilteredPersonList(p -> !p.getProperties().isEmpty());
        Person currentPerson = expectedModel.getFilteredPersonList().get(0);
        Set<Property> updatedProperties = new HashSet<>();
        updatedProperties.add(updatedProperty);
        Person updatedPerson = new PersonBuilder(currentPerson)
                .withProperties(updatedProperties)
                .build();
        expectedModel.setPerson(currentPerson, updatedPerson);
        expectedModel.updateFilteredPersonList(p -> p.isSamePerson(updatedPerson));

        assertCommandSuccess(command, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_noPersonInFilteredList_failure() {
        Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());
        // filter to empty list
        model.updateFilteredPersonList(p -> false);

        RemarkPropertyCommand command = new RemarkPropertyCommand(INDEX_FIRST_PERSON, VALID_REMARK);

        assertCommandFailure(command, model, Messages.MESSAGE_INVALID_NO_PROPERTY);
    }

    @Test
    public void execute_personHasNoProperties_failure() {
        Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());
        // ALICE in typical address book has no properties
        model.updateFilteredPersonList(p -> p.getName().fullName.equals("Alice Pauline"));

        RemarkPropertyCommand command = new RemarkPropertyCommand(INDEX_FIRST_PERSON, VALID_REMARK);

        assertCommandFailure(command, model, Messages.MESSAGE_INVALID_NO_PROPERTY);
    }

    @Test
    public void execute_invalidPropertyIndex_failure() {
        Model model = createModelWithPersonWithProperty();
        model.updateFilteredPersonList(p -> !p.getProperties().isEmpty());

        // only 1 property, so index 2 is out of bounds
        RemarkPropertyCommand command = new RemarkPropertyCommand(INDEX_SECOND_PERSON, VALID_REMARK);

        assertCommandFailure(command, model, Messages.MESSAGE_INVALID_PROPERTY_DISPLAYED_INDEX);
    }

    @Test
    public void equals() {
        RemarkPropertyCommand firstCommand = new RemarkPropertyCommand(INDEX_FIRST_PERSON, VALID_REMARK);
        RemarkPropertyCommand secondCommand = new RemarkPropertyCommand(INDEX_SECOND_PERSON, VALID_REMARK);
        RemarkPropertyCommand differentRemarkCommand = new RemarkPropertyCommand(INDEX_FIRST_PERSON, "Different");

        // same object -> returns true
        assertTrue(firstCommand.equals(firstCommand));

        // same values -> returns true
        RemarkPropertyCommand firstCommandCopy = new RemarkPropertyCommand(INDEX_FIRST_PERSON, VALID_REMARK);
        assertTrue(firstCommand.equals(firstCommandCopy));

        // null -> returns false
        assertFalse(firstCommand.equals(null));

        // different type -> returns false
        assertFalse(firstCommand.equals(new ClearCommand()));

        // different index -> returns false
        assertFalse(firstCommand.equals(secondCommand));

        // different remark -> returns false
        assertFalse(firstCommand.equals(differentRemarkCommand));
    }
}
