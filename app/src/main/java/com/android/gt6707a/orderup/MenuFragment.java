package com.android.gt6707a.orderup;


import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.gt6707a.orderup.adapter.MenuAdapter;
import com.android.gt6707a.orderup.entity.MenuItem;
import com.android.gt6707a.orderup.viewModel.MenuViewModel;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * A simple {@link Fragment} subclass.
 */
public class MenuFragment extends Fragment {

    @BindView(R.id.menu_recycler_view)
    RecyclerView menuRecyclerView;
    MenuAdapter menuAdapter;

    public MenuFragment() {
        // Required empty public constructor
    }

    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */
    public static MenuFragment newInstance() {
        MenuFragment fragment = new MenuFragment();
        Bundle args = new Bundle();
//        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
//        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_menu, container, false);

        ButterKnife.bind(this, view);

        menuRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        menuAdapter = new MenuAdapter(getContext());
        menuRecyclerView.setAdapter(menuAdapter);

        MenuViewModel menuViewModel = ViewModelProviders.of(this).get(MenuViewModel.class);
        menuViewModel.getMenuItems().observe(this, new Observer<List<MenuItem>>() {
            @Override
            public void onChanged(@Nullable List<MenuItem> menuItems) {
                menuAdapter.setMenuItems(menuItems);
            }
        });

        return view;
    }

}
