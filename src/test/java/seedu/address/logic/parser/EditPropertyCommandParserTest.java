package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_ADDRESS;
import static seedu.address.logic.parser.CliSyntax.PREFIX_PRICE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_SIZE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TYPE;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseSuccess;

import org.junit.jupiter.api.Test;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.EditPropertyCommand;
import seedu.address.logic.commands.EditPropertyCommand.EditPropertyDescriptor;
import seedu.address.model.property.Price;
import seedu.address.model.property.PropertyAddress;
import seedu.address.model.property.PropertyType;
import seedu.address.model.property.Size;

public class EditPropertyCommandParserTest {
    private static final String VALID_ADDRESS_AMY = "311 Clementi Ave 2, #02-25";
    private static final String VALID_ADDRESS_BOB = "123 Jurong West Ave 6";
    private static final String VALID_PRICE_AMY = "1200000";
    private static final String VALID_PRICE_BOB = "950000";
    private static final String VALID_SIZE_AMY = "1200";
    private static final String VALID_SIZE_BOB = "980";
    private static final String VALID_TYPE_AMY = "HDB";
    private static final String VALID_TYPE_BOB = "Condo";

    private static final String ADDRESS_DESC_AMY = " " + PREFIX_ADDRESS + VALID_ADDRESS_AMY;
    private static final String ADDRESS_DESC_BOB = " " + PREFIX_ADDRESS + VALID_ADDRESS_BOB;
    private static final String PRICE_DESC_AMY = " " + PREFIX_PRICE + VALID_PRICE_AMY;
    private static final String PRICE_DESC_BOB = " " + PREFIX_PRICE + VALID_PRICE_BOB;
    private static final String SIZE_DESC_AMY = " " + PREFIX_SIZE + VALID_SIZE_AMY;
    private static final String SIZE_DESC_BOB = " " + PREFIX_SIZE + VALID_SIZE_BOB;
    private static final String TYPE_DESC_AMY = " " + PREFIX_TYPE + VALID_TYPE_AMY;
    private static final String TYPE_DESC_BOB = " " + PREFIX_TYPE + VALID_TYPE_BOB;

    private static final String INVALID_ADDRESS_DESC = " " + PREFIX_ADDRESS;
    private static final String INVALID_PRICE_DESC = " " + PREFIX_PRICE + "abc";
    private static final String INVALID_SIZE_DESC = " " + PREFIX_SIZE + "abc";

    private static final String MESSAGE_INVALID_FORMAT =
            String.format(MESSAGE_INVALID_COMMAND_FORMAT, EditPropertyCommand.MESSAGE_USAGE);

    private final EditPropertyCommandParser parser = new EditPropertyCommandParser();

    @Test
    public void parse_allFieldsSpecified_success() {
        Index index = Index.fromOneBased(1);

        String userInput = "1" + ADDRESS_DESC_AMY + PRICE_DESC_AMY + SIZE_DESC_AMY + TYPE_DESC_AMY;

        EditPropertyDescriptor descriptor = new EditPropertyDescriptor();
        descriptor.setAddress(new PropertyAddress(VALID_ADDRESS_AMY));
        descriptor.setPrice(new Price(VALID_PRICE_AMY));
        descriptor.setSize(new Size(VALID_SIZE_AMY));
        descriptor.setType(new PropertyType(VALID_TYPE_AMY));

        EditPropertyCommand expectedCommand =
                new EditPropertyCommand(index, descriptor);

        assertParseSuccess(parser, userInput, expectedCommand);
    }

