package com.android.gt6707a.orderup;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.preference.PreferenceManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.gt6707a.orderup.entity.MenuItem;
import com.android.gt6707a.orderup.entity.OrderItem;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import timber.log.Timber;

/** A simple {@link Fragment} subclass. */
public class MenuFragment extends Fragment {

  @BindView(R.id.menu_recycler_view)
  RecyclerView menuRecyclerView;

  private DatabaseReference db;
  private FirebaseRecyclerAdapter<MenuItem, MenuItemViewHolder> menuAdapter;

  public MenuFragment() {
    // Required empty public constructor
  }

  /** Returns a new instance of this fragment for the given section number. */
  public static MenuFragment newInstance() {
    return new MenuFragment();
  }

  @Override
  public View onCreateView(
      LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    // Inflate the layout for this fragment
    View view = inflater.inflate(R.layout.fragment_menu, container, false);

    ButterKnife.bind(this, view);

    db = FirebaseDatabase.getInstance().getReference();

    return view;
  }

  @Override
  public void onActivityCreated(@Nullable Bundle savedInstanceState) {
    super.onActivityCreated(savedInstanceState);

    menuRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

    Query menuQuery = db.child("menuItems");
    FirebaseRecyclerOptions options =
        new FirebaseRecyclerOptions.Builder<MenuItem>().setQuery(menuQuery, MenuItem.class).build();

    menuAdapter =
        new FirebaseRecyclerAdapter<MenuItem, MenuItemViewHolder>(options) {
          @Override
          public MenuItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            return new MenuItemViewHolder(
                inflater.inflate(R.layout.menu_item_layout, parent, false));
          }

          @Override
          protected void onBindViewHolder(
              @NonNull MenuItemViewHolder viewHolder, int position, final MenuItem model) {

            // Bind Post to ViewHolder, setting OnClickListener for the star button
            viewHolder.bindToMenuItem(
                model,
                new View.OnClickListener() {
                  @Override
                  public void onClick(View view) {
                    Timber.d("Order item %s", model.getName());
                    onOrderItemClicked(model);
                  }
                });
          }
        };

    menuRecyclerView.setAdapter(menuAdapter);
  }

  private void onOrderItemClicked(MenuItem item) {
    OrderItem order = new OrderItem();
    order.setItem(item.getName());
    order.setOrderTime(new Timestamp(System.currentTimeMillis()).getTime());

    String customer =
        PreferenceManager.getDefaultSharedPreferences(getActivity())
            .getString(getString(R.string.settings_user_name_key), "deviceId");
    order.setCustomer(customer);

    order.setStatusId(OrderItem.WAITING);
    String token =
        getActivity().getSharedPreferences("settings", Context.MODE_PRIVATE).getString("token", "");
    order.setToken(token);

    String key = db.child("orders").push().getKey();
    Map<String, Object> orderItemValues = order.toMap();

    Map<String, Object> updates = new HashMap<>();
    updates.put("/orders/" + key, orderItemValues);

    db.updateChildren(updates);

    ((MainActivity) getActivity()).navigateToTab(1);
  }

  @Override
  public void onStart() {
    super.onStart();
    if (menuAdapter != null) {
      menuAdapter.startListening();
    }
  }

  @Override
  public void onStop() {
    super.onStop();
    if (menuAdapter != null) {
      menuAdapter.stopListening();
    }
  }
}
