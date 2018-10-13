package com.android.gt6707a.orderup.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.RemoteViews;

import com.android.gt6707a.orderup.MainActivity;
import com.android.gt6707a.orderup.R;
import com.android.gt6707a.orderup.entity.OrderItem;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.annotation.Nullable;

/** Implementation of App Widget functionality. */
public class OrderUpAppWidgetProvider extends AppWidgetProvider {

  private FirebaseFirestore firestore;

  public OrderUpAppWidgetProvider() {
    super();
  }

  static void updateAppWidget(
      Context context,
      AppWidgetManager appWidgetManager,
      int appWidgetId,
      @Nullable OrderItem orderItem) {

    RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.order_up_app_widget);

    Intent mainActivityIntent = new Intent(context, MainActivity.class);
    PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, mainActivityIntent, 0);
    views.setOnClickPendingIntent(R.id.widget_message_text_view, pendingIntent);

    if (orderItem != null) {
      String status = orderItem.getStatusId() == OrderItem.WAITING ? "Waiting" : "Ready";
      String message = String.format("Your %s is %s", orderItem.getItem(), status);
      views.setTextViewText(R.id.widget_message_text_view, message);

      // add pending intent to claim
      Intent claimOrderIntent = new Intent(context, OrderUpWidgetService.class);
      claimOrderIntent.setAction(OrderUpWidgetService.ACTION_CLAIM_ORDER);
      claimOrderIntent.putExtra("orderId", orderItem.getKey());
      PendingIntent claimOrderPendingIntent =
          PendingIntent.getService(context, 0, claimOrderIntent, PendingIntent.FLAG_UPDATE_CURRENT);
      views.setOnClickPendingIntent(R.id.widget_claim_button, claimOrderPendingIntent);
      if (status.equals("Ready")) {
        views.setViewVisibility(R.id.widget_claim_button, View.VISIBLE);
      } else {
        views.setViewVisibility(R.id.widget_claim_button, View.INVISIBLE);
      }
    } else {
      views.setTextViewText(R.id.widget_message_text_view, context.getString(R.string.no_orders));
      views.setViewVisibility(R.id.widget_claim_button, View.INVISIBLE);
    }

    // Instruct the widget manager to update the widget
    appWidgetManager.updateAppWidget(appWidgetId, views);
  }

  void initFirestore(final Context context) {
    if (firestore == null) {
      String token =
          context.getSharedPreferences("settings", Context.MODE_PRIVATE).getString("token", "");
      firestore = FirebaseFirestore.getInstance();
      firestore
          .collection("orders")
          .whereEqualTo("token", token)
          .addSnapshotListener(
              new EventListener<QuerySnapshot>() {
                @Override
                public void onEvent(
                    @Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException e) {

                  OrderItem orderItem = null;
                  if (value.size() > 0) {
                    List<OrderItem> orders = new ArrayList<>();
                    for (QueryDocumentSnapshot snapshot : value) {
                      OrderItem o = snapshot.toObject(OrderItem.class);
                      o.setKey(snapshot.getId());
                      orders.add(o);
                    }

                    Collections.sort(
                        orders,
                        new Comparator<OrderItem>() {
                          @Override
                          public int compare(OrderItem o1, OrderItem o2) {
                            if (o1.getStatusId() != o2.getStatusId())
                              return Long.compare(o1.getStatusId(), o2.getStatusId());

                            return Long.compare(o1.getOrderTime(), o2.getOrderTime());
                          }
                        });
                    orderItem = orders.get(0);
                  }

                  AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
                  int[] appWidgetIds =
                      appWidgetManager.getAppWidgetIds(
                          new ComponentName(context, OrderUpAppWidgetProvider.class));

                  for (int appWidgetId : appWidgetIds) {
                    updateAppWidget(context, appWidgetManager, appWidgetId, orderItem);
                  }
                }
              });
    }
  }

  @Override
  public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
    initFirestore(context);
  }

  @Override
  public void onEnabled(Context context) {
    // Enter relevant functionality for when the first widget is created
  }

  @Override
  public void onDisabled(Context context) {
    // Enter relevant functionality for when the last widget is disabled
  }
}
