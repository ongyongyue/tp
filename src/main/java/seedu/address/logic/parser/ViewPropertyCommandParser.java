package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.ViewPropertyCommand;
import seedu.address.logic.parser.exceptions.ParseException;

/**
 * Parses input arguments and creates a new ViewPropertyCommand object.
 */
public class ViewPropertyCommandParser implements Parser<ViewPropertyCommand> {

    /**
     * Parses the given {@code String} of arguments and returns a ViewPropertyCommand object.
     */
    @Override
    public ViewPropertyCommand parse(String args) throws ParseException {
        try {
            Index index = ParserUtil.parseIndex(args);
            return new ViewPropertyCommand(index);
        } catch (ParseException pe) {
            throw new ParseException(String.format(
                    MESSAGE_INVALID_COMMAND_FORMAT, ViewPropertyCommand.MESSAGE_USAGE), pe);
        }
    }
}
