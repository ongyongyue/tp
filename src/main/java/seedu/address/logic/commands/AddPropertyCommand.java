package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.parser.CliSyntax.PREFIX_ADDRESS;
import static seedu.address.logic.parser.CliSyntax.PREFIX_LISTING_INDEX;
import static seedu.address.logic.parser.CliSyntax.PREFIX_PRICE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_SIZE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TYPE;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import seedu.address.commons.core.index.Index;
import seedu.address.commons.util.ToStringBuilder;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.person.Person;
import seedu.address.model.property.Property;

/**
 * Adds a property to an existing person in the address book.
 */
public class AddPropertyCommand extends Command {

    public static final String COMMAND_WORD = "addProperty";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Adds a property to the person identified "
            + "by the index number used in the displayed person list. "
            + "Parameters: "
            + PREFIX_LISTING_INDEX + "CLIENT_INDEX "
            + "[" + PREFIX_LISTING_INDEX + "MORE_CLIENT_INDEX]... "
            + PREFIX_ADDRESS + "ADDRESS "
            + PREFIX_PRICE + "PRICE "
            + PREFIX_SIZE + "SIZE "
            + "[" + PREFIX_TYPE + "TYPE]\n"
            + "Example: " + COMMAND_WORD + " "
            + PREFIX_LISTING_INDEX + "1 "
            + PREFIX_ADDRESS + "311 Clementi Ave 2, #02-25 "
            + PREFIX_PRICE + "1200000 "
            + PREFIX_SIZE + "1200 "
            + PREFIX_TYPE + "HDB";

    public static final String MESSAGE_SUCCESS = "New property added to person: %1$s\n%2$s";
    public static final String MESSAGE_DUPLICATE_PROPERTY = "One or more selected persons already have this property.";
    public static final String MESSAGE_DUPLICATE_HDB_PROPERTY =
            "One or more selected persons already have an HDB property. "
            + "Each person can only have a maximum of 1 HDB property.";
    public static final String MESSAGE_NO_PERSONS = "No clients found. Please add a client first.";
    public static final String MESSAGE_INVALID_PERSON_INDEX = "One or more person indices provided are invalid.";

    private final List<Index> targetIndices;
    private final Property property;

    /**
     * Creates an AddPropertyCommand to add the specified {@code Property}
     * to the person at the specified {@code Index} list.
     *
     * @param targetIndices The indices of the person to add the property to.
     * @param property The property to add.
     */
    public AddPropertyCommand(List<Index> targetIndices, Property property) {
        requireNonNull(targetIndices);
        requireNonNull(property);
        this.targetIndices = new ArrayList<>(targetIndices);
        this.property = property;
    }

    /**
     * Executes the command and adds the property to the specified person.
     *
     * @param model The model to execute the command on.
     * @return The result of the command execution.
     * @throws CommandException If the index is invalid or the property already exists.
     */
    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);

        List<Person> lastShownList = model.getFilteredPersonList();

        if (lastShownList.isEmpty()) {
            throw new CommandException(MESSAGE_NO_PERSONS);
        }

        List<Person> personsToEdit = new ArrayList<>();

        for (Index targetIndex : targetIndices) {
            if (targetIndex.getZeroBased() >= lastShownList.size()) {
                throw new CommandException(MESSAGE_INVALID_PERSON_INDEX);
            }

            Person personToEdit = lastShownList.get(targetIndex.getZeroBased());

            if (personToEdit.hasProperty(property)) {
                throw new CommandException(MESSAGE_DUPLICATE_PROPERTY);
            }

            // Check if trying to add an HDB property when person already has one
            if (property.getPropertyType() != null
                    && property.getPropertyType().toString().equalsIgnoreCase("HDB")
                    && personToEdit.hasHdbProperty()) {
                throw new CommandException(MESSAGE_DUPLICATE_HDB_PROPERTY);
            }

            personsToEdit.add(personToEdit);
        }

        for (Person personToEdit : personsToEdit) {
            Person editedPerson = personToEdit.addProperty(property);
            model.setPerson(personToEdit, editedPerson);
        }

        String personsEdited = personsToEdit.stream()
                .map(person -> person.getName().toString())
                .collect(Collectors.joining(", "));

        return new CommandResult(String.format(MESSAGE_SUCCESS, personsEdited, property));
    }

    /**
     * Returns true if both commands have the same target index and property.
     *
     * @param other The other object to compare against.
     */
    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        if (!(other instanceof AddPropertyCommand)) {
            return false;
        }

        AddPropertyCommand otherAddPropertyCommand = (AddPropertyCommand) other;
        return targetIndices.equals(otherAddPropertyCommand.targetIndices)
                && property.equals(otherAddPropertyCommand.property);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("targetIndices", targetIndices)
                .add("property", property)
                .toString();
    }
}
