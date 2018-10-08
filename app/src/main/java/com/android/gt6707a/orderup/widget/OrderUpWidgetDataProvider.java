package com.android.gt6707a.orderup.widget;

import com.android.gt6707a.orderup.OrderUpFirebaseMessagingService;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import timber.log.Timber;

public class OrderUpWidgetDataProvider {
  private FirebaseFirestore ordersFirestore;

  public void init(EventListener<QuerySnapshot> querySnapshotEventListener) {
    Timber.d("token: " + OrderUpFirebaseMessagingService.token);

    FirebaseFirestore.getInstance()
        .collection("orders")
        .whereEqualTo("token", OrderUpFirebaseMessagingService.token)
        .addSnapshotListener(querySnapshotEventListener);
  }
}
