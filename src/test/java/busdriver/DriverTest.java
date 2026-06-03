package busdriver;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;

//unit tests for driver class
public class DriverTest {




    // D1 TESTS — Driver ID Validation


    @Test
    @DisplayName("D1 - Normal: Valid driver ID should pass validation")
    void testD1_ValidID_Normal() {
        assertTrue(Driver.isValidDriverID("23@#abcdAB"),
            "A valid driver ID should return true");
    }

    @Test
    @DisplayName("D1 - Normal: ID with special chars spread through middle section")
    void testD1_ValidID_SpecialCharsSpread() {
        assertTrue(Driver.isValidDriverID("45ab!@cdXY"),
            "ID with special chars in positions 3-8 should be valid");
    }

    @Test
    @DisplayName("D1 - Invalid: ID that is too short (9 chars) should fail")
    void testD1_TooShort() {
        assertFalse(Driver.isValidDriverID("23@#abcAB"),
            "ID shorter than 10 chars should be invalid");
    }

    @Test
    @DisplayName("D1 - Invalid: ID where first digit is 1 (must be 2-9) should fail")
    void testD1_FirstDigitTooLow() {
        assertFalse(Driver.isValidDriverID("13@#abcdAB"),
            "ID starting with digit 1 should be invalid");
    }

    @Test
    @DisplayName("D1 - Invalid: ID where first char is 0 should fail")
    void testD1_FirstDigitZero() {
        assertFalse(Driver.isValidDriverID("03@#abcdAB"),
            "ID starting with 0 should be invalid");
    }

    @Test
    @DisplayName("D1 - Invalid: ID with only one special character in middle should fail")
    void testD1_OnlyOneSpecialChar() {
        assertFalse(Driver.isValidDriverID("23abcde!AB"),
            "ID with only one special char in middle should be invalid");
    }

    @Test
    @DisplayName("D1 - Invalid: ID where last two chars are lowercase should fail")
    void testD1_LastCharsLowercase() {
        assertFalse(Driver.isValidDriverID("23@#abcdab"),
            "ID with lowercase last two chars should be invalid");
    }

    @Test
    @DisplayName("D1 - Edge case: null ID should fail without crashing")
    void testD1_NullID() {
        assertFalse(Driver.isValidDriverID(null),
            "null ID should return false (not throw NullPointerException)");
    }

    @Test
    @DisplayName("D1 - Edge case: ID that is exactly 10 chars but all digits should fail")
    void testD1_AllDigits() {
        assertFalse(Driver.isValidDriverID("2345678900"),
            "All-digit ID should be invalid (no special chars, no uppercase)");
    }

    // D2 TESTS


    @Test
    @DisplayName("D2 - Normal: Valid address format should pass")
    void testD2_ValidAddress() {
        assertTrue(Driver.isValidAddress("42|Swanston Street|Melbourne|Victoria|Australia"),
            "Properly formatted address should be valid");
    }

    @Test
    @DisplayName("D2 - Invalid: Address missing one section (only 4 parts) should fail")
    void testD2_MissingSection() {
        assertFalse(Driver.isValidAddress("42|Main Street|Melbourne|Victoria"),
            "Address with only 4 parts should be invalid");
    }

    @Test
    @DisplayName("D2 - Invalid: Address with empty section should fail")
    void testD2_EmptySection() {
        assertFalse(Driver.isValidAddress("42|Main Street||Victoria|Australia"),
            "Address with an empty section should be invalid");
    }

    @Test
    @DisplayName("D2 - Edge case: null address should fail without crashing")
    void testD2_NullAddress() {
        assertFalse(Driver.isValidAddress(null),
            "null address should return false");
    }

//D3 TESTS

    @Test
    @DisplayName("D3 - Normal: Valid birthdate should pass")
    void testD3_ValidBirthdate() {
        assertTrue(Driver.isValidBirthdate("15-06-1990"),
            "Valid date 15-06-1990 should pass");
    }

    @Test
    @DisplayName("D3 - Invalid: Wrong format (YYYY-MM-DD) should fail")
    void testD3_WrongFormat() {
        assertFalse(Driver.isValidBirthdate("1990-06-15"),
            "ISO format YYYY-MM-DD should be rejected");
    }

    @Test
    @DisplayName("D3 - Invalid: Month 13 (impossible month) should fail")
    void testD3_ImpossibleMonth() {
        assertFalse(Driver.isValidBirthdate("15-13-1990"),
            "Month 13 does not exist, should be invalid");
    }

    @Test
    @DisplayName("D3 - Edge case: Day 00 should fail")
    void testD3_ZeroDay() {
        assertFalse(Driver.isValidBirthdate("00-06-1990"),
            "Day 0 is not valid");
    }

