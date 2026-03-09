package seedu.address.logic.commands;

import java.util.List;
import java.util.Set;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.Messages;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.person.Person;
import seedu.address.model.property.Property;

/**
 * Displays the property information of a person.
 */
public class ViewPropertyCommand extends Command {

    public static final String COMMAND_WORD = "viewProperty";

    private final Index index;

    /**
     * Creates a ViewPropertyCommand to view the property of the specified person.
     */
    public ViewPropertyCommand(Index index) {
        this.index = index;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {

        List<Person> lastShownList = model.getFilteredPersonList();

        if (index.getZeroBased() >= lastShownList.size()) {
            throw new CommandException(Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
        }

        Person personToView = lastShownList.get(index.getZeroBased());

        if (personToView.getProperties().isEmpty()) {
            throw new CommandException(Messages.MESSAGE_INVALID_NO_PROPERTY);
        }


        return new CommandResult(personToView.propertiesToString());
    }
}
