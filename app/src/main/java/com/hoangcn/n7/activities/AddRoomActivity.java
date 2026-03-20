package com.hoangcn.n7.activities;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.switchmaterial.SwitchMaterial;
import com.google.android.material.textfield.TextInputEditText;
import com.hoangcn.n7.R;
import com.hoangcn.n7.models.Room;

public class AddRoomActivity extends AppCompatActivity {

    private TextInputEditText etName, etPrice, etTenantName, etTenantPhone;
    private SwitchMaterial cbIsRented;
    private LinearLayout layoutTenantInfo;
    private ImageView ivRoomImage;
    private FloatingActionButton btnSelectImage;
    private Button btnSave;
    private String selectedImageUri = "";
    private int nextId = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_room);

        nextId = getIntent().getIntExtra("NEXT_ID", 1);

        initViews();
        setupListeners();
    }

    private void initViews() {
        etName = findViewById(R.id.etRoomName);
        etPrice = findViewById(R.id.etPrice);
        cbIsRented = findViewById(R.id.cbIsRented);
        layoutTenantInfo = findViewById(R.id.layoutTenantInfo);
        etTenantName = findViewById(R.id.etTenantName);
        etTenantPhone = findViewById(R.id.etTenantPhone);
        ivRoomImage = findViewById(R.id.ivRoomImage);
        btnSelectImage = findViewById(R.id.btnSelectImage);
        btnSave = findViewById(R.id.btnSave);
    }

    private void setupListeners() {
        cbIsRented.setOnCheckedChangeListener((buttonView, isChecked) -> {
            layoutTenantInfo.setVisibility(isChecked ? View.VISIBLE : View.GONE);
        });

        ActivityResultLauncher<Intent> imagePickerLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK && result.getData() != null) {
                        Uri uri = result.getData().getData();
                        if (uri != null) {
                            try {
                                getContentResolver().takePersistableUriPermission(uri, Intent.FLAG_GRANT_READ_URI_PERMISSION);
                            } catch (Exception e) {
                                // Ignore if permission already exists or cannot be taken
                            }
                            selectedImageUri = uri.toString();
                            ivRoomImage.setImageURI(uri);
                        }
                    }
                }
        );

        btnSelectImage.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
            intent.addCategory(Intent.CATEGORY_OPENABLE);
            intent.setType("image/*");
            imagePickerLauncher.launch(intent);
        });

        btnSave.setOnClickListener(v -> saveRoom());
    }

    private void saveRoom() {
        String name = etName.getText().toString().trim();
        String priceStr = etPrice.getText().toString().trim();
        boolean isRented = cbIsRented.isChecked();
        String tenantName = etTenantName.getText().toString().trim();
        String tenantPhone = etTenantPhone.getText().toString().trim();

        if (validateData(name, priceStr, isRented, tenantName, tenantPhone)) {
            double price = Double.parseDouble(priceStr);
            String id = String.valueOf(nextId);
            Room newRoom = new Room(id, name, price, isRented, tenantName, tenantPhone, selectedImageUri);
            
            Intent resultIntent = new Intent();
            resultIntent.putExtra("NEW_ROOM", newRoom);
            setResult(RESULT_OK, resultIntent);
            finish();
        }
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
