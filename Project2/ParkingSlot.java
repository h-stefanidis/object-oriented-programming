/**
 * Represents a parking slot in the car park.
 * Each parking slot can hold a car, and can be either a staff or visitor slot.
 * 
 * @author Harrison Stefanidis, 105260443
 * @version 2.2, 4/09/2024
 */

/**
 * Represents a parking slot in the car park.
 */
public class ParkingSlot {
    private String id;
    private String type; // Either "staff" or "visitor"
    private Car parkedCar;

    /**
     * Constructs a ParkingSlot with the specified ID and type.
     *
     * @param id   the slot ID (e.g., "S01")
     * @param type the slot type ("staff" or "visitor")
     */
    public ParkingSlot(String id, String type) {
        this.id = id;
        this.type = type;
        this.parkedCar = null; // Initially unoccupied
    }

    /**
     * Returns the ID of the parking slot.
     * 
     * @return the ID of the parking slot.
     */
    public String getId() {
        return id;
    }

    /**
     * Returns the type of the parking slot.
     * 
     * @return the type of the parking slot ("staff" or "visitor").
     */
    public String getType() {
        return type;
    }

    /**
     * Checks if the parking slot is currently occupied.
     * 
     * @return true if the parking slot is occupied, false otherwise.
     */
    public boolean isOccupied() {
        return parkedCar != null;
    }

    /**
     * Returns the car currently parked in the slot.
     * 
     * @return the car currently parked in the slot, or null if the slot is
     *         unoccupied.
     */
    public Car getParkedCar() {
        return parkedCar;
    }

    /**
     * Parks a car in the slot if the slot is of the correct type and is unoccupied.
     * 
     * @param car the car to park in the slot
     * @return true if the car was successfully parked, false otherwise
     */
    public boolean parkCar(Car car) {
        if (car.isStaff() && type.equals("staff") || !car.isStaff() && type.equals("visitor")) {
            this.parkedCar = car;
            return true;
        }
        return false;
    }

    /**
     * Removes the car from the slot.
     */
    public void removeCar() {
        this.parkedCar = null;
    }
}