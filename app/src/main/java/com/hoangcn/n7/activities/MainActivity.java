package com.hoangcn.n7.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.Nullable;
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
    private final int UPDATE_REQUEST_CODE = 1;
    private final int ADD_REQUEST_CODE = 1;

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
    }

    private void setupRecyclerView() {
        adapter = new RoomAdapter(this, roomList, this);
        rvRooms.setLayoutManager(new LinearLayoutManager(this));
        rvRooms.setAdapter(adapter);
    }

    @Override
    public void onEdit(Room room, int position) {
        Intent intent = new Intent(this, EditRoomActivity.class);
        intent.putExtra("room", room);
        startActivityForResult(intent, UPDATE_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Room room = (Room) data.getSerializableExtra("room");

        // Update the room in the list
        if (requestCode == UPDATE_REQUEST_CODE && resultCode == RESULT_OK) {
            for (int i = 0; i < roomList.size(); i++) {
                if (roomList.get(i).getId().equals(room.getId())) {
                    roomList.set(i, room);
                }
                adapter.notifyItemChanged(roomList.indexOf(room));
                return;
            }
        }

        // Add the new room
        if (requestCode == ADD_REQUEST_CODE && resultCode == RESULT_OK) {
            roomList.add(room);
            adapter.notifyItemInserted(roomList.size() - 1);
            return;
        }
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
