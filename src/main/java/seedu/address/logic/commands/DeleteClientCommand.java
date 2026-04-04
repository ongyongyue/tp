package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;

import java.util.List;

import seedu.address.commons.core.index.Index;
import seedu.address.commons.util.ToStringBuilder;
import seedu.address.logic.Messages;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.person.Person;

/**
 * Deletes a client identified using its displayed index from the address book.
 */
public class DeleteClientCommand extends Command {

    public static final String COMMAND_WORD = "deleteClient";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Deletes the client identified by the index number used in the displayed client list. "
            + "All properties of the client will also be deleted.\n"
            + "Parameters: INDEX (must be a positive integer)\n"
            + "Example: " + COMMAND_WORD + " 1";

    public static final String MESSAGE_DELETE_CLIENT_SUCCESS = "Deleted Client: %1$s";

    private final Index targetIndex;

    public DeleteClientCommand(Index targetIndex) {
        this.targetIndex = targetIndex;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);
        List<Person> lastShownList = model.getFilteredPersonList();

        if (targetIndex.getZeroBased() >= lastShownList.size()) {
            throw new CommandException(Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
        }

        Person personToDelete = lastShownList.get(targetIndex.getZeroBased());

        // Format the person information for the success message
        String formattedPersonToDelete = Messages.format(personToDelete);

        // Collect information about deleted properties
        StringBuilder deletedPropertiesInfo = new StringBuilder();

        // Delete all properties associated with this client
        if (!personToDelete.getProperties().isEmpty()) {
            // Filter property list to show only properties belonging to this client
            model.updateFilteredPropertyList(
                    p -> personToDelete.getProperties().contains(p));

            // Get all properties in the filtered list (all belonging to this client)
            List<seedu.address.model.property.Property> propertiesToDelete =
                    model.getFilteredPropertyList();

            // Collect property details before deletion
            deletedPropertiesInfo.append("\nDeleted Properties:");
            for (seedu.address.model.property.Property property : propertiesToDelete) {
                deletedPropertiesInfo.append("\n- ").append(property);
            }

            // Delete each property starting from the last to avoid index shifting issues
            for (int i = propertiesToDelete.size() - 1; i >= 0; i--) {
                DeletePropertyCommand deletePropertyCommand =
                        new DeletePropertyCommand(Index.fromZeroBased(i));
                deletePropertyCommand.execute(model);
            }
        }

        // After deleting properties, get the updated person reference (properties may have been removed)
        Person personToDeleteUpdated = lastShownList.get(targetIndex.getZeroBased());

        // Delete the client
        model.deletePerson(personToDeleteUpdated);

        // After deleting the client, update property filter to match remaining displayed clients
        List<Person> remainingClients = model.getFilteredPersonList();
        model.updateFilteredPropertyList(
                p -> remainingClients.stream().anyMatch(person -> person.getProperties().contains(p)));

        return new CommandResult(String.format(MESSAGE_DELETE_CLIENT_SUCCESS, formattedPersonToDelete)
                + deletedPropertiesInfo);
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof DeleteClientCommand otherDeleteCommand)) {
            return false;
        }

        return targetIndex.equals(otherDeleteCommand.targetIndex);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("targetIndex", targetIndex)
                .toString();
    }
}
