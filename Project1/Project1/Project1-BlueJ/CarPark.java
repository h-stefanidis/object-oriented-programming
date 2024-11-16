import java.util.ArrayList;
import java.util.List;

/**
 * Manages the car park, which includes staff and visitor parking slots.
 * Provides functionality to add, remove, and list slots, park and remove cars,
 * and find cars by their registration number. Parking fees are charged at $5
 * per hour for both staff and visitors.
 * 
 * @author Harrison Stefanidis, 105260443
 * @version 2.2, 4/09/2024
 */

public class CarPark {
    private List<ParkingSlot> slots;

    /**
     * Constructs a CarPark with the specified number of staff and visitor slots.
     *
     * @param staffSlots   the number of staff slots
     * @param visitorSlots the number of visitor slots
     */
    public CarPark(int staffSlots, int visitorSlots) {
        slots = new ArrayList<>();
        for (int i = 1; i <= staffSlots; i++) {
            slots.add(new ParkingSlot("S" + String.format("%02d", i), "staff"));
        }
        for (int i = 1; i <= visitorSlots; i++) {
            slots.add(new ParkingSlot("V" + String.format("%02d", i), "visitor"));
        }
    }

    /**
     * Adds a parking slot to the car park.
     * 
     * @param slot the parking slot to add
     */
    public void addSlot(ParkingSlot slot) {
        slots.add(slot);
    }

    /**
     * Deletes a parking slot if it exists and is not occupied.
     * 
     * @param slotID the ID of the parking slot to delete
     * @return true if the slot was successfully deleted, false otherwise
     */
    public boolean deleteSlot(String slotID) {
        ParkingSlot slot = findSlotById(slotID);
        if (slot != null && !slot.isOccupied()) {
            return slots.remove(slot);
        }
        return false;
    }

    /**
     * Lists all parking slots in the car park.
     * 
     * @return a list of parking slots
     */
    public List<ParkingSlot> listSlots() {
        return slots;
    }

    /**
     * Deletes all unoccupied parking slots from the car park.
     */
    public void deleteAllUnoccupiedSlots() {
        slots.removeIf(slot -> !slot.isOccupied());
    }

    /**
     * Finds a parking slot by its ID.
     * 
     * @param slotID the ID of the parking slot to find
     * @return the parking slot with the specified ID, or null if not found
     */
    public ParkingSlot findSlotById(String slotID) {
        for (ParkingSlot slot : slots) {
            if (slot.getId().equalsIgnoreCase(slotID)) {
                return slot;
            }
        }
        return null;
    }

    /**
     * Finds the parking slot of a car by its registration number.
     * 
     * @param registrationNumber the registration number of the car to find
     * @return the parking slot containing the car, or null if the car is not found
     */
    public ParkingSlot findSlotByCar(String registrationNumber) {
        for (ParkingSlot slot : slots) {
            if (slot.isOccupied() && slot.getParkedCar().getRegistrationNumber().equalsIgnoreCase(registrationNumber)) {
                return slot;
            }
        }
        return null;
    }
}