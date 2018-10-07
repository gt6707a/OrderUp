package com.android.gt6707a.orderup;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.preference.PreferenceManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.gt6707a.orderup.adapter.MenuAdapter;
import com.android.gt6707a.orderup.entity.MenuItem;
import com.android.gt6707a.orderup.entity.OrderItem;
import com.android.gt6707a.orderup.viewModel.MenuViewModel;

import java.sql.Timestamp;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/** A simple {@link Fragment} subclass. */
public class MenuFragment extends Fragment implements MenuAdapter.OrderItemClickListener {

  @BindView(R.id.menu_recycler_view)
  RecyclerView menuRecyclerView;

  MenuAdapter menuAdapter;

  private MenuViewModel menuViewModel;

  public MenuFragment() {
    // Required empty public constructor
  }

  /** Returns a new instance of this fragment for the given section number. */
  public static MenuFragment newInstance() {
    MenuFragment fragment = new MenuFragment();
    return fragment;
  }

  @Override
  public View onCreateView(
      LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    // Inflate the layout for this fragment
    View view = inflater.inflate(R.layout.fragment_menu, container, false);

    ButterKnife.bind(this, view);

    menuRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
    menuAdapter = new MenuAdapter(getContext(), this);
    menuRecyclerView.setAdapter(menuAdapter);

    menuViewModel = ViewModelProviders.of(this).get(MenuViewModel.class);
    menuViewModel
        .getMenuItems()
        .observe(
            this,
            new Observer<List<MenuItem>>() {
              @Override
              public void onChanged(@Nullable List<MenuItem> menuItems) {
                menuAdapter.setMenuItems(menuItems);
              }
            });

    return view;
  }

  @Override
  public void onOrderItemClicked(MenuItem item) {
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

    menuViewModel.orderItem(order);

    ((MainActivity) getActivity()).navigateToTab(1);
  }
}
