package com.example.demo.models;

import jakarta.persistence.*;
import java.util.Date;

@Entity
public class Booking {
    
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "customer_id")
    private Customer customer;

    @ManyToOne
    @JoinColumn(name = "room_id")
    private Room room;
    
    private double price; // Price of the booking
    private Date checkIn;
    private Date checkOut;
    private String status; // Confirmed, Cancelled
    public Booking() {};
    public Booking(Long id, Customer customer, Room room, Date checkIn, Date checkOut, String status, double price) {
        super();
        this.id = id;
        this.customer = customer;
        this.room = room;
        this.checkIn = checkIn;
        this.checkOut = checkOut;
        this.status = status;
        this.price = price;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public Customer getCustomer() {
        return customer;
    }
    public void setCustomer(Customer customer) {
        this.customer = customer;
    }
    public Room getRoom() {
        return room;
    }
    public void setRoom(Room room) {
        this.room = room;
    }
    public Date getCheckIn() {
        return checkIn;
    }
    public void setCheckIn(Date checkIn) {
        this.checkIn = checkIn;
    }
    public Date getCheckOut() {
        return checkOut;
    }
    public void setCheckOut(Date checkOut) {
        this.checkOut = checkOut;
    }
    public String getStatus() {
        return status;
    }
    public void setStatus(String status) {
        this.status = status;
    }
    public double getPrice() {
        return price;
    }
    public void setPrice(double price) {
        this.price = price;
    }
    @Override
	public String toString() {
		return "Booking [customer=" + customer + ", room=" + room + ", price=" + price + ", checkIn=" + checkIn
				+ ", checkOut=" + checkOut + ", status=" + status + "]";
	}
}
