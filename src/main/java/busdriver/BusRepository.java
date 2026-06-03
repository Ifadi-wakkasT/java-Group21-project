package busdriver;

import java.io.*;
import java.util.ArrayList;

/**
 * Stores buses in a simple TXT file
 * Has add, retrieve, update and count
 */
public class BusRepository {

    private String filePath;
    private BusValidator validator;

    public BusRepository(String filePath) {
        this.filePath = filePath;
        this.validator = new BusValidator();
    }

    // Add new bus
    public boolean addBus(Bus bus) {
        if (!validator.isValidBus(bus)) {
            return false;
        }

        // B1: busID must be unique
        if (retrieveBus(bus.getBusID()) != null) {
            return false;
        }

        ArrayList<Bus> buses = loadBuses();
        buses.add(bus);
        saveBuses(buses);

        return true;
    }

    // Retrieve bus by ID
    public Bus retrieveBus(String busID) {
        ArrayList<Bus> buses = loadBuses();

        for (Bus bus : buses) {
            if (bus.getBusID().equals(busID)) {
                return bus;
            }
        }

        return null;
    }

    // Update existing bus
    public boolean updateBus(String busID, Bus updatedBus) {
        if (!validator.isValidBus(updatedBus)) {
            return false;
        }

        // Bus ID should stay the same during update
        if (!busID.equals(updatedBus.getBusID())) {
            return false;
        }

        ArrayList<Bus> buses = loadBuses();

        for (int i = 0; i < buses.size(); i++) {
            Bus oldBus = buses.get(i);

            if (oldBus.getBusID().equals(busID)) {

                // B2: capacity cannot increase
                if (!validator.canUpdateCapacity(oldBus.getCapacity(), updatedBus.getCapacity())) {
                    return false;
                }

                buses.set(i, updatedBus);
                saveBuses(buses);
                return true;
            }
        }

        return false;
    }

    // Count stored buses
    public int countBuses() {
        return loadBuses().size();
    }

    // Reads buses from TXT file
    private ArrayList<Bus> loadBuses() {
        ArrayList<Bus> buses = new ArrayList<>();

        File file = new File(filePath);

        if (!file.exists()) {
            return buses;
        }

        try {
            BufferedReader reader = new BufferedReader(new FileReader(file));
            String line;

            while ((line = reader.readLine()) != null) {
                if (!line.trim().isEmpty()) {
                    buses.add(Bus.fromFileString(line));
                }
            }

            reader.close();

        } catch (IOException e) {
            System.out.println("Error reading bus file");
        }

        return buses;
    }

    // Saves buses to TXT file
    private void saveBuses(ArrayList<Bus> buses) {
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(filePath));

            for (Bus bus : buses) {
                writer.write(bus.toFileString());
                writer.newLine();
            }

            writer.close();

        } catch (IOException e) {
            System.out.println("Error saving bus file");
        }
    }
}