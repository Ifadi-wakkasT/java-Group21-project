package busdriver;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.io.*;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

//DriverRepository class handles storing and loading drivers from a json file
public class DriverRepository {

    //path to the json file where driver data is saved
    private String filePath;

    // gson object handles converting between objects and json text
    // "setPrettyPrinting()" makes the JSON file nicely formatted and readable
    private Gson gson = new GsonBuilder().setPrettyPrinting().create();

    /**
     * constructor creates a repository that saves to the given file.
     * @param filePath  Path to the JSON file, e.g. "data/drivers.json"
     */
    public DriverRepository(String filePath) {
        this.filePath = filePath;

        // Make sure the folder for the file exists.
        // e.g. if filePath = "data/drivers.json", create the "data" folder.
        File file = new File(filePath);
        if (file.getParentFile() != null) {
            file.getParentFile().mkdirs(); // mkdirs() creates folder(s) if they don't exist
        }
    }

    //LOAD ALL DRIVERS FROM FILE

    //Reads the JSON file and returns all drivers as a list. If the file doesn't exist, returns an empty list.
    
    
    private List<Driver> loadAll() {
        File file = new File(filePath);

        // If the file doesn't exist yet, return an empty list
        if (!file.exists()) {
            return new ArrayList<>();
        }

        try {
            // Read the entire file content into a string
            BufferedReader reader = new BufferedReader(new FileReader(file));
            StringBuilder content = new StringBuilder();
            String line;

            // Read line by line until the end of the file (readLine returns null at end)
            while ((line = reader.readLine()) != null) {
                content.append(line).append("\n");
            }
            reader.close();

            // Convert the JSON text back into a List<Driver>
            // TypeToken tells Gson what type we want — a List of Driver objects
            Type listType = new TypeToken<List<Driver>>() {}.getType();
            List<Driver> drivers = gson.fromJson(content.toString(), listType);

            // If the file was empty or contained "null", return an empty list
            return drivers != null ? drivers : new ArrayList<>();

        } catch (IOException e) {
            // IOException = file reading error
            // We wrap it in a RuntimeException so we don't need to declare "throws"
            throw new RuntimeException("Error reading file: " + filePath, e);
        }
    }

//SAVE ALL DRIVERS TO FILE

//overrite the entire file with the given list of drivers
    private void saveAll(List<Driver> drivers) {
        try {
            // Convert the list to a nicely-formatted JSON string
            String jsonText = gson.toJson(drivers);

            // Write it to the file (FileWriter overwrites the file each time)
            FileWriter writer = new FileWriter(filePath);
            writer.write(jsonText);
            writer.close();

        } catch (IOException e) {
            throw new RuntimeException("Error writing file: " + filePath, e);
        }
    }

//add new driver

    /**
     * Adds a new driver to the repository.
     *
     * rules:
     *  - The driver object must already be valid (validated in the Driver constructor)
     *  - The driverID must not already exist in the file (D1: unique IDs)
     *
     * @param driver  The Driver object to add
     * @return true if added successfully, false if the ID already exists
     */
    public boolean add(Driver driver) {
        List<Driver> drivers = loadAll();

        // Check if a driver with this ID already exists
        for (Driver existing : drivers) {
            if (existing.getDriverID().equals(driver.getDriverID())) {
                // ID already exists — reject the add
                return false;
            }
        }

        // ID is unique — add the driver and save
        drivers.add(driver);
        saveAll(drivers);
        return true;
    }

    //retrieve a driver by ID

    /**
     * Finds and returns a driver by their driverID.
     *
     * @param driverID  The ID to search for
     * @return The Driver object, or null if not found
     */
    public Driver retrieve(String driverID) {
        List<Driver> drivers = loadAll();

        // Loop through all drivers looking for a matching ID
        for (Driver driver : drivers) {
            if (driver.getDriverID().equals(driverID)) {
                return driver; // Found it!
            }
        }

        return null; // Not found
    }

//modify an existing drivers details
// D4 AND D5
    public boolean update(String driverID, Driver updatedDriver) {
        List<Driver> drivers = loadAll();

        // Find the driver by ID
        for (int i = 0; i < drivers.size(); i++) {
            Driver existing = drivers.get(i);

            if (existing.getDriverID().equals(driverID)) {
                // Found the driver to update

                // D5: we do NOT update driverID or name, they come from the existing driver
                // D4: check license type change restriction
                if (!existing.getLicenseType().equals(updatedDriver.getLicenseType())) {
                    if (existing.getExperienceYears() > 10) {
                        throw new IllegalStateException(
                            "Cannot change license type: driver has more than 10 years experience.");
                    }
                }

                //build a new Driver with the updated fields.
                // driverID and name come from the EXISTING driver (enforcing D5).
                //Everything else comes from updatedDriver.
                Driver newDriver = new Driver(
                    existing.getDriverID(),            // D5: keep original ID
                    existing.getName(),                // D5: keep original name
                    updatedDriver.getExperienceYears(),
                    updatedDriver.getLicenseType(),
                    updatedDriver.getAddress(),
                    updatedDriver.getBirthdate()
                );

                //replace the old driver with the updated one in the list
                drivers.set(i, newDriver);
                saveAll(drivers);
                return true;
            }
        }

        return false; // Driver not found
    }

//count how many drivers are stored
    /**
     * Returns the total number of drivers stored in the file.
     * @return Number of drivers (0 if file is empty or doesn't exist)
     */
    public int count() {
        return loadAll().size();
    }

//returns all drivers as a list for testing/debugging
    public List<Driver> retrieveAll() {
        return loadAll();
    }
}