    @Test
    @DisplayName("D3 - Edge case: null birthdate should fail")
    void testD3_NullBirthdate() {
        assertFalse(Driver.isValidBirthdate(null),
            "null birthdate should return false");
    }

//D4 TESTS

    @Test
    @DisplayName("D4 - Normal: Driver with 5 years experience can change license")
    void testD4_CanChangeLicense_Under10Years() {
        Driver driver = new Driver(
            "23@#abcdAB", "John Smith", 5,
            "Light", "42|Main St|Melbourne|Victoria|Australia", "15-06-1990"
        );

        assertDoesNotThrow(() -> driver.setLicenseType("Medium"),
            "Driver with 5 years experience should be able to change license");

        assertEquals("Medium", driver.getLicenseType(),
            "License should now be Medium");
    }

    @Test
    @DisplayName("D4 - Invalid: Driver with exactly 11 years cannot change license")
    void testD4_CannotChangeLicense_Over10Years() {
        Driver driver = new Driver(
            "23@#abcdAB", "Jane Doe", 11,
            "Heavy", "1|Queen St|Brisbane|QLD|Australia", "01-01-1980"
        );

        assertThrows(IllegalStateException.class,
            () -> driver.setLicenseType("Light"),
            "Driver with 11 years experience should NOT be able to change license");
    }

    @Test
    @DisplayName("D4 - Edge case: Driver with exactly 10 years CAN still change license")
    void testD4_ExactlyTenYears_CanChange() {
        Driver driver = new Driver(
            "23@#abcdAB", "Alex Jones", 10,
            "Light", "5|Park Rd|Sydney|NSW|Australia", "20-03-1985"
        );

        // 10 years is not MORE than 10, so change should be allowed
        assertDoesNotThrow(() -> driver.setLicenseType("Medium"),
            "Driver with exactly 10 years experience should still be able to change license");
    }

//D5 TESTS

    @Test
    @DisplayName("D5 - driverID should remain the same after update attempt")
    void testD5_DriverID_IsImmutable() {
        Driver driver = new Driver(
            "23@#abcdAB", "Sam Wilson", 3,
            "Light", "10|Collins St|Melbourne|Victoria|Australia", "05-09-1995"
        );

        // There is no setDriverID method (how immutability is enforced)
        assertEquals("23@#abcdAB", driver.getDriverID(),
            "Driver ID should stay unchanged after creation");
    }

    @Test
    @DisplayName("D5 - Name should remain the same after creation")
    void testD5_Name_IsImmutable() {
        Driver driver = new Driver(
            "23@#abcdAB", "Sam Wilson", 3,
            "Light", "10|Collins St|Melbourne|Victoria|Australia", "05-09-1995"
        );

        assertEquals("Sam Wilson", driver.getName(),
            "Driver name should stay unchanged");
    }

    @Test
    @DisplayName("D5 - Edge case: creating two drivers with same ID should be detectable")
    void testD5_TwoDriversSameID_NotEqual() {
        // Both have the same ID — in a repository, the second add would be rejected
        Driver d1 = new Driver("23@#abcdAB", "Alice", 2,
            "Light", "1|A St|Melbourne|Victoria|Australia", "01-01-1995");
        Driver d2 = new Driver("23@#abcdAB", "Bob", 3,
            "Medium", "2|B St|Sydney|NSW|Australia", "02-02-1990");

        // Their ids are the same, names are different
        assertEquals(d1.getDriverID(), d2.getDriverID(),
            "Both drivers have the same ID");
        assertNotEquals(d1.getName(), d2.getName(),
            "But they have different names — so the repo should reject d2");
    }

        //additional constructer sets

    @Test
    @DisplayName("Constructor: Creating a fully valid driver should succeed")
    void testConstructor_ValidDriver() {
        // assertDoesNotThrow = the code should run without any exception
        assertDoesNotThrow(() -> {
            Driver driver = new Driver(
                "23@#abcdAB", "John Smith", 5,
                "Heavy", "42|Swanston St|Melbourne|Victoria|Australia", "15-06-1990"
            );
            assertNotNull(driver, "Driver object should be created successfully");
        });
    }

    @Test
    @DisplayName("Constructor: Invalid driver ID should throw IllegalArgumentException")
    void testConstructor_InvalidID_ThrowsException() {
        // "badID12345" — doesn't meet the D1 format rules
        assertThrows(IllegalArgumentException.class,
            () -> new Driver("badID12345", "John", 5,
                "Heavy", "1|Main St|Mel|VIC|AU", "15-06-1990"),
            "Constructor with invalid ID should throw IllegalArgumentException");
    }
}
