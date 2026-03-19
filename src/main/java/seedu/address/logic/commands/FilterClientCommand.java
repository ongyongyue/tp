package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;

import seedu.address.commons.util.ToStringBuilder;
import seedu.address.logic.Messages;
import seedu.address.model.Model;
import seedu.address.model.person.NameContainsKeywordsPredicate;

/**
 * Shows only clients whose names contain any of the specified keywords.
 */
public class FilterClientCommand extends Command {

    public static final String COMMAND_WORD = "filterClient";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Lists all persons whose names contain any of "
            + "the specified keywords (case-insensitive) and displays them as a list with index numbers.\n"
            + "Parameters: n/NAME\n"
            + "Example: " + COMMAND_WORD + " n/John";

    private final NameContainsKeywordsPredicate predicate;

    public FilterClientCommand(NameContainsKeywordsPredicate predicate) {
        this.predicate = predicate;
    }

    @Override
    public CommandResult execute(Model model) {
        requireNonNull(model);
        model.updateFilteredPersonList(predicate);
        model.updateFilteredPropertyList(property -> model.getFilteredPersonList().stream()
                .anyMatch(person -> person.getProperties().contains(property)));
        return new CommandResult(
                String.format(Messages.MESSAGE_PERSONS_LISTED_OVERVIEW, model.getFilteredPersonList().size()));
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof FilterClientCommand)) {
            return false;
        }

        FilterClientCommand otherViewClientCommand = (FilterClientCommand) other;
        return predicate.equals(otherViewClientCommand.predicate);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("predicate", predicate)
                .toString();
    }
}
