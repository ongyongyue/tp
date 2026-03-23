package seedu.address.logic.commands;

import java.util.List;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.Messages;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.person.Person;

/**
 * Displays the information of a client and the properties that the client owns.
 */
public class ViewClientCommand extends Command {

    public static final String COMMAND_WORD = "viewClient";

    private final Index index;

    /**
     * Creates a ViewClientCommand to view the information of the specified person.
     */
    public ViewClientCommand(Index index) {
        this.index = index;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {

        List<Person> lastShownList = model.getFilteredPersonList();

        if (index.getZeroBased() >= lastShownList.size()) {
            throw new CommandException(Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
        }

        Person personToView = lastShownList.get(index.getZeroBased());

        model.updateFilteredPersonList(p -> p.isSamePerson(personToView));
        model.updateFilteredPropertyList(
                p -> lastShownList.stream().anyMatch(person -> person.getProperties().contains(p)));

        return new CommandResult(
                String.format(Messages.MESSAGE_PROPERTIES_LISTED_OVERVIEW, model.getFilteredPropertyList().size()));
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof ViewClientCommand)) {
            return false;
        }

        ViewClientCommand otherViewClientCommand = (ViewClientCommand) other;
        return index.equals(otherViewClientCommand.index);
    }
}
