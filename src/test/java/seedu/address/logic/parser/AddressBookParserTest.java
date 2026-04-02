package seedu.address.logic.parser;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.Messages.MESSAGE_UNKNOWN_COMMAND;
import static seedu.address.testutil.Assert.assertThrows;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_PERSON;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Test;

import seedu.address.logic.commands.AddClientCommand;
import seedu.address.logic.commands.ClearCommand;
import seedu.address.logic.commands.DeleteClientCommand;
import seedu.address.logic.commands.ExitCommand;
import seedu.address.logic.commands.FilterClientCommand;
import seedu.address.logic.commands.FilterPropertyCommand;
import seedu.address.logic.commands.FilterTypeCommand;
import seedu.address.logic.commands.HelpCommand;
import seedu.address.logic.commands.ListCommand;
import seedu.address.logic.commands.RemarkPropertyCommand;
import seedu.address.logic.commands.SortPropertyCommand;
import seedu.address.logic.commands.ViewClientCommand;
import seedu.address.logic.commands.ViewPropertyCommand;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.person.Person;
import seedu.address.model.person.PersonMatchesFilterPredicate;
import seedu.address.model.property.PropertyMatchesFilterPredicate;
import seedu.address.model.property.PropertyTypeContainsKeywordsPredicate;
import seedu.address.testutil.PersonBuilder;
import seedu.address.testutil.PersonUtil;

public class AddressBookParserTest {

    private final AddressBookParser parser = new AddressBookParser();

    @Test
    public void parseCommand_add() throws Exception {
        Person person = new PersonBuilder().build();
        AddClientCommand command = (AddClientCommand) parser.parseCommand(PersonUtil.getAddClientCommand(person));
        assertEquals(new AddClientCommand(person), command);
    }

    @Test
    public void parseCommand_clear() throws Exception {
        assertTrue(parser.parseCommand(ClearCommand.COMMAND_WORD) instanceof ClearCommand);
        assertTrue(parser.parseCommand(ClearCommand.COMMAND_WORD + " 3") instanceof ClearCommand);
    }

    @Test
    public void parseCommand_delete() throws Exception {
        DeleteClientCommand command = (DeleteClientCommand) parser.parseCommand(
                DeleteClientCommand.COMMAND_WORD + " " + INDEX_FIRST_PERSON.getOneBased());
        assertEquals(new DeleteClientCommand(INDEX_FIRST_PERSON), command);
    }

    @Test
    public void parseCommand_exit() throws Exception {
        assertTrue(parser.parseCommand(ExitCommand.COMMAND_WORD) instanceof ExitCommand);
        assertTrue(parser.parseCommand(ExitCommand.COMMAND_WORD + " 3") instanceof ExitCommand);
    }

    @Test
    public void parseCommand_filterClient() throws Exception {
        List<String> keywords = Arrays.asList("foo", "bar", "baz");
        FilterClientCommand nameCommand = (FilterClientCommand) parser.parseCommand(
                FilterClientCommand.COMMAND_WORD + " n/"
                + keywords.stream().collect(Collectors.joining(" ")));
        assertEquals(new FilterClientCommand(new PersonMatchesFilterPredicate(keywords, List.of())), nameCommand);

        List<String> tagKeywords = Arrays.asList("owesMoney", "friends");
        FilterClientCommand tagCommand = (FilterClientCommand) parser.parseCommand(
                FilterClientCommand.COMMAND_WORD + " t/"
                + tagKeywords.stream().collect(Collectors.joining(" ")));
        assertEquals(new FilterClientCommand(new PersonMatchesFilterPredicate(List.of(), tagKeywords)), tagCommand);

        FilterClientCommand nameAndTagCommand = (FilterClientCommand) parser.parseCommand(
                FilterClientCommand.COMMAND_WORD + " n/"
                + keywords.stream().collect(Collectors.joining(" "))
                + " t/" + tagKeywords.stream().collect(Collectors.joining(" ")));
        assertEquals(new FilterClientCommand(new PersonMatchesFilterPredicate(
                keywords, tagKeywords)), nameAndTagCommand);

    }

    @Test
    public void parseCommand_filterProperty() throws Exception {
        List<String> keywords = Arrays.asList("clementi", "punggol");
        FilterPropertyCommand command = (FilterPropertyCommand) parser.parseCommand(
                FilterPropertyCommand.COMMAND_WORD + " a/"
                + keywords.stream().collect(Collectors.joining(" ")));
        assertEquals(new FilterPropertyCommand(new PropertyMatchesFilterPredicate(
            keywords, 0, Long.MAX_VALUE, 0, Long.MAX_VALUE)), command);
    }

    @Test
    public void parseCommand_filterType() throws Exception {
        List<String> keywords = Arrays.asList("HDB");
        FilterTypeCommand command = (FilterTypeCommand) parser.parseCommand(
                FilterTypeCommand.COMMAND_WORD + " type/"
                + keywords.stream().collect(Collectors.joining(" ")));
        assertEquals(new FilterTypeCommand(new PropertyTypeContainsKeywordsPredicate(keywords)), command);
    }

    @Test
    public void parseCommand_remarkProperty() throws Exception {
        RemarkPropertyCommand command = (RemarkPropertyCommand) parser.parseCommand(
                RemarkPropertyCommand.COMMAND_WORD + " " + INDEX_FIRST_PERSON.getOneBased() + " r/Near MRT");
        assertEquals(new RemarkPropertyCommand(INDEX_FIRST_PERSON, "Near MRT"), command);
    }

    @Test
    public void parseCommand_sortProperty() throws Exception {
        SortPropertyCommand command = (SortPropertyCommand) parser.parseCommand(
                SortPropertyCommand.COMMAND_WORD + " st/price o/up");
        assertEquals(new SortPropertyCommand("price", "up"), command);
    }

    @Test
    public void parseCommand_viewClient() throws Exception {
        ViewClientCommand command = (ViewClientCommand) parser.parseCommand(
                ViewClientCommand.COMMAND_WORD + " " + INDEX_FIRST_PERSON.getOneBased());
        assertEquals(new ViewClientCommand(INDEX_FIRST_PERSON), command);
    }

    @Test
    public void parseCommand_viewProperty() throws Exception {
        ViewPropertyCommand command = (ViewPropertyCommand) parser.parseCommand(
                ViewPropertyCommand.COMMAND_WORD + " " + INDEX_FIRST_PERSON.getOneBased());
        assertEquals(new ViewPropertyCommand(INDEX_FIRST_PERSON), command);
    }

    @Test
    public void parseCommand_help() throws Exception {
        assertTrue(parser.parseCommand(HelpCommand.COMMAND_WORD) instanceof HelpCommand);
        assertTrue(parser.parseCommand(HelpCommand.COMMAND_WORD + " 3") instanceof HelpCommand);
    }

    @Test
    public void parseCommand_list() throws Exception {
        assertTrue(parser.parseCommand(ListCommand.COMMAND_WORD) instanceof ListCommand);
        assertTrue(parser.parseCommand(ListCommand.COMMAND_WORD + " 3") instanceof ListCommand);
    }

    @Test
    public void parseCommand_unrecognisedInput_throwsParseException() {
        assertThrows(ParseException.class, String.format(MESSAGE_INVALID_COMMAND_FORMAT, HelpCommand.MESSAGE_USAGE), ()
            -> parser.parseCommand(""));
    }

    @Test
    public void parseCommand_unknownCommand_throwsParseException() {
        assertThrows(ParseException.class, MESSAGE_UNKNOWN_COMMAND, () -> parser.parseCommand("unknownCommand"));
    }
}
