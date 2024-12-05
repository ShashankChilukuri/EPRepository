package com.example.demo.service;

import com.example.demo.models.Customer;
import com.example.demo.repository.CustomerRepository;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
@Service
public class CustomerService {

    @Autowired
    private CustomerRepository customerRepository;

    // Add a customer
    public Customer addCustomer(Customer customer) {
        return customerRepository.save(customer);
    }

    // Get all customers
    public List<Customer> getAllCustomers() {
        return customerRepository.findAll();
    }
    
    // Update booked status of a customer
    public Customer updateBookingStatus(Long id, boolean booked) {
        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Customer not found with id: " + id));
        customer.setBooked(booked);
        return customerRepository.save(customer);
    }

    // Get all customers who have not booked
    public List<Customer> getNotBookedCustomers() {
        return customerRepository.findByBookedFalse(); // Custom query method
    }
}
