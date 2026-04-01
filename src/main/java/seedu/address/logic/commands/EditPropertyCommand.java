package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

import seedu.address.commons.core.index.Index;
import seedu.address.commons.util.CollectionUtil;
import seedu.address.logic.Messages;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.person.Person;
import seedu.address.model.property.Price;
import seedu.address.model.property.Property;
import seedu.address.model.property.PropertyAddress;
import seedu.address.model.property.PropertyType;
import seedu.address.model.property.Size;

/**
 * Edits a property belonging to an existing client in the address book.
 */
public class EditPropertyCommand extends Command {
    public static final String COMMAND_WORD = "editProperty";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Edits the property identified "
            + "by the index number used in the displayed property list.\n"
            + "Parameters: INDEX (must be a positive integer) "
            + "[a/ADDRESS] [pr/PRICE] [s/SIZE]\n"
            + "Example: " + COMMAND_WORD + " 1 a/123 Clementi Road pr/500000 s/1200";

    public static final String MESSAGE_EDIT_PROPERTY_SUCCESS = "Edited Property: %1$s";
    public static final String MESSAGE_NOT_EDITED = "At least one field to edit must be provided.";
    public static final String MESSAGE_DUPLICATE_PROPERTY =
            "Another property with the same address already exists.";
    public static final String MESSAGE_PROPERTY_OWNER_NOT_FOUND =
            "Property owner not found.";
    public static final String MESSAGE_NO_PROPERTIES =
            "No properties found. Please add a property first.";

    private final Index index;
    private final EditPropertyDescriptor editPropertyDescriptor;

    /**
     * @param index                  of the property to edit
     * @param editPropertyDescriptor details to edit the property with
     */
    public EditPropertyCommand(Index index, EditPropertyDescriptor editPropertyDescriptor) {
        requireNonNull(index);
        requireNonNull(editPropertyDescriptor);

        this.index = index;
        this.editPropertyDescriptor = new EditPropertyDescriptor(editPropertyDescriptor);
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);

        List<Property> lastShownPropertyList = model.getFilteredPropertyList();

        if (lastShownPropertyList.isEmpty()) {
            throw new CommandException(MESSAGE_NO_PROPERTIES);
        }

        if (index.getZeroBased() >= lastShownPropertyList.size()) {
            throw new CommandException(Messages.MESSAGE_INVALID_PROPERTY_DISPLAYED_INDEX);
        }

        Property propertyToEdit = lastShownPropertyList.get(index.getZeroBased());
        Property editedProperty = createEditedProperty(propertyToEdit, editPropertyDescriptor);

        for (Person person : model.getAddressBook().getPersonList()) {
            for (Property p : person.getProperties()) {
                if (!p.equals(propertyToEdit) && p.isSameProperty(editedProperty)) {
                    throw new CommandException(MESSAGE_DUPLICATE_PROPERTY);
                }
            }
        }

        Person owner = null;
        for (Person person : model.getFilteredPersonList()) {
            if (person.getProperties().contains(propertyToEdit)) {
                owner = person;
                break;
            }
        }

        if (owner == null) {
            throw new CommandException(MESSAGE_PROPERTY_OWNER_NOT_FOUND);
        }

        //@Liu Zhiyuan use chatgpt to help with writing next 8 lines
        //to ensure the order of property will not be changed.
        Set<Property> updatedProperties = new LinkedHashSet<>();
        for (Property p : owner.getProperties()) {
            if (p.equals(propertyToEdit)) {
                updatedProperties.add(editedProperty);
            } else {
                updatedProperties.add(p);
            }
        }

        Person editedPerson = new Person(
                owner.getName(),
                owner.getPhone(),
                owner.getEmail(),
                owner.getTags(),
                updatedProperties
        );

        model.setPerson(owner, editedPerson);

        model.updateFilteredPropertyList(p -> p.equals(editedProperty));
        model.updateFilteredPersonList(p -> p.isSamePerson(editedPerson));

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
        PropertyType updatedType = propertyToEdit.getPropertyType();
        return new Property(updatedAddress, updatedPrice, updatedSize, updatedType);
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
        return index.equals(otherEditPropertyCommand.index)
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

        public EditPropertyDescriptor() {
        }

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
