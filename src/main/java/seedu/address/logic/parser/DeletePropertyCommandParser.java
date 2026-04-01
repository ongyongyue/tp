package seedu.address.logic.parser;
import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.DeleteClientCommand;
import seedu.address.logic.commands.DeletePropertyCommand;
import seedu.address.logic.parser.exceptions.ParseException;
/**
 * Parses input arguments and creates a new DeletePropertyCommand object.
 */
public class DeletePropertyCommandParser implements Parser<DeletePropertyCommand> {
    /**
     * Parses the given {@code String} of arguments and returns a DeletePropertyCommand object.
     */
    @Override
    public DeletePropertyCommand parse(String args) throws ParseException {
        try {
            Index index = ParserUtil.parseIndex(args);
            return new DeletePropertyCommand(index);
        } catch (ParseException pe) {
            throw new ParseException(
                    String.format(MESSAGE_INVALID_COMMAND_FORMAT, DeletePropertyCommand.MESSAGE_USAGE), pe);
        }
    }
}
