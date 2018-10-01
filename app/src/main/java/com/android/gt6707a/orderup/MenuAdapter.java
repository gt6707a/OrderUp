package com.android.gt6707a.orderup;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MenuAdapter extends RecyclerView.Adapter<MenuAdapter.MenuItemViewHolder> {

    private Context context;
    private List<MenuItem> menuItems;

    public MenuAdapter(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public MenuItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context)
                .inflate(R.layout.menu_item_layout, parent, false);

        return new MenuItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MenuItemViewHolder holder, int position) {
        MenuItem item = getMenuItems().get(position);

        holder.menuItemNameTextView.setText(item.getName());
        if (!item.getPhoto().isEmpty()) {
            Picasso.get().load(item.getPhoto()).into(holder.menuItemImageView);
        }
    }

    @Override
    public int getItemCount() {
        return getMenuItems() == null ? 0 : getMenuItems().size();
    }

    public List<MenuItem> getMenuItems() {
        return menuItems;
    }

    public void setMenuItems(List<MenuItem> menuItems) {
        this.menuItems = menuItems;
        notifyDataSetChanged();
    }

    class MenuItemViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.menu_item_name_text_view)
        public TextView menuItemNameTextView;

        @BindView(R.id.menu_item_image_view)
        public ImageView menuItemImageView;

        public MenuItemViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
