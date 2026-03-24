package seedu.address.logic.commands;

import java.util.List;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.Messages;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.person.Person;
import seedu.address.model.property.Property;

/**
 * Deletes a property at a specified index from the property list.
 */
public class DeletePropertyCommand extends Command {

    public static final String COMMAND_WORD = "deleteProperty";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Deletes the property identified "
            + "by the index number used in the displayed property list.\n"
            + "Parameters: INDEX (must be a positive integer)\n"
            + "Example: " + COMMAND_WORD + " 1";

    public static final String MESSAGE_SUCCESS = "Property deleted: %1$s";
    public static final String MESSAGE_NO_PROPERTIES = "No properties found. Please add a property first.";

    private final Index index;

    /**
     * Creates a DeletePropertyCommand to delete the property at the specified index.
     */
    public DeletePropertyCommand(Index index) {
        this.index = index;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {

        List<Property> lastShownPropertyList = model.getFilteredPropertyList();

        if (lastShownPropertyList.isEmpty()) {
            throw new CommandException(MESSAGE_NO_PROPERTIES);
        }

        if (index.getZeroBased() >= lastShownPropertyList.size()) {
            throw new CommandException(Messages.MESSAGE_INVALID_PROPERTY_DISPLAYED_INDEX);
        }

        Property propertyToDelete = lastShownPropertyList.get(index.getZeroBased());

        // Remove the property from all persons who own it
        List<Person> allPersons = model.getFilteredPersonList();
        for (Person person : allPersons) {
            if (person.getProperties().contains(propertyToDelete)) {
                Person editedPerson = person.removeProperty(propertyToDelete);
                model.setPerson(person, editedPerson);
            }
        }

        return new CommandResult(String.format(MESSAGE_SUCCESS, propertyToDelete));
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        if (!(other instanceof DeletePropertyCommand)) {
            return false;
        }

        DeletePropertyCommand otherDeletePropertyCommand = (DeletePropertyCommand) other;
        return index.equals(otherDeletePropertyCommand.index);
    }

    @Override
    public String toString() {
        return "DeletePropertyCommand{index=" + index + "}";
    }
}
