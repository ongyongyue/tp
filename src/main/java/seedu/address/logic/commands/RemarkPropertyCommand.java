package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;

import java.util.List;
import java.util.LinkedHashSet;
import java.util.Set;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.Messages;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.person.Person;
import seedu.address.model.property.Property;

/**
 * Updates the remarks of a property in the currently displayed property list.
 */
public class RemarkPropertyCommand extends Command {

    public static final String COMMAND_WORD = "remarkProperty";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Updates the remark of the property identified "
            + "by the index number in the currently displayed property list.\n"
            + "Parameters: INDEX (must be a positive integer) r/REMARK\n"
            + "Example: " + COMMAND_WORD + " 1 r/Near MRT";

    public static final String MESSAGE_SUCCESS = "Updated remark for property: %1$s";

    private final Index propertyIndex;
    private final String remark;

    /**
     * @param propertyIndex index of the property in the currently displayed property list
     * @param remark the new remark to set
     */
    public RemarkPropertyCommand(Index propertyIndex, String remark) {
        requireNonNull(propertyIndex);
        requireNonNull(remark);
        this.propertyIndex = propertyIndex;
        this.remark = remark;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);

        List<Property> filteredPropertyList = model.getFilteredPropertyList();

        if (filteredPropertyList.isEmpty()) {
            throw new CommandException(Messages.MESSAGE_INVALID_NO_PROPERTY);
        }

        if (propertyIndex.getZeroBased() >= filteredPropertyList.size()) {
            throw new CommandException(Messages.MESSAGE_INVALID_PROPERTY_DISPLAYED_INDEX);
        }

        // Get the target property from the flat global property list
        Property targetProperty = filteredPropertyList.get(propertyIndex.getZeroBased());
        Property updatedProperty = targetProperty.withRemarks(remark);

        // Reverse-look up the owner
        Person owner = null;
        for (Person person : model.getAddressBook().getPersonList()) {
            if (person.getProperties().contains(targetProperty)) {
                owner = person;
                break;
            }
        }

        if (owner == null) {
            throw new CommandException(Messages.MESSAGE_INVALID_PROPERTY_DISPLAYED_INDEX);
        }

        // Rebuild the owner's property set preserving order
        Set<Property> updatedProperties = new LinkedHashSet<>();
        for (Property p : owner.getProperties()) {
            if (p.equals(targetProperty)) {
                updatedProperties.add(updatedProperty);
            } else {
                updatedProperties.add(p);
            }
        }

        Person updatedPerson = new Person(
                owner.getName(),
                owner.getPhone(),
                owner.getEmail(),
                owner.getTags(),
                updatedProperties
        );

        model.setPerson(owner, updatedPerson);
        model.updateFilteredPersonList(p -> p.isSamePerson(updatedPerson));
        model.updateFilteredPropertyList(p -> p.equals(updatedProperty));
        model.updateFilteredPersonList(p -> p.isSamePerson(updatedPerson));

        return new CommandResult(String.format(MESSAGE_SUCCESS, updatedProperty));
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if (!(other instanceof RemarkPropertyCommand)) {
            return false;
        }
        RemarkPropertyCommand otherCommand = (RemarkPropertyCommand) other;
        return propertyIndex.equals(otherCommand.propertyIndex)
                && remark.equals(otherCommand.remark);
    }
}
