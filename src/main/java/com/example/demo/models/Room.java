package com.example.demo.models;

import jakarta.persistence.*;
import java.util.Date;

@Entity
public class Room {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String roomNumber;
    private String roomType; // Single, Double, Suite
    private String status; // Available, Occupied
    private double price;
    public Room() {}
    
	public Room(Long id, String roomNumber, String roomType,double price, String status) {
		super();
		this.id = id;
		this.roomNumber = roomNumber;
		this.price=price;
		this.roomType = roomType;
		this.status = status;
	}
	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getRoomNumber() {
		return roomNumber;
	}
	public void setRoomNumber(String roomNumber) {
		this.roomNumber = roomNumber;
	}
	public String getRoomType() {
		return roomType;
	}
	public void setRoomType(String roomType) {
		this.roomType = roomType;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}

    // Getters and Setters
}