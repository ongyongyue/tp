package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandFailure;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_PERSON;
import static seedu.address.testutil.TypicalPersons.getTypicalAddressBook;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.property.Price;
import seedu.address.model.property.Property;
import seedu.address.model.property.PropertyAddress;
import seedu.address.model.property.PropertyType;
import seedu.address.model.property.Size;

/**
 * Tests for HDB property limit validation in AddPropertyCommand.
 */
public class AddPropertyHdbLimitTest {

    private Model model;

    @BeforeEach
    public void setUp() {
        model = new ModelManager(getTypicalAddressBook(), new UserPrefs());
    }

    @Test
    public void execute_duplicateHdbProperty_throwsCommandException() throws CommandException {
        PropertyType hdbType = new PropertyType("HDB");
        Property hdbProperty1 = new Property(
                new PropertyAddress("311 Clementi Ave 2, #02-25"),
                new Price("1200000"),
                new Size("1200"),
                hdbType
        );
        Property hdbProperty2 = new Property(
                new PropertyAddress("312 Clementi Ave 2, #02-25"),
                new Price("1200000"),
                new Size("1200"),
                hdbType
        );

        // Add first HDB property
        AddPropertyCommand addCommand1 = new AddPropertyCommand(INDEX_FIRST_PERSON, hdbProperty1);
        addCommand1.execute(model);

        // Try to add second HDB property to same person
        AddPropertyCommand addCommand2 = new AddPropertyCommand(INDEX_FIRST_PERSON, hdbProperty2);

        assertCommandFailure(addCommand2, model, AddPropertyCommand.MESSAGE_DUPLICATE_HDB_PROPERTY);
    }

    @Test
    public void execute_multipleHdbPropertiesToDifferentPersons_success() throws CommandException {
        PropertyType hdbType = new PropertyType("HDB");
        Property hdbProperty1 = new Property(
                new PropertyAddress("311 Clementi Ave 2, #02-25"),
                new Price("1200000"),
                new Size("1200"),
                hdbType
        );
        Property hdbProperty2 = new Property(
                new PropertyAddress("312 Clementi Ave 2, #02-25"),
                new Price("1200000"),
                new Size("1200"),
                hdbType
        );

        // Add HDB property to first person
        AddPropertyCommand addCommand1 = new AddPropertyCommand(INDEX_FIRST_PERSON, hdbProperty1);
        addCommand1.execute(model);

        // Add HDB property to second person - this should succeed
        AddPropertyCommand addCommand2 = new AddPropertyCommand(Index.fromOneBased(2), hdbProperty2);
        CommandResult result = addCommand2.execute(model);

        assertTrue(result.getFeedbackToUser().contains("New property added"));
    }

    @Test
    public void execute_nonHdbPropertiesWithoutLimit_success() throws CommandException {
        PropertyType condoType = new PropertyType("Condo");
        Property condoProperty1 = new Property(
                new PropertyAddress("311 Clementi Ave 2, #02-25"),
                new Price("1200000"),
                new Size("1200"),
                condoType
        );
        Property condoProperty2 = new Property(
                new PropertyAddress("312 Clementi Ave 2, #02-25"),
                new Price("1500000"),
                new Size("1500"),
                condoType
        );

        // Add first Condo property
        AddPropertyCommand addCommand1 = new AddPropertyCommand(INDEX_FIRST_PERSON, condoProperty1);
        addCommand1.execute(model);

        // Add second Condo property to same person - this should succeed
        AddPropertyCommand addCommand2 = new AddPropertyCommand(INDEX_FIRST_PERSON, condoProperty2);
        CommandResult result = addCommand2.execute(model);

        assertTrue(result.getFeedbackToUser().contains("New property added"));
    }

    @Test
    public void execute_hdbAndNonHdbPropertiesCanCoexist_success() throws CommandException {
        PropertyType hdbType = new PropertyType("HDB");
        PropertyType condoType = new PropertyType("Condo");
        Property hdbProperty = new Property(
                new PropertyAddress("311 Clementi Ave 2, #02-25"),
                new Price("1200000"),
                new Size("1200"),
                hdbType
        );
        Property condoProperty = new Property(
                new PropertyAddress("312 Clementi Ave 2, #02-25"),
                new Price("1500000"),
                new Size("1500"),
                condoType
        );

        // Add HDB property
        AddPropertyCommand addCommand1 = new AddPropertyCommand(INDEX_FIRST_PERSON, hdbProperty);
        addCommand1.execute(model);

        // Add Condo property to same person - this should succeed
        AddPropertyCommand addCommand2 = new AddPropertyCommand(INDEX_FIRST_PERSON, condoProperty);
        CommandResult result = addCommand2.execute(model);

        assertTrue(result.getFeedbackToUser().contains("New property added"));
    }

    @Test
    public void execute_propertyWithoutTypeNotLimited_success() throws CommandException {
        // Property without a type should not trigger HDB limit
        Property propertyNoType = new Property(
                new PropertyAddress("311 Clementi Ave 2, #02-25"),
                new Price("1200000"),
                new Size("1200")
        );
        Property propertyNoType2 = new Property(
                new PropertyAddress("312 Clementi Ave 2, #02-25"),
                new Price("1200000"),
                new Size("1200")
        );

        // Add first property without type
        AddPropertyCommand addCommand1 = new AddPropertyCommand(INDEX_FIRST_PERSON, propertyNoType);
        addCommand1.execute(model);

        // Add second property without type - should succeed
        AddPropertyCommand addCommand2 = new AddPropertyCommand(INDEX_FIRST_PERSON, propertyNoType2);
        CommandResult result = addCommand2.execute(model);

        assertTrue(result.getFeedbackToUser().contains("New property added"));
    }
}
