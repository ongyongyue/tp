package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseSuccess;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_PROPERTY;

import org.junit.jupiter.api.Test;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.DeletePropertyCommand;

/**
 * Contains unit tests for DeletePropertyCommandParser.
 */
class DeletePropertyCommandParserTest {

    private final DeletePropertyCommandParser parser = new DeletePropertyCommandParser();

    @Test
    void parse_validIndex_returnsDeletePropertyCommand() {
        // Valid index 1
        assertParseSuccess(parser, "1", new DeletePropertyCommand(INDEX_FIRST_PROPERTY));
    }

    @Test
    void parse_emptyArg_throwsParseException() {
        assertParseFailure(parser, "",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, DeletePropertyCommand.MESSAGE_USAGE));
    }

    @Test
    void parse_invalidArgs_throwsParseException() {
        // Non-numeric input
        assertParseFailure(parser, "abc",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, DeletePropertyCommand.MESSAGE_USAGE));

        // Negative number
        assertParseFailure(parser, "-1",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, DeletePropertyCommand.MESSAGE_USAGE));

        // Zero is invalid (Index is 1-based)
        assertParseFailure(parser, "0",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, DeletePropertyCommand.MESSAGE_USAGE));
    }

    @Test
    void parse_multipleArgs_throwsParseException() {
        // Multiple arguments provided
        assertParseFailure(parser, "1 2",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, DeletePropertyCommand.MESSAGE_USAGE));
    }

    @Test
    void parse_largeValidIndex_returnsDeletePropertyCommand() {
        // Large but valid index
        assertParseSuccess(parser, "999999", new DeletePropertyCommand(Index.fromOneBased(999999)));
    }
}
