package com.android.gt6707a.orderup;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MenuActivity extends AppCompatActivity {

  @BindView(R.id.menu_recycler_view)
  RecyclerView menuRecyclerView;
  MenuAdapter menuAdapter;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_menu);

    ButterKnife.bind(this);

    menuRecyclerView.setLayoutManager(new LinearLayoutManager(this));
    menuAdapter = new MenuAdapter(this);
    menuRecyclerView.setAdapter(menuAdapter);

    MenuViewModel menuViewModel = ViewModelProviders.of(this).get(MenuViewModel.class);
    menuViewModel.getMenuItems().observe(this, new Observer<List<MenuItem>>() {
        @Override
        public void onChanged(@Nullable List<MenuItem> menuItems) {
            menuAdapter.setMenuItems(menuItems);
        }
    });
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    MenuInflater inflater = getMenuInflater();
    inflater.inflate(R.menu.order_up_menu, menu);
    return true;
  }

  @Override
  public boolean onOptionsItemSelected(android.view.MenuItem item) {
    int id = item.getItemId();
    if (id == R.id.actions_settings) {
      Intent intent = new Intent(this, SettingsActivity.class);
      startActivity(intent);
      return true;
    }

    return super.onOptionsItemSelected(item);
  }
}
