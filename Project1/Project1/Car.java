import java.time.LocalDateTime;

/**
 * Represents a car with a registration number, owner, and parked time.
 * This class is used to manage car details within the car park system.
 * 
 * @author Harrison Stefanidis, 105260443
 * @version 2.2, 4/09/2024
 */

/**
 * Represents a Car with a registration number, owner name, and owner type
 * (staff/visitor).
 */
public class Car {
    private String registrationNumber;
    private String ownerName;
    private boolean isStaff;
    private LocalDateTime parkedTime;

    /**
     * Constructs a Car with the given registration number, owner name, and owner
     * type.
     *
     * @param registrationNumber the car's registration number (e.g., "T2345")
     * @param ownerName          the name of the car owner
     * @param isStaff            true if the owner is a staff member, false
     *                           otherwise
     * @param parkedTime         the time the car was parked
     */

    public Car(String registrationNumber, String ownerName, boolean isStaff) {
        this.registrationNumber = registrationNumber;
        this.ownerName = ownerName;
        this.isStaff = isStaff;
        this.parkedTime = LocalDateTime.now();
    }

    /**
     * Returns the registration number of the car.
     * 
     * @return the registration number of the car
     */
    public String getRegistrationNumber() {
        return registrationNumber;
    }

    /**
     * Returns the name of the car owner.
     * 
     * @return the name of the car owner
     */
    public String getOwnerName() {
        return ownerName;
    }

    /**
     * Returns true if the car owner is a staff member, false otherwise.
     * 
     * @return true if the car owner is a staff member, false otherwise
     */
    public boolean isStaff() {
        return isStaff;
    }

    /**
     * Returns the time the car was parked.
     * 
     * @return the time the car was parked
     */
    public LocalDateTime getParkedTime() {
        return parkedTime;
    }
}