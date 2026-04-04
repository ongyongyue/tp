package seedu.address.model.property;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.testutil.Assert.assertThrows;

import org.junit.jupiter.api.Test;

public class PropertyAddressTest {
    @Test
    public void constructor_null_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> new PropertyAddress(null));
    }

    @Test
    public void constructor_invalidPropertyAddress_throwsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> new PropertyAddress(""));
    }

    @Test
    public void isValidPropertyAddress() {
        assertThrows(NullPointerException.class, () -> PropertyAddress.isValidPropertyAddress(null));

        assertFalse(PropertyAddress.isValidPropertyAddress(""));
        assertFalse(PropertyAddress.isValidPropertyAddress(" "));
        assertFalse(PropertyAddress.isValidPropertyAddress("@123"));
        assertFalse(PropertyAddress.isValidPropertyAddress("123 Street!"));
        assertFalse(PropertyAddress.isValidPropertyAddress("a".repeat(101)));

        assertTrue(PropertyAddress.isValidPropertyAddress("123 Main Street"));
        assertTrue(PropertyAddress.isValidPropertyAddress("Blk 123 #12-34"));
        assertTrue(PropertyAddress.isValidPropertyAddress("123 Main St., Singapore"));
        assertTrue(PropertyAddress.isValidPropertyAddress("45 Orchard Road"));
    }

    @Test
    public void equals() {
        PropertyAddress firstAddress = new PropertyAddress("123 Main Street");
        PropertyAddress secondAddress = new PropertyAddress("456 Orchard Road");
        PropertyAddress firstAddressCopy = new PropertyAddress("123 Main Street");

        assertTrue(firstAddress.equals(firstAddress));
        assertTrue(firstAddress.equals(firstAddressCopy));
        assertFalse(firstAddress.equals("123 Main Street"));
        assertFalse(firstAddress.equals(null));
        assertFalse(firstAddress.equals(secondAddress));
    }
}
