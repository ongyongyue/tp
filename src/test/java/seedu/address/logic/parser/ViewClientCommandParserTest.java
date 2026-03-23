package seedu.address.logic.parser;

import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

import seedu.address.logic.parser.exceptions.ParseException;

class ViewClientCommandParserTest {

    private final ViewClientCommandParser parser = new ViewClientCommandParser();

    @Test
    void parse_invalidArgs_throwsParseException() {
        // Empty input
        assertThrows(ParseException.class, () -> parser.parse(""));

        // Non-numeric input
        assertThrows(ParseException.class, () -> parser.parse("abc"));

        // Negative number
        assertThrows(ParseException.class, () -> parser.parse("-1"));

        // Zero is invalid (Index is 1-based)
        assertThrows(ParseException.class, () -> parser.parse("0"));
    }
}
