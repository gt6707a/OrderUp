import android.app.Application;

import com.android.gt6707a.orderup.BuildConfig;

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
