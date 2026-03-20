package com.hoangcn.n7.activities;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.hoangcn.n7.R;
import com.hoangcn.n7.models.Room;

public class AddRoomActivity extends AppCompatActivity {

    private EditText etName, etPrice, etTenantName, etTenantPhone;
    private Spinner spnStatus;
    private ImageView ivRoomImage;
    private ImageButton btnSelectImage;
    private Button btnSave;
    // store selected image as drawable resource id
    private int selectedImageResId = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_room);
        initViews();
        setupListeners();
    }

    private void initViews() {
        etName = findViewById(R.id.etRoomName);
        etPrice = findViewById(R.id.etPrice);
        spnStatus = findViewById(R.id.spnStatus);
        etTenantName = findViewById(R.id.etTenantName);
        etTenantPhone = findViewById(R.id.etTenantPhone);
        ivRoomImage = findViewById(R.id.ivRoomImage);
        btnSelectImage = findViewById(R.id.btnSelectImage);
        btnSave = findViewById(R.id.btnSave);
    }

    private void setupListeners() {
        btnSelectImage.setOnClickListener(v -> openDrawableChooser());
        btnSave.setOnClickListener(v -> saveRoom());
    }

    private void openDrawableChooser() {
        final String[] names = new String[]{"Placeholder", "Launcher Foreground", "Launcher Background"};
        final int[] ids = new int[]{
                R.drawable.image_placeholder_bg,
                R.drawable.ic_launcher_foreground,
                R.drawable.ic_launcher_background
        };

        new AlertDialog.Builder(this)
                .setTitle("Chọn ảnh")
                .setItems(names, (dialog, which) -> {
                    selectedImageResId = ids[which];
                    ivRoomImage.setImageResource(selectedImageResId);
                })
                .setNegativeButton("Hủy", null)
                .show();
    }

    private void saveRoom() {
        String name = getTrimmedText(etName);
        String priceStr = getTrimmedText(etPrice);
        String status = spnStatus.getSelectedItem() != null ? spnStatus.getSelectedItem().toString() : "";
        boolean isRented = status.equals(getString(R.string.status_rented));
        String tenantName = getTrimmedText(etTenantName);
        String tenantPhone = getTrimmedText(etTenantPhone);

        if (validateData(name, priceStr, isRented, tenantName, tenantPhone)) {
            double price = Double.parseDouble(priceStr);
            String id = String.valueOf(nextId);
            Room newRoom = new Room(id, name, price, status, tenantName, tenantPhone, selectedImageResId);

            // add to RoomManager so the singleton holds data in-memory
            RoomManager.getInstance().getAllRooms().add(newRoom);

            Intent resultIntent = new Intent();
            resultIntent.putExtra("NEW_ROOM", newRoom);
            setResult(RESULT_OK, resultIntent);
            finish();
        }
    }

    private String getTrimmedText(EditText edit) {
        if (edit == null) return "";
        CharSequence cs = edit.getText();
        return cs == null ? "" : cs.toString().trim();
    }

    private boolean validateData(String name, String priceStr, boolean isRented, String tenantName, String tenantPhone) {
        if (TextUtils.isEmpty(name)) {
            showToast("Vui lòng nhập tên phòng"); return false;
        }
        if (TextUtils.isEmpty(priceStr)) {
            showToast("Vui lòng nhập giá phòng"); return false;
        }
        try {
            double p = Double.parseDouble(priceStr);
            if (p <= 0) {
                showToast("Giá phòng phải lớn hơn 0"); return false;
            }
        } catch (NumberFormatException e) {
            showToast("Giá phòng phải là số"); return false;
        }

        if (isRented) {
            if (TextUtils.isEmpty(tenantName)) {
                showToast("Vui lòng nhập tên người thuê"); return false;
            }
            if (TextUtils.isEmpty(tenantPhone)) {
                showToast("Vui lòng nhập số điện thoại"); return false;
            }
            if (tenantPhone.length() < 10 || tenantPhone.length() > 11) {
                showToast("Số điện thoại phải có 10 hoặc 11 chữ số"); return false;
            }
            if (!tenantPhone.matches("\\d+")) {
                showToast("Số điện thoại chỉ được chứa chữ số"); return false;
            }
        }
        return true;
    }

    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}
