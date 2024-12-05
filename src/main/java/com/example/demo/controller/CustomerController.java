package com.example.demo.controller;

import com.example.demo.models.Customer;
import com.example.demo.service.CustomerService;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
@RestController
@RequestMapping("/api/customers")
public class CustomerController {

    @Autowired
    private CustomerService customerService;

    // Add a new customer
    @PostMapping
    public String addCustomer(@RequestBody Customer customer) {
        Customer createdCustomer = customerService.addCustomer(customer);
        return "✅ Customer added successfully! ID: " + createdCustomer.getId() + 
               ", Name: " + createdCustomer.getName();
    }

    // Get all customers
    @GetMapping
    public List<Customer> getAllCustomers() {
        List<Customer> customers = customerService.getAllCustomers();
        if (customers.isEmpty()) {
            throw new RuntimeException("❌ No customers found.");
        }
        return customers;
    }

    // Update the booked status of a customer (booked or not)
    @PutMapping("/{id}/booked")
    public String updateBookingStatus(@PathVariable Long id, @RequestParam boolean booked) {
        Customer updatedCustomer = customerService.updateBookingStatus(id, booked);
        return "✅ Customer ID " + updatedCustomer.getId() + 
               " booking status updated to: " + (booked ? "Booked" : "Not Booked");
    }

    // Get all customers who have not booked
    @GetMapping("/not-booked")
    public List<Customer> getNotBookedCustomers() {
        List<Customer> notBookedCustomers = customerService.getNotBookedCustomers();
        if (notBookedCustomers.isEmpty()) {
            throw new RuntimeException("❌ No customers without bookings found.");
        }
        return notBookedCustomers;
    }
}
