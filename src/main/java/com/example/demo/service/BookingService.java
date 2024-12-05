package com.example.demo.service;

import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.example.demo.models.Booking;
import com.example.demo.models.Customer;
import com.example.demo.models.Room;
import com.example.demo.repository.BookingRepository;
import com.example.demo.repository.CustomerRepository;
import com.example.demo.repository.RoomRepository;

import jakarta.mail.MessagingException;

@Service
public class BookingService {
    @Autowired
    private EmailService es;

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private RoomRepository roomRepository;

    @Autowired
    private RoomService roomService;

    @Autowired
    private CustomerService customerService;

    // Create a new booking
    public String createBooking(Long customerId, Long roomId, Date checkIn, Date checkOut) {
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new IllegalArgumentException("❌ No customer found with ID " + customerId));

        if (customer.isBooked()) {
            return "❌ The customer (ID: " + customerId + ") already has an active booking.";
        }

        Room room = roomRepository.findById(roomId)
                .orElseThrow(() -> new IllegalArgumentException("❌ No room found with ID " + roomId));

        if ("Occupied".equals(room.getStatus())) {
            return "❌ The selected room (ID: " + roomId + ") is already occupied.";
        }

        long duration = checkOut.getTime() - checkIn.getTime();
        long days = TimeUnit.DAYS.convert(duration, TimeUnit.MILLISECONDS);
        if (days <= 0) {
            return "❌ Check-out date must be later than the check-in date.";
        }

        double price = room.getPrice() * days;
        Booking booking = new Booking(null, customer, room, checkIn, checkOut, "Pending", price);

        bookingRepository.save(booking);
        roomService.updateRoomStatus(room.getId(), "Occupied");
        customerService.updateBookingStatus(customer.getId(), true);

        booking.setStatus("Confirmed");
        bookingRepository.save(booking);

        // Prepare HTML content for the email
        String htmlContent = "<html><body>"
                + "<h2>Your Booking Confirmation</h2>"
                + "<p><strong>Booking ID:</strong> " + booking.getId() + "</p>"
                + "<p><strong>Customer Name:</strong> " + customer.getName() + "</p>"
                + "<p><strong>Room:</strong> " + room.getRoomType() + "</p>"
                + "<p><strong>Check-in:</strong> " + checkIn.toString() + "</p>"
                + "<p><strong>Check-out:</strong> " + checkOut.toString() + "</p>"
                + "<p><strong>Total Price:</strong> $" + price + "</p>"
                + "</body></html>";

        // Send the email with HTML content
        try {
            es.sendHtmlEmail(customer.getEmail(), "Room Booked Successfully", htmlContent);
        } catch (MessagingException e) {
            e.printStackTrace();
        }

        return "✅ Booking confirmed! Total price: $" + price + 
               ". Customer ID: " + customerId + ", Room ID: " + roomId +
               ", Duration: " + days + " day(s).";
    }

    // Cancel a booking
    public String cancelBooking(Long bookingId) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new IllegalArgumentException("❌ No booking found with ID " + bookingId));

        if (!"Confirmed".equals(booking.getStatus())) {
            return "❌ Only confirmed bookings can be canceled. Current status: " + booking.getStatus();
        }

        booking.setStatus("Cancelled");
        bookingRepository.save(booking);

        Customer customer = booking.getCustomer();
        customer.setBooked(false);
        customerRepository.save(customer);

        Room room = booking.getRoom();
        room.setStatus("Available");
        roomRepository.save(room);

        return "✅ Booking with ID " + bookingId + " has been canceled successfully.";
    }

    // Check and expire bookings (scheduler)
    @Scheduled(fixedRate = 60000) // Every minute
    public void checkAndExpireBookings() {
        List<Booking> bookings = bookingRepository.findAll();
        Date currentDate = new Date();

        for (Booking booking : bookings) {
            if (booking.getCheckOut().before(currentDate) && "Confirmed".equals(booking.getStatus())) {
                booking.setStatus("Completed");
                bookingRepository.save(booking);

                // Send the completed booking email with HTML content
                String htmlContent = "<html><body>"
                        + "<h2>Your Booking Slot Has Been Completed</h2>"
                        + "<p><strong>Booking ID:</strong> " + booking.getId() + "</p>"
                        + "<p><strong>Customer Name:</strong> " + booking.getCustomer().getName() + "</p>"
                        + "<p><strong>Room:</strong> " + booking.getRoom() + "</p>"
                        + "<p><strong>Check-out:</strong> " + booking.getCheckOut().toString() + "</p>"
                        + "</body></html>";

                try {
                    es.sendHtmlEmail(booking.getCustomer().getEmail(), "Room Booking Slot Completed", htmlContent);
                } catch (MessagingException e) {
                    e.printStackTrace();
                }

                Customer customer = booking.getCustomer();
                customer.setBooked(false);
                customerRepository.save(customer);

                Room room = booking.getRoom();
                room.setStatus("Available");
                roomRepository.save(room);

                System.out.println("✅ Booking ID " + booking.getId() + " has been marked as completed.");
            }
        }
    }

    // Fetch all bookings
    public List<Booking> getAllBookings() {
        return bookingRepository.findAll();
    }

    // Fetch bookings by date range
    public List<Booking> getBookingsByDateRange(Date startDate, Date endDate) {
        return bookingRepository.findBookingsByDateRange(startDate, endDate);
    }

    // Fetch bookings by specific date
    public List<Booking> getBookingsByDate(Date date) {
        return bookingRepository.findBookingsByDate(date);
    }

    // Fetch bookings by status
    public List<Booking> getBookingsByStatus(String status) {
        return bookingRepository.findByStatus(status);
    }
}
