package com.hoangcn.n7.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.hoangcn.n7.R;
import com.hoangcn.n7.adapters.RoomAdapter;
import com.hoangcn.n7.models.Room;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private RecyclerView rvRooms;
    private RoomAdapter adapter;
    private List<Room> roomList;
    private FloatingActionButton fabAdd;

    private final ActivityResultLauncher<Intent> addRoomLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                    Room newRoom = (Room) result.getData().getSerializableExtra("NEW_ROOM");
                    if (newRoom != null) {
                        roomList.add(newRoom);
                        adapter.notifyItemInserted(roomList.size() - 1);
                        rvRooms.scrollToPosition(roomList.size() - 1);
                        Toast.makeText(this, "Đã thêm phòng mới thành công", Toast.LENGTH_SHORT).show();
                    }
                }
            }
    );

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initViews();
        initData();
        setupRecyclerView();

        fabAdd.setOnClickListener(v -> {
            int nextId = roomList.size() + 1;
            Intent intent = new Intent(this, AddRoomActivity.class);
            intent.putExtra("NEXT_ID", nextId);
            addRoomLauncher.launch(intent);
        });
    }

    private void initViews() {
        rvRooms = findViewById(R.id.rvRooms);
        fabAdd = findViewById(R.id.fabAdd);
    }

    private void initData() {
        roomList = new ArrayList<>();
        roomList.add(new Room("1", "Phòng 101", 2500000, false, "", "", null));
        roomList.add(new Room("2", "Phòng 102", 3000000, true, "Nguyễn Văn A", "0987654321", null));
    }

    private void setupRecyclerView() {
        // Khởi tạo adapter không cần listener (bỏ tính năng xóa/sửa)
        adapter = new RoomAdapter(this, roomList);
        rvRooms.setLayoutManager(new LinearLayoutManager(this));
        rvRooms.setAdapter(adapter);
    }
}
