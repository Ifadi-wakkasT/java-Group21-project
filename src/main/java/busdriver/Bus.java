package busdriver;

/**
 * Simple Bus class.
 * Stores all bus details.
 */
public class Bus {

    private String busID;
    private int capacity;
    private double fuelLevel;
    private String fuelType; // Diesel, Hybrid, Electricity

    // Default constructor
    public Bus() {
    }

    // Main constructor
    public Bus(String busID, int capacity, double fuelLevel, String fuelType) {
        this.busID = busID;
        this.capacity = capacity;
        this.fuelLevel = fuelLevel;
        this.fuelType = fuelType;
    }

    // Getters
    public String getBusID() {
        return busID;
    }

    public int getCapacity() {
        return capacity;
    }

    public double getFuelLevel() {
        return fuelLevel;
    }

    public String getFuelType() {
        return fuelType;
    }

    // Setters
    public void setBusID(String busID) {
        this.busID = busID;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public void setFuelLevel(double fuelLevel) {
        this.fuelLevel = fuelLevel;
    }

    public void setFuelType(String fuelType) {
        this.fuelType = fuelType;
    }

    // Used for saving to TXT file
    public String toFileString() {
        return busID + "," + capacity + "," + fuelLevel + "," + fuelType;
    }

    // Used for reading from TXT file
    public static Bus fromFileString(String line) {
        String[] parts = line.split(",");

        String busID = parts[0];
        int capacity = Integer.parseInt(parts[1]);
        double fuelLevel = Double.parseDouble(parts[2]);
        String fuelType = parts[3];

        return new Bus(busID, capacity, fuelLevel, fuelType);
    }
}