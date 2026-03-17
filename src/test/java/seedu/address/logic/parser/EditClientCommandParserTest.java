package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.commands.CommandTestUtil.EMAIL_DESC_AMY;
import static seedu.address.logic.commands.CommandTestUtil.EMAIL_DESC_BOB;
import static seedu.address.logic.commands.CommandTestUtil.INVALID_EMAIL_DESC;
import static seedu.address.logic.commands.CommandTestUtil.INVALID_NAME_DESC;
import static seedu.address.logic.commands.CommandTestUtil.INVALID_PHONE_DESC;
import static seedu.address.logic.commands.CommandTestUtil.NAME_DESC_AMY;
import static seedu.address.logic.commands.CommandTestUtil.NAME_DESC_BOB;
import static seedu.address.logic.commands.CommandTestUtil.PHONE_DESC_AMY;
import static seedu.address.logic.commands.CommandTestUtil.PHONE_DESC_BOB;
import static seedu.address.logic.commands.EditClientCommand.MESSAGE_NOT_EDITED;
import static seedu.address.logic.commands.EditClientCommand.MESSAGE_USAGE;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseSuccess;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_PERSON;
import static seedu.address.testutil.TypicalIndexes.INDEX_SECOND_PERSON;

import org.junit.jupiter.api.Test;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.EditClientCommand;
import seedu.address.logic.commands.EditClientCommand.EditClientDescriptor;
import seedu.address.model.person.Email;
import seedu.address.model.person.Name;
import seedu.address.model.person.Phone;

public class EditClientCommandParserTest {
    private static final String MESSAGE_INVALID_FORMAT =
            String.format(MESSAGE_INVALID_COMMAND_FORMAT, MESSAGE_USAGE);

    private final EditClientCommandParser parser = new EditClientCommandParser();

    @Test
    public void parse_missingParts_failure() {
        assertParseFailure(parser, "1", MESSAGE_NOT_EDITED);
        assertParseFailure(parser, NAME_DESC_AMY, MESSAGE_INVALID_FORMAT);
        assertParseFailure(parser, "", MESSAGE_INVALID_FORMAT);
    }

    @Test
    public void parse_invalidPreamble_failure() {
        assertParseFailure(parser, "a" + NAME_DESC_AMY, MESSAGE_INVALID_FORMAT);
        assertParseFailure(parser, "-1" + NAME_DESC_AMY, MESSAGE_INVALID_FORMAT);
        assertParseFailure(parser, "0" + NAME_DESC_AMY, MESSAGE_INVALID_FORMAT);
        assertParseFailure(parser, "1 some random string", MESSAGE_INVALID_FORMAT);
    }

    @Test
    public void parse_invalidValue_failure() {
        assertParseFailure(parser, "1" + INVALID_NAME_DESC, Name.MESSAGE_CONSTRAINTS);
        assertParseFailure(parser, "1" + INVALID_PHONE_DESC, Phone.MESSAGE_CONSTRAINTS);
        assertParseFailure(parser, "1" + INVALID_EMAIL_DESC, Email.MESSAGE_CONSTRAINTS);
    }

    @Test
    public void parse_allFieldsSpecified_success() {
        Index targetIndex = INDEX_SECOND_PERSON;
        String userInput = targetIndex.getOneBased() + NAME_DESC_BOB + PHONE_DESC_BOB + EMAIL_DESC_BOB;

        EditClientDescriptor descriptor = new EditClientDescriptor();
        descriptor.setName(new Name("Bob Choo"));
        descriptor.setPhone(new Phone("22222222"));
        descriptor.setEmail(new Email("bob@example.com"));

        EditClientCommand expectedCommand = new EditClientCommand(targetIndex, descriptor);
        assertParseSuccess(parser, userInput, expectedCommand);
    }

    @Test
    public void parse_oneFieldSpecified_success() {
        Index targetIndex = INDEX_FIRST_PERSON;

        String userInput = targetIndex.getOneBased() + NAME_DESC_AMY;
        EditClientDescriptor descriptor = new EditClientDescriptor();
        descriptor.setName(new Name("Amy Bee"));
        assertParseSuccess(parser, userInput, new EditClientCommand(targetIndex, descriptor));

        userInput = targetIndex.getOneBased() + PHONE_DESC_AMY;
        descriptor = new EditClientDescriptor();
        descriptor.setPhone(new Phone("11111111"));
        assertParseSuccess(parser, userInput, new EditClientCommand(targetIndex, descriptor));

        userInput = targetIndex.getOneBased() + EMAIL_DESC_AMY;
        descriptor = new EditClientDescriptor();
        descriptor.setEmail(new Email("amy@example.com"));
        assertParseSuccess(parser, userInput, new EditClientCommand(targetIndex, descriptor));
    }
}
