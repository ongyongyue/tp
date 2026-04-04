package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_SORT_ORDER;
import static seedu.address.logic.parser.CliSyntax.PREFIX_SORT_TYPE;

import seedu.address.logic.commands.SortPropertyCommand;
import seedu.address.logic.parser.exceptions.ParseException;

/**
 * Parses input arguments and creates a new SortPropertyCommand object.
 */
public class SortPropertyCommandParser implements Parser<SortPropertyCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the SortPropertyCommand
     * and returns a SortPropertyCommand object for execution.
     * @throws ParseException if the user input does not conform the expected format
     */
    @Override
    public SortPropertyCommand parse(String args) throws ParseException {
        ArgumentMultimap argMultimap =
                ArgumentTokenizer.tokenize(args, PREFIX_SORT_TYPE, PREFIX_SORT_ORDER);

        if (!argMultimap.getValue(PREFIX_SORT_TYPE).isPresent()
                || !argMultimap.getValue(PREFIX_SORT_ORDER).isPresent()
                || !argMultimap.getPreamble().isEmpty()) {
            throw new ParseException(
                    String.format(MESSAGE_INVALID_COMMAND_FORMAT, SortPropertyCommand.MESSAGE_USAGE));
        }

        argMultimap.verifyNoDuplicatePrefixesFor(PREFIX_SORT_TYPE, PREFIX_SORT_ORDER);

        String sortType = argMultimap.getValue(PREFIX_SORT_TYPE).get().trim().toLowerCase();
        String order = argMultimap.getValue(PREFIX_SORT_ORDER).get().trim().toLowerCase();

        if (!sortType.equals("price") && !sortType.equals("size")) {
            throw new ParseException(
                    String.format(MESSAGE_INVALID_COMMAND_FORMAT, SortPropertyCommand.MESSAGE_USAGE));
        }

        if (!order.equals("up") && !order.equals("down")) {
            throw new ParseException(
                    String.format(MESSAGE_INVALID_COMMAND_FORMAT, SortPropertyCommand.MESSAGE_USAGE));
        }

        return new SortPropertyCommand(sortType, order);
    }
}
