import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Map;
import java.util.List;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.text.ParseException;

/**
 * A simple parking system GUI application that allows users to manage parking
 * slots, park cars, and view parking slot information. The application allows
 * users to add parking slots, delete parking slots, list all parking slots,
 * delete all unoccupied slots, park a car, find a car by registration number,
 * and remove a car by registration number. The system has an interactive GUI
 * which allows for clicking on to panels to remove or park cars. Additionally,
 * the application calculates parking fees based on the parked time of the car.
 * It also defines the date and time of which a car was parked in a slot.
 * 
 * @author Harrison Stefanidis, 105260443
 * @version 3.1, 15/10/2024
 */

class ParkingSlotInfo {
    String status; // For example, "available" or "occupied"
    String registration; // Registration number of the parked car
    String ownerName; // Owner's name
    String parkedTime; // Time when the car was parked
    String slotType; // Type of the slot: "staff" or "visitor"
    long parkedTimeMillis; // Time in milliseconds when the car was parked
    String slotId; // Slot ID

    /**
     * Constructor for ParkingSlotInfo.
     * 
     * @param status    The status of the parking slot.
     * @param slotType  The type of the parking slot (staff or visitor).
     * @param ownerName The name of the car owner.
     */
    public ParkingSlotInfo(String status, String slotType, String ownerName) {
        this.status = status;
        this.slotType = slotType;
        this.registration = null; // Default to null when slot is empty
        this.ownerName = ownerName;
        this.parkedTime = null; // Default to null when slot is empty
    }

    // Getters and Setters
    public String getSlotId() {
        return slotId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getRegistration() {
        return registration;
    }

    public void setRegistration(String registration) {
        this.registration = registration;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }

    public String getParkedTime() {
        return parkedTime;
    }

    public void setParkedTime(String parkedTime) {
        this.parkedTime = parkedTime;
    }

    public String getSlotType() {
        return slotType;
    }
}

/**
 * ParkingSystemGUI class that manages the graphical user interface for the
 * parking system.
 */
public class ParkingSystemGUI extends JFrame {
    private JPanel mainPanel;
    private JTextField staffSlotInput;
    private JTextField visitorSlotInput;
    private Map<String, ParkingSlotInfo> parkingSlots; // slot ID for ParkingSlotInfo
    private JPanel slotPanel; // Panel to hold the parking slot buttons

