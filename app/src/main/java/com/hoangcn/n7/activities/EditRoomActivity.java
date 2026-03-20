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
    private Button btnSave, btnCancel, btnChooseImage;
    private ImageView imgRoom;
    private Room currentRoom;
    private int roomPosition;
    // store drawable resource id for preview image (matches Room.previewImage)
    private int selectedImageResId = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_room);

        initializeViews();
        setupImageChooser();
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

    private void setupImageChooser() {
        // No-op for now; we show a dialog when user taps "Choose Image".
        // Kept as a separate method for clarity and future extension.
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

            if (currentRoom.getPreviewImage() != -1) {
                // previewImage is stored as a drawable resource id
                selectedImageResId = currentRoom.getPreviewImage();
                imgRoom.setImageResource(selectedImageResId);
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
        // Show a simple dialog allowing the user to pick one of the app's drawables.
        final String[] names = new String[]{"Placeholder", "Launcher Foreground", "Launcher Background"};
        final int[] ids = new int[]{
                R.drawable.image_placeholder_bg,
                R.drawable.ic_launcher_foreground,
                R.drawable.ic_launcher_background
        };

        new AlertDialog.Builder(this)
                .setTitle("Choose image")
                .setItems(names, (dialog, which) -> {
                    selectedImageResId = ids[which];
                    imgRoom.setImageResource(selectedImageResId);
                })
                .setNegativeButton("Cancel", null)
                .show();
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
        // Use the Room setters that exist in the model
        currentRoom.setTenantName(tenant);
        currentRoom.setTenantPhone(phone);
        if (selectedImageResId != -1) {
            currentRoom.setPreviewImage(selectedImageResId);
        }

        getIntent().putExtra("updatedRoom", currentRoom);
        getIntent().putExtra("position", roomPosition);
        setResult(RESULT_OK, getIntent());
        finish();
    }
}
