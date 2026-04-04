package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseSuccess;

import java.util.Arrays;

import org.junit.jupiter.api.Test;

import seedu.address.logic.commands.FilterPropertyCommand;
import seedu.address.model.property.PropertyMatchesFilterPredicate;

public class FilterPropertyCommandParserTest {

    private FilterPropertyCommandParser parser = new FilterPropertyCommandParser();

    @Test
    public void parse_emptyArg_throwsParseException() {
        assertParseFailure(parser, "Clementi", String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                FilterPropertyCommand.MESSAGE_USAGE));
        assertParseFailure(parser, " ", String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                FilterPropertyCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_validAddressArgs_returnsFilterPropertyCommand() {
        // no leading and trailing whitespaces
        FilterPropertyCommand expectedFilterPropertyCommand =
                new FilterPropertyCommand(new PropertyMatchesFilterPredicate(
                        Arrays.asList("Clementi", "Punggol"),
                        0,
                        Long.MAX_VALUE,
                        0,
                        Long.MAX_VALUE));
        assertParseSuccess(parser, " a/Clementi Punggol", expectedFilterPropertyCommand);

        // multiple whitespaces between keywords
        assertParseSuccess(parser, " a/ Clementi \n \t Punggol  \t", expectedFilterPropertyCommand);
    }

    @Test
    public void parse_validRangeArgs_returnsFilterPropertyCommand() {
        FilterPropertyCommand expectedFilterPropertyCommand =
                new FilterPropertyCommand(new PropertyMatchesFilterPredicate(
                        Arrays.asList("Clementi"),
                        1000,
                        10000,
                        500,
                        5000));

        assertParseSuccess(parser, " a/Clementi pr/1000 10000 s/500 5000", expectedFilterPropertyCommand);
    }

    @Test
    public void parse_reversedRangeArgs_throwsParseException() {
        assertParseFailure(parser, " pr/10000 1000",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, FilterPropertyCommand.MESSAGE_USAGE));
        assertParseFailure(parser, " s/5000 500",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, FilterPropertyCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_invalidRangeArgCount_throwsParseException() {
        assertParseFailure(parser, " pr/1000 10000 500", String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                FilterPropertyCommand.MESSAGE_USAGE));
        assertParseFailure(parser, " s/500 5000 1000", String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                FilterPropertyCommand.MESSAGE_USAGE));
        assertParseFailure(parser, " pr/1000", String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                FilterPropertyCommand.MESSAGE_USAGE));
        assertParseFailure(parser, " s/500", String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                FilterPropertyCommand.MESSAGE_USAGE));
    }
}
