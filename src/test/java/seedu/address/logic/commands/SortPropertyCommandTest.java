package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_PERSON;
import static seedu.address.testutil.TypicalPersons.getTypicalAddressBook;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import seedu.address.commons.core.index.Index;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.property.Price;
import seedu.address.model.property.Property;
import seedu.address.model.property.PropertyAddress;
import seedu.address.model.property.PropertyType;
import seedu.address.model.property.Size;

/**
 * Contains integration tests (interaction with the Model) for {@code SortPropertyCommand}.
 */
public class SortPropertyCommandTest {
    private Model model;

    @BeforeEach
    public void setUp() throws Exception {
        model = new ModelManager(getTypicalAddressBook(), new UserPrefs());
        addPropertyToModel(model, INDEX_FIRST_PERSON, "311 Clementi Ave 2", "1200000", "1200");
        addPropertyToModel(model, INDEX_FIRST_PERSON, "50 Jurong East St 24", "850000", "1000");
    }

    @Test
    public void execute_sortByPriceAscending_success() {
        CommandResult result = new SortPropertyCommand("price", "up").execute(model);
        assertEquals(String.format(SortPropertyCommand.MESSAGE_SUCCESS, "price", "ascending"),
                result.getFeedbackToUser());
    }

    @Test
    public void execute_sortBySizeDescending_success() {
        CommandResult result = new SortPropertyCommand("size", "down").execute(model);
        assertEquals(String.format(SortPropertyCommand.MESSAGE_SUCCESS, "size", "descending"),
                result.getFeedbackToUser());
    }

    @Test
    public void equals() {
        SortPropertyCommand command = new SortPropertyCommand("price", "up");

        // same object -> true
        assertTrue(command.equals(command));

        // same values -> true
        assertTrue(command.equals(new SortPropertyCommand("price", "up")));

        // null -> false
        assertFalse(command.equals(null));

        // different type -> false
        assertFalse(command.equals(1));

        // different sort type -> false
        assertFalse(command.equals(new SortPropertyCommand("size", "up")));

        // different order -> false
        assertFalse(command.equals(new SortPropertyCommand("price", "down")));
    }

    @Test
    public void toStringMethod() {
        SortPropertyCommand command = new SortPropertyCommand("price", "up");
        String expected = SortPropertyCommand.class.getCanonicalName()
                + "{sortType=price, order=up}";
        assertEquals(expected, command.toString());
    }

    private void addPropertyToModel(Model model, Index index, String address, String price, String size)
            throws Exception {
        Property property = new Property(new PropertyAddress(address), new Price(price), new Size(size),
                    new PropertyType("HDB"));
        new AddPropertyCommand(index, property).execute(model);
    }
}
