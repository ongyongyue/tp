package seedu.address.storage;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static seedu.address.testutil.Assert.assertThrows;

import org.junit.jupiter.api.Test;

import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.model.property.Price;
import seedu.address.model.property.Property;
import seedu.address.model.property.PropertyAddress;
import seedu.address.model.property.Size;

public class JsonAdaptedPropertyTest {
    private static final String VALID_ADDRESS = "123 Main Street";
    private static final String VALID_PRICE = "500000";
    private static final String VALID_SIZE = "1200";

    @Test
    public void toModelType_validPropertyDetails_returnsProperty() throws Exception {
        JsonAdaptedProperty property = new JsonAdaptedProperty(VALID_ADDRESS, VALID_PRICE, VALID_SIZE);
        Property expectedProperty = new Property(
                new PropertyAddress(VALID_ADDRESS),
                new Price(VALID_PRICE),
                new Size(VALID_SIZE));

        assertEquals(expectedProperty, property.toModelType());
    }

    @Test
    public void toModelType_nullAddress_throwsIllegalValueException() {
        JsonAdaptedProperty property = new JsonAdaptedProperty(null, VALID_PRICE, VALID_SIZE);

        assertThrows(IllegalValueException.class,
                String.format(JsonAdaptedProperty.MISSING_FIELD_MESSAGE_FORMAT,
                        PropertyAddress.class.getSimpleName()),
                property::toModelType);
    }

    @Test
    public void toModelType_invalidAddress_throwsIllegalValueException() {
        String invalidAddress = "@123";
        JsonAdaptedProperty property = new JsonAdaptedProperty(invalidAddress, VALID_PRICE, VALID_SIZE);

        assertThrows(IllegalValueException.class,
                PropertyAddress.MESSAGE_CONSTRAINTS,
                property::toModelType);
    }

    @Test
    public void toModelType_nullPrice_throwsIllegalValueException() {
        JsonAdaptedProperty property = new JsonAdaptedProperty(VALID_ADDRESS, null, VALID_SIZE);

        assertThrows(IllegalValueException.class,
                String.format(JsonAdaptedProperty.MISSING_FIELD_MESSAGE_FORMAT,
                        Price.class.getSimpleName()),
                property::toModelType);
    }

    @Test
    public void toModelType_invalidPrice_throwsIllegalValueException() {
        String invalidPrice = "abc";
        JsonAdaptedProperty property = new JsonAdaptedProperty(VALID_ADDRESS, invalidPrice, VALID_SIZE);

        assertThrows(IllegalValueException.class,
                Price.MESSAGE_CONSTRAINTS,
                property::toModelType);
    }

    @Test
    public void toModelType_nullSize_throwsIllegalValueException() {
        JsonAdaptedProperty property = new JsonAdaptedProperty(VALID_ADDRESS, VALID_PRICE, null);

        assertThrows(IllegalValueException.class,
                String.format(JsonAdaptedProperty.MISSING_FIELD_MESSAGE_FORMAT,
                        Size.class.getSimpleName()),
                property::toModelType);
    }

    @Test
    public void toModelType_invalidSize_throwsIllegalValueException() {
        String invalidSize = "abc";
        JsonAdaptedProperty property = new JsonAdaptedProperty(VALID_ADDRESS, VALID_PRICE, invalidSize);

        assertThrows(IllegalValueException.class,
                Size.MESSAGE_CONSTRAINTS,
                property::toModelType);
    }
}
