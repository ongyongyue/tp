package seedu.address.logic.parser;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.ViewClientCommand;
import seedu.address.logic.parser.exceptions.ParseException;

/**
 * Parses input arguments and creates a new ViewClientCommand object.
 */
public class ViewClientCommandParser implements Parser<ViewClientCommand> {

    /**
     * Parses the given {@code String} of arguments and returns a ViewClientCommand object.
     */
    @Override
    public ViewClientCommand parse(String args) throws ParseException {
        Index index = ParserUtil.parseIndex(args);
        return new ViewClientCommand(index);
    }
}
