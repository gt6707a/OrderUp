package com.android.gt6707a.orderup.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.gt6707a.orderup.R;
import com.android.gt6707a.orderup.entity.MenuItem;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import timber.log.Timber;

public class MenuAdapter extends RecyclerView.Adapter<MenuAdapter.MenuItemViewHolder> {

  private Context context;
  private List<MenuItem> menuItems;
  private OrderItemClickListener orderItemClickListener;

  public List<MenuItem> getMenuItems() {
    return menuItems;
  }

  public void setMenuItems(List<MenuItem> menuItems) {
    this.menuItems = menuItems;
    notifyDataSetChanged();
  }

  public interface OrderItemClickListener {
    void onOrderItemClicked(MenuItem item);
  }

  public MenuAdapter(Context context, @NonNull OrderItemClickListener orderItemClickListener) {
    this.context = context;
    this.orderItemClickListener = orderItemClickListener;
  }

  @NonNull
  @Override
  public MenuItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    View view = LayoutInflater.from(context).inflate(R.layout.menu_item_layout, parent, false);

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

  class MenuItemViewHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.menu_item_name_text_view)
    public TextView menuItemNameTextView;

    @BindView(R.id.menu_item_image_view)
    public ImageView menuItemImageView;

    @BindView(R.id.order_item_fab)
    public FloatingActionButton orderItemFab;

    public MenuItemViewHolder(View itemView) {
      super(itemView);
      ButterKnife.bind(this, itemView);
    }

    @OnClick(R.id.order_item_fab)
    public void orderItemClicked() {
      Timber.d("order item");
      MenuItem item = menuItems.get(getAdapterPosition());
      orderItemClickListener.onOrderItemClicked(item);
    }
  }
}
