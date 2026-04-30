package com.example.lostandfound;

import android.net.Uri;
import android.os.Bundle;
import android.widget.*;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import com.example.lostandfound.database.DatabaseHelper;
import com.example.lostandfound.model.LostFoundItem;

public class ItemDetailActivity extends AppCompatActivity {

    private DatabaseHelper dbHelper;
    private LostFoundItem item;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_detail);

        dbHelper = new DatabaseHelper(this);
        int itemId = getIntent().getIntExtra("item_id", -1);
        if (itemId == -1) { finish(); return; }
        item = dbHelper.getItemById(itemId);
        if (item == null) { finish(); return; }

        TextView tvPostType    = findViewById(R.id.tvPostType);
        TextView tvName        = findViewById(R.id.tvName);
        TextView tvPhone       = findViewById(R.id.tvPhone);
        TextView tvDescription = findViewById(R.id.tvDescription);
        TextView tvDate        = findViewById(R.id.tvDate);
        TextView tvLocation    = findViewById(R.id.tvLocation);
        TextView tvCategory    = findViewById(R.id.tvCategory);
        TextView tvTimestamp   = findViewById(R.id.tvTimestamp);
        ImageView ivImage      = findViewById(R.id.ivImage);
        Button btnRemove       = findViewById(R.id.btnRemove);

        tvPostType.setText(item.getPostType());
        tvName.setText(item.getName());
        tvPhone.setText(item.getPhone());
        tvDescription.setText(item.getDescription());
        tvDate.setText(item.getDate());
        tvLocation.setText(item.getLocation());
        tvCategory.setText(item.getCategory());
        tvTimestamp.setText("Posted: " + item.getTimestamp());

        if ("Lost".equals(item.getPostType())) {
            tvPostType.setBackgroundResource(R.drawable.tag_lost);
        } else {
            tvPostType.setBackgroundResource(R.drawable.tag_found);
        }

        if (item.getImageUri() != null && !item.getImageUri().isEmpty()) {
            try { ivImage.setImageURI(Uri.parse(item.getImageUri())); }
            catch (Exception e) { ivImage.setImageResource(android.R.drawable.ic_menu_gallery); }
        } else {
            ivImage.setImageResource(android.R.drawable.ic_menu_gallery);
        }

        btnRemove.setOnClickListener(v ->
                new AlertDialog.Builder(this)
                        .setTitle("Remove Advert")
                        .setMessage("Are you sure you want to remove this advert?")
                        .setPositiveButton("Remove", (dialog, which) -> {
                            if (dbHelper.deleteItem(item.getId())) {
                                Toast.makeText(this, "Advert removed", Toast.LENGTH_SHORT).show();
                                finish();
                            }
                        })
                        .setNegativeButton("Cancel", null)
                        .show()
        );
    }
}