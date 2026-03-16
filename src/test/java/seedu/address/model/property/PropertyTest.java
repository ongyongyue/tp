package seedu.address.model.property;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.testutil.Assert.assertThrows;

import org.junit.jupiter.api.Test;


public class PropertyTest {

    private final PropertyAddress validAddress = new PropertyAddress("123 Main Street");
    private final Price validPrice = new Price("500000");
    private final Size validSize = new Size("1200");

    @Test
    public void constructor_nullAddress_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> new Property(null, validPrice, validSize));
    }

    @Test
    public void constructor_nullPrice_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> new Property(validAddress, null, validSize));
    }

    @Test
    public void constructor_nullSize_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> new Property(validAddress, validPrice, null));
    }

    @Test
    public void isSameProperty() {
        Property property = new Property(validAddress, validPrice, validSize);

        assertTrue(property.isSameProperty(property));

        assertFalse(property.isSameProperty(null));

        Property sameAddressDifferentDetails = new Property(validAddress, new Price("600000"), new Size("1500"));
        assertTrue(property.isSameProperty(sameAddressDifferentDetails));

        Property differentAddress = new Property(new PropertyAddress("456 Orchard Road"), validPrice, validSize);
        assertFalse(property.isSameProperty(differentAddress));
    }

    @Test
    public void equals() {
        Property property = new Property(validAddress, validPrice, validSize);
        Property propertyCopy = new Property(validAddress, validPrice, validSize);
        Property differentProperty = new Property(new PropertyAddress("456 Orchard Road"), validPrice, validSize);

        assertTrue(property.equals(property));

        assertTrue(property.equals(propertyCopy));

        assertFalse(property.equals(null));

        assertFalse(property.equals("123"));

        assertFalse(property.equals(differentProperty));
    }
    
}
