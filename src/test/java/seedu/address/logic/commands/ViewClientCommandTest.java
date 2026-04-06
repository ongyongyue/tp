package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.logic.commands.ViewClientCommand.MESSAGE_CLIENT_VIEWED_SUCCESS;
import static seedu.address.testutil.Assert.assertThrows;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_PERSON;
import static seedu.address.testutil.TypicalIndexes.INDEX_SECOND_PERSON;
import static seedu.address.testutil.TypicalPersons.ALICE;
import static seedu.address.testutil.TypicalPersons.BENSON;

import java.util.LinkedHashSet;
import java.util.Set;

import org.junit.jupiter.api.Test;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.Messages;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.person.Person;
import seedu.address.model.property.Price;
import seedu.address.model.property.Property;
import seedu.address.model.property.PropertyAddress;
import seedu.address.model.property.PropertyType;
import seedu.address.model.property.Size;

public class ViewClientCommandTest {

    private final Model model = new ModelManager();

    @Test
    public void execute_validIndex_success() throws Exception {
        Set<Property> properties = new LinkedHashSet<>();

        Property property = new Property(
                new PropertyAddress("123 Clementi Ave 3"),
                new Price("1000000"),
                new Size("121"),
                new PropertyType("HDB")
        );
        properties.add(property);

        Person aliceWithProperty = ALICE.addProperty(property);
        Person bensonWithProperty = BENSON.addProperty(property);
        model.addPerson(aliceWithProperty);
        model.addPerson(bensonWithProperty);

        ViewClientCommand command = new ViewClientCommand(Index.fromZeroBased(0));

        CommandResult result = command.execute(model);

        assertEquals(String.format(MESSAGE_CLIENT_VIEWED_SUCCESS, Messages.format(aliceWithProperty)),
                result.getFeedbackToUser());
        assertEquals(aliceWithProperty, model.getFilteredPersonList().get(0));
    }

    @Test
    public void execute_invalidIndex_throwsCommandException() {
        ViewClientCommand command = new ViewClientCommand(Index.fromZeroBased(5));

        assertThrows(CommandException.class,
                Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX, () -> command.execute(model)
        );
    }

    @Test
    public void execute_noProperties_success() throws Exception {
        model.addPerson(ALICE);

        ViewClientCommand command = new ViewClientCommand(Index.fromZeroBased(0));

        CommandResult result = command.execute(model);

        assertEquals(String.format(MESSAGE_CLIENT_VIEWED_SUCCESS, Messages.format(ALICE)), result.getFeedbackToUser());
        assertEquals(ALICE, model.getFilteredPersonList().get(0));
    }

    @Test
    public void execute_clientsPropertyFiltered_success() throws Exception {
        Property aliceOnlyProperty = new Property(
                new PropertyAddress("10 Alpha Street"),
                new Price("500000"),
                new Size("90"),
                new PropertyType("Condo")
        );
        Property bensonOnlyProperty = new Property(
                new PropertyAddress("20 Beta Road"),
                new Price("900000"),
                new Size("120"),
                new PropertyType("Condo")
        );

        Person aliceWithProperty = ALICE.addProperty(aliceOnlyProperty);
        Person bensonWithProperty = BENSON.addProperty(bensonOnlyProperty);
        model.addPerson(aliceWithProperty);
        model.addPerson(bensonWithProperty);

        ViewClientCommand command = new ViewClientCommand(Index.fromZeroBased(1));

        CommandResult result = command.execute(model);

        assertEquals(String.format(MESSAGE_CLIENT_VIEWED_SUCCESS,
                Messages.format(bensonWithProperty)), result.getFeedbackToUser());
        assertEquals(1, model.getFilteredPropertyList().size());
        assertTrue(model.getFilteredPropertyList().contains(bensonOnlyProperty));
        assertFalse(model.getFilteredPropertyList().contains(aliceOnlyProperty));
        assertEquals(bensonWithProperty, model.getFilteredPersonList().get(0));
    }

    @Test
    public void equals() {
        ViewClientCommand viewFirstCommand = new ViewClientCommand(INDEX_FIRST_PERSON);
        ViewClientCommand viewSecondCommand = new ViewClientCommand(INDEX_SECOND_PERSON);

        // same object -> returns true
        assertTrue(viewFirstCommand.equals(viewFirstCommand));

        // same values -> returns true
        ViewClientCommand viewFirstCommandCopy = new ViewClientCommand(INDEX_FIRST_PERSON);
        assertTrue(viewFirstCommand.equals(viewFirstCommandCopy));

        // different types -> returns false
        assertFalse(viewFirstCommand.equals(1));

        // null -> returns false
        assertFalse(viewFirstCommand.equals(null));

        // different client -> returns false
        assertFalse(viewFirstCommand.equals(viewSecondCommand));
    }
}
