package busdriver;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for bus rules.
 */
public class BusTest {

    BusValidator validator = new BusValidator();

    // B1 tests - bus ID rules

    @Test
    public void validBusIDShouldPass() {
        assertTrue(validator.isValidBusID("12345678"));
    }

    @Test
    public void shortBusIDShouldFail() {
        assertFalse(validator.isValidBusID("1234567"));
    }

    @Test
    public void longBusIDShouldFail() {
        assertFalse(validator.isValidBusID("123456789"));
    }

    @Test
    public void busIDWithLettersShouldFail() {
        assertFalse(validator.isValidBusID("1234ABCD"));
    }

    @Test
    public void busIDWithSpecialCharactersShouldFail() {
        assertFalse(validator.isValidBusID("1234!678"));
    }

    @Test
    public void nullBusIDShouldFail() {
        assertFalse(validator.isValidBusID(null));
    }

    // B2 tests - capacity update rule

    @Test
    public void capacityDecreaseShouldPass() {
        assertTrue(validator.canUpdateCapacity(60, 50));
    }

    @Test
    public void sameCapacityShouldPass() {
        assertTrue(validator.canUpdateCapacity(50, 50));
    }

    @Test
    public void capacityIncreaseShouldFail() {
        assertFalse(validator.canUpdateCapacity(40, 50));
    }

    @Test
    public void zeroCapacityShouldFail() {
        assertFalse(validator.canUpdateCapacity(50, 0));
    }

    // Extra validation tests

    @Test
    public void validFuelTypeDieselShouldPass() {
        assertTrue(validator.isValidFuelType("Diesel"));
    }

    @Test
    public void validFuelTypeHybridShouldPass() {
        assertTrue(validator.isValidFuelType("Hybrid"));
    }

    @Test
    public void validFuelTypeElectricityShouldPass() {
        assertTrue(validator.isValidFuelType("Electricity"));
    }

    @Test
    public void invalidFuelTypeShouldFail() {
        assertFalse(validator.isValidFuelType("Petrol"));
    }

    @Test
    public void fullValidBusShouldPass() {
        Bus bus = new Bus("12345678", 45, 75.5, "Diesel");
        assertTrue(validator.isValidBus(bus));
    }
}