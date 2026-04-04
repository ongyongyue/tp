package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.Messages.MESSAGE_INVALID_TYPE;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseSuccess;

import java.util.Arrays;

import org.junit.jupiter.api.Test;

import seedu.address.logic.commands.FilterTypeCommand;
import seedu.address.model.property.PropertyTypeContainsKeywordsPredicate;

/**
 * Contains unit tests for FilterTypeCommandParser.
 */
public class FilterTypeCommandParserTest {

    private final FilterTypeCommandParser parser = new FilterTypeCommandParser();

    @Test
    public void parse_emptyArg_throwsParseException() {
        assertParseFailure(parser, "", String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                FilterTypeCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_emptyKeyword_throwsParseException() {
        assertParseFailure(parser, "type/", String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                FilterTypeCommand.MESSAGE_USAGE));
        assertParseFailure(parser, " type/   ", String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                FilterTypeCommand.MESSAGE_USAGE));
        assertParseFailure(parser, " type/\t", String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                FilterTypeCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_noPrefix_throwsParseException() {
        assertParseFailure(parser, "HDB", String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                FilterTypeCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_doublePrefixKeyword_throwsParseException() {
        assertParseFailure(parser, "type/HDB type/Condo", String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                FilterTypeCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_validSingleKeyword_returnsFilterTypeCommand() {
        // Single keyword HDB
        FilterTypeCommand expectedFilterTypeCommand =
                new FilterTypeCommand(new PropertyTypeContainsKeywordsPredicate(Arrays.asList("HDB")));
        assertParseSuccess(parser, " type/HDB", expectedFilterTypeCommand);

        // Single keyword Condo
        expectedFilterTypeCommand =
                new FilterTypeCommand(new PropertyTypeContainsKeywordsPredicate(Arrays.asList("Condo")));
        assertParseSuccess(parser, " type/Condo", expectedFilterTypeCommand);
    }

    @Test
    public void parse_caseInsensitiveKeywords_returnsFilterTypeCommand() {
        FilterTypeCommand expectedFilterTypeCommand =
                new FilterTypeCommand(new PropertyTypeContainsKeywordsPredicate(Arrays.asList("hdb")));
        assertParseSuccess(parser, " type/hdb", expectedFilterTypeCommand);

        expectedFilterTypeCommand =
                new FilterTypeCommand(new PropertyTypeContainsKeywordsPredicate(Arrays.asList("CONDO")));
        assertParseSuccess(parser, " type/CONDO", expectedFilterTypeCommand);
    }

    @Test
    public void parse_multipleKeywords_throwsParseException() {
        assertParseFailure(parser, " type/HDB Condo",
                String.format(MESSAGE_INVALID_TYPE, FilterTypeCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_invalidType_throwsParseException() {
        assertParseFailure(parser, " type/Apartment",
                String.format(MESSAGE_INVALID_TYPE, FilterTypeCommand.MESSAGE_USAGE));
    }
}
