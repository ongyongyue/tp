package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_ADDRESS;
import static seedu.address.logic.parser.CliSyntax.PREFIX_LISTING_INDEX;
import static seedu.address.logic.parser.CliSyntax.PREFIX_PRICE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_SIZE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TYPE;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseSuccess;

import org.junit.jupiter.api.Test;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.AddPropertyCommand;
import seedu.address.model.property.Price;
import seedu.address.model.property.Property;
import seedu.address.model.property.PropertyAddress;
import seedu.address.model.property.PropertyType;
import seedu.address.model.property.Size;

public class AddPropertyCommandParserTest {

    private static final String VALID_ADDRESS = "311 Clementi Ave 2, #02-25";
    private static final String VALID_PRICE = "1200000";
    private static final String VALID_SIZE = "1200";

    private static final String INVALID_ADDRESS = "@123";
    private static final String INVALID_PRICE = "abc";
    private static final String INVALID_SIZE = "abc";

    private AddPropertyCommandParser parser = new AddPropertyCommandParser();

    @Test
    public void parse_allFieldsPresent_success() {
        String userInput = " "
                + PREFIX_LISTING_INDEX + "1 "
                + PREFIX_ADDRESS + VALID_ADDRESS + " "
                + PREFIX_PRICE + VALID_PRICE + " "
                + PREFIX_SIZE + VALID_SIZE;

        Property property = new Property(
                new PropertyAddress(VALID_ADDRESS),
                new Price(VALID_PRICE),
                new Size(VALID_SIZE));
        AddPropertyCommand expectedCommand = new AddPropertyCommand(Index.fromOneBased(1), property);

        assertParseSuccess(parser, userInput, expectedCommand);
    }

    @Test
    public void parse_missingIndex_failure() {
        String userInput = " "
                + PREFIX_ADDRESS + VALID_ADDRESS + " "
                + PREFIX_PRICE + VALID_PRICE + " "
                + PREFIX_SIZE + VALID_SIZE;

        assertParseFailure(parser, userInput,
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, AddPropertyCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_missingAddress_failure() {
        String userInput = " "
                + PREFIX_LISTING_INDEX + "1 "
                + PREFIX_PRICE + VALID_PRICE + " "
                + PREFIX_SIZE + VALID_SIZE;

        assertParseFailure(parser, userInput,
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, AddPropertyCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_missingPrice_failure() {
        String userInput = " "
                + PREFIX_LISTING_INDEX + "1 "
                + PREFIX_ADDRESS + VALID_ADDRESS + " "
                + PREFIX_SIZE + VALID_SIZE;

        assertParseFailure(parser, userInput,
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, AddPropertyCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_missingSize_failure() {
        String userInput = " "
                + PREFIX_LISTING_INDEX + "1 "
                + PREFIX_ADDRESS + VALID_ADDRESS + " "
                + PREFIX_PRICE + VALID_PRICE;

        assertParseFailure(parser, userInput,
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, AddPropertyCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_multipleIndices_failure() {
        assertParseFailure(parser,
                " i/1 i/2 a/311 Clementi Ave 2 pr/1200000 s/1200",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, AddPropertyCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_invalidAddress_failure() {
        String userInput = " "
                + PREFIX_LISTING_INDEX + "1 "
                + PREFIX_ADDRESS + INVALID_ADDRESS + " "
                + PREFIX_PRICE + VALID_PRICE + " "
                + PREFIX_SIZE + VALID_SIZE;

        assertParseFailure(parser, userInput, PropertyAddress.MESSAGE_CONSTRAINTS);
    }

    @Test
    public void parse_invalidPrice_failure() {
        String userInput = " "
                + PREFIX_LISTING_INDEX + "1 "
                + PREFIX_ADDRESS + VALID_ADDRESS + " "
                + PREFIX_PRICE + INVALID_PRICE + " "
                + PREFIX_SIZE + VALID_SIZE;

        assertParseFailure(parser, userInput, Price.MESSAGE_CONSTRAINTS);
    }

    @Test
    public void parse_invalidSize_failure() {
        String userInput = " "
                + PREFIX_LISTING_INDEX + "1 "
                + PREFIX_ADDRESS + VALID_ADDRESS + " "
                + PREFIX_PRICE + VALID_PRICE + " "
                + PREFIX_SIZE + INVALID_SIZE;

        assertParseFailure(parser, userInput, Size.MESSAGE_CONSTRAINTS);
    }

    @Test
    public void parse_withValidType_success() {
        String userInput = " "
                + PREFIX_LISTING_INDEX + "1 "
                + PREFIX_ADDRESS + VALID_ADDRESS + " "
                + PREFIX_PRICE + VALID_PRICE + " "
                + PREFIX_SIZE + VALID_SIZE + " "
                + PREFIX_TYPE + "HDB";

        Property property = new Property(
                new PropertyAddress(VALID_ADDRESS),
                new Price(VALID_PRICE),
                new Size(VALID_SIZE),
                new PropertyType("HDB"));
        AddPropertyCommand expectedCommand = new AddPropertyCommand(Index.fromOneBased(1), property);

        assertParseSuccess(parser, userInput, expectedCommand);
    }

    @Test
    public void parse_withInvalidType_failure() {
        String userInput = " "
                + PREFIX_LISTING_INDEX + "1 "
                + PREFIX_ADDRESS + VALID_ADDRESS + " "
                + PREFIX_PRICE + VALID_PRICE + " "
                + PREFIX_SIZE + VALID_SIZE + " "
                + PREFIX_TYPE + "@@@invalid";

        assertParseFailure(parser, userInput, PropertyType.MESSAGE_CONSTRAINTS);
    }

    @Test
    public void parse_nonEmptyPreamble_failure() {
        String userInput = " randomText "
                + PREFIX_LISTING_INDEX + "1 "
                + PREFIX_ADDRESS + VALID_ADDRESS + " "
                + PREFIX_PRICE + VALID_PRICE + " "
                + PREFIX_SIZE + VALID_SIZE;

        assertParseFailure(parser, userInput,
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, AddPropertyCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_repeatedAddress_failure() {
        assertParseFailure(parser,
                " i/1 a/311 Clementi Ave 2 a/222 Bedok North pr/1200000 s/1200",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, AddPropertyCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_repeatedPrice_failure() {
        assertParseFailure(parser,
                " i/1 a/311 Clementi Ave 2 pr/1200000 pr/1300000 s/1200",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, AddPropertyCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_repeatedSize_failure() {
        assertParseFailure(parser,
                " i/1 a/311 Clementi Ave 2 pr/1200000 s/1200 s/1300",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, AddPropertyCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_repeatedType_failure() {
        assertParseFailure(parser,
                " i/1 a/311 Clementi Ave 2 pr/1200000 s/1200 type/HDB type/Condo",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, AddPropertyCommand.MESSAGE_USAGE));
    }
}
