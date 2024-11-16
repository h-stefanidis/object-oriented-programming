import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.InputMismatchException;
import java.util.Scanner;

/**
 * This application manages a car park with staff and visitor parking slots.
 * Users can add, delete, and list parking slots, park and remove cars, and find
 * cars by registration number. Staff parking slots are reserved for staff
 * members, while visitor parking slots are for visitors. Staff members are
 * charged $5 per hour for parking, and visitors are charged $5 per hour. The
 * application calculates the parking fee based on the parked time. The parking
 * fee is charged for a minimum of 1 hour if the parked time is less than 1
 * hour.
 * 
 * @author Harrison Stefanidis, 105260443
 * @version 2.2, 4/09/2024
 */

public class Application {
    private static CarPark carPark;

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        // Prompt user for number of staff and visitor slots
        System.out.print("Enter number of staff slots: ");
        int staffSlots = scanner.nextInt(); // Handles integer input
        System.out.print("Enter number of visitor slots: ");
        int visitorSlots = scanner.nextInt(); // Handles integer input
        scanner.nextLine(); // Consume newline

        carPark = new CarPark(staffSlots, visitorSlots);

        // Main menu loop
        while (true) {
            displayMenu();
            int choice; // Ensures valid integer input
            try {
                choice = scanner.nextInt();
                scanner.nextLine(); // Consume newline
            } catch (InputMismatchException e) {
                // Handles non-integer input
                scanner.nextLine(); // Consume invalid input
                choice = -1; // Set an invalid choice to trigger default case.
            }

            // Menu options
            switch (choice) {
                case 1:
                    addParkingSlot(scanner);
                    break;
                case 2:
                    deleteParkingSlot(scanner);
                    break;
                case 3:
                    listAllSlots();
                    break;
                case 4:
                    deleteAllUnoccupiedSlots();
                    break;
                case 5:
                    parkCar(scanner);
                    break;
                case 6:
                    findCar(scanner);
                    break;
                case 7:
                    removeCar(scanner);
                    break;
                case 8:
                    System.out.println("Program end!");
                    return;
                default:
                    System.out.println("Invalid choice. Please try again.");
                    break;
            }
        }

    }

    /**
     * Displays the main menu options.
     */
    private static void displayMenu() {
        System.out.println("\nMenu:");
        System.out.println("1. Add a parking slot");
        System.out.println("2. Delete a parking slot");
        System.out.println("3. List all slots");
        System.out.println("4. Delete all unoccupied parking slots");
        System.out.println("5. Park a car into a slot");
        System.out.println("6. Find a car by registration number");
        System.out.println("7. Remove a car by registration number");
        System.out.println("8. Exit");
        System.out.print("Choose an option: ");
    }

    /**
     * Adds a new parking slot with user input.
     * Validates input and adds a parking slot if it doesn't already exist.
     */
    private static void addParkingSlot(Scanner scanner) {
        String slotID = promptForValidSlotID(scanner); // Get valid slot ID
        System.out.print("Enter slot type (staff/visitor): ");
        String type = scanner.nextLine().toLowerCase();

        // Validate slot type
        if (!type.equals("staff") && !type.equals("visitor")) {
            System.out.println("Invalid slot type. Must be either 'staff' or 'visitor'.");
            return;
        }

        // Check if slot ID already exists
        if (carPark.findSlotById(slotID) != null) {
            System.out.println("Failed to add parking slot, parking slot ID already in use.");
            return;
        }

        // Add new parking slot
        ParkingSlot slot = new ParkingSlot(slotID, type);
        carPark.addSlot(slot);
        System.out.println("Parking slot added successfully.");
    }

    /**
     * Deletes a parking slot with user input of slot ID.
     * Ensures the slot exists and is not occupied.
     */
    private static void deleteParkingSlot(Scanner scanner) {
        System.out.print("Enter slot ID to delete: ");
        String slotID = scanner.nextLine().toUpperCase();

        if (carPark.deleteSlot(slotID)) {
            System.out.println("Parking slot deleted successfully.");
        } else {
            System.out.println("Slot not found or is currently occupied.");
        }
    }

    /**
     * Lists all parking slots in the car park.
     * Displays slot ID, type, and status (occupied/unoccupied).
     * If occupied, also displays parked time and parking fee.
     */
    private static void listAllSlots() {
        System.out.println("\nListing all slots:");
        for (ParkingSlot slot : carPark.listSlots()) {
            String status = slot.isOccupied()
                    ? "Occupied by " + slot.getParkedCar().getRegistrationNumber() + " (Owner: "
                            + slot.getParkedCar().getOwnerName() + ")"
                    : "Unoccupied";
            System.out.println("Slot ID: " + slot.getId() + ", Type: " + slot.getType() + ", Status: " + status);

            // Display parked time and parking fee if occupied
            if (slot.isOccupied()) {
                Car car = slot.getParkedCar();
                long hours = java.time.Duration.between(car.getParkedTime(), LocalDateTime.now()).toHours();
                long minutes = java.time.Duration.between(car.getParkedTime(), LocalDateTime.now()).toMinutes() % 60;
                long seconds = java.time.Duration.between(car.getParkedTime(), LocalDateTime.now()).toSeconds() % 60;
                System.out.println("Parked Time: " + hours + " hours " + minutes + " minutes " + seconds + " seconds");
                System.out.println("Parking Fee: $" + ((hours > 0 ? hours : 1) * 5)); // Charge minimum 1 hour if less
                                                                                      // than 1 hour
            }
        }
    }

    /**
     * Deletes all unoccupied parking slots from the car park.
     */
    private static void deleteAllUnoccupiedSlots() {
        carPark.deleteAllUnoccupiedSlots();
        System.out.println("All unoccupied parking slots have been deleted.");
    }

    /**
     * Parks a car into a parking slot with user input.
     * Validates input and parks the car if the slot is unoccupied.
     */
    private static void parkCar(Scanner scanner) {
        System.out.print("Enter slot ID to park in: ");
        String slotID = scanner.nextLine().toUpperCase();

        ParkingSlot slot = carPark.findSlotById(slotID);
        if (slot == null) {
            System.out.println("Slot not found.");
            return;
        }

        if (slot.isOccupied()) {
            System.out.println("Slot is already occupied.");
            return;
        }

        // Get car registration number, owner name, and staff status
        String registrationNumber = promptForValidRegistrationNumber(scanner);
        System.out.print("Enter car owner name: ");
        String ownerName = scanner.nextLine();

        System.out.print("Is the owner a staff member? (yes/no): ");
        boolean isStaff = scanner.nextLine().trim().equalsIgnoreCase("yes");

        // Park the car
        Car car = new Car(registrationNumber, ownerName, isStaff);

        // Park the car and display success message with parked time
        if (slot.parkCar(car)) {
            LocalDateTime parkedTime = car.getParkedTime();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
            System.out.println(
                    "Car " + registrationNumber + " parked successfully in slot " + slotID + " at "
                            + parkedTime.format(formatter));
        } else {
            System.out.println(
                    "Failed to park the car. Ensure the slot type matches the car owner type (staff/visitor).");
        }
    }

    /**
     * Finds a car by registration number with user input.
     * Displays the slot ID, owner name, parked time, and parking fee.
     */
    private static void findCar(Scanner scanner) {
        String registrationNumber = promptForValidRegistrationNumber(scanner);
        ParkingSlot slot = carPark.findSlotByCar(registrationNumber);

        if (slot != null) {
            System.out.println("Car found in slot: " + slot.getId() + ", Owner: " + slot.getParkedCar().getOwnerName());
            long hours = java.time.Duration.between(slot.getParkedCar().getParkedTime(), LocalDateTime.now()).toHours();
            long minutes = java.time.Duration.between(slot.getParkedCar().getParkedTime(), LocalDateTime.now())
                    .toMinutes() % 60;
            long seconds = java.time.Duration.between(slot.getParkedCar().getParkedTime(), LocalDateTime.now())
                    .toSeconds() % 60;
            System.out.println("Parked Time: " + hours + " hours " + minutes + " minutes " + seconds + " seconds");
            System.out.println("Parking Fee: $" + ((hours > 0 ? hours : 1) * 5)); // Charge minimum 1 hour if less than
                                                                                  // 1 hour
        } else {
            System.out.println("Car not found.");
        }
    }

    /**
     * Removes a car from parking slot by registration number with user input.
     * Ensures the car is parked in a valid slot before removing it.
     */
    private static void removeCar(Scanner scanner) {
        String registrationNumber = promptForValidRegistrationNumber(scanner);
        ParkingSlot slot = carPark.findSlotByCar(registrationNumber);

        if (slot != null) {
            slot.removeCar();
            System.out.println("Car removed successfully from slot " + slot.getId() + ".");
        } else {
            System.out.println("Car not found.");
        }
    }

    /**
     * Prompts the user for a valid slot ID.
     * Ensures the slot ID is in a valid format (e.g., S01 or V01).
     */
    private static String promptForValidSlotID(Scanner scanner) {
        while (true) {
            System.out.print("Enter slot ID (e.g., S01 or V01): ");
            String slotID = scanner.nextLine().toUpperCase();
            if (slotID.matches("[A-Z]\\d{2}")) {
                return slotID;
            }
            System.out.println("Invalid slot ID format. Must be an uppercase letter followed by two digits.");
        }
    }

    /**
     * Prompts the user for a valid car registration number.
     * Ensures the registration number is in a valid format (e.g., T2345).
     */
    private static String promptForValidRegistrationNumber(Scanner scanner) {
        while (true) {
            System.out.print("Enter car registration number (e.g., T2345): ");
            String registrationNumber = scanner.nextLine().toUpperCase();
            if (registrationNumber.matches("[A-Z]\\d{4}")) {
                return registrationNumber;
            }
            System.out.println(
                    "Invalid registration number format. Must be an uppercase letter followed by four digits.");
        }
    }
}