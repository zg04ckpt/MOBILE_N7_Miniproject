package com.hoangcn.n7.activities;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.hoangcn.n7.R;
import com.hoangcn.n7.adapters.RoomAdapter;
import com.hoangcn.n7.models.Room;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements RoomAdapter.OnRoomActionListener {
    private final int REQUEST_ADD_CODE = 1;
    private final int REQUEST_UPDATE_CODE = 2;

    private RecyclerView rvRooms;
    private RoomAdapter adapter;
    private List<Room> rooms; // source data
    private List<Room> filterRooms; // filtered data

    // filter UI
    private EditText etSearch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initViews();
        initData();
        setupRecyclerView();

    }


    private void initViews() {
        rvRooms = findViewById(R.id.rvRooms);
        FloatingActionButton fabAdd = findViewById(R.id.fabAdd);

        etSearch = findViewById(R.id.etSearch);

        fabAdd.setOnClickListener(v -> {
            Intent intent = new Intent(this, AddRoomActivity.class);
            startActivityForResult(intent, REQUEST_ADD_CODE);
        });
    }


    private void initData() {
        rooms = new ArrayList<>();
        filterRooms = new ArrayList<>(rooms);
    }

    private void setupRecyclerView() {
        adapter = new RoomAdapter(this, filterRooms, this);
        rvRooms.setLayoutManager(new LinearLayoutManager(this));
        rvRooms.setAdapter(adapter);
    }

    @Override
    public void onEdit(Room room, int position) {
        Intent intent = new Intent(this, EditRoomActivity.class);
        intent.putExtra("room", room);
        intent.putExtra("position", position);
        startActivityForResult(intent, REQUEST_UPDATE_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_ADD_CODE && resultCode == RESULT_OK) {
            handleAddResult(data);
        } else if (requestCode == REQUEST_UPDATE_CODE && resultCode == RESULT_OK) {
            handleUpdateResult(data);
        }
    }

    private void handleAddResult(Intent data) {
        Room newRoom = (Room) data.getSerializableExtra("room");
        if (newRoom != null) {
            newRoom.setId(rooms.size() + 1);
            rooms.add(newRoom);
            adapter.notifyDataSetChanged();
        }
    }

    private void handleUpdateResult(Intent data) {
        Room updated = (Room) data.getSerializableExtra("room");
        int pos = data.getIntExtra("position", -1);
        if (updated != null && pos >= 0 && pos < rooms.size()) {
            rooms.set(pos, updated);
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onDelete(Room room, int position) {
        new AlertDialog.Builder(this)
            .setTitle("Xác nhận xóa")
            .setMessage("Bạn có chắc chắn muốn xóa " + room.getName() + "?")
            .setPositiveButton("Xóa", (dialog, which) -> {
                rooms.remove(position);
                adapter.notifyDataSetChanged();
                Toast.makeText(this, "Đã xóa " + room.getName(), Toast.LENGTH_SHORT).show();
            })
            .setNegativeButton("Hủy", null)
            .show();
    }
}
