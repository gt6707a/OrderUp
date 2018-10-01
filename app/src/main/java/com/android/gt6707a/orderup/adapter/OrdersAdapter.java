package com.android.gt6707a.orderup.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.gt6707a.orderup.R;
import com.android.gt6707a.orderup.entity.OrderItem;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.Query;

import butterknife.BindView;
import butterknife.ButterKnife;

public class OrdersAdapter extends FirestoreAdapter<OrdersAdapter.ViewHolder> {

  public OrdersAdapter(Query query) {
    super(query);
    // mListener = listener;
  }

  @NonNull
  @Override
  public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    LayoutInflater inflater = LayoutInflater.from(parent.getContext());
    return new ViewHolder(inflater.inflate(R.layout.order_item_layout, parent, false));
  }

  @Override
  public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
    holder.bind(getSnapshot(position));
  }

  static class ViewHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.order_item_text_view)
    TextView orderItemTextView;

    @BindView(R.id.order_item_customer_text_view)
    TextView orderItemCustomerTextView;

    public ViewHolder(View itemView) {
      super(itemView);
      ButterKnife.bind(this, itemView);
    }

    public void bind(final DocumentSnapshot snapshot) {

      OrderItem orderItem = snapshot.toObject(OrderItem.class);

      orderItemTextView.setText(orderItem.getItem());
      orderItemCustomerTextView.setText(orderItem.getCustomer());

      // Click listener
      //            itemView.setOnClickListener(new View.OnClickListener() {
      //                @Override
      //                public void onClick(View view) {
      //                    if (listener != null) {
      //                        listener.onRestaurantSelected(snapshot);
      //                    }
      //                }
      //            });
    }
  }
}
