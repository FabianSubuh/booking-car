package org.bian.exc.testdrivebooking.service;

import org.bian.exc.testdrivebooking.entity.Booking;
import org.bian.exc.testdrivebooking.repository.BookingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class BookingService {

    private final BookingRepository bookingRepository;

    @Autowired
    public BookingService(BookingRepository bookingRepository) {
        this.bookingRepository = bookingRepository;
    }

    /**
     * Creates a new test drive booking.
     * Performs basic availability check (simplified).
     * @param booking The booking object to create.
     * @return The created booking object.
     * @throws IllegalStateException if the selected time slot is not available.
     */
    @Transactional
    public Booking createBooking(Booking booking) {
        // Simple availability check: Ensure no other booking exists for the exact same car and time slot.
        // In a real system, this would be more sophisticated (e.g., specific vehicle availability, buffer times).
        LocalDateTime startTime = booking.getPreferredDateTime();
        LocalDateTime endTime = startTime.plusMinutes(60); // Assume 1-hour test drive slot

        List<Booking> existingBookings = bookingRepository.findByCarIdAndPreferredDateTimeBetween(
                booking.getCarId(),
                startTime.minusMinutes(59), // Check for overlaps
                endTime.minusMinutes(1)
        );

        // Check if the specific preferredDateTime slot is taken by another booking for the same car.
        boolean slotTaken = existingBookings.stream()
                .anyMatch(b -> b.getPreferredDateTime().equals(booking.getPreferredDateTime()));

        if (slotTaken) {
            throw new IllegalStateException("The selected time slot is already booked for this car model.");
        }

        return bookingRepository.save(booking);
    }

    /**
     * Retrieves all test drive bookings.
     * @return A list of all bookings.
     */
    @Transactional(readOnly = true)
    public List<Booking> getAllBookings() {
        return bookingRepository.findAll();
    }

    /**
     * Retrieves a test drive booking by its ID.
     * @param id The ID of the booking.
     * @return An Optional containing the booking if found, otherwise empty.
     */
    @Transactional(readOnly = true)
    public Optional<Booking> getBookingById(Long id) {
        return bookingRepository.findById(id);
    }

    /**
     * Updates an existing test drive booking.
     * @param id The ID of the booking to update.
     * @param updatedBooking The booking object with updated details.
     * @return The updated booking object.
     * @throws RuntimeException if the booking is not found.
     */
    @Transactional
    public Booking updateBooking(Long id, Booking updatedBooking) {
        return bookingRepository.findById(id).map(booking -> {
            booking.setCarId(updatedBooking.getCarId());
            booking.setCustomerName(updatedBooking.getCustomerName());
            booking.setCustomerEmail(updatedBooking.getCustomerEmail());
            booking.setCustomerPhone(updatedBooking.getCustomerPhone());
            booking.setPreferredDateTime(updatedBooking.getPreferredDateTime());
            booking.setStatus(updatedBooking.getStatus());
            return bookingRepository.save(booking);
        }).orElseThrow(() -> new RuntimeException("Booking not found with id " + id));
    }

    /**
     * Deletes a test drive booking by its ID.
     * @param id The ID of the booking to delete.
     */
    @Transactional
    public void deleteBooking(Long id) {
        bookingRepository.deleteById(id);
    }

    /**
     * Retrieves available time slots for a specific car model and date.
     * @param carId The ID of the car model.
     * @param date The date for which to check availability.
     * @return A list of LocalTime representing available slots.
     */
    @Transactional(readOnly = true)
    public List<LocalTime> getAvailableTimeSlots(Long carId, LocalDate date) {
        List<LocalTime> allPossibleSlots = Booking.getAvailableTimeSlots(); // All predefined slots

        // Get bookings for the specified car and date
        LocalDateTime startOfDay = date.atStartOfDay();
        LocalDateTime endOfDay = date.atTime(LocalTime.MAX);
        List<Booking> bookedSlots = bookingRepository.findByCarIdAndPreferredDateTimeBetween(carId, startOfDay, endOfDay);

        // Filter out slots that are already booked
        return allPossibleSlots.stream()
                .filter(slot -> bookedSlots.stream()
                        .noneMatch(booking -> booking.getPreferredDateTime().toLocalTime().equals(slot)))
                .collect(Collectors.toList());
    }
}