    @Test
    public void parse_singleFieldSpecified_success() {
        Index index = Index.fromOneBased(1);

        EditPropertyDescriptor addressDescriptor = new EditPropertyDescriptor();
        addressDescriptor.setAddress(new PropertyAddress(VALID_ADDRESS_AMY));
        assertParseSuccess(parser,
                "1" + ADDRESS_DESC_AMY,
                new EditPropertyCommand(index, addressDescriptor));

        EditPropertyDescriptor priceDescriptor = new EditPropertyDescriptor();
        priceDescriptor.setPrice(new Price(VALID_PRICE_AMY));
        assertParseSuccess(parser,
                "1" + PRICE_DESC_AMY,
                new EditPropertyCommand(index, priceDescriptor));

        EditPropertyDescriptor sizeDescriptor = new EditPropertyDescriptor();
        sizeDescriptor.setSize(new Size(VALID_SIZE_AMY));
        assertParseSuccess(parser,
                "1" + SIZE_DESC_AMY,
                new EditPropertyCommand(index, sizeDescriptor));

        EditPropertyDescriptor typeDescriptor = new EditPropertyDescriptor();
        typeDescriptor.setType(new PropertyType(VALID_TYPE_AMY));
        assertParseSuccess(parser,
                "1" + TYPE_DESC_AMY,
                new EditPropertyCommand(index, typeDescriptor));
    }

    @Test
    public void parse_multipleFieldsSpecified_success() {
        Index index = Index.fromOneBased(2);

        String userInput = "2" + ADDRESS_DESC_BOB + PRICE_DESC_BOB + SIZE_DESC_BOB + TYPE_DESC_BOB;

        EditPropertyDescriptor descriptor = new EditPropertyDescriptor();
        descriptor.setAddress(new PropertyAddress(VALID_ADDRESS_BOB));
        descriptor.setPrice(new Price(VALID_PRICE_BOB));
        descriptor.setSize(new Size(VALID_SIZE_BOB));
        descriptor.setType(new PropertyType(VALID_TYPE_BOB));

        EditPropertyCommand expectedCommand = new EditPropertyCommand(index, descriptor);

        assertParseSuccess(parser, userInput, expectedCommand);
    }

    @Test
    public void parse_missingIndex_failure() {
        assertParseFailure(parser,
                ADDRESS_DESC_AMY,
                MESSAGE_INVALID_FORMAT);
    }

    @Test
    public void parse_invalidIndex_failure() {
        assertParseFailure(parser,
                "0" + ADDRESS_DESC_AMY,
                MESSAGE_INVALID_FORMAT);

        assertParseFailure(parser,
                "-1" + ADDRESS_DESC_AMY,
                MESSAGE_INVALID_FORMAT);

        assertParseFailure(parser,
                "abc" + ADDRESS_DESC_AMY,
                MESSAGE_INVALID_FORMAT);
    }

    @Test
    public void parse_invalidType_failure() {
        assertParseFailure(parser,
                "1 type/villa",
                PropertyType.MESSAGE_CONSTRAINTS);
    }

    @Test
    public void parse_noFieldsSpecified_failure() {
        assertParseFailure(parser,
                "1",
                EditPropertyCommand.MESSAGE_NOT_EDITED);
    }

    @Test
    public void parse_invalidAddress_failure() {
        assertParseFailure(parser,
                "1" + INVALID_ADDRESS_DESC,
                PropertyAddress.MESSAGE_CONSTRAINTS);
    }

    @Test
    public void parse_invalidPrice_failure() {
        assertParseFailure(parser,
                "1" + INVALID_PRICE_DESC,
                Price.MESSAGE_CONSTRAINTS);
    }

    @Test
    public void parse_invalidSize_failure() {
        assertParseFailure(parser,
                "1" + INVALID_SIZE_DESC,
                Size.MESSAGE_CONSTRAINTS);
    }

    @Test
    public void parse_repeatedAddress_failure() {
        assertParseFailure(parser,
                "1 a/111 Clementi Ave 1 a/222 Clementi Ave 2",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, EditPropertyCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_repeatedPrice_failure() {
        assertParseFailure(parser,
                "1 pr/1000000 pr/2000000",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, EditPropertyCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_repeatedSize_failure() {
        assertParseFailure(parser,
                "1 s/1000 s/2000",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, EditPropertyCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_repeatedType_failure() {
        assertParseFailure(parser,
                "1 type/HDB type/Condo",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, EditPropertyCommand.MESSAGE_USAGE));
    }
}
