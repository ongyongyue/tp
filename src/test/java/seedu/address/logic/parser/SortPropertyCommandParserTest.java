package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseSuccess;

import org.junit.jupiter.api.Test;

import seedu.address.logic.commands.SortPropertyCommand;

public class SortPropertyCommandParserTest {

    private SortPropertyCommandParser parser = new SortPropertyCommandParser();

    @Test
    public void parse_validArgs_success() {
        assertParseSuccess(parser, " st/price o/up", new SortPropertyCommand("price", "up"));
        assertParseSuccess(parser, " st/size o/down", new SortPropertyCommand("size", "down"));
    }

    @Test
    public void parse_missingPrefix_failure() {
        String expectedMessage = String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                SortPropertyCommand.MESSAGE_USAGE);
        assertParseFailure(parser, " o/up", expectedMessage);
        assertParseFailure(parser, " st/price", expectedMessage);
    }

    @Test
    public void parse_invalidSortType_failure() {
        String expectedMessage = String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                SortPropertyCommand.MESSAGE_USAGE);
        assertParseFailure(parser, " st/address o/up", expectedMessage);
    }

    @Test
    public void parse_invalidOrder_failure() {
        String expectedMessage = String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                SortPropertyCommand.MESSAGE_USAGE);
        assertParseFailure(parser, " st/price o/left", expectedMessage);
    }

    @Test
    public void parse_preamblePresent_failure() {
        String expectedMessage = String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                SortPropertyCommand.MESSAGE_USAGE);
        assertParseFailure(parser, "blah st/price o/up", expectedMessage);
    }
}
