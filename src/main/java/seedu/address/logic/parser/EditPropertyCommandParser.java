package seedu.address.logic.parser;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.commands.EditPropertyCommand.MESSAGE_NOT_EDITED;
import static seedu.address.logic.commands.EditPropertyCommand.MESSAGE_USAGE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_ADDRESS;
import static seedu.address.logic.parser.CliSyntax.PREFIX_PRICE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_SIZE;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.EditPropertyCommand;
import seedu.address.logic.commands.EditPropertyCommand.EditPropertyDescriptor;
import seedu.address.logic.parser.exceptions.ParseException;

/**
 * Parses input arguments and creates a new EditPropertyCommand object.
 */
public class EditPropertyCommandParser implements Parser<EditPropertyCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the EditPropertyCommand
     * and returns an EditPropertyCommand object for execution.
     *
     * @throws ParseException if the user input does not conform the expected format
     */
    @Override
    public EditPropertyCommand parse(String args) throws ParseException {
        requireNonNull(args);

        ArgumentMultimap argMultimap =
                ArgumentTokenizer.tokenize(args, PREFIX_ADDRESS, PREFIX_PRICE, PREFIX_SIZE);

        Index index;

        try {
            index = ParserUtil.parseIndex(argMultimap.getPreamble());
        } catch (ParseException pe) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, MESSAGE_USAGE), pe);
        }

        EditPropertyDescriptor editPropertyDescriptor = new EditPropertyDescriptor();

        if (argMultimap.getValue(PREFIX_ADDRESS).isPresent()) {
            editPropertyDescriptor.setAddress(
                    ParserUtil.parsePropertyAddress(argMultimap.getValue(PREFIX_ADDRESS).get()));
        }
        if (argMultimap.getValue(PREFIX_PRICE).isPresent()) {
            editPropertyDescriptor.setPrice(
                    ParserUtil.parsePrice(argMultimap.getValue(PREFIX_PRICE).get()));
        }
        if (argMultimap.getValue(PREFIX_SIZE).isPresent()) {
            editPropertyDescriptor.setSize(
                    ParserUtil.parseSize(argMultimap.getValue(PREFIX_SIZE).get()));
        }

        if (!editPropertyDescriptor.isAnyFieldEdited()) {
            throw new ParseException(MESSAGE_NOT_EDITED);
        }

        return new EditPropertyCommand(index, editPropertyDescriptor);
    }
}
