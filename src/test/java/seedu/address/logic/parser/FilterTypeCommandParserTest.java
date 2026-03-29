package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
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
    public void parse_noPrefix_throwsParseException() {
        assertParseFailure(parser, "HDB", String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                FilterTypeCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_validSingleKeyword_returnsFilterTypeCommand() {
        // Single keyword
        FilterTypeCommand expectedFilterTypeCommand =
                new FilterTypeCommand(new PropertyTypeContainsKeywordsPredicate(Arrays.asList("HDB")));
        assertParseSuccess(parser, " type/HDB", expectedFilterTypeCommand);
    }

    @Test
    public void parse_validMultipleKeywords_returnsFilterTypeCommand() {
        // Multiple keywords
        FilterTypeCommand expectedFilterTypeCommand =
                new FilterTypeCommand(new PropertyTypeContainsKeywordsPredicate(Arrays.asList("HDB", "Condo")));
        assertParseSuccess(parser, " type/HDB Condo", expectedFilterTypeCommand);
    }

    @Test
    public void parse_multipleWhitespaces_returnsFilterTypeCommand() {
        // Multiple whitespaces between keywords
        FilterTypeCommand expectedFilterTypeCommand =
                new FilterTypeCommand(new PropertyTypeContainsKeywordsPredicate(Arrays.asList("HDB", "Condo")));
        assertParseSuccess(parser, " type/ HDB \n \t Condo  \t", expectedFilterTypeCommand);
    }

    @Test
    public void parse_caseInsensitiveKeywords_returnsFilterTypeCommand() {
        FilterTypeCommand expectedFilterTypeCommand =
                new FilterTypeCommand(new PropertyTypeContainsKeywordsPredicate(Arrays.asList("hdb", "condo")));
        assertParseSuccess(parser, " type/hdb condo", expectedFilterTypeCommand);
    }
}
