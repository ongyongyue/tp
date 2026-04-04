package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.parser.CliSyntax.PREFIX_SORT_ORDER;
import static seedu.address.logic.parser.CliSyntax.PREFIX_SORT_TYPE;

import java.util.Comparator;

import seedu.address.commons.util.ToStringBuilder;
import seedu.address.model.Model;
import seedu.address.model.property.Property;

/**
 * Shows the sorted property list displayed by price or size in ascending or descending order.
 */
public class SortPropertyCommand extends Command {

    public static final String COMMAND_WORD = "sortProperty";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Sorts the displayed property list by the specified field "
            + "in the specified order.\n"
            + "Parameters: " + PREFIX_SORT_TYPE + "SORT_TYPE " + PREFIX_SORT_ORDER + "ORDER\n"
            + "Example: " + COMMAND_WORD + " " + PREFIX_SORT_TYPE + "price " + PREFIX_SORT_ORDER + "up";

    public static final String MESSAGE_SUCCESS = "Properties sorted by %1$s in %2$s order.";

    private final String sortType;
    private final String order;

    /**
     * Creates a {@code SortPropertyCommand} to sort properties by the given type and order.
     *
     * @param sortType The field to sort by ("price" or "size").
     * @param order The sort direction ("up" for ascending, "down" for descending).
     */
    public SortPropertyCommand(String sortType, String order) {
        requireNonNull(sortType);
        requireNonNull(order);
        this.sortType = sortType;
        this.order = order;
    }

    @Override
    public CommandResult execute(Model model) {
        requireNonNull(model);
        Comparator<Property> comparator;
        if (sortType.equals("price")) {
            comparator = Comparator.comparingLong(p -> Long.parseLong(p.getPrice().value));
        } else {
            comparator = Comparator.comparingLong(p -> Long.parseLong(p.getSize().value));
        }

        if (order.equals("down")) {
            comparator = comparator.reversed();
        }

        model.sortPropertyList(comparator);

        String orderDescription = order.equals("up") ? "ascending" : "descending";
        return new CommandResult(String.format(MESSAGE_SUCCESS, sortType, orderDescription));
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof SortPropertyCommand)) {
            return false;
        }

        SortPropertyCommand otherSortPropertyCommand = (SortPropertyCommand) other;
        return sortType.equals(otherSortPropertyCommand.sortType)
                && order.equals(otherSortPropertyCommand.order);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("sortType", sortType)
                .add("order", order)
                .toString();
    }
}
