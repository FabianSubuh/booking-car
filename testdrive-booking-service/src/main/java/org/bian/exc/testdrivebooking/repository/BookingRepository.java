package org.bian.exc.testdrivebooking.repository;

import  org.bian.exc.testdrivebooking.entity.Booking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {
    List<Booking> findByCarIdAndPreferredDateTimeBetween(Long carId, LocalDateTime startOfDay, LocalDateTime endOfDay);
}

