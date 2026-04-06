package seedu.address.logic.parser;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_ADDRESS;
import static seedu.address.logic.parser.CliSyntax.PREFIX_LISTING_INDEX;
import static seedu.address.logic.parser.CliSyntax.PREFIX_PRICE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_SIZE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TYPE;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.AddPropertyCommand;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.property.Price;
import seedu.address.model.property.Property;
import seedu.address.model.property.PropertyAddress;
import seedu.address.model.property.PropertyType;
import seedu.address.model.property.Size;

/**
 * Parses input arguments and creates a new AddPropertyCommand object.
 */
public class AddPropertyCommandParser implements Parser<AddPropertyCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the AddPropertyCommand
     * and returns an AddPropertyCommand object for execution.
     *
     * @param args The arguments string to parse.
     * @throws ParseException if the user input does not conform the expected format.
     */
    public AddPropertyCommand parse(String args) throws ParseException {
        requireNonNull(args);

        ArgumentMultimap argMultimap = ArgumentTokenizer.tokenize(
                args, PREFIX_LISTING_INDEX, PREFIX_ADDRESS, PREFIX_PRICE, PREFIX_SIZE, PREFIX_TYPE);

        if (!arePrefixesPresent(argMultimap, PREFIX_LISTING_INDEX, PREFIX_ADDRESS, PREFIX_PRICE, PREFIX_SIZE,
                PREFIX_TYPE)
                || !argMultimap.getPreamble().isEmpty()) {
            throw new ParseException(
                    String.format(MESSAGE_INVALID_COMMAND_FORMAT, AddPropertyCommand.MESSAGE_USAGE));
        }

        if (argMultimap.getAllValues(PREFIX_LISTING_INDEX).size() != 1
                || argMultimap.getAllValues(PREFIX_ADDRESS).size() != 1
                || argMultimap.getAllValues(PREFIX_PRICE).size() != 1
                || argMultimap.getAllValues(PREFIX_SIZE).size() != 1
                || argMultimap.getAllValues(PREFIX_TYPE).size() != 1) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, AddPropertyCommand.MESSAGE_USAGE));
        }

        if (argMultimap.getAllValues(PREFIX_TYPE).size() != 1) {
            throw new ParseException(String.format(
                    MESSAGE_INVALID_COMMAND_FORMAT, AddPropertyCommand.MESSAGE_USAGE));
        }

        try {
            Index targetIndex = ParserUtil.parseIndex(argMultimap.getValue(PREFIX_LISTING_INDEX).get());
            PropertyAddress address = new PropertyAddress(argMultimap.getValue(PREFIX_ADDRESS).get());
            Price price = new Price(argMultimap.getValue(PREFIX_PRICE).get());
            Size size = new Size(argMultimap.getValue(PREFIX_SIZE).get());

            PropertyType propertyType = new PropertyType(argMultimap.getValue(PREFIX_TYPE).get());;

            Property property = new Property(address, price, size, propertyType);
            return new AddPropertyCommand(targetIndex, property);
        } catch (IllegalArgumentException e) {
            throw new ParseException(e.getMessage());
        }
    }

    /**
     * Returns true if all the specified prefixes are present in the argument multimap.
     *
     * @param argumentMultimap The multimap to check.
     * @param prefixes The prefixes to check for.
     */
    private static boolean arePrefixesPresent(ArgumentMultimap argumentMultimap, Prefix... prefixes) {
        for (Prefix prefix : prefixes) {
            if (argumentMultimap.getValue(prefix).isEmpty()) {
                return false;
            }
        }
        return true;
    }
}
