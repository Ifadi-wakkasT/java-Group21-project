package busdriver;

public class Driver {
    private String driverID;
    private String name;
    private int experienceYears; 
    private String licenseType;
    private String address;
    private String birthdate;
    /** Constructor for the Driver class
     * @param driverID
     * @param name
     * @param experienceYears
     * @param licenseType
     * @param address       StreetNum|StreetName|City|State|Country (D2)
     * @param birthdate     DD-MM-YYYY (D3)
     */
    public Driver(String driverID, String name, int experienceYears,
                  String licenseType, String address, String birthdate) {

// check each field

        if (!isValidDriverID(driverID)) {
            throw new IllegalArgumentException("Invalid driverID: " + driverID
                + ". Must be 10 chars: first 2 digits (2-9), "
                + "chars 3-8 contain 2+ special chars, last 2 uppercase letters.");
        }

        if (!isValidAddress(address)) {
            throw new IllegalArgumentException("Invalid address: " + address
                + ". Must be: StreetNumber|StreetName|City|State|Country");
        }

        if (!isValidBirthdate(birthdate)) {
            throw new IllegalArgumentException("Invalid birthdate: " + birthdate
                + ". Must be in DD-MM-YYYY format.");
        }

        if (!isValidLicenseType(licenseType)) {
            throw new IllegalArgumentException("Invalid licenseType: " + licenseType
                + ". Must be: Light, Medium, Heavy, or PublicTransport.");
        }

        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Name cannot be empty.");
        }

        if (experienceYears < 0) {
            throw new IllegalArgumentException("Experience years cannot be negative.");
        }

        // store the values if everything is valid
        this.driverID = driverID;
        this.name = name;
        this.experienceYears = experienceYears;
        this.licenseType = licenseType;
        this.address = address;
        this.birthdate = birthdate;
    }

    //VALIDATION METHODS (static)

    /**
     * D1: Validates the driver ID format.
     * Example valid ID: "23@#abcdAB"
     */
    public static boolean isValidDriverID(String id) {
        // null check
        if (id == null) return false;

        // Must be exactly 10 characters
        if (id.length() != 10) return false;

        // Check first two characters are digits between 2 and 9
        char first = id.charAt(0);
        char second = id.charAt(1);
        if (first < '2' || first > '9') return false;
        if (second < '2' || second > '9') return false;

        // check characters 3–8 (index 2 to 7) contain at least 2 special characters
        int specialCount = 0;
        for (int i = 2; i <= 7; i++) {
            char c = id.charAt(i);
            // Character.isLetterOrDigit(c) returns true if c is a-z, A-Z, or 0-9
            if (!Character.isLetterOrDigit(c)) {
                specialCount++;
            }
        }
        if (specialCount < 2) return false;

        // Check last two characters (index 8 and 9) are uppercase letters A-Z
        char ninth = id.charAt(8);
        char tenth = id.charAt(9);
        if (ninth < 'A' || ninth > 'Z') return false;
        if (tenth < 'A' || tenth > 'Z') return false;

        // All checks passed!
        return true;
    }


    //D2: Validates address format
    //must be: StreetNumber|StreetName|City|State|Country
    public static boolean isValidAddress(String address) {
        if (address == null) return false;

  
        String[] parts = address.split("\\|");

        // must have exactly 5 parts
        if (parts.length != 5) return false;

        // each part must not be empty (no "||" allowed)
        for (String part : parts) {
            if (part.trim().isEmpty()) return false;
        }

        return true;
    }

    /**
     * D3: vaildates birthdate format DD-MM-YYYY.
     * example valid date: 15-06-1990
     */
    public static boolean isValidBirthdate(String birthdate) {
        if (birthdate == null) return false;

        // Check the format using a "regex" (pattern matching)
        // \\d{2} = exactly 2 digits, \\d{4} = exactly 4 digits
        if (!birthdate.matches("\\d{2}-\\d{2}-\\d{4}")) return false;

        // Split on "-" to get the day, month, year parts
        String[] parts = birthdate.split("-");
        int day   = Integer.parseInt(parts[0]);   // converts "15" into 15
        int month = Integer.parseInt(parts[1]);
        int year  = Integer.parseInt(parts[2]);

        // Basic range checks
        if (day < 1 || day > 31)     return false;
        if (month < 1 || month > 12) return false;
        if (year < 1900 || year > 2025) return false;

        return true;
    }

    //valudates license type
    public static boolean isValidLicenseType(String licenseType) {
        if (licenseType == null) return false;
        // Check if it matches any of the four valid options
        return licenseType.equals("Light")
            || licenseType.equals("Medium")
            || licenseType.equals("Heavy")
            || licenseType.equals("PublicTransport");
    }

    // GETTERS — methods to READ the private fields

    /** Returns the driver's ID */
    public String getDriverID() { return driverID; }

    /** Returns the driver's name */
    public String getName() { return name; }

    /** Returns the driver's years of experience */
    public int getExperienceYears() { return experienceYears; }

    /** Returns the driver's license type */
    public String getLicenseType() { return licenseType; }

    /** Returns the driver's address */
    public String getAddress() { return address; }

    /** Returns the driver's birthdate */
    public String getBirthdate() { return birthdate; }

    // SETTERS — methods to UPDATE the private fields
    // D5: driverID and name have NO setters (they can never be changed)
    // D4: licenseType setter checks experience years

    /**
     * D4: Updates the license type.
     * If the driver has MORE than 10 years experience, this is not allowed
     * @param newLicenseType  The new license type to set
     */
    public void setLicenseType(String newLicenseType) {
        // D4: Block the change if experience > 10 years
        if (this.experienceYears > 10) {
            throw new IllegalStateException(
                "Cannot change license type for drivers with more than 10 years of experience.");
        }
        if (!isValidLicenseType(newLicenseType)) {
            throw new IllegalArgumentException("Invalid licenseType: " + newLicenseType);
        }
        this.licenseType = newLicenseType;
    }

    /** Updates the driver's experience years */
    public void setExperienceYears(int experienceYears) {
        if (experienceYears < 0) {
            throw new IllegalArgumentException("Experience years cannot be negative.");
        }
        this.experienceYears = experienceYears;
    }

    /** Updates the driver's address (must still match the required format) */
    public void setAddress(String address) {
        if (!isValidAddress(address)) {
            throw new IllegalArgumentException("Invalid address format: " + address);
        }
        this.address = address;
    }

    /** Updates the driver's birthdate (must still match DD-MM-YYYY) */
    public void setBirthdate(String birthdate) {
        if (!isValidBirthdate(birthdate)) {
            throw new IllegalArgumentException("Invalid birthdate format: " + birthdate);
        }
        this.birthdate = birthdate;
    }

    @Override
    public String toString() {
        return "Driver{" +
            "driverID='" + driverID + "'" +
            ", name='" + name + "'" +
            ", experienceYears=" + experienceYears +
            ", licenseType='" + licenseType + "'" +
            ", address='" + address + "'" +
            ", birthdate='" + birthdate + "'" +
            "}";
    }
}
