package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseSuccess;

import org.junit.jupiter.api.Test;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.EditPropertyCommand;
import seedu.address.logic.commands.EditPropertyCommand.EditPropertyDescriptor;
import seedu.address.model.property.Price;
import seedu.address.model.property.PropertyAddress;
import seedu.address.model.property.Size;

public class EditPropertyCommandParserTest {
    private static final String VALID_ADDRESS_AMY = "311 Clementi Ave 2, #02-25";
    private static final String VALID_ADDRESS_BOB = "123 Jurong West Ave 6";
    private static final String VALID_PRICE_AMY = "1200000";
    private static final String VALID_PRICE_BOB = "950000";
    private static final String VALID_SIZE_AMY = "1200";
    private static final String VALID_SIZE_BOB = "980";

    private static final String ADDRESS_DESC_AMY = " a/" + VALID_ADDRESS_AMY;
    private static final String ADDRESS_DESC_BOB = " a/" + VALID_ADDRESS_BOB;
    private static final String PRICE_DESC_AMY = " pr/" + VALID_PRICE_AMY;
    private static final String PRICE_DESC_BOB = " pr/" + VALID_PRICE_BOB;
    private static final String SIZE_DESC_AMY = " s/" + VALID_SIZE_AMY;
    private static final String SIZE_DESC_BOB = " s/" + VALID_SIZE_BOB;

    private static final String INVALID_ADDRESS_DESC = " a/";
    private static final String INVALID_PRICE_DESC = " pr/abc";
    private static final String INVALID_SIZE_DESC = " s/abc";

    private static final String MESSAGE_INVALID_FORMAT =
            String.format(MESSAGE_INVALID_COMMAND_FORMAT, EditPropertyCommand.MESSAGE_USAGE);

    private final EditPropertyCommandParser parser = new EditPropertyCommandParser();

    @Test
    public void parse_allFieldsSpecified_success() {
        Index clientIndex = Index.fromOneBased(1);
        Index propertyIndex = Index.fromOneBased(1);

        String userInput = "1 i/1" + ADDRESS_DESC_AMY + PRICE_DESC_AMY + SIZE_DESC_AMY;

        EditPropertyDescriptor descriptor = new EditPropertyDescriptor();
        descriptor.setAddress(new PropertyAddress(VALID_ADDRESS_AMY));
        descriptor.setPrice(new Price(VALID_PRICE_AMY));
        descriptor.setSize(new Size(VALID_SIZE_AMY));

        EditPropertyCommand expectedCommand =
                new EditPropertyCommand(clientIndex, propertyIndex, descriptor);

        assertParseSuccess(parser, userInput, expectedCommand);
    }

    @Test
    public void parse_singleFieldSpecified_success() {
        Index clientIndex = Index.fromOneBased(1);
        Index propertyIndex = Index.fromOneBased(1);

        EditPropertyDescriptor addressDescriptor = new EditPropertyDescriptor();
        addressDescriptor.setAddress(new PropertyAddress(VALID_ADDRESS_AMY));
        assertParseSuccess(parser,
                "1 i/1" + ADDRESS_DESC_AMY,
                new EditPropertyCommand(clientIndex, propertyIndex, addressDescriptor));

        EditPropertyDescriptor priceDescriptor = new EditPropertyDescriptor();
        priceDescriptor.setPrice(new Price(VALID_PRICE_AMY));
        assertParseSuccess(parser,
                "1 i/1" + PRICE_DESC_AMY,
                new EditPropertyCommand(clientIndex, propertyIndex, priceDescriptor));

        EditPropertyDescriptor sizeDescriptor = new EditPropertyDescriptor();
        sizeDescriptor.setSize(new Size(VALID_SIZE_AMY));
        assertParseSuccess(parser,
                "1 i/1" + SIZE_DESC_AMY,
                new EditPropertyCommand(clientIndex, propertyIndex, sizeDescriptor));
    }

