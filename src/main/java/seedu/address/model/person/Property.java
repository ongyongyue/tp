package seedu.address.model.person;

import static java.util.Objects.requireNonNull;

/**
 * Represents a Property in the address book.
 */
public class Property {

    private final Name owner;
    private final Address address;
    private final String type;
    private final String price;
    private final String size;

    /**
     * Constructs a {@code Property}.
     */
    public Property(Name owner, Address address, String type, String price, String size) {
        requireNonNull(owner);
        requireNonNull(address);
        requireNonNull(type);
        requireNonNull(price);
        requireNonNull(size);

        this.owner = owner;
        this.address = address;
        this.type = type;
        this.price = price;
        this.size = size;
    }

    /**
     * Returns the property owner.
     */
    public Name getOwner() {
        return owner;
    }

    /**
     * Returns the property address.
     */
    public Address getAddress() {
        return address;
    }

    /**
     * Returns the property type.
     */
    public String getType() {
        return type;
    }

    /**
     * Returns the property price.
     */
    public String getPrice() {
        return price;
    }

    /**
     * Returns the property size.
     */
    public String getSize() {
        return size;
    }

    @Override
    public String toString() {
        return String.format(
                "Owner: %s\n"
                        + "Address: %s\n"
                        + "Type: %s\n"
                        + "Price: S$%s\n"
                        + "Size: %s\n",
                getOwner(),
                getAddress(),
                getType(),
                getPrice(),
                getSize());
    }
}
