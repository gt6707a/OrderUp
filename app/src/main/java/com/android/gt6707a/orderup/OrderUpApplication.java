package com.android.gt6707a.orderup;

import android.app.Application;
import timber.log.Timber;

public class OrderUpApplication extends Application {
  @Override
  public void onCreate() {
    super.onCreate();

    if (BuildConfig.DEBUG) {
      Timber.plant(new Timber.DebugTree());
    }
  }
}
