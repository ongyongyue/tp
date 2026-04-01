package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NAME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TAG;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseSuccess;

import java.util.Arrays;
import java.util.Collections;

import org.junit.jupiter.api.Test;

import seedu.address.logic.Messages;
import seedu.address.logic.commands.FilterClientCommand;
import seedu.address.model.person.Name;
import seedu.address.model.person.PersonMatchesFilterPredicate;
import seedu.address.model.tag.Tag;

public class FilterClientCommandParserTest {

    private FilterClientCommandParser parser = new FilterClientCommandParser();

    @Test
    public void parse_emptyArg_throwsParseException() {
        assertParseFailure(parser, "Alice", String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                FilterClientCommand.MESSAGE_USAGE));
        assertParseFailure(parser, " n/", String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                FilterClientCommand.MESSAGE_USAGE));
        assertParseFailure(parser, " t/", String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                FilterClientCommand.MESSAGE_USAGE));
        assertParseFailure(parser, " ", String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                FilterClientCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_invalidName_throwsParseException() {
        assertParseFailure(parser, " n/Alice@Bob", Name.MESSAGE_CONSTRAINTS);
    }

    @Test
    public void parse_invalidTag_throwsParseException() {
        assertParseFailure(parser, " t/owe$Money", Tag.MESSAGE_CONSTRAINTS);
    }

    @Test
    public void parse_duplicatePrefixes_throwsParseException() {
        assertParseFailure(parser, " n/Alice n/Bob",
                Messages.getErrorMessageForDuplicatePrefixes(PREFIX_NAME));
        assertParseFailure(parser, " t/friends t/owesMoney",
                Messages.getErrorMessageForDuplicatePrefixes(PREFIX_TAG));
    }

    @Test
    public void parse_validArgs_returnsFilterClientCommand() {
        // no leading and trailing whitespaces
        FilterClientCommand expectedNameFilterClientCommand =
                new FilterClientCommand(new PersonMatchesFilterPredicate(
                        Arrays.asList("Alice", "Bob"), Collections.emptyList()));
        assertParseSuccess(parser, " n/Alice Bob", expectedNameFilterClientCommand);

        // multiple whitespaces between keywords
        assertParseSuccess(parser, " n/ Alice \n \t Bob  \t", expectedNameFilterClientCommand);

        // tag-only filtering
        FilterClientCommand expectedTagFilterClientCommand =
                new FilterClientCommand(new PersonMatchesFilterPredicate(
                        Collections.emptyList(), Arrays.asList("owesMoney", "friends")));
        assertParseSuccess(parser, " t/owesMoney friends", expectedTagFilterClientCommand);

        // multiple whitespaces between keywords
        assertParseSuccess(parser, " t/owesMoney \n \t friends  \t", expectedTagFilterClientCommand);

        // name and tag filtering
        FilterClientCommand expectedNameAndTagFilterClientCommand =
                new FilterClientCommand(new PersonMatchesFilterPredicate(
                        Arrays.asList("Alice"), Arrays.asList("friends")));
        assertParseSuccess(parser, " n/Alice t/friends", expectedNameAndTagFilterClientCommand);
    }

}
