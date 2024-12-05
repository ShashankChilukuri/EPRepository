package com.example.demo.controller;

import com.example.demo.models.Room;
import com.example.demo.service.RoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/rooms")
public class RoomController {

    @Autowired
    private RoomService roomService;

    // Get all rooms
    @GetMapping
    public ResponseEntity<?> getAllRooms() {
        List<Room> rooms = roomService.getAllRooms();
        if (rooms.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("❌ No rooms found in the system.");
        }
        return ResponseEntity.ok(rooms);
    }

    // Get all available rooms (status = "Available")
    @GetMapping("/available")
    public ResponseEntity<?> getAvailableRooms() {
        List<Room> availableRooms = roomService.getAvailableRooms();
        if (availableRooms.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("❌ No available rooms found.");
        }
        return ResponseEntity.ok(availableRooms);
    }

    // Get room by ID
    @GetMapping("/{id}")
    public ResponseEntity<?> getRoomById(@PathVariable Long id) {
        try {
            Room room = roomService.getRoomById(id);
            return ResponseEntity.ok(room);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("❌ Room with ID " + id + " not found.");
        }
    }

    // Add a new room
    @PostMapping
    public ResponseEntity<?> addRoom(@RequestBody Room room) {
        if (room.getRoomNumber() == null || room.getRoomNumber().isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("❌ Room number is required.");
        }
        Room newRoom = roomService.addRoom(room);
        return ResponseEntity.status(HttpStatus.CREATED).body("✅ Room added successfully. Room ID: " + newRoom.getId());
    }

    // Update room status (Available, Occupied, etc.)
    @PutMapping("/{id}/status")
    public ResponseEntity<?> updateRoomStatus(@PathVariable Long id, @RequestParam String status) {
        try {
            Room updatedRoom = roomService.updateRoomStatus(id, status);
            return ResponseEntity.ok("✅ Room status updated successfully. New status: " + status);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("❌ " + e.getMessage());
        }
    }

    // Delete a room by ID
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteRoom(@PathVariable Long id) {
        try {
            roomService.deleteRoom(id);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body("✅ Room deleted successfully.");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("❌ Room with ID " + id + " not found.");
        }
    }

    // Check if a specific room is available
    @GetMapping("/check-availability")
    public ResponseEntity<?> checkRoomAvailability(@RequestParam String roomNumber) {
        boolean isAvailable = roomService.isRoomAvailable(roomNumber);
        if (isAvailable) {
            return ResponseEntity.ok("✅ Room " + roomNumber + " is available.");
        } else {
            return ResponseEntity.ok("❌ Room " + roomNumber + " is not available.");
        }
    }
}
