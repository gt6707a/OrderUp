package com.android.gt6707a.orderup.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.gt6707a.orderup.R;
import com.android.gt6707a.orderup.entity.OrderItem;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.Query;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import timber.log.Timber;

public class OrdersAdapter extends FirestoreAdapter<OrdersAdapter.ViewHolder> {

  Context context;
  OrderHandler orderHandler;

  public interface OrderHandler {
    void onClaimingOrder(OrderItem order);
    void onReadyOrder(OrderItem order);
  }

  public OrdersAdapter(Context context, OrderHandler orderHandler, Query query) {
    super(query);
    this.context = context;
    this.orderHandler = orderHandler;
  }

  @NonNull
  @Override
  public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    LayoutInflater inflater = LayoutInflater.from(parent.getContext());
    return new ViewHolder(inflater.inflate(R.layout.order_item_layout, parent, false));
  }

  @Override
  public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
    holder.bind(getSnapshot(position), context);
  }

  @Override
  protected void onDocumentModified(DocumentChange change) {
    super.onDocumentModified(change);

    String myToken = context.getSharedPreferences("settings", Context.MODE_PRIVATE).getString("token", "");
    long statusId = change.getDocument().getLong("statusId");
    String orderToken = change.getDocument().getString("token");
    if (statusId == OrderItem.READY && orderToken.equals(myToken)) {
      Toast.makeText(context, "Your order is ready for pick up", Toast.LENGTH_SHORT).show();
    }
  }

  class ViewHolder extends RecyclerView.ViewHolder {

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

    public ViewHolder(View itemView) {
      super(itemView);
      ButterKnife.bind(this, itemView);
    }

    public void bind(final DocumentSnapshot snapshot, Context context) {

      final OrderItem orderItem = snapshot.toObject(OrderItem.class);
      orderItem.setId(snapshot.getId());

      orderItemTextView.setText(orderItem.getItem());
      orderItemCustomerTextView.setText(orderItem.getCustomer());
      orderItemStatusTextView.setText(toStatusText(orderItem.getStatusId(), context));

      String myToken = context.getSharedPreferences("settings", Context.MODE_PRIVATE).getString("token", "");

      if (orderItem.getStatusId() == OrderItem.WAITING) {
          readyButton.setVisibility(View.VISIBLE);
          claimButton.setVisibility(View.INVISIBLE);
      } else {
          readyButton.setVisibility(View.INVISIBLE);
          if (orderItem.getToken().equals(myToken)) {
            claimButton.setVisibility(View.VISIBLE);
            Timber.d("set claim visible");
          } else {
            claimButton.setVisibility(View.INVISIBLE);
          }
      }

      if (orderItem.getToken().equals(myToken)) {
        orderItemLayout.setBackgroundColor(Color.LTGRAY);
      }

      readyButton.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
          orderHandler.onReadyOrder(orderItem);
        }
      });

      claimButton.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
          orderHandler.onClaimingOrder(orderItem);
        }
      });
    }

    String toStatusText(long statusId, Context context) {
      switch ((int)statusId) {
        case OrderItem.WAITING:
          return context.getString(R.string.order_waiting);
        case OrderItem.READY:
          return context.getString(R.string.order_ready);
        default:
          return "";
      }
    }
  }
}
