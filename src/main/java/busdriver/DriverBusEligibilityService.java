package busdriver;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * Checks if a driver can drive a selected bus.
 * Covers B3, B4 and B5.
 */
public class DriverBusEligibilityService {

    // B3: drivers older than 50 cannot drive buses with capacity 50 or more
    public boolean passesAgeRule(String birthdate, Bus bus) {
        int age = calculateAge(birthdate);

        if (age > 50 && bus.getCapacity() >= 50) {
            return false;
        }

        return true;
    }

    // B4: electric buses need at least 5 years experience
    public boolean passesElectricExperienceRule(int experienceYears, Bus bus) {
        if (bus.getFuelType().equals("Electricity") && experienceYears < 5) {
            return false;
        }

        return true;
    }

    // B5: electric and hybrid buses need Heavy or PublicTransport licence
    public boolean passesLicenceRule(String licenseType, Bus bus) {
        if (bus.getFuelType().equals("Electricity") || bus.getFuelType().equals("Hybrid")) {
            return licenseType.equals("Heavy") || licenseType.equals("PublicTransport");
        }

        return true;
    }

    // Full eligibility check
    public boolean canDriverOperateBus(String birthdate, int experienceYears, String licenseType, Bus bus) {
        return passesAgeRule(birthdate, bus)
                && passesElectricExperienceRule(experienceYears, bus)
                && passesLicenceRule(licenseType, bus);
    }

    // Works out age from DD-MM-YYYY
    private int calculateAge(String birthdate) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        LocalDate dob = LocalDate.parse(birthdate, formatter);
        LocalDate today = LocalDate.now();

        int age = today.getYear() - dob.getYear();

        if (today.getDayOfYear() < dob.getDayOfYear()) {
            age--;
        }

        return age;
    }
}