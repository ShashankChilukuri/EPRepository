package com.example.demo.service;

import com.example.demo.models.Room;
import com.example.demo.repository.RoomRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoomService {

    @Autowired
    private RoomRepository roomRepository;

    // Fetch all rooms
    public List<Room> getAllRooms() {
        return roomRepository.findAll();
    }

    // Update room status (Available, Occupied, etc.)
    public Room updateRoomStatus(Long roomId, String status) {
        Room room = roomRepository.findById(roomId).orElseThrow();
        room.setStatus(status);
        return roomRepository.save(room);
    }

    // Add a new room
    public Room addRoom(Room room) {
        return roomRepository.save(room);
    }
    public Room getRoomById(Long roomId) {
        return roomRepository.findById(roomId).orElseThrow(() -> new IllegalArgumentException("Room not found"));
    }


    // Get all available rooms (status = Available)
    public List<Room> getAvailableRooms() {
        return roomRepository.findByStatus("Available");
    }

    // Check if a specific room is available by room number
    public boolean isRoomAvailable(String roomNumber) {
        List<Room> rooms = roomRepository.findByStatus("Available");
        for (Room room : rooms) {
            if (room.getRoomNumber().equals(roomNumber)) {
                return true;  // Room is available
            }
        }
        return false;  // Room not found or not available
    }
    public void deleteRoom(Long roomId) {
        Room room = roomRepository.findById(roomId).orElseThrow(() -> new IllegalArgumentException("Room not found"));
        roomRepository.delete(room);
    }
}
