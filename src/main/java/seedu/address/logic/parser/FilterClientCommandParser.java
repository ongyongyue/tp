package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NAME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TAG;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import seedu.address.logic.commands.FilterClientCommand;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.person.PersonMatchesFilterPredicate;

/**
 * Parses input arguments and creates a new FilterClientCommand object.
 */
public class FilterClientCommandParser implements Parser<FilterClientCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the FilterClientCommand
     * and returns a FilterClientCommand object for execution.
     * @throws ParseException if the user input does not conform to the expected format
     */
    @Override
    public FilterClientCommand parse(String args) throws ParseException {
        ArgumentMultimap argMultimap =
                ArgumentTokenizer.tokenize(args, PREFIX_NAME, PREFIX_TAG);

        if (!argMultimap.getPreamble().isEmpty()
                || (argMultimap.getValue(PREFIX_NAME).isEmpty() && argMultimap.getValue(PREFIX_TAG).isEmpty())) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, FilterClientCommand.MESSAGE_USAGE));
        }

        argMultimap.verifyNoDuplicatePrefixesFor(PREFIX_NAME, PREFIX_TAG);

        List<String> nameKeywords = parseKeywords(argMultimap, PREFIX_NAME);
        List<String> tagKeywords = parseKeywords(argMultimap, PREFIX_TAG);

        return new FilterClientCommand(new PersonMatchesFilterPredicate(nameKeywords, tagKeywords));
    }

    private List<String> parseKeywords(ArgumentMultimap argMultimap, Prefix prefix) throws ParseException {
        List<String> keywords = Collections.emptyList();
        if (argMultimap.getValue(prefix).isPresent()) {
            String input = argMultimap.getValue(prefix).get().trim();
            if (input.isEmpty()) {
                throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                        FilterClientCommand.MESSAGE_USAGE));
            }

            keywords = Arrays.asList(input.split("\\s+"));

            // Keyword validation using ParserUtil methods
            if (prefix.equals(PREFIX_NAME)) {
                for (String keyword : keywords) {
                    ParserUtil.parseName(keyword);
                }
            } else if (prefix.equals(PREFIX_TAG)) {
                for (String keyword : keywords) {
                    ParserUtil.parseTag(keyword);
                }
            }
        }
        return keywords;
    }
}
