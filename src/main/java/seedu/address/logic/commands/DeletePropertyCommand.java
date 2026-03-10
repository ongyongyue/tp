package seedu.address.logic.commands;

import java.util.List;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.Messages;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.person.Person;

/**
 * Deletes the property information of a person.
 */
public class DeletePropertyCommand extends Command {

    public static final String COMMAND_WORD = "deleteProperty";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Deletes the property of the person identified "
            + "by the index number used in the displayed person list.\n"
            + "Parameters: INDEX (must be a positive integer)\n"
            + "Example: " + COMMAND_WORD + " 1";

    public static final String MESSAGE_SUCCESS = "Properties deleted from person: %1$s";
    public static final String MESSAGE_NO_PERSONS = "No clients found. Please add a client first.";

    private final Index index;

    /**
     * Creates a DeletePropertyCommand to delete the property of the specified person.
     */
    public DeletePropertyCommand(Index index) {
        this.index = index;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {

        List<Person> lastShownList = model.getFilteredPersonList();

        if (lastShownList.isEmpty()) {
            throw new CommandException(MESSAGE_NO_PERSONS);
        }

        if (index.getZeroBased() >= lastShownList.size()) {
            throw new CommandException("The person index provided is invalid.");
        }

        Person personToEdit = lastShownList.get(index.getZeroBased());

        if (personToEdit.getProperties().isEmpty()) {
            throw new CommandException(Messages.MESSAGE_INVALID_NO_PROPERTY);
        }

        Person editedPerson = new Person(personToEdit.getName(), personToEdit.getPhone(),
                personToEdit.getEmail(), personToEdit.getTags());

        model.setPerson(personToEdit, editedPerson);

        return new CommandResult(String.format(MESSAGE_SUCCESS, Messages.format(editedPerson)));
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
