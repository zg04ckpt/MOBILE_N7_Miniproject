package com.hoangcn.n7.activities;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.Spinner;
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
    private List<Room> allRooms;
    private List<Room> filteredRooms;
    private FloatingActionButton fabAdd;
    private EditText etSearch;
    private Spinner spStatus;
    private EditText etMinPrice;
    private EditText etMaxPrice;
    private View btnReset;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initViews();
        initData();
        setupRecyclerView();
        setupFilters();

        fabAdd.setOnClickListener(v -> {
            // Placeholder for Add functionality
            Toast.makeText(this, "Thêm phòng mới", Toast.LENGTH_SHORT).show();
        });
    }

    private void initViews() {
        rvRooms = findViewById(R.id.rvRooms);
        fabAdd = findViewById(R.id.fabAdd);
        etSearch = findViewById(R.id.etSearch);
        spStatus = findViewById(R.id.spStatus);
        etMinPrice = findViewById(R.id.etMinPrice);
        etMaxPrice = findViewById(R.id.etMaxPrice);
        btnReset = findViewById(R.id.btnReset);
    }

    private void initData() {
        // Dữ liệu mẫu
        allRooms = new ArrayList<>();
        allRooms.add(new Room("1", "Phòng 101", 2500000, false, "", ""));
        allRooms.add(new Room("2", "Phòng 102", 3000000, true, "Nguyễn Văn A", "0987654321"));
        allRooms.add(new Room("3", "Phòng 201", 2800000, false, "", ""));
        allRooms.add(new Room("4", "Phòng 202", 3500000, true, "Trần Thị B", "0123456789"));
        filteredRooms = new ArrayList<>(allRooms);
    }

    private void setupRecyclerView() {
        adapter = new RoomAdapter(this, filteredRooms, this);
        rvRooms.setLayoutManager(new LinearLayoutManager(this));
        rvRooms.setAdapter(adapter);
    }

    private void setupFilters() {
        spStatus.setSelection(0);
        etSearch.addTextChangedListener(simpleWatcher);
        etMinPrice.addTextChangedListener(simpleWatcher);
        etMaxPrice.addTextChangedListener(simpleWatcher);

        spStatus.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                applyFilter();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                applyFilter();
            }
        });

        btnReset.setOnClickListener(v -> resetFilters());

        applyFilter();
    }

    private final TextWatcher simpleWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            applyFilter();
        }

        @Override
        public void afterTextChanged(Editable s) {
        }
    };

    private void resetFilters() {
        etSearch.setText("");
        etMinPrice.setText("");
        etMaxPrice.setText("");
        spStatus.setSelection(0);
        applyFilter();
    }

    private void applyFilter() {
        String keyword = etSearch.getText().toString().trim().toLowerCase();
        String statusSelected = spStatus.getSelectedItem() != null
                ? spStatus.getSelectedItem().toString()
                : "";
        String statusAvailable = getString(R.string.status_available);
        String statusRented = getString(R.string.status_rented);
        String statusAll = getString(R.string.status_all);

        double minPrice = parseDoubleOrZero(etMinPrice.getText().toString());
        double maxPrice = parseDoubleOrZero(etMaxPrice.getText().toString());

        filteredRooms.clear();
        for (Room room : allRooms) {
            boolean matchKeyword = keyword.isEmpty()
                    || room.getName().toLowerCase().contains(keyword);

            boolean matchStatus;
            if (statusSelected.equals(statusAll)) {
                matchStatus = true;
            } else if (statusSelected.equals(statusRented)) {
                matchStatus = room.isRented();
            } else {
                matchStatus = !room.isRented();
            }

            boolean matchPrice;
            if (maxPrice == 0 && minPrice == 0) {
                matchPrice = true;
            } else if (maxPrice == 0) {
                matchPrice = room.getPrice() >= minPrice;
            } else {
                matchPrice = room.getPrice() >= minPrice && room.getPrice() <= maxPrice;
            }

            if (matchKeyword && matchStatus && matchPrice) {
                filteredRooms.add(room);
            }
        }

        adapter.notifyDataSetChanged();
    }

    private double parseDoubleOrZero(String value) {
        if (value == null || value.trim().isEmpty()) {
            return 0;
        }
        try {
            return Double.parseDouble(value.trim());
        } catch (NumberFormatException e) {
            return 0;
        }
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
                    int indexInAll = allRooms.indexOf(room);
                    if (indexInAll >= 0) {
                        allRooms.remove(indexInAll);
                    }
                    filteredRooms.remove(position);
                    adapter.notifyItemRemoved(position);
                    adapter.notifyItemRangeChanged(position, filteredRooms.size());
                    Toast.makeText(this, "Đã xóa " + room.getName(), Toast.LENGTH_SHORT).show();
                })
                .setNegativeButton("Hủy", null)
                .show();
    }
}
