package seedu.address.logic.commands;

import java.util.List;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.Messages;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.person.Person;
import seedu.address.model.property.Property;

/**
 * Displays the property and the client who owns it.
 */
public class ViewPropertyCommand extends Command {

    public static final String COMMAND_WORD = "viewProperty";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Displays the property and the client who owns it.\n"
            + "Parameters: INDEX (must be a positive integer)\n"
            + "Example: " + COMMAND_WORD + " 1";

    private final Index index;

    /**
     * Creates a ViewPropertyCommand to view the information of the specified property.
     */
    public ViewPropertyCommand(Index index) {
        this.index = index;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        List<Property> lastShownPropertyList = model.getFilteredPropertyList();

        if (index.getZeroBased() >= lastShownPropertyList.size()) {
            throw new CommandException(Messages.MESSAGE_INVALID_PROPERTY_DISPLAYED_INDEX);
        }

        Property propertyToView = lastShownPropertyList.get(index.getZeroBased());

        // Find the person who owns this property (with single ownership, there's only one)
        Person ownerOfProperty = null;
        for (Person person : model.getFilteredPersonList()) {
            if (person.getProperties().contains(propertyToView)) {
                ownerOfProperty = person;
                break;
            }
        }

        // Filter properties to show only the selected property
        model.updateFilteredPropertyList(property -> property.isSameProperty(propertyToView));

        // Filter persons to show only the owner
        if (ownerOfProperty != null) {
            Person finalOwner = ownerOfProperty;
            model.updateFilteredPersonList(person -> person.equals(finalOwner));
        } else {
            model.updateFilteredPersonList(person -> false);
        }

        return new CommandResult(
                String.format(Messages.MESSAGE_PERSONS_LISTED_OVERVIEW,
                        ownerOfProperty != null ? 1 : 0));
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof ViewPropertyCommand)) {
            return false;
        }

        ViewPropertyCommand otherViewPropertyCommand = (ViewPropertyCommand) other;
        return index.equals(otherViewPropertyCommand.index);
    }
}
