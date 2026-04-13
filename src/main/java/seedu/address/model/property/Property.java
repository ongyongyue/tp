package seedu.address.model.property;

import static java.util.Objects.requireNonNull;

import java.util.Objects;

/**
 * Represents a property listing in the address book.
 * Guarantees: address, price, size and type are present and not null.
 */
public class Property {

    private final PropertyAddress address;
    private final Price price;
    private final Size size;
    private final PropertyType propertyType;
    private String remarks;

    /**
     * Constructs a {@code Property} with a property type.
     *
     * @param address A valid property address.
     * @param price A valid property price.
     * @param size A valid property size.
     * @param propertyType The type of the property (e.g. HDB, Condo).
     */
    public Property(PropertyAddress address, Price price, Size size, PropertyType propertyType) {
        requireNonNull(address);
        requireNonNull(price);
        requireNonNull(size);
        requireNonNull(propertyType);
        this.address = address;
        this.price = price;
        this.size = size;
        this.propertyType = propertyType;
    }

    /**
     * Returns the address of this property.
     */
    public PropertyAddress getAddress() {
        return address;
    }

    /**
     * Returns the price of this property.
     */
    public Price getPrice() {
        return price;
    }

    /**
     * Returns the size of this property.
     */
    public Size getSize() {
        return size;
    }

    /**
     * Returns the type of this property, or null if not set.
     */
    public PropertyType getPropertyType() {
        return propertyType;
    }

    /**
     * Sets the remarks for this property.
     *
     * @param remarks The remarks string to set.
     */
    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    /**
     * Returns the remarks for this property, or null if not set.
     */
    public String getRemarks() {
        return this.remarks;
    }

    /**
     * Returns a copy of this property with the given remarks, preserving all other fields.
     *
     * @param remarks The new remarks to apply.
     */
    public Property withRemarks(String remarks) {
        Property updated = new Property(this.address, this.price, this.size, this.propertyType);
        updated.setRemarks(remarks);
        return updated;
    }

    /**
     * Returns true if both properties have the same address.
     * This defines a weaker notion of equality between two properties.
     *
     * @param otherProperty The other property to compare against.
     */
    public boolean isSameProperty(Property otherProperty) {
        if (otherProperty == this) {
            return true;
        }
        return otherProperty != null
                && address.toString().equalsIgnoreCase(otherProperty.address.toString());
    }

    @Override
    public String toString() {
        return "Address: " + address + ", Price: " + price + ", Size: " + size
                + ", Type: " + propertyType;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if (!(other instanceof Property)) {
            return false;
        }
        Property otherProperty = (Property) other;
        return address.equals(otherProperty.address)
                && price.equals(otherProperty.price)
                && size.equals(otherProperty.size)
                && Objects.equals(propertyType, otherProperty.propertyType);
    }

    @Override
    public int hashCode() {
        return Objects.hash(address, price, size, propertyType);
    }
}
