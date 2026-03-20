package com.hoangcn.n7.activities;

import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.hoangcn.n7.R;
import com.hoangcn.n7.adapters.RoomAdapter;
import com.hoangcn.n7.models.Room;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements RoomAdapter.OnRoomActionListener {

    private RecyclerView rvRooms;
    private RoomAdapter adapter;
    private List<Room> roomList;
    private FloatingActionButton fabAdd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initViews();
        initData();
        setupRecyclerView();

        fabAdd.setOnClickListener(v -> {
            // Placeholder for Add functionality
            Toast.makeText(this, "Thêm phòng mới", Toast.LENGTH_SHORT).show();
        });
    }

    private void initViews() {
        rvRooms = findViewById(R.id.rvRooms);
        fabAdd = findViewById(R.id.fabAdd);
    }

    private void initData() {
        // Dữ liệu mẫu
        roomList = new ArrayList<>();
        roomList.add(new Room("1", "Phòng 101", 2500000, false, "", ""));
        roomList.add(new Room("2", "Phòng 102", 3000000, true, "Nguyễn Văn A", "0987654321"));
        roomList.add(new Room("3", "Phòng 201", 2800000, false, "", ""));
        roomList.add(new Room("4", "Phòng 202", 3500000, true, "Trần Thị B", "0123456789"));
    }

    private void setupRecyclerView() {
        adapter = new RoomAdapter(this, roomList, this);
        rvRooms.setLayoutManager(new LinearLayoutManager(this));
        rvRooms.setAdapter(adapter);
    }

    @Override
    public void onEdit(Room room, int position) {
        // Placeholder for Edit functionality
        Toast.makeText(this, "Sửa: " + room.getName(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onDelete(Room room, int position) {
        new AlertDialog.Builder(this)
                .setTitle("Xác nhận xóa")
                .setMessage("Bạn có chắc chắn muốn xóa " + room.getName() + "?")
                .setPositiveButton("Xóa", (dialog, which) -> {
                    roomList.remove(position);
                    adapter.notifyItemRemoved(position);
                    adapter.notifyItemRangeChanged(position, roomList.size());
                    Toast.makeText(this, "Đã xóa " + room.getName(), Toast.LENGTH_SHORT).show();
                })
                .setNegativeButton("Hủy", null)
                .show();
    }
}
