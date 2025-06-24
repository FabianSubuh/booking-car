package org.bian.exc.testdrivebooking.service;

import org.bian.exc.testdrivebooking.entity.Booking;
import org.bian.exc.testdrivebooking.repository.BookingRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BookingServiceTest {

    @Mock
    private BookingRepository bookingRepository;

    @InjectMocks
    private BookingService bookingService;

    private Booking testBooking;
    private LocalDateTime futureDateTime;

    @BeforeEach
    void setUp() {
        futureDateTime = LocalDateTime.now().plusDays(1).withHour(10).withMinute(0).withSecond(0).withNano(0);
        testBooking = new Booking(1L, "Jane Doe", "jane.doe@example.com", "08123456789", futureDateTime);
        testBooking.setId(100L);
        testBooking.setStatus("PENDING");
    }

    @Test
    void testCreateBookingSuccess() {
        when(bookingRepository.findByCarIdAndPreferredDateTimeBetween(anyLong(), any(LocalDateTime.class), any(LocalDateTime.class)))
                .thenReturn(Collections.emptyList()); // No existing bookings
        when(bookingRepository.save(any(Booking.class))).thenReturn(testBooking);

        Booking createdBooking = bookingService.createBooking(new Booking(1L, "Jane Doe", "jane.doe@example.com", "08123456789", futureDateTime));
        assertNotNull(createdBooking);
        assertEquals("Jane Doe", createdBooking.getCustomerName());
        assertEquals("PENDING", createdBooking.getStatus());
        verify(bookingRepository, times(1)).save(any(Booking.class));
    }

    @Test
    void testCreateBookingSlotAlreadyTaken() {
        Booking existingBooking = new Booking(1L, "Another Customer", "another@example.com", "08987654321", futureDateTime);
        when(bookingRepository.findByCarIdAndPreferredDateTimeBetween(anyLong(), any(LocalDateTime.class), any(LocalDateTime.class)))
                .thenReturn(Arrays.asList(existingBooking)); // Slot is taken

        IllegalStateException exception = assertThrows(IllegalStateException.class, () ->
                bookingService.createBooking(new Booking(1L, "Jane Doe", "jane.doe@example.com", "08123456789", futureDateTime)));

        assertEquals("The selected time slot is already booked for this car model.", exception.getMessage());
        verify(bookingRepository, never()).save(any(Booking.class)); // Should not save
    }

    @Test
    void testGetAllBookings() {
        when(bookingRepository.findAll()).thenReturn(Arrays.asList(testBooking,
                new Booking(2L, "John Smith", "john@example.com", "08567890123", LocalDateTime.now().plusDays(2))));
        List<Booking> bookings = bookingService.getAllBookings();
        assertNotNull(bookings);
        assertEquals(2, bookings.size());
        verify(bookingRepository, times(1)).findAll();
    }

    @Test
    void testGetBookingByIdFound() {
        when(bookingRepository.findById(100L)).thenReturn(Optional.of(testBooking));
        Optional<Booking> foundBooking = bookingService.getBookingById(100L);
        assertTrue(foundBooking.isPresent());
        assertEquals("Jane Doe", foundBooking.get().getCustomerName());
        verify(bookingRepository, times(1)).findById(100L);
    }

    @Test
    void testGetBookingByIdNotFound() {
        when(bookingRepository.findById(999L)).thenReturn(Optional.empty());
        Optional<Booking> foundBooking = bookingService.getBookingById(999L);
        assertFalse(foundBooking.isPresent());
        verify(bookingRepository, times(1)).findById(999L);
    }

    @Test
    void testUpdateBooking() {
        Booking updatedDetails = new Booking(1L, "Jane Doe Updated", "jane.updated@example.com", "08111222333", LocalDateTime.now().plusDays(5));
        updatedDetails.setStatus("CONFIRMED");

        when(bookingRepository.findById(100L)).thenReturn(Optional.of(testBooking));
        when(bookingRepository.save(any(Booking.class))).thenReturn(updatedDetails);

        Booking result = bookingService.updateBooking(100L, updatedDetails);
        assertNotNull(result);
        assertEquals("Jane Doe Updated", result.getCustomerName());
        assertEquals("CONFIRMED", result.getStatus());
        verify(bookingRepository, times(1)).findById(100L);
        verify(bookingRepository, times(1)).save(testBooking);
    }

    @Test
    void testUpdateBookingNotFound() {
        when(bookingRepository.findById(anyLong())).thenReturn(Optional.empty());
        Booking updatedDetails = new Booking(1L, "Jane Doe", "jane.doe@example.com", "08123456789", LocalDateTime.now().plusDays(5));
        assertThrows(RuntimeException.class, () -> bookingService.updateBooking(999L, updatedDetails));
        verify(bookingRepository, times(1)).findById(999L);
        verify(bookingRepository, never()).save(any(Booking.class));
    }

    @Test
    void testDeleteBooking() {
        doNothing().when(bookingRepository).deleteById(100L);
        bookingService.deleteBooking(100L);
        verify(bookingRepository, times(1)).deleteById(100L);
    }

    @Test
    void testGetAvailableTimeSlots() {
        LocalDate testDate = LocalDate.now().plusDays(7);
        LocalTime bookedTime = LocalTime.of(10, 0);
        LocalDateTime bookedDateTime = testDate.atTime(bookedTime);

        Booking existingBooking = new Booking(1L, "Booked User", "booked@test.com", "08111", bookedDateTime);

        when(bookingRepository.findByCarIdAndPreferredDateTimeBetween(
                eq(1L),
                eq(testDate.atStartOfDay()),
                eq(testDate.atTime(LocalTime.MAX))))
                .thenReturn(Arrays.asList(existingBooking));

        List<LocalTime> availableSlots = bookingService.getAvailableTimeSlots(1L, testDate);

        // Expected slots exclude 10:00, which is booked
        List<LocalTime> expectedSlots = Arrays.asList(
                LocalTime.of(9, 0), LocalTime.of(11, 0),
                LocalTime.of(13, 0), LocalTime.of(14, 0), LocalTime.of(15, 0),
                LocalTime.of(16, 0)
        );

        assertNotNull(availableSlots);
        assertEquals(expectedSlots.size(), availableSlots.size());
        assertTrue(availableSlots.containsAll(expectedSlots));
        assertFalse(availableSlots.contains(bookedTime)); // Ensure the booked slot is not present
    }
}
