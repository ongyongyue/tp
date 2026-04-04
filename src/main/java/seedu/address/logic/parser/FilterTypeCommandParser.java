package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.Messages.MESSAGE_INVALID_TYPE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TYPE;

import java.util.Arrays;

import seedu.address.logic.commands.FilterTypeCommand;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.property.PropertyTypeContainsKeywordsPredicate;

/**
 * Parses input arguments and creates a new FilterTypeCommand object.
 */
public class FilterTypeCommandParser implements Parser<FilterTypeCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the FilterTypeCommand
     * and returns a FilterTypeCommand object for execution.
     * @throws ParseException if the user input does not conform the expected format
     */
    @Override
    public FilterTypeCommand parse(String args) throws ParseException {
        ArgumentMultimap argMultimap =
                ArgumentTokenizer.tokenize(args, PREFIX_TYPE);

        if (!argMultimap.getPreamble().isEmpty()) {
            throw new ParseException(
                    String.format(MESSAGE_INVALID_COMMAND_FORMAT, FilterTypeCommand.MESSAGE_USAGE));
        }

        argMultimap.verifyNoDuplicatePrefixesFor(PREFIX_TYPE);

        if (argMultimap.getValue(PREFIX_TYPE).isEmpty()) {
            throw new ParseException(
                    String.format(MESSAGE_INVALID_COMMAND_FORMAT, FilterTypeCommand.MESSAGE_USAGE));
        }

        if (argMultimap.getValue(PREFIX_TYPE).get().isBlank()) {
            throw new ParseException(
                    String.format(MESSAGE_INVALID_COMMAND_FORMAT, FilterTypeCommand.MESSAGE_USAGE));
        }

        String type = argMultimap.getValue(PREFIX_TYPE).get();
        String[] typeKeywords = type.split("\\s+");

        if (typeKeywords.length != 1) {
            throw new ParseException(String.format(MESSAGE_INVALID_TYPE, FilterTypeCommand.MESSAGE_USAGE));
        }

        String keyword = typeKeywords[0].toLowerCase();
        if (!keyword.equals("hdb") && !keyword.equals("condo")) {
            throw new ParseException(String.format(MESSAGE_INVALID_TYPE, FilterTypeCommand.MESSAGE_USAGE));
        }

        return new FilterTypeCommand(new PropertyTypeContainsKeywordsPredicate(Arrays.asList(typeKeywords)));
    }
}
