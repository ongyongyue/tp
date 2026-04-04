package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.parser.CliSyntax.PREFIX_ADDRESS;
import static seedu.address.logic.parser.CliSyntax.PREFIX_PRICE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_SIZE;

import seedu.address.commons.util.ToStringBuilder;
import seedu.address.model.Model;
import seedu.address.model.property.PropertyMatchesFilterPredicate;

/**
 * Shows only properties that match the specified filter criteria.
 */
public class FilterPropertyCommand extends Command {

    public static final String COMMAND_WORD = "filterProperty";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Lists all properties that match the given "
            + "address keywords and/or price and size ranges, and displays them as a list with index numbers.\n"
            + "Parameters: [" + PREFIX_ADDRESS + "ADDRESS_KEYWORDS] "
            + "[" + PREFIX_PRICE + "MIN_PRICE MAX_PRICE] "
            + "[" + PREFIX_SIZE + "MIN_SIZE MAX_SIZE]\n"
            + "Example: " + COMMAND_WORD + " " + PREFIX_ADDRESS + "Clementi "
            + PREFIX_PRICE + "1000 10000 "
            + PREFIX_SIZE + "500 5000";

    public static final String MESSAGE_PROPERTIES_LISTED = "%1$d properties listed!";

    private final PropertyMatchesFilterPredicate predicate;

    /**
     * Creates a {@code FilterPropertyCommand} to filter the list of properties with the specified {@code predicate}.
    */
    public FilterPropertyCommand(PropertyMatchesFilterPredicate predicate) {
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
        if (!(other instanceof FilterPropertyCommand)) {
            return false;
        }

        FilterPropertyCommand otherFilterPropertyCommand = (FilterPropertyCommand) other;
        return predicate.equals(otherFilterPropertyCommand.predicate);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("predicate", predicate)
                .toString();
    }
}
