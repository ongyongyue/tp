package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.parser.CliSyntax.PREFIX_ADDRESS;
import static seedu.address.logic.parser.CliSyntax.PREFIX_LISTING_INDEX;
import static seedu.address.logic.parser.CliSyntax.PREFIX_PRICE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_SIZE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TYPE;

import java.util.List;

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
    public static final String HDB_TYPE = "HDB";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Adds a property to the client identified "
            + "by the index number used in the displayed person list. \n"
            + "Parameters: "
            + PREFIX_LISTING_INDEX + "CLIENT_INDEX "
            + PREFIX_ADDRESS + "ADDRESS "
            + PREFIX_PRICE + "PRICE "
            + PREFIX_SIZE + "SIZE "
            + PREFIX_TYPE + "TYPE\n"
            + "Example: " + COMMAND_WORD + " "
            + PREFIX_LISTING_INDEX + "1 "
            + PREFIX_ADDRESS + "311 Clementi Ave 2, #02-25 "
            + PREFIX_PRICE + "1200000 "
            + PREFIX_SIZE + "1200 "
            + PREFIX_TYPE + "HDB";

    public static final String MESSAGE_SUCCESS = "New property added to client: %1$s\n%2$s";
    public static final String MESSAGE_DUPLICATE_PROPERTY = "This client already has this property.";
    public static final String MESSAGE_DUPLICATE_HDB_PROPERTY = "This client already has an HDB property. "
            + "Each client can only have a maximum of 1 HDB property.";
    public static final String MESSAGE_NO_PERSONS = "No clients found. Please add a client first.";
    public static final String MESSAGE_INVALID_PERSON_INDEX = "The client index provided is invalid.";
    public static final String MESSAGE_PROPERTY_ALREADY_OWNED = "This property is already owned by another client.";

    private final Index targetIndex;
    private final Property property;

    /**
     * Creates an AddPropertyCommand to add the specified {@code Property}
     * to the client at the specified {@code Index}.
     *
     * @param targetIndex The index of the person to add the property to.
     * @param property    The property to add.
     */
    public AddPropertyCommand(Index targetIndex, Property property) {
        requireNonNull(targetIndex);
        requireNonNull(property);
        this.targetIndex = targetIndex;
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
        validatePersonList(lastShownList);

        Person personToEdit = getTargetPerson(lastShownList);
        ensurePropertyCanBeAdded(model, personToEdit);

        Person editedPerson = personToEdit.addProperty(property);
        model.setPerson(personToEdit, editedPerson);

        return new CommandResult(String.format(MESSAGE_SUCCESS, personToEdit.getName(), property));
    }

    /**
     * Returns true if both commands have the same target index and property.
     *
     * @param other The other object to compare against.
     * @return True if both commands are equal, false otherwise.
     */
    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        if (!(other instanceof AddPropertyCommand otherAddPropertyCommand)) {
            return false;
        }

        return targetIndex.equals(otherAddPropertyCommand.targetIndex)
                && property.equals(otherAddPropertyCommand.property);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("targetIndex", targetIndex)
                .add("property", property)
                .toString();
    }

    /**
     * Validates that the displayed client list is not empty.
     *
     * @param lastShownList The list of clients currently displayed.
     * @throws CommandException If the list is empty.
     */
    private void validatePersonList(List<Person> lastShownList) throws CommandException {
        if (lastShownList.isEmpty()) {
            throw new CommandException(MESSAGE_NO_PERSONS);
        }
    }

    /**
     * Returns the target person identified by the target index.
     *
     * @param lastShownList The list of clients currently displayed.
     * @return The client at the specified index.
     * @throws CommandException If the index is invalid.
     */
    private Person getTargetPerson(List<Person> lastShownList) throws CommandException {
        if (targetIndex.getZeroBased() >= lastShownList.size()) {
            throw new CommandException(MESSAGE_INVALID_PERSON_INDEX);
        }

        return lastShownList.get(targetIndex.getZeroBased());
    }

    /**
     * Ensures that the property can be added to the specified client.
     *
     * @param model        The model containing the address book data.
     * @param personToEdit The client to add the property to.
     * @throws CommandException If the property violates any constraints.
     */
    private void ensurePropertyCanBeAdded(Model model, Person personToEdit) throws CommandException {
        if (personToEdit.getProperties().stream()
                .anyMatch(p -> p.isSameProperty(property))) {
            throw new CommandException(MESSAGE_DUPLICATE_PROPERTY);
        }

        ensureAddressUniqueAcrossAllClients(model, personToEdit);

        if (isHdbProperty(property) && personToEdit.hasHdbProperty()) {
            throw new CommandException(MESSAGE_DUPLICATE_HDB_PROPERTY);
        }
    }

    /**
     * Ensures that the property address is unique and not already owned by any other client.
     * Each property address can only be owned by one client.
     *
     * @param model        The model containing the address book data.
     * @param personToEdit The client attempting to own the property.
     * @throws CommandException If the property address is already owned by another client.
     */
    private void ensureAddressUniqueAcrossAllClients(Model model, Person personToEdit)
            throws CommandException {
        for (Person person : model.getAddressBook().getPersonList()) {
            if (!person.equals(personToEdit) && person.getProperties().stream()
                    .anyMatch(p -> p.getAddress().equals(property.getAddress()))) {
                throw new CommandException(MESSAGE_PROPERTY_ALREADY_OWNED);
            }
        }
    }

    /**
     * Returns true if the given property is an HDB property.
     *
     * @param property The property to check.
     * @return True if the property type is HDB, false otherwise.
     */
    private boolean isHdbProperty(Property property) {
        return property.getPropertyType() != null
                && property.getPropertyType().toString().equalsIgnoreCase(HDB_TYPE);
    }
}
