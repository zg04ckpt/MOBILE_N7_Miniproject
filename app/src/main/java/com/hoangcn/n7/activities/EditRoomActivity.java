package com.hoangcn.n7.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import com.hoangcn.n7.R;
import com.hoangcn.n7.models.Room;
import com.hoangcn.n7.utils.RoomValidator;

public class EditRoomActivity extends AppCompatActivity {
    private EditText edtName, edtPrice, edtTenant, edtPhone;
    private Spinner spnStatus;
    private Button btnSave, btnCancel, btnChooseImage;
    private ImageView imgRoom;
    private Room currentRoom;
    private int roomPosition;
    private Uri selectedImageUri;
    private ActivityResultLauncher<Intent> imagePickerLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_room);

        initializeViews();
        setupImagePicker();
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
        btnChooseImage = findViewById(R.id.btnChooseImage);
        imgRoom = findViewById(R.id.imgRoom);
    }

    private void setupImagePicker() {
        imagePickerLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                        selectedImageUri = result.getData().getData();
                        if (selectedImageUri != null) {
                            imgRoom.setImageURI(selectedImageUri);
                        }
                    }
                }
        );
    }

    private void loadRoomData() {
        currentRoom = (Room) getIntent().getSerializableExtra("room");
        roomPosition = getIntent().getIntExtra("position", -1);

        if (currentRoom != null) {
            edtName.setText(currentRoom.getName());
            edtPrice.setText(String.valueOf(currentRoom.getPrice()));
            edtTenant.setText(currentRoom.getTenant());
            edtPhone.setText(currentRoom.getPhoneNumber());
            setStatusSpinner(currentRoom.getStatus());

            if (currentRoom.getImageUri() != null) {
                selectedImageUri = Uri.parse(currentRoom.getImageUri());
                imgRoom.setImageURI(selectedImageUri);
            }
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
        btnChooseImage.setOnClickListener(v -> openGallery());
        btnSave.setOnClickListener(v -> saveRoom());
        btnCancel.setOnClickListener(v -> finish());
    }

    private void openGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        imagePickerLauncher.launch(intent);
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
        currentRoom.setTenant(tenant);
        currentRoom.setPhoneNumber(phone);
        if (selectedImageUri != null) {
            currentRoom.setImageUri(selectedImageUri.toString());
        }

        getIntent().putExtra("updatedRoom", currentRoom);
        getIntent().putExtra("position", roomPosition);
        setResult(RESULT_OK, getIntent());
        finish();
    }
}
