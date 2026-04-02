package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NAME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TAG;

import seedu.address.commons.util.ToStringBuilder;
import seedu.address.logic.Messages;
import seedu.address.model.Model;
import seedu.address.model.person.PersonMatchesFilterPredicate;

/**
 * Shows only clients whose names or tags contain any of the specified keywords.
 */
public class FilterClientCommand extends Command {

    public static final String COMMAND_WORD = "filterClient";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Lists all persons whose names and/or tags match"
            + " the given case-insensitive keywords.\n"
            + "Parameters: [" + PREFIX_NAME + "NAME] [" + PREFIX_TAG + "TAG]\n"
            + "At least one prefix must be provided.\n"
            + "Examples: " + COMMAND_WORD + " " + PREFIX_NAME + "John " + PREFIX_TAG + "owesMoney";

    private final PersonMatchesFilterPredicate predicate;

    public FilterClientCommand(PersonMatchesFilterPredicate predicate) {
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

        FilterClientCommand otherFilterClientCommand = (FilterClientCommand) other;
        return predicate.equals(otherFilterClientCommand.predicate);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("predicate", predicate)
                .toString();
    }
}
