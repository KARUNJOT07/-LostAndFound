package com.example.lostandfound;

import android.Manifest;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.widget.*;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import com.example.lostandfound.database.DatabaseHelper;
import com.example.lostandfound.model.LostFoundItem;
import java.text.SimpleDateFormat;
import java.util.*;

public class CreateAdvertActivity extends AppCompatActivity {

    private EditText etName, etPhone, etDescription, etDate, etLocation;
    private RadioButton rbLost, rbFound;
    private Spinner spCategory;
    private ImageView ivPreview;
    private String selectedImageUri = "";
    private DatabaseHelper dbHelper;

    private final ActivityResultLauncher<Intent> imagePickerLauncher =
            registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
                if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                    Uri uri = result.getData().getData();
                    if (uri != null) {
                        getContentResolver().takePersistableUriPermission(
                                uri, Intent.FLAG_GRANT_READ_URI_PERMISSION);
                        selectedImageUri = uri.toString();
                        ivPreview.setImageURI(uri);
                    }
                }
            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_advert);

        dbHelper      = new DatabaseHelper(this);
        etName        = findViewById(R.id.etName);
        etPhone       = findViewById(R.id.etPhone);
        etDescription = findViewById(R.id.etDescription);
        etDate        = findViewById(R.id.etDate);
        etLocation    = findViewById(R.id.etLocation);
        rbLost        = findViewById(R.id.rbLost);
        rbFound       = findViewById(R.id.rbFound);
        spCategory    = findViewById(R.id.spCategory);
        ivPreview     = findViewById(R.id.ivImagePreview);

        ArrayAdapter<CharSequence> catAdapter = ArrayAdapter.createFromResource(
                this, R.array.categories, android.R.layout.simple_spinner_item);
        catAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spCategory.setAdapter(catAdapter);

        etDate.setFocusable(false);
        etDate.setOnClickListener(v -> showDatePicker());
        findViewById(R.id.btnPickImage).setOnClickListener(v -> pickImage());
        findViewById(R.id.btnSave).setOnClickListener(v -> saveItem());
    }

    private void showDatePicker() {
        Calendar cal = Calendar.getInstance();
        new DatePickerDialog(this,
                (view, year, month, day) ->
                        etDate.setText(String.format(Locale.getDefault(),
                                "%02d/%02d/%04d", day, month + 1, year)),
                cal.get(Calendar.YEAR), cal.get(Calendar.MONTH),
                cal.get(Calendar.DAY_OF_MONTH)).show();
    }

    private void pickImage() {
        String permission = Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU
                ? Manifest.permission.READ_MEDIA_IMAGES
                : Manifest.permission.READ_EXTERNAL_STORAGE;
        if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{permission}, 100);
        } else { launchPicker(); }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] results) {
        super.onRequestPermissionsResult(requestCode, permissions, results);
        if (requestCode == 100 && results.length > 0 && results[0] == PackageManager.PERMISSION_GRANTED) {
            launchPicker();
        } else {
            Toast.makeText(this, "Permission required to pick an image", Toast.LENGTH_SHORT).show();
        }
    }

    private void launchPicker() {
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.setType("image/*");
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_PERSISTABLE_URI_PERMISSION);
        imagePickerLauncher.launch(intent);
    }

    private void saveItem() {
        String name        = etName.getText().toString().trim();
        String phone       = etPhone.getText().toString().trim();
        String description = etDescription.getText().toString().trim();
        String date        = etDate.getText().toString().trim();
        String location    = etLocation.getText().toString().trim();
        String postType    = rbLost.isChecked() ? "Lost" : "Found";
        String category    = spCategory.getSelectedItem().toString();

        if (name.isEmpty() || phone.isEmpty() || description.isEmpty()
                || date.isEmpty() || location.isEmpty()) {
            Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
            return;
        }
        if (selectedImageUri.isEmpty()) {
            Toast.makeText(this, "Please upload an image", Toast.LENGTH_SHORT).show();
            return;
        }

        String timestamp = new SimpleDateFormat("dd MMM yyyy, hh:mm a", Locale.getDefault())
                .format(new Date());
        LostFoundItem item = new LostFoundItem(postType, name, phone, description,
                date, location, category, selectedImageUri, timestamp);

        long result = dbHelper.insertItem(item);
        if (result != -1) {
            Toast.makeText(this, "Advert posted!", Toast.LENGTH_SHORT).show();
            finish();
        } else {
            Toast.makeText(this, "Error saving. Try again.", Toast.LENGTH_SHORT).show();
        }
    }
}