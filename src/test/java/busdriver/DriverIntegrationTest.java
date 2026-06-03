package busdriver;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

import java.io.File;

/**
 * Integration tests for DriverRepository.

 *   @BeforeEach used to reset state
 *   @AfterEach  used to clean up files
 */
public class DriverIntegrationTest {

    // using a separate test file so we don't mess with real data
    private static final String TEST_FILE = "data/test_drivers_integration.json";

    private DriverRepository repo;

    // A few reusable valid drivers
    private Driver validDriver1;
    private Driver validDriver2;

    @BeforeEach
    void setUp() {
        // delete the test file if it exists, so each test starts fresh
        File testFile = new File(TEST_FILE);
        if (testFile.exists()) {
            testFile.delete();
        }

        //create a fresh repository pointing to the test file
        repo = new DriverRepository(TEST_FILE);

        // create two valid test drivers we can reuse across tests
        validDriver1 = new Driver(
            "23@#abcdAB", "Alice Brown", 4,
            "Heavy", "42|Swanston St|Melbourne|Victoria|Australia", "10-05-1988"
        );

        validDriver2 = new Driver(
            "56!!xycdCD", "Bob Green", 8,
            "Medium", "7|Collins St|Sydney|NSW|Australia", "22-11-1992"
        );
    }

    
    //@AfterEach runs after every test.
    // clean up the test file
    @AfterEach
    void tearDown() {
        File testFile = new File(TEST_FILE);
        if (testFile.exists()) {
            testFile.delete();
        }
    }

//INTEGRATION TEST 1 (valid drivers are stored correctly)

    @Test
    @DisplayName("Integration 1: A valid driver should be saved and retrievable from the file")
    void testIntegration_ValidDriverStoredCorrectly() {
        // Step 1: Add a driver to the repository (this writes to the JSON file)
        boolean added = repo.add(validDriver1);
        assertTrue(added, "Adding a valid driver should return true");

        // step 2: Create a NEW repository instance pointing to the same file.
        //this simulates restarting the program — the new instance reads from disk.
        DriverRepository freshRepo = new DriverRepository(TEST_FILE);

        // Step 3: Try to retrieve the driver from the fresh repository
        Driver retrieved = freshRepo.retrieve("23@#abcdAB");

        // Step 4: Verify the retrieved data matches what we stored
        assertNotNull(retrieved,
            "Retrieved driver should not be null — it should have been saved to disk");
        assertEquals("23@#abcdAB", retrieved.getDriverID(),
            "Driver ID should match");
        assertEquals("Alice Brown", retrieved.getName(),
            "Driver name should match");
        assertEquals(4, retrieved.getExperienceYears(),
            "Experience years should match");
        assertEquals("Heavy", retrieved.getLicenseType(),
            "License type should match");
    }

//INTEGRATION TEST 2 (duplicate IDs and invalid formats are rejected)

    @Test
    @DisplayName("Integration 2: A driver with a duplicate ID should be rejected")
    void testIntegration_DuplicateDriverRejected() {
        // Add the first driver — should succeed
        boolean firstAdd = repo.add(validDriver1);
        assertTrue(firstAdd, "First add should succeed");

        // try to add a second driver with the SAME ID — should fail
        Driver duplicateID = new Driver(
            "23@#abcdAB",          // Same ID as validDriver1!
            "Imposter Person", 2,
            "Light", "1|Fake St|Faketown|FakeState|FakeCountry", "01-01-2000"
        );
        boolean secondAdd = repo.add(duplicateID);
        assertFalse(secondAdd, "Adding a driver with a duplicate ID should return false");

        // Verify the count is still 1 (the duplicate was not stored)
        assertEquals(1, repo.count(),
            "Count should still be 1 after rejecting the duplicate");
    }

    @Test
    @DisplayName("Integration 2b: A driver with invalid ID format should be rejected at creation")
    void testIntegration_InvalidIDRejectedAtCreation() {
        // The Driver constructor itself should throw an exception for bad IDs
        assertThrows(IllegalArgumentException.class,
            () -> new Driver(
                "BADID12345",   // Doesn't meet D1 format
                "Bad Driver", 3,
                "Light", "1|Street|City|State|Country", "01-01-1990"
            ),
            "Creating a Driver with an invalid ID should throw IllegalArgumentException"
        );

        // Verify nothing was added to the file
        assertEquals(0, repo.count(),
            "Repository should still be empty after failed creation");
    }

//INTEGRATION TEST 3 (updates are presisted correctly)

    @Test
    @DisplayName("Integration 3: Updating a driver should save changes to the JSON file")
    void testIntegration_UpdatePersistedToFile() {
        // Step 1: Add a driver
        repo.add(validDriver1);
        assertEquals(1, repo.count(), "Should have 1 driver after add");

        // step 2 create an updated version of the driver
        // d5: driverid and name must stay the same — so we keep the same constructor values but change address and experienceYears
        Driver updatedData = new Driver(
            "23@#abcdAB",       //same ID (required, will be ignored by update but needs to be valid)
            "Alice Brown",      // Same name (required)
            6,                  // changed experience from 4 to 6
            "Heavy",            // same license (> 10 check: 4 years, so change is fine)
            "100|New St|Brisbane|QLD|Australia",  // changed address
            "10-05-1988"        // Same birthdate
        );

        // Step 3: Perform the update
        boolean updated = repo.update("23@#abcdAB", updatedData);
        assertTrue(updated, "Updating an existing driver should return true");

        // Step 4: Load a fresh repository from disk to confirm the file was updated
        DriverRepository freshRepo = new DriverRepository(TEST_FILE);
        Driver retrieved = freshRepo.retrieve("23@#abcdAB");

        assertNotNull(retrieved, "Driver should still exist after update");
        assertEquals(6, retrieved.getExperienceYears(),
            "Experience years should be updated to 6 on disk");
        assertEquals("100|New St|Brisbane|QLD|Australia", retrieved.getAddress(),
            "Address should be updated on disk");

        // D5: Verify name and ID were NOT changed
        assertEquals("Alice Brown", retrieved.getName(),
            "Name should NOT change after update (D5)");
        assertEquals("23@#abcdAB", retrieved.getDriverID(),
            "ID should NOT change after update (D5)");
    }

//INTEGRATION TEST 4 (record counts are accurate)

    @Test
    @DisplayName("Integration 4: Count should reflect the number of stored drivers accurately")
    void testIntegration_CountIsAccurate() {
        // start with no drivers
        assertEquals(0, repo.count(), "Empty repository should have count 0");

        // Add first driver
        repo.add(validDriver1);
        assertEquals(1, repo.count(), "Count should be 1 after adding first driver");

        // add second driver
        repo.add(validDriver2);
        assertEquals(2, repo.count(), "Count should be 2 after adding second driver");

        // try to add a duplicate — should NOT increase count
        Driver duplicate = new Driver(
            "23@#abcdAB",   // Same ID as validDriver1
            "Duplicate Person", 1,
            "Light", "1|St|City|State|Country", "01-01-2000"
        );
        repo.add(duplicate);
        assertEquals(2, repo.count(),
            "Count should still be 2 after rejecting duplicate driver");

        // Confirm the count persists on disk by creating a fresh repository
        DriverRepository freshRepo = new DriverRepository(TEST_FILE);
        assertEquals(2, freshRepo.count(),
            "Count should still be 2 when loading from disk in a fresh repository");
    }
}