    /**
     * Constructor for ParkingSystemGUI.
     * Initialises the parking system GUI.
     */
    public ParkingSystemGUI() {
        parkingSlots = new HashMap<>(); // Initialise parking slots map

        // Initial setup window
        setTitle("Parking System Setup");
        setSize(400, 200);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        mainPanel = new JPanel();
        mainPanel.setLayout(new GridLayout(3, 2)); // Set layout

        // Labels and Text Fields for input
        JLabel staffLabel = new JLabel("Number of Staff Parking Slots:");
        staffSlotInput = new JTextField();
        JLabel visitorLabel = new JLabel("Number of Visitor Parking Slots:");
        visitorSlotInput = new JTextField();

        // Button to submit and proceed
        JButton submitButton = new JButton("Submit");
        submitButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    int staffSlots = Integer.parseInt(staffSlotInput.getText());
                    int visitorSlots = Integer.parseInt(visitorSlotInput.getText());
                    if (staffSlots <= 0 || visitorSlots <= 0) {
                        JOptionPane.showMessageDialog(null, "Please enter positive numbers for slots.");
                        return; // Exit the method if the numbers are invalid
                    }
                    // Proceed to the main menu
                    proceedToMainMenu(staffSlots, visitorSlots);
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(null, "Please enter valid integers for the number of slots.");
                }
            }
        });

        // Button to exit the application
        JButton exitButton = new JButton("Exit");
        exitButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                System.exit(0); // Exit the application
            }
        });

        // Adding components to the panel
        mainPanel.add(staffLabel);
        mainPanel.add(staffSlotInput);
        mainPanel.add(visitorLabel);
        mainPanel.add(visitorSlotInput);
        mainPanel.add(submitButton);
        mainPanel.add(exitButton);

        add(mainPanel); // Add the main panel to the frame
        setVisible(true); // Show the frame
    }

    /**
     * Proceed to main menu function after input.
     * 
     * @param staffSlots   Number of staff parking slots.
     * @param visitorSlots Number of visitor parking slots.
     */
    private void proceedToMainMenu(int staffSlots, int visitorSlots) {
        setVisible(false); // Hide the initial setup window

        // Create the main menu window
        JFrame mainMenuFrame = new JFrame("Parking System Main Menu");
        mainMenuFrame.setSize(800, 600);
        mainMenuFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel menuPanel = new JPanel();
        menuPanel.setLayout(new BorderLayout());

        // Placeholder for parking slots (staff and visitor)
        slotPanel = new JPanel();
        slotPanel.setLayout(new GridLayout(0, Math.max(staffSlots, visitorSlots))); // Dynamic adjustment

        // Adding initial staff and visitor slot buttons
        addInitialSlots(staffSlots, visitorSlots);

        menuPanel.add(slotPanel, BorderLayout.CENTER); // Add slot panel to menu

        // Menu buttons on the right side
        JPanel controlPanel = new JPanel();
        controlPanel.setLayout(new GridLayout(8, 1)); // Vertical layout for buttons

        JButton addSlotButton = new JButton("Add a Parking Slot");
        addSlotButton.addActionListener(e -> addParkingSlot());

        JButton deleteSlotButton = new JButton("Delete a Parking Slot");
        deleteSlotButton.addActionListener(e -> deleteParkingSlot());

        JButton listAllButton = new JButton("List All Slots");
        listAllButton.addActionListener(e -> listAllSlots());

        JButton deleteAllButton = new JButton("Delete All Unoccupied Slots");
        deleteAllButton.addActionListener(e -> deleteAllUnoccupiedSlots());

        JButton parkCarButton = new JButton("Park a Car");
        parkCarButton.addActionListener(e -> parkCar());

        JButton findCarButton = new JButton("Find a Car by Registration");
        findCarButton.addActionListener(e -> findCarByRegistration());

        JButton removeCarButton = new JButton("Remove a Car");
        removeCarButton.addActionListener(e -> removeCarByRegistration());

        JButton exitButton = new JButton("Exit");
        exitButton.addActionListener(e -> System.exit(0));

        // Add buttons to the control panel
        controlPanel.add(addSlotButton);
        controlPanel.add(deleteSlotButton);
        controlPanel.add(listAllButton);
        controlPanel.add(deleteAllButton);
        controlPanel.add(parkCarButton);
        controlPanel.add(findCarButton);
        controlPanel.add(removeCarButton);
        controlPanel.add(exitButton);

        menuPanel.add(controlPanel, BorderLayout.EAST); // Add control panel to menu

        mainMenuFrame.add(menuPanel); // Add menu panel to the main frame
        mainMenuFrame.setVisible(true); // Show the main menu frame
    }

    /**
     * Method to add initial slots for staff and visitors.
     * 
     * @param staffSlots   Number of staff parking slots.
     * @param visitorSlots Number of visitor parking slots.
     */
    private void addInitialSlots(int staffSlots, int visitorSlots) {
        // Add staff slots
        for (int i = 1; i <= staffSlots; i++) {
            String slotId = "S" + String.format("%02d", i); // Staff slot ID
            JButton staffSlot = createSlotButton(slotId, "staff"); // Create button
            parkingSlots.put(slotId, new ParkingSlotInfo("available", null, null)); // Set initial status
            slotPanel.add(staffSlot); // Add button to the panel
        }

        // Add visitor slots
        for (int i = 1; i <= visitorSlots; i++) {
            String slotId = "V" + String.format("%02d", i); // Visitor slot ID
            JButton visitorSlot = createSlotButton(slotId, "visitor"); // Create button
            parkingSlots.put(slotId, new ParkingSlotInfo("available", null, null)); // Set initial status
            slotPanel.add(visitorSlot); // Add button to the panel
        }

        slotPanel.revalidate(); // Refresh the slot panel
        slotPanel.repaint(); // Repaint to show changes
    }

    /**
     * Create a parking slot button.
     * 
     * @param slotId   The ID of the slot.
     * @param slotType The type of the slot (staff or visitor).
     * @return JButton representing the parking slot.
     */
    private JButton createSlotButton(String slotId, String slotType) {
        JButton slotButton = new JButton(slotType.equals("staff") ? "Staff " + slotId : "Visitor " + slotId);

        // Set the background color based on the slot type
        if (slotType.equals("staff")) {
            slotButton.setBackground(Color.RED); // Staff slots in red
        } else {
            slotButton.setBackground(Color.BLUE); // Visitor slots in blue
        }
        slotButton.setForeground(Color.WHITE); // Set text color to white

        slotButton.addActionListener(new SlotActionListener(slotId, slotType));
        return slotButton; // Return button
    }

    /**
     * SlotActionListener handles the actions performed on parking slot buttons.
     */
    private class SlotActionListener implements ActionListener {
        private String slotId; // Parking slot ID
        private String slotType; // Type of slot: "staff" or "visitor"

        /**
         * Constructor to initialise SlotActionListener with slot ID and type.
         *
         * @param slotId   The ID of the parking slot
         * @param slotType The type of the parking slot
         */
        public SlotActionListener(String slotId, String slotType) {
            this.slotId = slotId;
            this.slotType = slotType;
        }

        public void actionPerformed(ActionEvent e) {
            // Retrieve the status of the slot
            ParkingSlotInfo info = parkingSlots.get(slotId);
            if (info != null) {
                String message;
                if (info.getStatus().equals("available")) {
                    // Slot is unoccupied
                    message = slotType.equals("staff") ? "Staff" : "Visitor";
                    message += " Slot " + slotId + " is unoccupied. Would you like to park a car here?";

                    int response = JOptionPane.showConfirmDialog(null, message, "Slot Unoccupied",
                            JOptionPane.YES_NO_CANCEL_OPTION); // Include Cancel option
                    if (response == JOptionPane.YES_OPTION) {
                        parkCarInSlot(slotId, slotType); // Call the method to park the car
                    } else if (response == JOptionPane.NO_OPTION) {
                        // User clicked "No", do nothing
                    } else {
                        // User clicked "Cancel", exit the dialog
                        return;
                    }
                } else {
                    // Slot is occupied
                    message = slotType.equals("staff") ? "Staff" : "Visitor";
                    message += " Slot " + slotId + " is occupied. Would you like to remove the car from this slot?";

                    int response = JOptionPane.showConfirmDialog(null, message, "Slot Occupied",
                            JOptionPane.YES_NO_CANCEL_OPTION); // Include Cancel option
                    if (response == JOptionPane.YES_OPTION) {
                        // Confirm removal
                        int confirmRemoval = JOptionPane.showConfirmDialog(null,
                                "Are you sure you want to remove the car from slot: " + slotId + "?",
                                "Confirm Removal", JOptionPane.YES_NO_OPTION);
                        if (confirmRemoval == JOptionPane.YES_OPTION) {
                            removeCarFromSlot(slotId); // Call the method to remove the car
                        }
                    } else if (response == JOptionPane.NO_OPTION) {
                        // User clicked "No", do nothing
                    } else {
                        // User clicked "Cancel", exit the dialog
                        return;
                    }
                }
            }
        }
    }

    /**
     * Parks a car in the specified parking slot.
     *
     * @param slotId   The ID of the parking slot
     * @param slotType The type of the parking slot
     */
    private void parkCarInSlot(String slotId, String slotType) {
        ParkingSlotInfo info = parkingSlots.get(slotId);

        if (info != null && info.status.equals("available")) {
            // Prompt for user type only if the slot is a staff slot
            if (slotType.equals("staff")) {
                String[] options = { "Staff", "Visitor" };
                int userType = JOptionPane.showOptionDialog(null,
                        "Are you a staff member or a visitor?", "User Type",
                        JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0]);

                // If cancel is pressed, exit quietly
                if (userType == JOptionPane.CLOSED_OPTION) {
                    return;
                }

                // If the user selects Visitor, show an error message
                if (userType == 1) {
                    JOptionPane.showMessageDialog(null, "Error: Visitors cannot park in staff slots.");
                    return; // Exit the method if the user tries to park in the wrong slot
                }
            }

            // Prompt for owner details
            String registration = JOptionPane.showInputDialog("Enter Registration Number (e.g., T2345):");

            // If the user pressed cancel, return without taking any action
            if (registration == null) {
                return; // Exit quietly
            }

            // Validate registration number format
            if (!registration.matches("^[A-Za-z]\\d{4}$")) {
                JOptionPane.showMessageDialog(null,
                        "Invalid registration number format. It should be in the format 'Letter followed by 4 digits' (e.g., T2345).");
                return; // Exit the method if the format is invalid
            }

            // Check if registration is unique
            if (!isRegistrationUnique(registration)) {
                JOptionPane.showMessageDialog(null,
                        "Error: No two cars can park with the same registration number.");
                return; // Exit the method if the registration is not unique
            }

            String ownerName = JOptionPane.showInputDialog("Enter Owner's Name:");

            // If cancel is pressed, exit quietly
            if (ownerName == null) {
                return;
            }

            // Validate owner's name
            if (ownerName.trim().isEmpty() || !ownerName.matches("^[A-Za-z\\s]+$")) {
                JOptionPane.showMessageDialog(null,
                        "Invalid owner's name. It should contain only letters and spaces, and cannot be empty.");
                return; // Exit the method if the name is invalid
            }

            // Update parking slot information
            info.status = "occupied"; // Update status
            info.registration = registration; // Store registration
            info.ownerName = ownerName; // Store owner's name
            info.parkedTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()); // Store current date
                                                                                              // and time

            // Show confirmation message with details
            String message = String.format("Car parked in slot: %s\nRegistration: %s\nOwner: %s\nDate and Time: %s",
                    slotId, registration, ownerName, info.parkedTime);
            JOptionPane.showMessageDialog(null, message);

            // Change the slot button color
            JButton slotButton = findSlotButton(slotId);
            if (slotButton != null) {
                if (slotType.equals("staff")) {
                    slotButton.setBackground(new Color(139, 0, 0)); // Dark Red for occupied staff slots
                } else {
                    slotButton.setBackground(new Color(0, 0, 139)); // Dark Blue for occupied visitor slots
                }
            }

            refreshSlotPanel(); // Refresh the panel to show updated status
        } else {
            JOptionPane.showMessageDialog(null, "Slot " + slotId + " is already occupied.");
        }
    }

    /**
     * Removes a car from the specified parking slot.
     *
     * @param slotId The ID of the parking slot
     */
    private void removeCarFromSlot(String slotId) {
        ParkingSlotInfo info = parkingSlots.get(slotId);

        // Check if the slot exists
        if (info != null) {
            // Check if the slot is occupied
            if (info.status.equals("occupied")) {
                // Reset parking slot information
                info.status = "available"; // Update status
                info.registration = ""; // Clear registration
                info.ownerName = ""; // Clear owner's name
                info.parkedTime = ""; // Clear parked time

                // Show confirmation message
                JOptionPane.showMessageDialog(null,
                        "Car has been removed from slot: " + slotId);

                // Change the slot button color back to available
                JButton slotButton = findSlotButton(slotId);
                if (slotButton != null) {
                    if (slotId.startsWith("S")) {
                        slotButton.setBackground(Color.RED); // Color for available staff slots
                    } else {
                        slotButton.setBackground(Color.BLUE); // Color for available visitor slots
                    }
                }

                refreshSlotPanel(); // Refresh the panel to show updated status
            } else {
                // Show an error message if the slot is unoccupied
                JOptionPane.showMessageDialog(null,
                        "Error: Slot " + slotId + " is unoccupied. No car to remove.");
            }
        } else {
            // Show an error message if the slot ID does not exist
            JOptionPane.showMessageDialog(null,
                    "Error: Slot ID " + slotId + " does not exist.");
        }
    }

    /**
     * Adds a new parking slot.
     */
    private void addParkingSlot() {
        String slotId = JOptionPane.showInputDialog("Enter Slot ID (e.g., S01 = Staff01 or V01 = Visitor01):");

        // Exit out of the method if "Cancel" is pressed or no input is provided
        if (slotId == null) {
            return; // Exit the method if the user presses cancel or doesn't input anything
        }

        String slotType = slotId.startsWith("S") ? "staff" : "visitor"; // Determine type

        // Check for valid slot ID format and uniqueness
        if (!parkingSlots.containsKey(slotId) && (slotId.matches("S\\d{2}") || slotId.matches("V\\d{2}"))) {
            parkingSlots.put(slotId, new ParkingSlotInfo("available", null, null));

            // Clear existing buttons
            slotPanel.removeAll();

            // Create lists to hold sorted slot IDs
            List<String> staffSlots = new ArrayList<>();
            List<String> visitorSlots = new ArrayList<>();

            // Separate the slot IDs into their respective lists
            for (String key : parkingSlots.keySet()) {
                if (key.startsWith("S")) {
                    staffSlots.add(key);
                } else if (key.startsWith("V")) {
                    visitorSlots.add(key);
                }
            }

            // Sort the lists in natural order
            Collections.sort(staffSlots);
            Collections.sort(visitorSlots);

            // Re-add buttons for sorted slots
            for (String staffSlot : staffSlots) {
                JButton newSlotButton = createSlotButton(staffSlot, "staff");
                slotPanel.add(newSlotButton);
            }
            for (String visitorSlot : visitorSlots) {
                JButton newSlotButton = createSlotButton(visitorSlot, "visitor");
                slotPanel.add(newSlotButton);
            }

            // Refresh the panel to show the newly ordered buttons
            refreshSlotPanel();
            slotPanel.revalidate();
            slotPanel.repaint();

            JOptionPane.showMessageDialog(null, slotType.substring(0, 1).toUpperCase() + slotType.substring(1)
                    + " parking slot " + slotId + " added successfully.");
        } else {
            JOptionPane.showMessageDialog(null,
                    "Slot ID already exists or is invalid (Formatting Example: Staff = S01, Visitor = V01).");
        }
    }

    /**
     * Deletes a parking slot based on the provided Slot ID.
     * Prompts the user for a Slot ID and checks if the slot exists and is
     * unoccupied before deletion.
     */
    private void deleteParkingSlot() {
        String slotId = JOptionPane.showInputDialog("Enter Slot ID to delete (e.g., S01 or V01):");

        // If the user pressed cancel (slotId is null), return without taking any action
        if (slotId == null) {
            return; // Exit the method quietly without any message or further actions
        }

        // Check if the slot exists
        if (parkingSlots.containsKey(slotId)) {
            ParkingSlotInfo slotInfo = parkingSlots.get(slotId);

            // Check if the slot is occupied
            if (slotInfo.getStatus().equals("occupied")) {
                JOptionPane.showMessageDialog(null,
                        "Cannot delete slot " + slotId + " because it is currently occupied.");
                return;
            }

            // Proceed to delete the slot
            parkingSlots.remove(slotId);
            // Find and remove the button from the slot panel
            for (Component component : slotPanel.getComponents()) {
                if (component instanceof JButton) {
                    JButton button = (JButton) component;
                    if (button.getText().contains(slotId)) {
                        slotPanel.remove(button);
                        break; // Exit loop after finding and removing
                    }
                }
            }
            slotPanel.revalidate(); // Refresh the panel
            slotPanel.repaint(); // Repaint to show changes
            JOptionPane.showMessageDialog(null, "Parking slot " + slotId + " deleted successfully.");
        } else {
            JOptionPane.showMessageDialog(null,
                    "Slot ID not found. Please enter an existing parking slot (Formatting Example: S01, V01).");
        }
    }

    /**
     * Lists all parking slots, categorised by staff and visitor slots.
     * Displays the status of each slot, including parked time and fees for occupied
     * slots.
     */
    private void listAllSlots() {
        StringBuilder slotList = new StringBuilder("Parking Slots:\n");

        // Lists to hold staff and visitor slots separately
        List<Map.Entry<String, ParkingSlotInfo>> staffSlots = new ArrayList<>();
        List<Map.Entry<String, ParkingSlotInfo>> visitorSlots = new ArrayList<>();

        for (Map.Entry<String, ParkingSlotInfo> entry : parkingSlots.entrySet()) {
            String slotId = entry.getKey();

            // Determine the slot type based on the slot ID and add to the appropriate list
            if (slotId.startsWith("S")) {
                staffSlots.add(entry);
            } else {
                visitorSlots.add(entry);
            }
        }

        // Sort staff and visitor slots by the numeric part of their IDs
        Comparator<Map.Entry<String, ParkingSlotInfo>> slotComparator = (s1, s2) -> {
            int id1 = Integer.parseInt(s1.getKey().replaceAll("\\D", ""));
            int id2 = Integer.parseInt(s2.getKey().replaceAll("\\D", ""));
            return Integer.compare(id1, id2);
        };

        Collections.sort(staffSlots, slotComparator);
        Collections.sort(visitorSlots, slotComparator);

        // Build the display for staff slots
        slotList.append("Staff Parking Slots:\n");
        for (Map.Entry<String, ParkingSlotInfo> entry : staffSlots) {
            appendSlotInfo(slotList, entry.getKey(), entry.getValue());
        }

        // Add a line break before visitor slots
        slotList.append("\nVisitor Parking Slots:\n");
        // Build the display for visitor slots
        for (Map.Entry<String, ParkingSlotInfo> entry : visitorSlots) {
            appendSlotInfo(slotList, entry.getKey(), entry.getValue());
        }

        // Display the sorted list in a dialog
        JOptionPane.showMessageDialog(null, slotList.toString());
    }

    /**
     * Appends information about a specific parking slot to the provided
     * StringBuilder.
     * Includes details such as slot ID, type, status, parked time, and fee if
     * occupied.
     *
     * @param slotList StringBuilder to append slot information to
     * @param slotId   ID of the parking slot
     * @param info     ParkingSlotInfo object containing details about the slot
     */
    private void appendSlotInfo(StringBuilder slotList, String slotId, ParkingSlotInfo info) {
        String slotType = slotId.startsWith("S") ? "Staff" : "Visitor";
        String status = info.getStatus().equals("available") ? "Unoccupied" : "Occupied";

        // Base slot info
        slotList.append("Slot ID: ").append(slotId)
                .append(", Type: ").append(slotType)
                .append(", Status: ").append(status);

        // If occupied, show the parked time and fee
        if ("occupied".equals(info.getStatus())) {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            long parkedMillis = 0;

            try {
                Date parkedDate = dateFormat.parse(info.getParkedTime());
                parkedMillis = System.currentTimeMillis() - parkedDate.getTime();

                // Show parked date
                String parkedDateString = dateFormat.format(parkedDate);
                slotList.append(", Parked Date: ").append(parkedDateString);
            } catch (ParseException ex) {
                ex.printStackTrace();
            }

            // Calculate hours, minutes, and seconds from parkedMillis
            long parkedSeconds = parkedMillis / 1000;
            long parkedHours = parkedSeconds / 3600; // 3600 seconds in an hour
            long parkedMinutes = (parkedSeconds % 3600) / 60; // Remaining minutes
            parkedSeconds = parkedSeconds % 60; // Remaining seconds

            int fee = calculateParkingFee(parkedHours * 3600 + parkedMinutes * 60 + parkedSeconds); // Calculate fee
                                                                                                    // based on total
                                                                                                    // parked time
            slotList.append(", Parked Time: ").append(parkedHours).append(" hours, ")
                    .append(parkedMinutes).append(" minutes, ")
                    .append(parkedSeconds).append(" seconds, Fee: $").append(fee);
        }

        slotList.append("\n"); // End the slot info with a new line
    }

    /**
     * Calculates the parking fee based on the total parked time in seconds.
     *
     * @param parkedSeconds Total parked time in seconds
     * @return Calculated parking fee
     */
    private int calculateParkingFee(long parkedSeconds) {
        // Example parking fee logic: $5 initially, then $5 for every hour (3600
        // seconds)
        int baseFee = 5;
        int hourlyFee = 5;
        long hoursParked = parkedSeconds / 3600;
        return baseFee + (int) (hoursParked * hourlyFee);
    }

    /**
     * Deletes all unoccupied parking slots.
     * Refreshes the slot panel after deletion and displays a confirmation message.
     */
    private void deleteAllUnoccupiedSlots() {
        List<String> slotsToRemove = new ArrayList<>();

        // Identify unoccupied slots to remove
        for (Map.Entry<String, ParkingSlotInfo> entry : parkingSlots.entrySet()) {
            if ("available".equals(entry.getValue().getStatus())) {
                slotsToRemove.add(entry.getKey());
            }
        }

        // Remove identified slots
        for (String slotId : slotsToRemove) {
            parkingSlots.remove(slotId);
            JButton slotButton = findSlotButton(slotId);
            if (slotButton != null) {
                slotPanel.remove(slotButton);
            }
        }

        // Refresh the slot panel
        slotPanel.revalidate(); // Inform the layout manager that the component has changed
        slotPanel.repaint(); // Request a repaint to reflect changes on the screen

        // Show message after deletion
        JOptionPane.showMessageDialog(null, "All unoccupied slots have been deleted.");
    }

    /**
     * Refreshes the slot panel to reflect the current status of all parking slots.
     * Updates the text and background color of each button based on its
     * availability and type.
     */
    private void refreshSlotPanel() {
        for (String slotId : parkingSlots.keySet()) {
            JButton slotButton = findSlotButton(slotId); // Get the button associated with the slot
            ParkingSlotInfo info = parkingSlots.get(slotId); // Get the slot info from the map

            if (slotButton != null && info != null) { // Ensure both the button and info exist
                if ("occupied".equals(info.getStatus())) { // If the slot is occupied
                    // Show slot ID, "Occupied", and the registration number
                    slotButton.setText("<html>" + (slotId.startsWith("S") ? "Staff " : "Visitor ") + slotId +
                            "<br>Occupied<br>Reg: " + info.getRegistration() + "</html>");

                    // Set background color based on slot type
                    if (slotId.startsWith("S")) {
                        slotButton.setBackground(new Color(139, 0, 0)); // Dark red for occupied staff slots
                    } else {
                        slotButton.setBackground(new Color(0, 0, 139)); // Dark blue for occupied visitor slots
                    }
                } else { // If the slot is available
                    // Show the slot type (Staff or Visitor) and the ID
                    slotButton.setText((slotId.startsWith("S") ? "Staff " : "Visitor ") + slotId);

                    // Set background color based on slot type
                    if (slotId.startsWith("S")) {
                        slotButton.setBackground(Color.RED); // Default red for available staff slots
                    } else {
                        slotButton.setBackground(Color.BLUE); // Default blue for available visitor slots
                    }
                }
                slotButton.setForeground(Color.WHITE); // Ensure text color is white for contrast
            } else {
                // Log or handle case where slotButton or info is null (for debugging purposes)
                System.out.println("Slot button not found for ID: " + slotId);
            }
        }
    }

    /**
     * Checks if the registration number is unique.
     *
     * @param registration The registration number to check for uniqueness.
     * @return true if the registration number is unique, false otherwise.
     */
    private boolean isRegistrationUnique(String registration) {
        // Check if the registration number is already in use
        for (ParkingSlotInfo info : parkingSlots.values()) {
            if (info.registration != null && info.registration.equals(registration)) {
                return false; // Registration is not unique
            }
        }
        return true; // Registration is unique
    }

    /**
     * Parks a car in a selected parking slot.
     */
    private void parkCar() {
        String slotId = JOptionPane.showInputDialog("Enter Slot ID to park (e.g., S01 or V01):");

        // If the user pressed cancel (slotId is null), return without taking any action
        if (slotId == null) {
            return; // Exit quietly
        }

        // Check if the slot ID is valid
        if (parkingSlots.containsKey(slotId)) {
            ParkingSlotInfo info = parkingSlots.get(slotId);

            // Determine the slot type based on the slot ID
            String slotType = slotId.startsWith("S") ? "staff" : "visitor";

            // If the slot is a staff slot, prompt for user type
            String userTypeString = "staff"; // Default to staff

            // Only prompt if the user is attempting to park in a staff slot
            if (slotType.equals("staff")) {
                String[] options = { "Staff", "Visitor" };
                int userType = JOptionPane.showOptionDialog(null,
                        "Are you a staff member or a visitor?", "User Type",
                        JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0]);

                // If cancel is pressed, exit quietly
                if (userType == JOptionPane.CLOSED_OPTION) {
                    return;
                }

                // Map userType to string ("staff" or "visitor")
                userTypeString = (userType == 0) ? "staff" : "visitor";
            }

            // Check if user is trying to park in a visitor slot
            if (slotType.equals("visitor")) {
                // Allow any user (staff or visitor) to park in visitor slots
            } else if (!slotType.equals(userTypeString)) {
                // Staff cannot park in visitor slots
                JOptionPane.showMessageDialog(null,
                        "Error: Visitors cannot park in staff slots.");
                return; // Exit the method if the user tries to park in the wrong slot
            }

            // Prompt for owner details
            String registration = JOptionPane.showInputDialog("Enter Registration Number (e.g., T2345):");

            // If cancel is pressed, exit quietly
            if (registration == null) {
                return;
            }

            // Validate registration number format (e.g., T2345)
            if (!registration.matches("^[A-Za-z]\\d{4}$")) {
                JOptionPane.showMessageDialog(null,
                        "Invalid registration number format. It should be in the format 'Letter followed by 4 digits' (e.g., T2345).");
                return; // Exit the method if the format is invalid
            }

            // Check if registration is unique
            if (!isRegistrationUnique(registration)) {
                JOptionPane.showMessageDialog(null, "Error: No two cars can park with the same registration number.");
                return; // Exit the method if the registration is not unique
            }

            String ownerName = JOptionPane.showInputDialog("Enter Owner's Name:");

            // If cancel is pressed, exit quietly
            if (ownerName == null) {
                return;
            }

            // Validate owner's name
            if (ownerName.trim().isEmpty() || !ownerName.matches("^[A-Za-z\\s]+$")) {
                JOptionPane.showMessageDialog(null,
                        "Invalid owner's name. It should contain only letters and spaces, and cannot be empty.");
                return; // Exit the method if the name is invalid
            }

            // Validate slot availability
            if (info.status.equals("available")) {
                // Update parking slot information
                info.status = "occupied"; // Update status
                info.registration = registration; // Store registration
                info.ownerName = ownerName; // Store owner's name
                info.parkedTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()); // Store current date
                                                                                                  // and time

                // Show confirmation message with details
                String message = String.format("Car parked in slot: %s\nRegistration: %s\nOwner: %s\nDate and Time: %s",
                        slotId, registration, ownerName, info.parkedTime);
                JOptionPane.showMessageDialog(null, message);

                // Change the slot button color
                JButton slotButton = findSlotButton(slotId);
                if (slotButton != null) {
                    if (slotType.equals("staff")) {
                        slotButton.setBackground(new Color(139, 0, 0)); // Dark Red for occupied staff slots
                    } else {
                        slotButton.setBackground(new Color(0, 0, 139)); // Dark Blue for occupied visitor slots
                    }
                }

                refreshSlotPanel(); // Refresh the panel to show updated status
            } else {
                JOptionPane.showMessageDialog(null, "Slot " + slotId + " is already occupied.");
            }
        } else {
            JOptionPane.showMessageDialog(null,
                    "Slot ID not found (Please use the following format for existing slots: S01 or V01).");
        }
    }

    /**
     * Finds the JButton corresponding to a given slot ID.
     *
     * @param slotId ID of the parking slot to find.
     * @return The JButton associated with the slot ID, or null if not found.
     */
    private JButton findSlotButton(String slotId) {
        for (Component component : slotPanel.getComponents()) {
            if (component instanceof JButton) {
                JButton button = (JButton) component;
                String buttonText = button.getText();

                // Remove HTML tags for comparison
                String cleanedText = buttonText.replaceAll("\\<.*?\\>", ""); // Remove HTML tags

                if (cleanedText.contains(slotId)) {
                    return button;
                }
            }
        }
        return null; // Button not found
    }

    /**
     * Finds a car in the parking system by its registration number.
     */
    private void findCarByRegistration() {
        String registration = JOptionPane.showInputDialog("Enter Registration Number to find (e.g., T2345):");

        // If the user pressed cancel (registration is null), return without taking any
        // action
        if (registration == null) {
            return; // Exit quietly
        }

        for (Map.Entry<String, ParkingSlotInfo> entry : parkingSlots.entrySet()) {
            ParkingSlotInfo info = entry.getValue();
            if (info.registration != null && info.registration.equals(registration)) {
                // Calculate parked time
                String parkedTime = info.parkedTime; // Assuming you have stored the parked time as a String
                long parkedDurationInMillis;
                try {
                    parkedDurationInMillis = System.currentTimeMillis()
                            - new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(parkedTime).getTime();
                } catch (java.text.ParseException ex) {
                    JOptionPane.showMessageDialog(null, "Error parsing parked time.");
                    return;
                }
                long parkedHours = parkedDurationInMillis / (1000 * 60 * 60);
                long parkedMinutes = (parkedDurationInMillis / (1000 * 60)) % 60;
                long parkedSeconds = (parkedDurationInMillis / 1000) % 60;

                // Calculate parking fee
                int parkingFee = 5 + (int) (parkedHours * 5); // $5 initial fee + $5 for every hour parked

                JOptionPane.showMessageDialog(null,
                        "Car with registration " + registration + " found in slot: " + entry.getKey() +
                                "\nOwner: " + info.ownerName +
                                "\nParked Time: " + parkedHours + " hours " + parkedMinutes + " minutes "
                                + parkedSeconds + " seconds" +
                                "\nParking Fee: $" + parkingFee);
                return;
            }
        }
        JOptionPane.showMessageDialog(null,
                "Car with registration " + registration + " not found. Please enter a valid format (e.g., T2345).");
    }

    /**
     * Removes a car from the parking system by its registration number.
     */
    private void removeCarByRegistration() {
        String registrationToRemove = JOptionPane
                .showInputDialog("Enter the car's registration number to remove from a parking spot (e.g., T2345):");

        // If the user pressed cancel (registrationToRemove is null), return without any
        // action
        if (registrationToRemove == null) {
            return; // Exit quietly
        }

        if (!registrationToRemove.isEmpty()) {
            boolean found = false;

            // Loop through parkingSlots to find the slot with the given registration
            for (Map.Entry<String, ParkingSlotInfo> entry : parkingSlots.entrySet()) {
                ParkingSlotInfo info = entry.getValue();

                if (registrationToRemove.equals(info.getRegistration())) {
                    found = true;
                    // Reset the slot information
                    info.setStatus("available");
                    info.setRegistration(null); // Clear the registration
                    info.setOwnerName(null); // Clear the owner name
                    info.setParkedTime(null); // Clear the parked time
                    info.parkedTimeMillis = 0; // Reset parked time millis

                    // Refresh the slot panel to reflect the update
                    refreshSlotPanel();

                    JOptionPane.showMessageDialog(null,
                            "Car with registration " + registrationToRemove + " has been removed from slot "
                                    + entry.getKey() + ".");
                    break;
                }
            }

            // Show message if registration number was not found
            if (!found) {
                JOptionPane.showMessageDialog(null,
                        "No car found with registration number " + registrationToRemove + ".");
            }
        } else {
            JOptionPane.showMessageDialog(null, "Please enter a valid registration number (e.g., T2345).");
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new ParkingSystemGUI());
    }
}