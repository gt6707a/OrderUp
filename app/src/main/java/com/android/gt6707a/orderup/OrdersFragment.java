package com.android.gt6707a.orderup;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.gt6707a.orderup.entity.OrderItem;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import timber.log.Timber;

/** A simple {@link Fragment} subclass. */
public class OrdersFragment extends Fragment implements OrderItemViewHolder.OrderHandler {

  private DatabaseReference db;
  private FirebaseRecyclerAdapter<OrderItem, OrderItemViewHolder> ordersAdapter;

  @BindView(R.id.orders_recycler_view)
  RecyclerView ordersRecyclerView;

  public OrdersFragment() {
    // Required empty public constructor
  }

  @Override
  public View onCreateView(
      LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    // Inflate the layout for this fragment
    View view = inflater.inflate(R.layout.fragment_orders, container, false);
    ButterKnife.bind(this, view);

    db = FirebaseDatabase.getInstance().getReference();

    return view;
  }

  @Override
  public void onActivityCreated(@Nullable Bundle savedInstanceState) {
    super.onActivityCreated(savedInstanceState);

    ordersRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
    FirebaseRecyclerOptions options =
        new FirebaseRecyclerOptions.Builder<OrderItem>()
            .setQuery(db.child("orders"), OrderItem.class)
            .build();

    ordersAdapter =
        new FirebaseRecyclerAdapter<OrderItem, OrderItemViewHolder>(options) {
          @NonNull
          @Override
          public OrderItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            return new OrderItemViewHolder(
                inflater.inflate(R.layout.order_item_layout, parent, false));
          }

          @Override
          protected void onBindViewHolder(
              @NonNull OrderItemViewHolder holder,
              int position,
              @NonNull final OrderItem orderItem) {

            holder.bindToOrderItem(orderItem, getContext(), OrdersFragment.this);
          }

          @NonNull
          @Override
          public OrderItem getItem(int position) {
            DataSnapshot snapshot = getSnapshots().getSnapshot(position);
            OrderItem orderItem = snapshot.getValue(OrderItem.class);
            orderItem.setKey(snapshot.getKey());
            return orderItem;
          }
        };

    ordersRecyclerView.setAdapter(ordersAdapter);
  }

  @Override
  public void onStart() {
    super.onStart();
    if (ordersAdapter != null) {
      ordersAdapter.startListening();
    }
  }

  @Override
  public void onStop() {
    super.onStop();
    if (ordersAdapter != null) {
      ordersAdapter.stopListening();
    }
  }

  public void onClaimingOrder(OrderItem order) {
    db.child("/orders/" + order.getKey())
        .removeValue()
        .addOnSuccessListener(
            new OnSuccessListener<Void>() {
              @Override
              public void onSuccess(Void aVoid) {
                Timber.d("order claimed");
              }
            })
        .addOnFailureListener(
            new OnFailureListener() {
              @Override
              public void onFailure(@NonNull Exception e) {
                Timber.d("Failed to claim order");
              }
            });
  }

  public void onReadyOrder(OrderItem order) {
    Map<String, Object> updates = new HashMap<>();
    updates.put("/orders/" + order.getKey() + "/statusId", OrderItem.READY);

    db.updateChildren(updates)
        .addOnSuccessListener(
            new OnSuccessListener<Void>() {
              @Override
              public void onSuccess(Void aVoid) {
                Timber.d("order marked ready");
              }
            })
        .addOnFailureListener(
            new OnFailureListener() {
              @Override
              public void onFailure(@NonNull Exception e) {
                Timber.d("Failed to update order");
              }
            });
    ;
  }
}
