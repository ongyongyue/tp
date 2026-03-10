package seedu.address.logic.parser;
import seedu.address.commons.core.index.Index;
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
        Index index = ParserUtil.parseIndex(args);
        return new DeletePropertyCommand(index);
    }
}
