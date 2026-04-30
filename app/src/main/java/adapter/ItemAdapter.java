package com.example.lostandfound.adapter;

import android.content.Context;
import android.net.Uri;
import android.view.*;
import android.widget.*;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.lostandfound.R;
import com.example.lostandfound.model.LostFoundItem;
import java.util.List;

public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ViewHolder> {

    private final Context context;
    private List<LostFoundItem> items;
    private final OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(LostFoundItem item);
    }

    public ItemAdapter(Context context, List<LostFoundItem> items, OnItemClickListener listener) {
        this.context  = context;
        this.items    = items;
        this.listener = listener;
    }

    public void updateList(List<LostFoundItem> newItems) {
        this.items = newItems;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_row, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        LostFoundItem item = items.get(position);
        holder.tvDescription.setText(item.getDescription());
        holder.tvTimestamp.setText(item.getTimestamp());
        holder.tvLocation.setText("📍 " + item.getLocation());
        holder.tvCategory.setText(item.getCategory());
        holder.tvPostType.setText(item.getPostType());

        if ("Lost".equals(item.getPostType())) {
            holder.tvPostType.setBackgroundResource(R.drawable.tag_lost);
        } else {
            holder.tvPostType.setBackgroundResource(R.drawable.tag_found);
        }

        if (item.getImageUri() != null && !item.getImageUri().isEmpty()) {
            try {
                holder.ivImage.setImageURI(Uri.parse(item.getImageUri()));
            } catch (Exception e) {
                holder.ivImage.setImageResource(android.R.drawable.ic_menu_gallery);
            }
        } else {
            holder.ivImage.setImageResource(android.R.drawable.ic_menu_gallery);
        }

        holder.itemView.setOnClickListener(v -> listener.onItemClick(item));
    }

    @Override
    public int getItemCount() { return items != null ? items.size() : 0; }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvPostType, tvDescription, tvLocation, tvTimestamp, tvCategory;
        ImageView ivImage;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvPostType    = itemView.findViewById(R.id.tvPostType);
            tvDescription = itemView.findViewById(R.id.tvDescription);
            tvLocation    = itemView.findViewById(R.id.tvLocation);
            tvTimestamp   = itemView.findViewById(R.id.tvTimestamp);
            tvCategory    = itemView.findViewById(R.id.tvCategory);
            ivImage       = itemView.findViewById(R.id.ivItemImage);
        }
    }
}