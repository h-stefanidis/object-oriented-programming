# Parking System GUI

## Introduction

Welcome to my Parking System GUI! This application is designed to manage parking slots for staff and visitors efficiently. With a user-friendly interface, users can easily set up the parking slots, park cars, remove cars, and manage parking information through various functionalities.

## Getting Started

### Installation

To set up the Parking System GUI, ensure you have Java or BlueJ installed on your machine. Clone this repository to your local environment and compile the Java files into a text editor or BlueJ.

### Running the Application

1. **Launch the Application**: Run the main method in the `ParkingSystemGUI` class to start the application.
2. **Initial Setup**:
   - You will be prompted to enter the number of staff and visitor parking slots.
   - After entering the values, click the **Submit** button to proceed.

### Main Features

Once the initial setup is complete, you will be presented with the main menu, which includes the following functionalities:

- **Add Slot**: Create new parking slots (staff or visitor) by clicking the **Add Slot** button and following the prompts.
- **Delete Slot**: Remove a selected parking slot by providing its slot ID.
- **List All Slots**: Display a comprehensive list of all parking slots, including their status (occupied/unoccupied), type (staff/visitor), parked time, and fees.
- **Delete All Unoccupied Slots**: Remove all unoccupied parking slots to keep the system organised.
- **Park Car**: Park a car in a selected slot by entering the slot ID, user type (staff or visitor), registration number, and owner name.
- **Find Car**: Search for a parked car using its registration number to view the associated slot ID, parking duration, and fees.
- **Remove Car**: Remove a parked car from its slot using its registration number.

### GUI Components

- **JFrame**: The main container for the entire GUI, including the setup frame and main menu frame.
- **JPanel**: Holds various components like input fields and buttons, organised in logical panels.
- **JLabel**: Displays text to indicate the purpose of input fields.
- **JTextField**: Used for user input of parking slot numbers and other necessary details.
- **JButton**: Interactive buttons that trigger different functionalities throughout the application.
- **JOptionPane**: Dialogs for confirmations, messages, and user prompts.

### Event Handling

The application responds to user actions through action listeners attached to buttons, handling events like submitting data, adding/removing slots, and managing parked cars.

### Refresh and Validation

The GUI updates dynamically based on user interactions, ensuring that the status of parking slots is always current. Validation checks are in place to ensure that registration numbers are unique and slot IDs are correctly identified.

## Conclusion

My Parking System GUI simplifies the management of parking slots and car information, making it an ideal solution for organisations needing an efficient parking management system. If you encounter any issues or have suggestions for improvement, please feel free to contribute!
