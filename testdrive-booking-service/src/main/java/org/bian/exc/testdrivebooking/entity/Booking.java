package org.bian.exc.testdrivebooking.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Data; // From Lombok
import lombok.NoArgsConstructor; // From Lombok

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;

@Entity
@Data // Generates getters, setters, toString, equals, hashCode
@NoArgsConstructor // Generates a no-argument constructor
public class Booking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "Car ID is required")
    private Long carId; // Represents the ID of the car from Car Catalog Service

    @NotBlank(message = "Customer name is required")
    @Size(min = 2, max = 100, message = "Customer name must be between 2 and 100 characters")
    private String customerName;

    @NotBlank(message = "Customer email is required")
    @Email(message = "Customer email must be a valid email format")
    private String customerEmail;

    @NotBlank(message = "Customer phone number is required")
    @Pattern(regexp = "^\\+?[0-9. ()-]{7,25}$", message = "Phone number format is invalid")
    private String customerPhone;

    @NotNull(message = "Preferred date and time is required")
    @Future(message = "Booking date and time must be in the future")
    private LocalDateTime preferredDateTime;

    private String status = "COMPLETED"; // PENDING, CONFIRMED, COMPLETED, CANCELED


    @Transient
    public static List<LocalTime> getAvailableTimeSlots() {
        return Arrays.asList(
                LocalTime.of(9, 0), LocalTime.of(10, 0), LocalTime.of(11, 0),
                LocalTime.of(13, 0), LocalTime.of(14, 0), LocalTime.of(15, 0),
                LocalTime.of(16, 0)
        );
    }

    public Booking(Long carId, String customerName, String customerEmail, String customerPhone, LocalDateTime preferredDateTime) {
        this.carId = carId;
        this.customerName = customerName;
        this.customerEmail = customerEmail;
        this.customerPhone = customerPhone;
        this.preferredDateTime = preferredDateTime;
    }
}
