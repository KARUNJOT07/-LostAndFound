package com.example.lostandfound;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.lostandfound.adapter.ItemAdapter;
import com.example.lostandfound.database.DatabaseHelper;
import com.example.lostandfound.model.LostFoundItem;
import java.util.List;

public class ShowItemsActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ItemAdapter adapter;
    private DatabaseHelper dbHelper;
    private Spinner spFilter;
    private TextView tvEmpty;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_items);

        dbHelper     = new DatabaseHelper(this);
        recyclerView = findViewById(R.id.recyclerView);
        spFilter     = findViewById(R.id.spFilter);
        tvEmpty      = findViewById(R.id.tvEmpty);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        List<LostFoundItem> items = dbHelper.getAllItems();
        adapter = new ItemAdapter(this, items, item -> {
            Intent intent = new Intent(this, ItemDetailActivity.class);
            intent.putExtra("item_id", item.getId());
            startActivity(intent);
        });
        recyclerView.setAdapter(adapter);
        toggleEmpty(items);

        ArrayAdapter<CharSequence> filterAdapter = ArrayAdapter.createFromResource(
                this, R.array.categories_filter, android.R.layout.simple_spinner_item);
        filterAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spFilter.setAdapter(filterAdapter);

        spFilter.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                String selected = parent.getItemAtPosition(pos).toString();
                List<LostFoundItem> filtered = selected.equals("All")
                        ? dbHelper.getAllItems()
                        : dbHelper.getItemsByCategory(selected);
                adapter.updateList(filtered);
                toggleEmpty(filtered);
            }
            @Override public void onNothingSelected(AdapterView<?> parent) {}
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        String selected = spFilter.getSelectedItem() != null
                ? spFilter.getSelectedItem().toString() : "All";
        List<LostFoundItem> refreshed = selected.equals("All")
                ? dbHelper.getAllItems()
                : dbHelper.getItemsByCategory(selected);
        adapter.updateList(refreshed);
        toggleEmpty(refreshed);
    }

    private void toggleEmpty(List<LostFoundItem> list) {
        boolean empty = list == null || list.isEmpty();
        tvEmpty.setVisibility(empty ? View.VISIBLE : View.GONE);
        recyclerView.setVisibility(empty ? View.GONE : View.VISIBLE);
    }
}