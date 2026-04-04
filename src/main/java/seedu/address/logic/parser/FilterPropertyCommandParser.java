package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_ADDRESS;
import static seedu.address.logic.parser.CliSyntax.PREFIX_PRICE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_SIZE;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import seedu.address.logic.commands.FilterPropertyCommand;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.property.Price;
import seedu.address.model.property.PropertyMatchesFilterPredicate;
import seedu.address.model.property.Size;

/**
 * Parses input arguments and creates a new FilterPropertyCommand object.
 */
public class FilterPropertyCommandParser implements Parser<FilterPropertyCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the FilterPropertyCommand
     * and returns a FilterPropertyCommand object for execution.
     * @throws ParseException if the user input does not conform the expected format
     */
    @Override
    public FilterPropertyCommand parse(String args) throws ParseException {
        ArgumentMultimap argMultimap =
                ArgumentTokenizer.tokenize(args, PREFIX_ADDRESS, PREFIX_PRICE, PREFIX_SIZE);

        if (!argMultimap.getPreamble().isEmpty()) {
            throw new ParseException(String.format(
                    MESSAGE_INVALID_COMMAND_FORMAT, FilterPropertyCommand.MESSAGE_USAGE));
        }

        argMultimap.verifyNoDuplicatePrefixesFor(PREFIX_ADDRESS, PREFIX_PRICE, PREFIX_SIZE);

        List<String> addressKeywords = argMultimap.getValue(PREFIX_ADDRESS)
                .map(this::parseAddressKeywords)
                .orElse(List.of());

        Optional<long[]> priceRange = Optional.empty();
        if (argMultimap.getValue(PREFIX_PRICE).isPresent()) {
            priceRange = Optional.of(parsePriceRange(argMultimap.getValue(PREFIX_PRICE).get()));
        }

        Optional<long[]> sizeRange = Optional.empty();
        if (argMultimap.getValue(PREFIX_SIZE).isPresent()) {
            sizeRange = Optional.of(parseSizeRange(argMultimap.getValue(PREFIX_SIZE).get()));
        }

        if (addressKeywords.isEmpty() && priceRange.isEmpty() && sizeRange.isEmpty()) {
            throw new ParseException(String.format(
                    MESSAGE_INVALID_COMMAND_FORMAT, FilterPropertyCommand.MESSAGE_USAGE));
        }

        long minPrice = priceRange.map(range -> range[0]).orElse(Long.valueOf(0));
        long maxPrice = priceRange.map(range -> range[1]).orElse(Long.MAX_VALUE);
        long minSize = sizeRange.map(range -> range[0]).orElse(Long.valueOf(0));
        long maxSize = sizeRange.map(range -> range[1]).orElse(Long.MAX_VALUE);

        return new FilterPropertyCommand(new PropertyMatchesFilterPredicate(
                addressKeywords, minPrice, maxPrice, minSize, maxSize));
    }

    private List<String> parseAddressKeywords(String rawAddress) {
        return Arrays.stream(rawAddress.trim().split("\\s+"))
                .filter(keyword -> !keyword.isBlank())
                .collect(Collectors.toList());
    }

    private long[] parsePriceRange(String rawPriceRange) throws ParseException {
        return parseRange(rawPriceRange, true);
    }

    private long[] parseSizeRange(String rawSizeRange) throws ParseException {
        return parseRange(rawSizeRange, false);
    }

    private long[] parseRange(String rawRange, boolean isPriceRange) throws ParseException {
        String[] parts = rawRange.trim().split("\\s+");

        if (parts.length != 2 || parts[0].isBlank() || parts[1].isBlank()) {
            throw new ParseException(String.format(
                    MESSAGE_INVALID_COMMAND_FORMAT, FilterPropertyCommand.MESSAGE_USAGE));
        }

        long first = parseNumericBoundary(parts[0], isPriceRange);
        long second = parseNumericBoundary(parts[1], isPriceRange);
        if (first > second) {
            throw new ParseException(String.format(
                    MESSAGE_INVALID_COMMAND_FORMAT, FilterPropertyCommand.MESSAGE_USAGE));
        }

        return new long[] {first, second};
    }

    private long parseNumericBoundary(String rawValue, boolean isPriceRange) throws ParseException {
        if (isPriceRange) {
            Price price = ParserUtil.parsePrice(rawValue);
            return Long.parseLong(price.value);
        }

        Size size = ParserUtil.parseSize(rawValue);
        return Long.parseLong(size.value);
    }
}
