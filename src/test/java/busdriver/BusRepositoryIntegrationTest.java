package busdriver;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Integration tests using real TXT file.
 */
public class BusRepositoryIntegrationTest {

    private BusRepository repository;
    private String testFilePath = "test-buses.txt";

    @BeforeEach
    public void setup() {
        File file = new File(testFilePath);

        if (file.exists()) {
            file.delete();
        }

        repository = new BusRepository(testFilePath);
    }

    // 1. Valid buses are stored correctly
    @Test
    public void validBusShouldBeStoredCorrectly() {
        Bus bus = new Bus("12345678", 45, 90, "Diesel");

        boolean result = repository.addBus(bus);
        Bus savedBus = repository.retrieveBus("12345678");

        assertTrue(result);
        assertNotNull(savedBus);
        assertEquals("12345678", savedBus.getBusID());
        assertEquals(45, savedBus.getCapacity());
        assertEquals(90, savedBus.getFuelLevel());
        assertEquals("Diesel", savedBus.getFuelType());
    }

    // 2. Invalid buses are rejected
    @Test
    public void invalidBusShouldBeRejected() {
        Bus bus = new Bus("ABC12345", 45, 90, "Diesel");

        boolean result = repository.addBus(bus);

        assertFalse(result);
        assertEquals(0, repository.countBuses());
    }

    // 3. Updates are persisted correctly
    @Test
    public void busUpdateShouldBeSavedCorrectly() {
        Bus bus = new Bus("12345678", 60, 90, "Diesel");
        repository.addBus(bus);

        Bus updatedBus = new Bus("12345678", 50, 70, "Hybrid");
        boolean result = repository.updateBus("12345678", updatedBus);

        Bus savedBus = repository.retrieveBus("12345678");

        assertTrue(result);
        assertEquals(50, savedBus.getCapacity());
        assertEquals(70, savedBus.getFuelLevel());
        assertEquals("Hybrid", savedBus.getFuelType());
    }

    // 4. Record counts are updated correctly
    @Test
    public void busCountShouldUpdateCorrectly() {
        Bus bus1 = new Bus("12345678", 45, 90, "Diesel");
        Bus bus2 = new Bus("87654321", 35, 80, "Electricity");

        repository.addBus(bus1);
        repository.addBus(bus2);

        assertEquals(2, repository.countBuses());
    }

    // Extra integration test - duplicate ID rejected
    @Test
    public void duplicateBusIDShouldBeRejected() {
        Bus bus1 = new Bus("12345678", 45, 90, "Diesel");
        Bus bus2 = new Bus("12345678", 35, 80, "Hybrid");

        assertTrue(repository.addBus(bus1));
        assertFalse(repository.addBus(bus2));
        assertEquals(1, repository.countBuses());
    }

    // Extra integration test - capacity increase rejected
    @Test
    public void capacityIncreaseShouldBeRejectedDuringUpdate() {
        Bus bus = new Bus("12345678", 40, 90, "Diesel");
        repository.addBus(bus);

        Bus updatedBus = new Bus("12345678", 50, 80, "Diesel");
        boolean result = repository.updateBus("12345678", updatedBus);

        assertFalse(result);

        Bus savedBus = repository.retrieveBus("12345678");
        assertEquals(40, savedBus.getCapacity());
    }
}