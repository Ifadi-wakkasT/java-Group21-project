package busdriver;
/**
 * Checks if bus data follows the assignment rules.
 */
public class BusValidator {

    // B1: busID must be 8 digits
    public boolean isValidBusID(String busID) {
        if (busID == null) {
            return false;
        }

        if (busID.length() != 8) {
            return false;
        }

        for (int i = 0; i < busID.length(); i++) {
            if (!Character.isDigit(busID.charAt(i))) {
                return false;
            }
        }

        return true;
    }

    // Checks if fuel type is allowed
    public boolean isValidFuelType(String fuelType) {
        if (fuelType == null) {
            return false;
        }

        return fuelType.equals("Diesel")
                || fuelType.equals("Hybrid")
                || fuelType.equals("Electricity");
    }

    // Basic capacity check
    public boolean isValidCapacity(int capacity) {
        return capacity > 0;
    }

    // Basic fuel level check
    public boolean isValidFuelLevel(double fuelLevel) {
        return fuelLevel >= 0 && fuelLevel <= 100;
    }

    // Checks the full bus object
    public boolean isValidBus(Bus bus) {
        if (bus == null) {
            return false;
        }

        return isValidBusID(bus.getBusID())
                && isValidCapacity(bus.getCapacity())
                && isValidFuelLevel(bus.getFuelLevel())
                && isValidFuelType(bus.getFuelType());
    }

    // B2: capacity can decrease, but cannot increase
    public boolean canUpdateCapacity(int oldCapacity, int newCapacity) {
        return newCapacity <= oldCapacity && newCapacity > 0;
    }
}