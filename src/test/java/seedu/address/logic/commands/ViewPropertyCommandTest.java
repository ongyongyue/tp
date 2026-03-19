package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static seedu.address.testutil.Assert.assertThrows;

import java.util.HashSet;
import java.util.Set;

import org.junit.jupiter.api.Test;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.Messages;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.person.Email;
import seedu.address.model.person.Name;
import seedu.address.model.person.Person;
import seedu.address.model.person.Phone;
import seedu.address.model.property.Price;
import seedu.address.model.property.Property;
import seedu.address.model.property.PropertyAddress;
import seedu.address.model.property.Size;
import seedu.address.model.tag.Tag;

public class ViewPropertyCommandTest {

    private final Model model = new ModelManager();

    @Test
    public void execute_validIndex_success() throws Exception {
        Set<Tag> tags = new HashSet<>();
        Set<Property> properties = new HashSet<>();

        Property property = new Property(
                new PropertyAddress("123 Clementi Ave 3"),
                new Price("1000000"),
                new Size("121")
        );
        properties.add(property);

        // Person constructor no longer has Address
        Person person = new Person(
                new Name("Alice"),
                new Phone("91234567"),
                new Email("alice@email.com"),
                tags,
                properties
        );

        model.addPerson(person);

        ViewPropertyCommand command = new ViewPropertyCommand(Index.fromZeroBased(0));

        CommandResult result = command.execute(model);

        assertEquals(String.format(Messages.MESSAGE_PROPERTIES_LISTED_OVERVIEW, model.getFilteredPropertyList().size()),
                result.getFeedbackToUser());
        assertEquals(person, model.getFilteredPersonList().get(0));
    }

    @Test
    public void execute_invalidIndex_throwsCommandException() {
        ViewPropertyCommand command = new ViewPropertyCommand(Index.fromZeroBased(5));

        assertThrows(CommandException.class,
                Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX, () -> command.execute(model)
        );
    }

    @Test
    public void execute_noProperties_throwsCommandException() {
        Set<Tag> tags = new HashSet<>();
        Set<Property> properties = new HashSet<>(); // empty property set

        Person person = new Person(
                new Name("Bob"),
                new Phone("98765432"),
                new Email("bob@email.com"),
                tags,
                properties
        );

        model.addPerson(person);

        ViewPropertyCommand command = new ViewPropertyCommand(Index.fromZeroBased(0));

        assertThrows(CommandException.class,
                Messages.MESSAGE_INVALID_NO_PROPERTY, () -> command.execute(model)
        );
    }
}
