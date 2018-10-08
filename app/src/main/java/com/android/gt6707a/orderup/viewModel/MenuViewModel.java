package com.android.gt6707a.orderup.viewModel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;

import com.android.gt6707a.orderup.entity.MenuItem;
import com.android.gt6707a.orderup.entity.OrderItem;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreSettings;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import timber.log.Timber;

public class MenuViewModel extends AndroidViewModel {

  private FirebaseFirestore db;

  private MutableLiveData<List<MenuItem>> menuItems;

  public LiveData<List<MenuItem>> getMenuItems() {
    return menuItems;
  }

  public MenuViewModel(@NonNull Application application) {
    super(application);

    menuItems = new MutableLiveData<>();

    db = FirebaseFirestore.getInstance();
    FirebaseFirestoreSettings settings =
        new FirebaseFirestoreSettings.Builder().setTimestampsInSnapshotsEnabled(true).build();
    db.setFirestoreSettings(settings);

    db.collection("menuItems")
        .get()
        .addOnCompleteListener(
            new OnCompleteListener<QuerySnapshot>() {
              @Override
              public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                  menuItems.setValue(new ArrayList<MenuItem>());
                  for (QueryDocumentSnapshot doc : task.getResult()) {
                    MenuItem menuItem =
                        new MenuItem(
                            doc.getString("name"),
                            doc.getString("description"),
                            doc.getString("photo"));
                    menuItems.getValue().add(menuItem);
                  }
                } else {
                  Timber.e(task.getException(), "Get menu failed");
                }
              }
            });
  }

  public void orderItem(OrderItem order) {
    CollectionReference orders = db.collection("orders");
    orders.add(order);
  }
}
