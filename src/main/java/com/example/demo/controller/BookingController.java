package com.example.demo.controller;

import com.example.demo.models.Booking;
import com.example.demo.models.BookingRequest;
import com.example.demo.service.BookingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
@RestController
@RequestMapping("/api/bookings")
public class BookingController {

    @Autowired
    private BookingService bookingService;

    // Create a new booking
    @PostMapping
    public String createBooking(@RequestBody BookingRequest bookingRequest) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date checkInDate;
        Date checkOutDate;

        try {
            // Parse the check-in and check-out dates
            checkInDate = dateFormat.parse(bookingRequest.getCheckIn());
            checkOutDate = dateFormat.parse(bookingRequest.getCheckOut());
        } catch (ParseException e) {
            return "❌ Invalid date format. Please use the format 'yyyy-MM-dd'. Example: 2024-12-05";
        }

        // Call service to create booking
        return bookingService.createBooking(
                bookingRequest.getCustomerId(),
                bookingRequest.getRoomId(),
                checkInDate,
                checkOutDate
        );
    }

    // Get all bookings
    @GetMapping
    public List<Booking> getAllBookings() {
        return bookingService.getAllBookings();
    }

    // Get bookings by date range
    @GetMapping("/range")
    public List<Booking> getBookingsByDateRange(
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date startDate,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date endDate) {
        return bookingService.getBookingsByDateRange(startDate, endDate);
    }

    // Get bookings by a specific date
    @GetMapping("/date")
    public List<Booking> getBookingsByDate(
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date date) {
        return bookingService.getBookingsByDate(date);
    }

    // Get bookings by status
    @GetMapping("/status")
    public List<Booking> getBookingsByStatus(@RequestParam String status) {
        return bookingService.getBookingsByStatus(status);
    }
}
