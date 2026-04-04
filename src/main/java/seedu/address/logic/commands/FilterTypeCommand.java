package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TYPE;

import seedu.address.commons.util.ToStringBuilder;
import seedu.address.model.Model;
import seedu.address.model.property.PropertyTypeContainsKeywordsPredicate;

/**
 * Shows only properties whose types contain any of the specified keywords.
 */
public class FilterTypeCommand extends Command {

    public static final String COMMAND_WORD = "filterType";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Lists all properties whose types contain any"
            + " of the specified keywords (case-insensitive) and displays them as a list with index numbers.\n"
            + "Parameters: " + PREFIX_TYPE + "TYPE\n"
            + "Example: " + COMMAND_WORD + " " + PREFIX_TYPE + "HDB";

    public static final String MESSAGE_PROPERTIES_LISTED = "%1$d properties listed!";

    private final PropertyTypeContainsKeywordsPredicate predicate;

    /**
     * Creates a {@code FilterTypeCommand} to filter the list of properties with the specified {@code predicate}.
     */
    public FilterTypeCommand(PropertyTypeContainsKeywordsPredicate predicate) {
        requireNonNull(predicate);
        this.predicate = predicate;
    }

    @Override
    public CommandResult execute(Model model) {
        requireNonNull(model);
        model.updateFilteredPropertyList(predicate);
        model.updateFilteredPersonList(person -> model.getFilteredPropertyList().stream()
                .anyMatch(property -> person.getProperties().contains(property)));
        return new CommandResult(
                String.format(MESSAGE_PROPERTIES_LISTED, model.getFilteredPropertyList().size()));
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof FilterTypeCommand)) {
            return false;
        }

        FilterTypeCommand otherFilterTypeCommand = (FilterTypeCommand) other;
        return predicate.equals(otherFilterTypeCommand.predicate);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("predicate", predicate)
                .toString();
    }
}




