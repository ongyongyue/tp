package seedu.address.model.property;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.testutil.Assert.assertThrows;

import org.junit.jupiter.api.Test;

public class PriceTest {

    @Test
    public void constructor_null_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> new Price(null));
    }

    @Test
    public void constructor_invalidPrice_throwsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> new Price(""));
    }

    @Test
    public void isValidPrice() {
        assertThrows(NullPointerException.class, () -> Price.isValidPrice(null));

        assertFalse(Price.isValidPrice(""));
        assertFalse(Price.isValidPrice(" "));
        assertFalse(Price.isValidPrice("12345678901"));
        assertFalse(Price.isValidPrice("12.34"));
        assertFalse(Price.isValidPrice("12a34"));
        assertFalse(Price.isValidPrice("12 34"));
        assertFalse(Price.isValidPrice("-100"));

        assertTrue(Price.isValidPrice("0"));
        assertTrue(Price.isValidPrice("12345"));
        assertTrue(Price.isValidPrice("1234567890"));
    }

    @Test
    public void equals() {
        Price firstPrice = new Price("500000");
        Price secondPrice = new Price("600000");
        Price firstPriceCopy = new Price("500000");

        assertTrue(firstPrice.equals(firstPrice));

        assertTrue(firstPrice.equals(firstPriceCopy));

        assertFalse(firstPrice.equals("500000"));

        assertFalse(firstPrice.equals(null));

        assertFalse(firstPrice.equals(secondPrice));
    }
}
