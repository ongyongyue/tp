package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseSuccess;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_PERSON;

import org.junit.jupiter.api.Test;

import seedu.address.logic.commands.ViewPropertyCommand;

class ViewPropertyCommandParserTest {

    private final ViewPropertyCommandParser parser = new ViewPropertyCommandParser();

    @Test
    void parse_invalidArgs_throwsParseException() {
        // Empty input
        assertParseFailure(parser, " ", String.format(
                MESSAGE_INVALID_COMMAND_FORMAT, ViewPropertyCommand.MESSAGE_USAGE));

        // Non-numeric input
        assertParseFailure(parser, "abc", String.format(
                MESSAGE_INVALID_COMMAND_FORMAT, ViewPropertyCommand.MESSAGE_USAGE));

        // Negative number
        assertParseFailure(parser, "-1", String.format(
                MESSAGE_INVALID_COMMAND_FORMAT, ViewPropertyCommand.MESSAGE_USAGE));

        // Zero is invalid (Index is 1-based)
        assertParseFailure(parser, "0", String.format(
                MESSAGE_INVALID_COMMAND_FORMAT, ViewPropertyCommand.MESSAGE_USAGE));
    }

    @Test
    void parse_validArgs_returnsViewPropertyCommand() {
        assertParseSuccess(parser, "1", new ViewPropertyCommand(INDEX_FIRST_PERSON));
    }
}
