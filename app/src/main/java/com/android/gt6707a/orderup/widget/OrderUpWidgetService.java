package com.android.gt6707a.orderup.widget;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;

import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import timber.log.Timber;

public class OrderUpWidgetService extends IntentService {
  public static final String ACTION_UPDATE_WIDGETS =
      "com.android.gt6707a.orderup.action.update_widgets";

  private FirebaseFirestore ordersFirestore;

  public OrderUpWidgetService() {
    super("OrderUpWidgetService");
    init();
  }

  private void init() {
      ordersFirestore = FirebaseFirestore.getInstance();
      Query query = ordersFirestore.collection("orders").whereEqualTo("token", "my token");

      ordersFirestore
              .collection("orders")
              .whereEqualTo("token", "cqUMpSi4dJQ:APA91bELmJZrJa4NNy1GAJTKeCsCGNEYM1uFYLScLRheHl1O8mro1bWSWSVjaNKm_Wx2luoG-fO9ezcWdni0WBqm2k4MHhxbxrh6SylqrCZwEU04f8w6_0w3MDK8pvOkkoMllsoJ25nc")
              .addSnapshotListener(
                      new EventListener<QuerySnapshot>() {
                          @Override
                          public void onEvent(
                                  @javax.annotation.Nullable QuerySnapshot queryDocumentSnapshots,
                                  @javax.annotation.Nullable FirebaseFirestoreException e) {
                              Timber.d("here");

                          }
                      });
  }

  @Override
  protected void onHandleIntent(@Nullable Intent intent) {
    if (intent != null) {
      final String action = intent.getAction();
      switch (action) {
        case ACTION_UPDATE_WIDGETS:
          handleActionUpdateWidgets();
        default:
          break;
      }
    }
  }

  private void startActionUpdateWidgets() {
    Intent intent = new Intent(ACTION_UPDATE_WIDGETS);
    startService(intent);
  }

  private void handleActionUpdateWidgets() {
    ordersFirestore = FirebaseFirestore.getInstance();
    Query query = ordersFirestore.collection("orders").whereEqualTo("token", "my token");

    ordersFirestore
        .collection("orders")
        .whereEqualTo("token", "cqUMpSi4dJQ:APA91bELmJZrJa4NNy1GAJTKeCsCGNEYM1uFYLScLRheHl1O8mro1bWSWSVjaNKm_Wx2luoG-fO9ezcWdni0WBqm2k4MHhxbxrh6SylqrCZwEU04f8w6_0w3MDK8pvOkkoMllsoJ25nc")
        .addSnapshotListener(
            new EventListener<QuerySnapshot>() {
              @Override
              public void onEvent(
                  @javax.annotation.Nullable QuerySnapshot queryDocumentSnapshots,
                  @javax.annotation.Nullable FirebaseFirestoreException e) {
                Timber.d("i'm here");
              }
            });
  }
}