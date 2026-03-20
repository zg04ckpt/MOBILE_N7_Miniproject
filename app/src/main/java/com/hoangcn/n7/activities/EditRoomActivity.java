package com.hoangcn.n7.activities;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.hoangcn.n7.R;
import com.hoangcn.n7.models.Room;
import com.hoangcn.n7.utils.RoomValidator;

public class EditRoomActivity extends AppCompatActivity {
    private EditText edtName, edtPrice, edtTenant, edtPhone;
    private Spinner spnStatus;
    private Button btnSave, btnCancel;
    private Room currentRoom;
    private int roomPosition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_room);

        initializeViews();
        loadRoomData();
        setupListeners();
    }

    private void initializeViews() {
        edtName = findViewById(R.id.edtName);
        edtPrice = findViewById(R.id.edtPrice);
        edtTenant = findViewById(R.id.edtTenant);
        edtPhone = findViewById(R.id.edtPhone);
        spnStatus = findViewById(R.id.spnStatus);
        btnSave = findViewById(R.id.btnSave);
        btnCancel = findViewById(R.id.btnCancel);
    }

    private void loadRoomData() {
        currentRoom = (Room) getIntent().getSerializableExtra("room");
        roomPosition = getIntent().getIntExtra("position", -1);

        if (currentRoom != null) {
            edtName.setText(currentRoom.getName());
            edtPrice.setText(String.valueOf(currentRoom.getPrice()));
            edtTenant.setText(currentRoom.getTenantName());
            edtPhone.setText(currentRoom.getTenantPhone());
            setStatusSpinner(currentRoom.getStatus());
        }
    }

    private void setStatusSpinner(String status) {
        for (int i = 0; i < spnStatus.getCount(); i++) {
            if (spnStatus.getItemAtPosition(i).toString().equals(status)) {
                spnStatus.setSelection(i);
                break;
            }
        }
    }

    private void setupListeners() {
        btnSave.setOnClickListener(v -> saveRoom());
        btnCancel.setOnClickListener(v -> finish());
    }

    private void saveRoom() {
        String name = edtName.getText().toString().trim();
        String price = edtPrice.getText().toString().trim();
        String tenant = edtTenant.getText().toString().trim();
        String phone = edtPhone.getText().toString().trim();
        String status = spnStatus.getSelectedItem().toString();

        String error = RoomValidator.validateRoom(name, price, status, tenant, phone);
        if (error != null) {
            Toast.makeText(this, error, Toast.LENGTH_SHORT).show();
            return;
        }

        currentRoom.setName(name);
        currentRoom.setPrice(Float.parseFloat(price));
        currentRoom.setStatus(status);
        currentRoom.setTenantName(tenant);
        currentRoom.setTenantPhone(phone);

        getIntent().putExtra("room", currentRoom);
        getIntent().putExtra("position", roomPosition);
        setResult(RESULT_OK, getIntent());
        finish();
    }
}
