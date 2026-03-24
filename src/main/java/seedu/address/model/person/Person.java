package seedu.address.model.person;

import static seedu.address.commons.util.CollectionUtil.requireAllNonNull;

import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;

import seedu.address.commons.util.ToStringBuilder;
import seedu.address.model.property.Property;
import seedu.address.model.tag.Tag;

/**
 * Represents a Person in the address book.
 * Guarantees: details are present and not null, field values are validated, immutable.
 */
public class Person {

    // Identity fields
    private final Name name;
    private final Phone phone;
    private final Email email;

    // Data fields
    private final Set<Tag> tags = new LinkedHashSet<>();
    private final Set<Property> properties = new LinkedHashSet<>();

    /**
     * Every field must be present and not null.
     */
    public Person(Name name, Phone phone, Email email, Set<Tag> tags) {
        this(name, phone, email, tags, Collections.emptySet());
    }

    /**
     * Create the client
     */
    public Person(Name name, Phone phone, Email email, Set<Tag> tags, Set<Property> properties) {
        requireAllNonNull(name, phone, email, tags);
        this.name = name;
        this.phone = phone;
        this.email = email;
        this.tags.addAll(tags);
        this.properties.addAll(properties);
    }

    public Name getName() {
        return name;
    }

    public Phone getPhone() {
        return phone;
    }

    public Email getEmail() {
        return email;
    }

    /**
     * Returns an immutable tag set, which throws {@code UnsupportedOperationException}
     * if modification is attempted.
     */
    public Set<Tag> getTags() {
        return Collections.unmodifiableSet(tags);
    }

    /**
     * Returns an immutable property set, which throws {@code UnsupportedOperationException}
     * if modification is attempted.
     */
    public Set<Property> getProperties() {
        return Collections.unmodifiableSet(properties);
    }

    /**
     * Returns the String representation of a Set of Properties for display
     */
    public String propertiesToString() {
        if (this.properties.isEmpty()) {
            return "None";
        }

        StringBuilder sb = new StringBuilder();
        int index = 1;

        for (Property property : this.properties) {
            sb.append("Property ").append(index).append(":\n")
                    .append(property.toString())
                    .append("\n");
            index++;
        }

        return sb.toString();
    }
    /**
     * Returns true if the person already has the given property.
     */
    public boolean hasProperty(Property property) {
        return properties.stream().anyMatch(existingProperty -> existingProperty.isSameProperty(property));
    }

    /**
     * Returns true if the person already has an HDB property.
     */
    public boolean hasHdbProperty() {
        return properties.stream()
                .anyMatch(property -> property.getPropertyType() != null
                        && property.getPropertyType().toString().equalsIgnoreCase("HDB"));
    }

    /**
     * Returns a new person with the given property added.
     */
    public Person addProperty(Property property) {
        Set<Property> updatedProperties = new LinkedHashSet<>(properties);
        updatedProperties.add(property);
        return new Person(name, phone, email, tags, updatedProperties);
    }

    /**
     * Returns a new person with the given property removed.
     */
    public Person removeProperty(Property property) {
        Set<Property> updatedProperties = new LinkedHashSet<>(properties);
        updatedProperties.remove(property);
        return new Person(name, phone, email, tags, updatedProperties);
    }

    /**
     * Returns true if both persons have the same name.
     * This defines a weaker notion of equality between two persons.
     */
    public boolean isSamePerson(Person otherPerson) {
        if (otherPerson == this) {
            return true;
        }

        return otherPerson != null
                && otherPerson.getName().equals(getName());
    }

    /**
     * Returns true if both persons have the same identity and data fields.
     * This defines a stronger notion of equality between two persons.
     */
    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof Person)) {
            return false;
        }

        Person otherPerson = (Person) other;
        return name.equals(otherPerson.name)
                && phone.equals(otherPerson.phone)
                && email.equals(otherPerson.email)
                && tags.equals(otherPerson.tags)
                && properties.equals(otherPerson.properties);
    }

    @Override
    public int hashCode() {
        // use this method for custom fields hashing instead of implementing your own
        return Objects.hash(name, phone, email, tags, properties);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("name", name)
                .add("phone", phone)
                .add("email", email)
                .add("tags", tags)
                .add("properties", properties)
                .toString();
    }

}
