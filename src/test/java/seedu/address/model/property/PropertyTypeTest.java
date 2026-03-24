package seedu.address.model.property;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.testutil.Assert.assertThrows;

import org.junit.jupiter.api.Test;

/**
 * Contains unit tests for {@link PropertyType}.
 */
public class PropertyTypeTest {

    @Test
    public void constructor_validPropertyType_success() {
        PropertyType propertyType = new PropertyType("HDB");
        assertEquals("HDB", propertyType.value);
    }

    @Test
    public void constructor_null_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> new PropertyType(null));
    }

    @Test
    public void constructor_invalidPropertyType_throwsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> new PropertyType("@HDB"));
    }

    @Test
    public void constructor_emptyString_throwsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> new PropertyType(""));
    }

    @Test
    public void constructor_whitespaceOnly_throwsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> new PropertyType("   "));
    }

    @Test
    public void isValidPropertyType_validTypes_returnsTrue() {
        assertTrue(PropertyType.isValidPropertyType("HDB"));
        assertTrue(PropertyType.isValidPropertyType("Condo"));
        assertTrue(PropertyType.isValidPropertyType("hDb"));
        assertTrue(PropertyType.isValidPropertyType("coNdO"));
        assertTrue(PropertyType.isValidPropertyType("condo"));
        assertTrue(PropertyType.isValidPropertyType("hdb"));
    }

    @Test
    public void isValidPropertyType_invalidTypes_returnsFalse() {
        assertFalse(PropertyType.isValidPropertyType(""));
        assertFalse(PropertyType.isValidPropertyType(" "));
        assertFalse(PropertyType.isValidPropertyType("@HDB"));
        assertFalse(PropertyType.isValidPropertyType("HDB!"));
        assertFalse(PropertyType.isValidPropertyType("#Condo"));
        assertFalse(PropertyType.isValidPropertyType("Landed"));
        assertFalse(PropertyType.isValidPropertyType("Semi Detached"));
        assertFalse(PropertyType.isValidPropertyType("Executive Apartment"));
    }

    @Test
    public void toString_validPropertyType_returnsValue() {
        PropertyType propertyType = new PropertyType("HDB");
        assertEquals("HDB", propertyType.toString());
    }

    @Test
    public void equals_sameObject_returnsTrue() {
        PropertyType propertyType = new PropertyType("HDB");
        assertTrue(propertyType.equals(propertyType));
    }

    @Test
    public void equals_sameValue_returnsTrue() {
        PropertyType first = new PropertyType("HDB");
        PropertyType second = new PropertyType("HDB");
        assertTrue(first.equals(second));
    }

    @Test
    public void equals_differentValue_returnsFalse() {
        PropertyType first = new PropertyType("HDB");
        PropertyType second = new PropertyType("Condo");
        assertFalse(first.equals(second));
    }

    @Test
    public void equals_null_returnsFalse() {
        PropertyType propertyType = new PropertyType("HDB");
        assertFalse(propertyType.equals(null));
    }

    @Test
    public void equals_differentType_returnsFalse() {
        PropertyType propertyType = new PropertyType("HDB");
        assertFalse(propertyType.equals("HDB"));
    }

    @Test
    public void hashCode_sameValue_returnsSameHashCode() {
        PropertyType first = new PropertyType("HDB");
        PropertyType second = new PropertyType("HDB");
        assertEquals(first.hashCode(), second.hashCode());
    }

    @Test
    public void hashCode_differentValue_returnsDifferentHashCode() {
        PropertyType first = new PropertyType("HDB");
        PropertyType second = new PropertyType("Condo");
        assertNotEquals(first.hashCode(), second.hashCode());
    }
}
