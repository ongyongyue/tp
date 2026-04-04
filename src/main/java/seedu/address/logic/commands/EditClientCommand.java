package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX;
import static seedu.address.logic.parser.CliSyntax.PREFIX_EMAIL;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NAME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_PHONE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TAG;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import seedu.address.commons.core.index.Index;
import seedu.address.commons.util.CollectionUtil;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.person.Email;
import seedu.address.model.person.Name;
import seedu.address.model.person.Person;
import seedu.address.model.person.Phone;
import seedu.address.model.property.Property;
import seedu.address.model.tag.Tag;

/**
 * Edits the details of an existing client in the address book.
 */
public class EditClientCommand extends Command {
    public static final String COMMAND_WORD = "editClient";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Edits the details of the client identified "
            + "by the index number used in the displayed client list. "
            + "Existing values will be overwritten by the input values.\n"
            + "Parameters: INDEX (must be a positive integer) "
            + "[" + PREFIX_NAME + "NAME] "
            + "[" + PREFIX_PHONE + "PHONE] "
            + "[" + PREFIX_EMAIL + "EMAIL] "
            + "[" + PREFIX_TAG + "TAG]...\n"
            + "Example: " + COMMAND_WORD + " 1 "
            + PREFIX_PHONE + "91234567 "
            + PREFIX_EMAIL + "johndoe@example.com "
            + PREFIX_TAG + "vip";

    public static final String MESSAGE_EDIT_CLIENT_SUCCESS =
            "Edited Client: %1$s; Phone: %2$s; Email: %3$s; Tags: %4$s";
    public static final String MESSAGE_NOT_EDITED = "At least one field to edit must be provided.";
    public static final String MESSAGE_DUPLICATE_PERSON = "This client already exists in the address book.";

    private final Index index;
    private final EditClientDescriptor editClientDescriptor;

    /**
     * @param index of the person in the filtered person list to edit
     * @param editClientDescriptor details to edit the person with
     */
    public EditClientCommand(Index index, EditClientDescriptor editClientDescriptor) {
        requireNonNull(index);
        requireNonNull(editClientDescriptor);

        this.index = index;
        this.editClientDescriptor = new EditClientDescriptor(editClientDescriptor);
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);
        List<Person> lastShownList = model.getFilteredPersonList();

        if (index.getZeroBased() >= lastShownList.size()) {
            throw new CommandException(MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
        }

        Person personToEdit = lastShownList.get(index.getZeroBased());
        Person editedPerson = createEditedPerson(personToEdit, editClientDescriptor);

        if (!personToEdit.isSamePerson(editedPerson) && model.hasPerson(editedPerson)) {
            throw new CommandException(MESSAGE_DUPLICATE_PERSON);
        }

        model.setPerson(personToEdit, editedPerson);

        model.updateFilteredPersonList(p -> p.isSamePerson(editedPerson));
        model.updateFilteredPropertyList(p -> editedPerson.getProperties().contains(p));
        return new CommandResult(String.format(
                MESSAGE_EDIT_CLIENT_SUCCESS,
                editedPerson.getName(),
                editedPerson.getPhone(),
                editedPerson.getEmail(),
                formatTags(editedPerson.getTags())
        ));
    }

    /**
     * Creates and returns a {@code Person} with the details of {@code personToEdit}
     * edited with {@code editClientDescriptor}.
     */
    private static Person createEditedPerson(Person personToEdit, EditClientDescriptor editClientDescriptor) {
        assert personToEdit != null;

        Name updatedName = editClientDescriptor.getName().orElse(personToEdit.getName());
        Phone updatedPhone = editClientDescriptor.getPhone().orElse(personToEdit.getPhone());
        Email updatedEmail = editClientDescriptor.getEmail().orElse(personToEdit.getEmail());
        Set<Tag> updatedTags = editClientDescriptor.getTags().orElse(personToEdit.getTags());
        Set<Property> updatedProperties = personToEdit.getProperties();

        return new Person(updatedName, updatedPhone, updatedEmail, updatedTags, updatedProperties);
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        if (!(other instanceof EditClientCommand)) {
            return false;
        }

        EditClientCommand otherEditClientCommand = (EditClientCommand) other;
        return index.equals(otherEditClientCommand.index)
                && editClientDescriptor.equals(otherEditClientCommand.editClientDescriptor);
    }

    /**
     * Formats a set of tags into [tag][tag] style for display.
     */
    private static String formatTags(Set<Tag> tags) {
        return tags.stream()
                .map(tag -> tag.tagName)
                .sorted()
                .map(tag -> "[" + tag + "]")
                .collect(Collectors.joining());
    }

    /**
     * Stores the details to edit the person with. Each non-empty field value will replace the
     * corresponding field value of the person.
     */
    public static class EditClientDescriptor {
        private Name name;
        private Phone phone;
        private Email email;
        private Set<Tag> tags;

        public EditClientDescriptor() {}

        /**
         * Copy constructor.
         */
        public EditClientDescriptor(EditClientDescriptor toCopy) {
            setName(toCopy.name);
            setPhone(toCopy.phone);
            setEmail(toCopy.email);
            setTags(toCopy.tags);
        }

        /**
         * Returns true if at least one field is edited.
         */
        public boolean isAnyFieldEdited() {
            return CollectionUtil.isAnyNonNull(name, phone, email, tags);
        }

        public void setName(Name name) {
            this.name = name;
        }

        public Optional<Name> getName() {
            return Optional.ofNullable(name);
        }

        public void setPhone(Phone phone) {
            this.phone = phone;
        }

        public Optional<Phone> getPhone() {
            return Optional.ofNullable(phone);
        }

        public void setEmail(Email email) {
            this.email = email;
        }

        public Optional<Email> getEmail() {
            return Optional.ofNullable(email);
        }

        public void setTags(Set<Tag> tags) {
            this.tags = (tags != null) ? new HashSet<>(tags) : null;
        }

        public Optional<Set<Tag>> getTags() {
            return (tags != null) ? Optional.of(Collections.unmodifiableSet(tags)) : Optional.empty();
        }

        @Override
        public boolean equals(Object other) {
            if (other == this) {
                return true;
            }

            if (!(other instanceof EditClientDescriptor)) {
                return false;
            }

            EditClientDescriptor otherDescriptor = (EditClientDescriptor) other;
            return Objects.equals(name, otherDescriptor.name)
                    && Objects.equals(phone, otherDescriptor.phone)
                    && Objects.equals(email, otherDescriptor.email)
                    && Objects.equals(tags, otherDescriptor.tags);
        }
    }
}
