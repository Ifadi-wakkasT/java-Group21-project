package busdriver;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests driver rules for driving different bus types.
 */
public class DriverBusEligibilityTest {

    DriverBusEligibilityService service = new DriverBusEligibilityService();

    // B3 tests - age restriction

    @Test
    public void driverOver50WithLargeBusShouldFail() {
        Bus bus = new Bus("12345678", 50, 80, "Diesel");
        assertFalse(service.passesAgeRule("01-01-1970", bus));
    }

    @Test
    public void driverOver50WithSmallBusShouldPass() {
        Bus bus = new Bus("12345678", 40, 80, "Diesel");
        assertTrue(service.passesAgeRule("01-01-1970", bus));
    }

    @Test
    public void youngerDriverWithLargeBusShouldPass() {
        Bus bus = new Bus("12345678", 60, 80, "Diesel");
        assertTrue(service.passesAgeRule("01-01-1995", bus));
    }

    // B4 tests - electric bus experience

    @Test
    public void electricBusWithLowExperienceShouldFail() {
        Bus bus = new Bus("12345678", 40, 80, "Electricity");
        assertFalse(service.passesElectricExperienceRule(4, bus));
    }

    @Test
    public void electricBusWithFiveYearsExperienceShouldPass() {
        Bus bus = new Bus("12345678", 40, 80, "Electricity");
        assertTrue(service.passesElectricExperienceRule(5, bus));
    }

    @Test
    public void dieselBusWithLowExperienceShouldPass() {
        Bus bus = new Bus("12345678", 40, 80, "Diesel");
        assertTrue(service.passesElectricExperienceRule(1, bus));
    }

    // B5 tests - licence restriction

    @Test
    public void electricBusWithHeavyLicenceShouldPass() {
        Bus bus = new Bus("12345678", 40, 80, "Electricity");
        assertTrue(service.passesLicenceRule("Heavy", bus));
    }

    @Test
    public void hybridBusWithPublicTransportLicenceShouldPass() {
        Bus bus = new Bus("12345678", 40, 80, "Hybrid");
        assertTrue(service.passesLicenceRule("PublicTransport", bus));
    }

    @Test
    public void electricBusWithLightLicenceShouldFail() {
        Bus bus = new Bus("12345678", 40, 80, "Electricity");
        assertFalse(service.passesLicenceRule("Light", bus));
    }

    @Test
    public void hybridBusWithMediumLicenceShouldFail() {
        Bus bus = new Bus("12345678", 40, 80, "Hybrid");
        assertFalse(service.passesLicenceRule("Medium", bus));
    }

    @Test
    public void dieselBusWithLightLicenceShouldPass() {
        Bus bus = new Bus("12345678", 40, 80, "Diesel");
        assertTrue(service.passesLicenceRule("Light", bus));
    }

    // Full check

    @Test
    public void fullyEligibleDriverShouldPass() {
        Bus bus = new Bus("12345678", 40, 80, "Electricity");
        assertTrue(service.canDriverOperateBus("01-01-1995", 6, "Heavy", bus));
    }

    @Test
    public void driverFailingMultipleRulesShouldFail() {
        Bus bus = new Bus("12345678", 60, 80, "Electricity");
        assertFalse(service.canDriverOperateBus("01-01-1970", 2, "Light", bus));
    }
}