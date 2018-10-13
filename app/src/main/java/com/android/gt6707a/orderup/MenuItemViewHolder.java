package com.android.gt6707a.orderup;

import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.gt6707a.orderup.entity.MenuItem;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MenuItemViewHolder extends RecyclerView.ViewHolder {
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

  public void bindToMenuItem(MenuItem menuItem, View.OnClickListener orderItemClickListener) {
    menuItemNameTextView.setText(menuItem.getName());
    if (!menuItem.getPhoto().isEmpty()) {
      Picasso.get().load(menuItem.getPhoto()).into(menuItemImageView);
    }
    orderItemFab.setOnClickListener(orderItemClickListener);
  }
}
