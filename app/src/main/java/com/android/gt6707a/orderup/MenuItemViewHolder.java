package com.android.gt6707a.orderup;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MenuItemViewHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.menu_item_name_text_view)
    public TextView menuItemNameTextView;

    public MenuItemViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(itemView);
    }
}
