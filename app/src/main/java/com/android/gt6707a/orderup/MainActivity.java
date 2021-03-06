package com.android.gt6707a.orderup;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

import butterknife.BindView;
import butterknife.ButterKnife;
import timber.log.Timber;

public class MainActivity extends AppCompatActivity {

  @BindView(R.id.main_view_pager)
  ViewPager viewPager;

  @BindView(R.id.main_tabs)
  TabLayout tabLayout;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    ButterKnife.bind(this);

    /*
     The {@link android.support.v4.view.PagerAdapter} that will provide fragments for each of the
     sections. We use a {@link FragmentPagerAdapter} derivative, which will keep every loaded
     fragment in memory. If this becomes too memory intensive, it may be best to switch to a {@link
    * android.support.v4.app.FragmentStatePagerAdapter}.
    */
    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide fragments for each of the
     * sections. We use a {@link FragmentPagerAdapter} derivative, which will keep every loaded
     * fragment in memory. If this becomes too memory intensive, it may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    SectionsPagerAdapter mSectionsPagerAdapter =
        new SectionsPagerAdapter(getSupportFragmentManager());
    viewPager.setAdapter(mSectionsPagerAdapter);

    tabLayout.setupWithViewPager(viewPager);

    getDeviceToken();
  }

  /**
   * A {@link FragmentPagerAdapter} that returns a fragment corresponding to one of the
   * sections/tabs/pages.
   */
  class SectionsPagerAdapter extends FragmentPagerAdapter {

    SectionsPagerAdapter(FragmentManager fm) {
      super(fm);
    }

    @Override
    public Fragment getItem(int position) {
      switch (position) {
        case 0:
          return MenuFragment.newInstance();
        case 1:
          return new OrdersFragment();
        default:
          return null;
      }
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
      switch (position) {
        case 0:
          return getString(R.string.main_tabs_menu);
        case 1:
          return getString(R.string.main_tabs_orders);
        default:
          return "";
      }
    }

    @Override
    public int getCount() {
      return 2;
    }
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

  public void navigateToTab(int tabIndex) {
    viewPager.setCurrentItem(tabIndex);
  }

  private void getDeviceToken() {
    FirebaseInstanceId.getInstance()
        .getInstanceId()
        .addOnSuccessListener(
            new OnSuccessListener<InstanceIdResult>() {
              @Override
              public void onSuccess(InstanceIdResult instanceIdResult) {
                String newToken = instanceIdResult.getToken();
                Timber.d("new token %s", newToken);
                getSharedPreferences("settings", Context.MODE_PRIVATE)
                    .edit()
                    .putString("token", newToken)
                    .apply();
              }
            });
  }
}
