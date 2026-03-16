package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseSuccess;

import java.util.Arrays;

import org.junit.jupiter.api.Test;

import seedu.address.logic.commands.ViewClientCommand;
import seedu.address.model.person.NameContainsKeywordsPredicate;

public class ViewClientCommandParserTest {

    private ViewClientCommandParser parser = new ViewClientCommandParser();

    @Test
    public void parse_emptyArg_throwsParseException() {
        assertParseFailure(parser, "Alice", String.format(MESSAGE_INVALID_COMMAND_FORMAT, ViewClientCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_validArgs_returnsViewClientCommand() {
        // no leading and trailing whitespaces
        ViewClientCommand expectedViewClientCommand =
                new ViewClientCommand(new NameContainsKeywordsPredicate(Arrays.asList("Alice", "Bob")));
        assertParseSuccess(parser, "n/Alice Bob", expectedViewClientCommand);

        // multiple whitespaces between keywords
        assertParseSuccess(parser, " n/ Alice \n \t Bob  \t", expectedViewClientCommand);
    }

}
