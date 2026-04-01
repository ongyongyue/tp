package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.logic.Messages.MESSAGE_PERSONS_LISTED_OVERVIEW;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.address.testutil.TypicalPersons.BENSON;
import static seedu.address.testutil.TypicalPersons.CARL;
import static seedu.address.testutil.TypicalPersons.ELLE;
import static seedu.address.testutil.TypicalPersons.FIONA;
import static seedu.address.testutil.TypicalPersons.getTypicalAddressBook;

import java.nio.file.DirectoryStream.Filter;
import java.util.Arrays;
import java.util.Collections;

import org.junit.jupiter.api.Test;

import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.person.PersonMatchesFilterPredicate;

/**
 * Contains integration tests (interaction with the Model) for {@code FilterClientCommand}.
 */
public class FilterClientCommandTest {
    private Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());
    private Model expectedModel = new ModelManager(getTypicalAddressBook(), new UserPrefs());

    @Test
    public void equals() {
        PersonMatchesFilterPredicate firstPredicate =
                new PersonMatchesFilterPredicate(Collections.singletonList("first"), Collections.emptyList());
        PersonMatchesFilterPredicate secondPredicate =
                new PersonMatchesFilterPredicate(Collections.singletonList("second"), Collections.emptyList());
        PersonMatchesFilterPredicate thirdPredicate =
                new PersonMatchesFilterPredicate(Collections.singletonList("first"),
                        Collections.singletonList("first"));

        FilterClientCommand findFirstCommand = new FilterClientCommand(firstPredicate);
        FilterClientCommand findSecondCommand = new FilterClientCommand(secondPredicate);
        FilterClientCommand findThirdCommand = new FilterClientCommand(thirdPredicate);

        // same object -> returns true
        assertTrue(findFirstCommand.equals(findFirstCommand));

        // same values -> returns true
        FilterClientCommand findFirstCommandCopy = new FilterClientCommand(firstPredicate);
        assertTrue(findFirstCommand.equals(findFirstCommandCopy));

        // different types -> returns false
        assertFalse(findFirstCommand.equals(1));

        // null -> returns false
        assertFalse(findFirstCommand.equals(null));

        // different name keywords -> returns false
        assertFalse(findFirstCommand.equals(findSecondCommand));

        // different tag keywords -> returns false
        assertFalse(findFirstCommand.equals(findThirdCommand));
    }

    @Test
    public void execute_nonMatchingKeyword_noPersonFound() {
        String expectedMessage = String.format(MESSAGE_PERSONS_LISTED_OVERVIEW, 0);
        PersonMatchesFilterPredicate predicate = preparePredicate("zzz");
        FilterClientCommand command = new FilterClientCommand(predicate);
        expectedModel.updateFilteredPersonList(predicate);
        assertCommandSuccess(command, model, expectedMessage, expectedModel);
        assertEquals(Collections.emptyList(), model.getFilteredPersonList());
    }

    @Test
    public void execute_multipleKeywords_multiplePersonsFound() {
        String expectedMessage = String.format(MESSAGE_PERSONS_LISTED_OVERVIEW, 3);
        PersonMatchesFilterPredicate predicate = preparePredicate("Kurz Elle Kunz");
        FilterClientCommand command = new FilterClientCommand(predicate);
        expectedModel.updateFilteredPersonList(predicate);
        assertCommandSuccess(command, model, expectedMessage, expectedModel);
        assertEquals(Arrays.asList(CARL, ELLE, FIONA), model.getFilteredPersonList());
    }

    @Test
    public void execute_tagKeyword_singlePersonFound() {
        String expectedMessage = String.format(MESSAGE_PERSONS_LISTED_OVERVIEW, 1);
        PersonMatchesFilterPredicate predicate = new PersonMatchesFilterPredicate(
                Collections.emptyList(), Collections.singletonList("owesMoney"));
        FilterClientCommand command = new FilterClientCommand(predicate);
        expectedModel.updateFilteredPersonList(predicate);
        assertCommandSuccess(command, model, expectedMessage, expectedModel);
        assertEquals(Collections.singletonList(BENSON), model.getFilteredPersonList());
    }

    @Test
    public void toStringMethod() {
        PersonMatchesFilterPredicate predicate =
                new PersonMatchesFilterPredicate(Arrays.asList("keyword"), Collections.emptyList());
        FilterClientCommand filterClientCommand = new FilterClientCommand(predicate);
        String expected = FilterClientCommand.class.getCanonicalName() + "{predicate=" + predicate + "}";
        assertEquals(expected, filterClientCommand.toString());
    }

    /**
     * Parses {@code userInput} into a {@code PersonMatchesFilterPredicate}.
     */
    private PersonMatchesFilterPredicate preparePredicate(String userInput) {
        return new PersonMatchesFilterPredicate(Arrays.asList(userInput.split("\\s+")), Collections.emptyList());
    }
}
