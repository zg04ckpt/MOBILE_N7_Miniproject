package com.hoangcn.n7.managers;

import com.hoangcn.n7.models.Room;

import java.util.ArrayList;
import java.util.List;

public class RoomManager {
    private static RoomManager instance;
    private List<Room> rooms;

    private RoomManager() {
        rooms = new ArrayList<>();
        initializeData();
    }

    public static RoomManager getInstance() {
        if (instance == null) {
            instance = new RoomManager();
        }
        return instance;
    }

    private void initializeData() {
        rooms.add(new Room(1, "Phòng 101", 2000000, "Còn trống", "", "", null));
        rooms.add(new Room(2, "Phòng 102", 2500000, "Đã thuê", "Nguyễn Văn A", "0912345678", null));
        rooms.add(new Room(3, "Phòng 103", 2000000, "Còn trống", "", "", null));
    }

    public List<Room> getAllRooms() {
        return rooms;
    }

    public void updateRoom(int position, Room room) {
        if (position >= 0 && position < rooms.size()) {
            rooms.set(position, room);
        }
    }

    public Room getRoomAt(int position) {
        if (position >= 0 && position < rooms.size()) {
            return rooms.get(position);
        }
        return null;
    }
}