    @Test
    public void parse_multipleFieldsSpecified_success() {
        Index clientIndex = Index.fromOneBased(2);
        Index propertyIndex = Index.fromOneBased(3);

        String userInput = "2 i/3" + ADDRESS_DESC_BOB + PRICE_DESC_BOB + SIZE_DESC_BOB;

        EditPropertyDescriptor descriptor = new EditPropertyDescriptor();
        descriptor.setAddress(new PropertyAddress(VALID_ADDRESS_BOB));
        descriptor.setPrice(new Price(VALID_PRICE_BOB));
        descriptor.setSize(new Size(VALID_SIZE_BOB));

        EditPropertyCommand expectedCommand =
                new EditPropertyCommand(clientIndex, propertyIndex, descriptor);

        assertParseSuccess(parser, userInput, expectedCommand);
    }

    @Test
    public void parse_repeatedFields_acceptsLast() {
        Index clientIndex = Index.fromOneBased(1);
        Index propertyIndex = Index.fromOneBased(1);

        String userInput = "1 i/1"
                + ADDRESS_DESC_AMY + ADDRESS_DESC_BOB
                + PRICE_DESC_AMY + PRICE_DESC_BOB
                + SIZE_DESC_AMY + SIZE_DESC_BOB;

        EditPropertyDescriptor descriptor = new EditPropertyDescriptor();
        descriptor.setAddress(new PropertyAddress(VALID_ADDRESS_BOB));
        descriptor.setPrice(new Price(VALID_PRICE_BOB));
        descriptor.setSize(new Size(VALID_SIZE_BOB));

        EditPropertyCommand expectedCommand =
                new EditPropertyCommand(clientIndex, propertyIndex, descriptor);

        assertParseSuccess(parser, userInput, expectedCommand);
    }

    @Test
    public void parse_missingClientIndex_failure() {
        assertParseFailure(parser,
                " i/1" + ADDRESS_DESC_AMY,
                MESSAGE_INVALID_FORMAT);
    }

    @Test
    public void parse_missingPropertyIndex_failure() {
        assertParseFailure(parser,
                "1" + ADDRESS_DESC_AMY,
                MESSAGE_INVALID_FORMAT);
    }

    @Test
    public void parse_invalidClientIndex_failure() {
        assertParseFailure(parser,
                "0 i/1" + ADDRESS_DESC_AMY,
                MESSAGE_INVALID_FORMAT);

        assertParseFailure(parser,
                "-1 i/1" + ADDRESS_DESC_AMY,
                MESSAGE_INVALID_FORMAT);

        assertParseFailure(parser,
                "abc i/1" + ADDRESS_DESC_AMY,
                MESSAGE_INVALID_FORMAT);
    }

    @Test
    public void parse_invalidPropertyIndex_failure() {
        assertParseFailure(parser,
                "1 i/0" + ADDRESS_DESC_AMY,
                MESSAGE_INVALID_FORMAT);

        assertParseFailure(parser,
                "1 i/-1" + ADDRESS_DESC_AMY,
                MESSAGE_INVALID_FORMAT);

        assertParseFailure(parser,
                "1 i/abc" + ADDRESS_DESC_AMY,
                MESSAGE_INVALID_FORMAT);
    }

    @Test
    public void parse_noFieldsSpecified_failure() {
        assertParseFailure(parser,
                "1 i/1",
                EditPropertyCommand.MESSAGE_NOT_EDITED);
    }

    @Test
    public void parse_invalidAddress_failure() {
        assertParseFailure(parser,
                "1 i/1" + INVALID_ADDRESS_DESC,
                PropertyAddress.MESSAGE_CONSTRAINTS);
    }

    @Test
    public void parse_invalidPrice_failure() {
        assertParseFailure(parser,
                "1 i/1" + INVALID_PRICE_DESC,
                Price.MESSAGE_CONSTRAINTS);
    }

    @Test
    public void parse_invalidSize_failure() {
        assertParseFailure(parser,
                "1 i/1" + INVALID_SIZE_DESC,
                Size.MESSAGE_CONSTRAINTS);
    }
}
