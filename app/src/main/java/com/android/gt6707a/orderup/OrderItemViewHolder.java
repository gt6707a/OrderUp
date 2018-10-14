package com.android.gt6707a.orderup;

import android.content.Context;
import android.graphics.Color;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.android.gt6707a.orderup.entity.OrderItem;

import butterknife.BindView;
import butterknife.ButterKnife;

public class OrderItemViewHolder extends RecyclerView.ViewHolder {

  public interface OrderHandler {
    void onClaimingOrder(OrderItem order);

    void onReadyOrder(OrderItem order);
  }

  @BindView(R.id.order_item_layout)
  ConstraintLayout orderItemLayout;

  @BindView(R.id.order_item_text_view)
  TextView orderItemTextView;

  @BindView(R.id.order_item_customer_text_view)
  TextView orderItemCustomerTextView;

  @BindView(R.id.order_item_status_text_view)
  TextView orderItemStatusTextView;

  @BindView(R.id.ready_button)
  Button readyButton;

  @BindView(R.id.claim_button)
  Button claimButton;

  public OrderItemViewHolder(View itemView) {
    super(itemView);
    ButterKnife.bind(this, itemView);
  }

  public void bindToOrderItem(
      final OrderItem orderItem, Context context, final OrderHandler orderHandler) {
    orderItemTextView.setText(orderItem.getItem());
    orderItemCustomerTextView.setText(orderItem.getCustomer());
    orderItemStatusTextView.setText(toStatusText(orderItem.getStatusId(), context));

    String myToken =
        context.getSharedPreferences("settings", Context.MODE_PRIVATE).getString("token", "");

    if (orderItem.getStatusId() == OrderItem.WAITING) {
      readyButton.setVisibility(View.VISIBLE);
      claimButton.setVisibility(View.INVISIBLE);
    } else {
      readyButton.setVisibility(View.INVISIBLE);
      claimButton.setVisibility(
          orderItem.getToken().equals(myToken) ? View.VISIBLE : View.INVISIBLE);
    }

    if (orderItem.getToken().equals(myToken)) {
      orderItemLayout.setBackgroundColor(Color.LTGRAY);
    }

    readyButton.setOnClickListener(
        new View.OnClickListener() {
          @Override
          public void onClick(View v) {
            orderHandler.onReadyOrder(orderItem);
          }
        });

    claimButton.setOnClickListener(
        new View.OnClickListener() {
          @Override
          public void onClick(View v) {
            orderHandler.onClaimingOrder(orderItem);
          }
        });
  }

  private String toStatusText(long statusId, Context context) {
    switch ((int) statusId) {
      case OrderItem.WAITING:
        return context.getString(R.string.order_waiting);
      case OrderItem.READY:
        return context.getString(R.string.order_ready);
      default:
        return "";
    }
  }
}
