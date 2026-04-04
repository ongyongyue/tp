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
 * Parses input arguments and creates a new FilterClientCommand object
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

        if (!argMultimap.getPreamble().isEmpty()) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, FilterClientCommand.MESSAGE_USAGE));
        }

        if (argMultimap.getValue(PREFIX_NAME).isEmpty() && argMultimap.getValue(PREFIX_TAG).isEmpty()) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, FilterClientCommand.MESSAGE_USAGE));
        }

        argMultimap.verifyNoDuplicatePrefixesFor(PREFIX_NAME, PREFIX_TAG);

        List<String> nameKeywords = Collections.emptyList();
        if (argMultimap.getValue(PREFIX_NAME).isPresent()) {
            String name = argMultimap.getValue(PREFIX_NAME).get().trim();
            if (name.isEmpty()) {
                throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                        FilterClientCommand.MESSAGE_USAGE));
            }

            String[] parsedNameKeywords = name.split("\\s+");
            for (String nameKeyword : parsedNameKeywords) {
                // Use ParserUtil to validate name keywords
                ParserUtil.parseName(nameKeyword);
            }
            nameKeywords = Arrays.asList(parsedNameKeywords);
        }

        List<String> tagKeywords = Collections.emptyList();
        if (argMultimap.getValue(PREFIX_TAG).isPresent()) {
            String tag = argMultimap.getValue(PREFIX_TAG).get().trim();
            if (tag.isEmpty()) {
                throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                        FilterClientCommand.MESSAGE_USAGE));
            }

            String[] parsedTagKeywords = tag.split("\\s+");
            for (String tagKeyword : parsedTagKeywords) {
                // Use ParserUtil to validate tag keywords
                ParserUtil.parseTag(tagKeyword);
            }
            tagKeywords = Arrays.asList(parsedTagKeywords);
        }

        return new FilterClientCommand(new PersonMatchesFilterPredicate(nameKeywords, tagKeywords));
    }

}
