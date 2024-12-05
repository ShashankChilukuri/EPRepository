package com.example.demo.repository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.demo.models.Booking;

import java.util.Date;
import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Long> {

    // Find bookings within a date range
    @Query("SELECT b FROM Booking b WHERE b.checkIn >= :startDate AND b.checkOut <= :endDate")
    List<Booking> findBookingsByDateRange(@Param("startDate") Date startDate, @Param("endDate") Date endDate);

    // Find bookings by a specific date
    @Query("SELECT b FROM Booking b WHERE :date BETWEEN b.checkIn AND b.checkOut")
    List<Booking> findBookingsByDate(@Param("date") Date date);

    // Find bookings by status
    List<Booking> findByStatus(String status);
}
