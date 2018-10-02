package com.android.gt6707a.orderup;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.gt6707a.orderup.adapter.OrdersAdapter;
import com.android.gt6707a.orderup.entity.OrderItem;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import butterknife.BindView;
import butterknife.ButterKnife;
import timber.log.Timber;

/** A simple {@link Fragment} subclass. */
public class OrdersFragment extends Fragment
    implements OrdersAdapter.OrderHandler {

  private FirebaseFirestore ordersFirestore;
  private OrdersAdapter ordersAdapter;
  private Query ordersQuery;

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

    FirebaseFirestore.setLoggingEnabled(true);

    initFirestore();
    initRecyclerView();

    return view;
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

  private void initFirestore() {
    ordersFirestore = FirebaseFirestore.getInstance();
    ordersQuery =
        ordersFirestore.collection("orders").orderBy("statusId", Query.Direction.DESCENDING);
  }

  private void initRecyclerView() {
    if (ordersQuery == null) {
      Timber.w("No query, not initializing RecyclerView");
    }

    ordersAdapter = new OrdersAdapter(getActivity(), this, ordersQuery);

    ordersRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
    ordersRecyclerView.setAdapter(ordersAdapter);
  }

    @Override
    public void onClaimingOrder(OrderItem order) {
        ordersFirestore.collection("orders").document(order.getId())
                .delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                  @Override
                  public void onSuccess(Void aVoid) {
                    Timber.d("order removed");
                  }
                })
                .addOnFailureListener(new OnFailureListener() {
                  @Override
                  public void onFailure(@NonNull Exception e) {
                    Timber.d("Failed to remove order");
                  }
                });
    }

    @Override
    public void onReadyOrder(OrderItem order) {
        ordersFirestore.collection("orders").document(order.getId())
                .update("statusId", OrderItem.READY)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Timber.d("order marked ready");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Timber.d("Failed to update order");
                    }
                });
    }
}
