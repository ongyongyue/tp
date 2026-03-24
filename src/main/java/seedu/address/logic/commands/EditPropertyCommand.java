package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

import seedu.address.commons.core.index.Index;
import seedu.address.commons.util.CollectionUtil;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.person.Person;
import seedu.address.model.property.Price;
import seedu.address.model.property.Property;
import seedu.address.model.property.PropertyAddress;
import seedu.address.model.property.Size;

/**
 * Edits a property belonging to an existing client in the address book.
 */
public class EditPropertyCommand extends Command {
    public static final String COMMAND_WORD = "editProperty";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Edits the property identified by the property index "
            + "for the client identified by the client index.\n"
            + "Parameters: CLIENT_INDEX (must be a positive integer) "
            + "i/PROPERTY_INDEX (must be a positive integer) "
            + "[a/ADDRESS] [pr/PRICE] [s/SIZE]\n"
            + "Example: " + COMMAND_WORD + " 1 i/1 a/123 Clementi Road pr/500000 s/1200";

    public static final String MESSAGE_EDIT_PROPERTY_SUCCESS = "Edited Property: %1$s";
    public static final String MESSAGE_NOT_EDITED = "At least one field to edit must be provided.";
    public static final String MESSAGE_INVALID_PROPERTY_INDEX =
            "The property index provided for this client is invalid.";
    public static final String MESSAGE_DUPLICATE_PROPERTY =
            "This client already has another property with the same address.";

    private final Index clientIndex;
    private final Index propertyIndex;
    private final EditPropertyDescriptor editPropertyDescriptor;

    /**
     * @param clientIndex of the client in the filtered person list to edit
     * @param propertyIndex of the property belonging to the client to edit
     * @param editPropertyDescriptor details to edit the property with
     */
    public EditPropertyCommand(Index clientIndex, Index propertyIndex,
                               EditPropertyDescriptor editPropertyDescriptor) {
        requireNonNull(clientIndex);
        requireNonNull(propertyIndex);
        requireNonNull(editPropertyDescriptor);

        this.clientIndex = clientIndex;
        this.propertyIndex = propertyIndex;
        this.editPropertyDescriptor = new EditPropertyDescriptor(editPropertyDescriptor);
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);
        List<Person> lastShownList = model.getFilteredPersonList();

        if (clientIndex.getZeroBased() >= lastShownList.size()) {
            throw new CommandException(MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
        }

        Person personToEdit = lastShownList.get(clientIndex.getZeroBased());
        List<Property> propertyList = new ArrayList<>(personToEdit.getProperties());

        if (propertyIndex.getZeroBased() >= propertyList.size()) {
            throw new CommandException(MESSAGE_INVALID_PROPERTY_INDEX);
        }

        Property propertyToEdit = propertyList.get(propertyIndex.getZeroBased());
        Property editedProperty = createEditedProperty(propertyToEdit, editPropertyDescriptor);

        for (int i = 0; i < propertyList.size(); i++) {
            if (i != propertyIndex.getZeroBased()
                    && propertyList.get(i).isSameProperty(editedProperty)) {
                throw new CommandException(MESSAGE_DUPLICATE_PROPERTY);
            }
        }

        propertyList.set(propertyIndex.getZeroBased(), editedProperty);
        Set<Property> updatedProperties = new LinkedHashSet<>(propertyList);

        Person editedPerson = new Person(
                personToEdit.getName(),
                personToEdit.getPhone(),
                personToEdit.getEmail(),
                personToEdit.getTags(),
                updatedProperties
        );

        model.setPerson(personToEdit, editedPerson);

        model.updateFilteredPersonList(p -> p.isSamePerson(editedPerson));
        model.updateFilteredPropertyList(p -> p.equals(editedProperty));
        return new CommandResult(String.format(MESSAGE_EDIT_PROPERTY_SUCCESS, editedProperty));
    }

    /**
     * Creates and returns a {@code Property} with the details of {@code propertyToEdit}
     * edited with {@code editPropertyDescriptor}.
     */
    private static Property createEditedProperty(Property propertyToEdit,
                                                 EditPropertyDescriptor editPropertyDescriptor) {
        assert propertyToEdit != null;

        PropertyAddress updatedAddress = editPropertyDescriptor.getAddress().orElse(propertyToEdit.getAddress());
        Price updatedPrice = editPropertyDescriptor.getPrice().orElse(propertyToEdit.getPrice());
        Size updatedSize = editPropertyDescriptor.getSize().orElse(propertyToEdit.getSize());

        return new Property(updatedAddress, updatedPrice, updatedSize);
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        if (!(other instanceof EditPropertyCommand)) {
            return false;
        }

        EditPropertyCommand otherEditPropertyCommand = (EditPropertyCommand) other;
        return clientIndex.equals(otherEditPropertyCommand.clientIndex)
                && propertyIndex.equals(otherEditPropertyCommand.propertyIndex)
                && editPropertyDescriptor.equals(otherEditPropertyCommand.editPropertyDescriptor);
    }

    /**
     * Stores the details to edit the property with.
     * Each non-empty field value will replace the corresponding field value of the property.
     */
    public static class EditPropertyDescriptor {
        private PropertyAddress address;
        private Price price;
        private Size size;

        public EditPropertyDescriptor() {}

        /**
         * Copy constructor.
         */
        public EditPropertyDescriptor(EditPropertyDescriptor toCopy) {
            setAddress(toCopy.address);
            setPrice(toCopy.price);
            setSize(toCopy.size);
        }

        /**
         * Returns true if at least one field is edited.
         */
        public boolean isAnyFieldEdited() {
            return CollectionUtil.isAnyNonNull(address, price, size);
        }

        public void setAddress(PropertyAddress address) {
            this.address = address;
        }

        public Optional<PropertyAddress> getAddress() {
            return Optional.ofNullable(address);
        }

        public void setPrice(Price price) {
            this.price = price;
        }

        public Optional<Price> getPrice() {
            return Optional.ofNullable(price);
        }

        public void setSize(Size size) {
            this.size = size;
        }

        public Optional<Size> getSize() {
            return Optional.ofNullable(size);
        }

        @Override
        public boolean equals(Object other) {
            if (other == this) {
                return true;
            }

            if (!(other instanceof EditPropertyDescriptor)) {
                return false;
            }

            EditPropertyDescriptor otherDescriptor = (EditPropertyDescriptor) other;
            return Objects.equals(address, otherDescriptor.address)
                    && Objects.equals(price, otherDescriptor.price)
                    && Objects.equals(size, otherDescriptor.size);
        }
    }
}
