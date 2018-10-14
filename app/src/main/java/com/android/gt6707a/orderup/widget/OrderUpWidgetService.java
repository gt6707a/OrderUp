package com.android.gt6707a.orderup.widget;

import android.app.IntentService;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import timber.log.Timber;

public class OrderUpWidgetService extends IntentService {
  public static final String ACTION_CLAIM_ORDER = "com.android.gt6707a.orderup.action.claim_order";

  private DatabaseReference db;

  public OrderUpWidgetService() {
    super("OrderUpWidgetService");
    db = FirebaseDatabase.getInstance().getReference();
  }

  @Override
  protected void onHandleIntent(@Nullable Intent intent) {
    if (intent != null) {
      final String action = intent.getAction();
      switch (action) {
        case ACTION_CLAIM_ORDER:
          handleActionClaimOrder(intent);
        default:
          break;
      }
    }
  }

  private void handleActionClaimOrder(Intent intent) {
    db.child("/orders/" + intent.getStringExtra("orderId"))
        .removeValue()
        .addOnSuccessListener(
            new OnSuccessListener<Void>() {
              @Override
              public void onSuccess(Void aVoid) {
                Timber.d("order claimed");
              }
            })
        .addOnFailureListener(
            new OnFailureListener() {
              @Override
              public void onFailure(@NonNull Exception e) {
                Timber.d("Failed to claim order");
              }
            });
  }
}
