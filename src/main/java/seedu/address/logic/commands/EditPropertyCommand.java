package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.parser.CliSyntax.PREFIX_ADDRESS;
import static seedu.address.logic.parser.CliSyntax.PREFIX_PRICE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_SIZE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TYPE;

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
            + "[" + PREFIX_ADDRESS + "ADDRESS] "
            + "[" + PREFIX_PRICE + "PRICE] "
            + "[" + PREFIX_SIZE + "SIZE] "
            + "[" + PREFIX_TYPE + "TYPE]\n"
            + "Example: " + COMMAND_WORD + " 1 "
            + PREFIX_ADDRESS + "123 Clementi Road "
            + PREFIX_PRICE + "500000 "
            + PREFIX_SIZE + "1200 "
            + PREFIX_TYPE + "HDB";

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
     * Creates an EditPropertyCommand to edit a property.
     *
     * @param index The index of the property to edit.
     * @param editPropertyDescriptor The descriptor containing new values.
     */
    public EditPropertyCommand(Index index, EditPropertyDescriptor editPropertyDescriptor) {
        requireNonNull(index);
        requireNonNull(editPropertyDescriptor);

        this.index = index;
        this.editPropertyDescriptor = new EditPropertyDescriptor(editPropertyDescriptor);
    }

    /**
     * Executes the command and edits the specified property.
     *
     * @param model The model to execute the command on.
     * @return The result of the command execution.
     * @throws CommandException If the index is invalid or constraints are violated.
     */
    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);

        List<Property> lastShownPropertyList = model.getFilteredPropertyList();
        validatePropertyList(lastShownPropertyList);

        Property propertyToEdit = getTargetProperty(lastShownPropertyList);
        Property editedProperty = createEditedProperty(propertyToEdit, editPropertyDescriptor);

        ensureNoDuplicateProperty(model, propertyToEdit, editedProperty);

        Person owner = findOwner(model, propertyToEdit);

        Set<Property> updatedProperties =
                replacePropertyPreserveOrder(owner, propertyToEdit, editedProperty);

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
     * Returns true if both commands have the same index and descriptor.
     */
    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        if (!(other instanceof EditPropertyCommand)) {
            return false;
        }

        EditPropertyCommand otherCommand = (EditPropertyCommand) other;
        return index.equals(otherCommand.index)
                && editPropertyDescriptor.equals(otherCommand.editPropertyDescriptor);
    }

    /**
     * Validates that the property list is not empty.
     *
     * @param propertyList The list of properties displayed.
     * @throws CommandException If the list is empty.
     */
    private void validatePropertyList(List<Property> propertyList) throws CommandException {
        if (propertyList.isEmpty()) {
            throw new CommandException(MESSAGE_NO_PROPERTIES);
        }
    }

    /**
     * Returns the target property.
     *
     * @param propertyList The list of properties displayed.
     * @return The property to edit.
     * @throws CommandException If index is invalid.
     */
    private Property getTargetProperty(List<Property> propertyList) throws CommandException {
        if (index.getZeroBased() >= propertyList.size()) {
            throw new CommandException(Messages.MESSAGE_INVALID_PROPERTY_DISPLAYED_INDEX);
        }
        return propertyList.get(index.getZeroBased());
    }

    /**
     * Ensures no duplicate property exists.
     */
    private void ensureNoDuplicateProperty(Model model,
                                           Property original,
                                           Property edited) throws CommandException {
        for (Person person : model.getAddressBook().getPersonList()) {
            for (Property p : person.getProperties()) {
                if (!p.equals(original) && p.isSameProperty(edited)) {
                    throw new CommandException(MESSAGE_DUPLICATE_PROPERTY);
                }
            }
        }
    }

    /**
     * Finds the owner of the property.
     *
     * @throws CommandException If owner is not found.
     */
    private Person findOwner(Model model, Property property) throws CommandException {
        for (Person person : model.getFilteredPersonList()) {
            if (person.getProperties().contains(property)) {
                return person;
            }
        }
        throw new CommandException(MESSAGE_PROPERTY_OWNER_NOT_FOUND);
    }

    /**
     * Replaces a property while preserving order.
     * This method is written by @liuzhiyuan with the help of chatgpt.
     */
    private Set<Property> replacePropertyPreserveOrder(Person owner,
                                                       Property original,
                                                       Property edited) {
        Set<Property> updated = new LinkedHashSet<>();
        for (Property p : owner.getProperties()) {
            updated.add(p.equals(original) ? edited : p);
        }
        return updated;
    }

    /**
     * Creates an edited property.
     */
    private static Property createEditedProperty(Property propertyToEdit,
                                                 EditPropertyDescriptor descriptor) {
        PropertyAddress address = descriptor.getAddress().orElse(propertyToEdit.getAddress());
        Price price = descriptor.getPrice().orElse(propertyToEdit.getPrice());
        Size size = descriptor.getSize().orElse(propertyToEdit.getSize());
        PropertyType type = descriptor.getType().orElse(propertyToEdit.getPropertyType());

        return new Property(address, price, size, type);
    }

    /**
     * Descriptor for editing property.
     */
    public static class EditPropertyDescriptor {
        private PropertyAddress address;
        private Price price;
        private Size size;
        private PropertyType type;

        public EditPropertyDescriptor() {}

        /**
         * Copy constructor.
         */
        public EditPropertyDescriptor(EditPropertyDescriptor toCopy) {
            setAddress(toCopy.address);
            setPrice(toCopy.price);
            setSize(toCopy.size);
            setType(toCopy.type);
        }

        public boolean isAnyFieldEdited() {
            return CollectionUtil.isAnyNonNull(address, price, size, type);
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

        public void setType(PropertyType type) {
            this.type = type;
        }

        public Optional<PropertyType> getType() {
            return Optional.ofNullable(type);
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
                    && Objects.equals(size, otherDescriptor.size)
                    && Objects.equals(type, otherDescriptor.type);
        }
    }
}
