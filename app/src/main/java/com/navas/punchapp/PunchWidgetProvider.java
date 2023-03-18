package com.navas.punchapp;

import android.appwidget.AppWidgetProvider;
import android.appwidget.AppWidgetManager;
import android.widget.RemoteViews;
import android.os.Bundle;
import android.app.Activity;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.ComponentName;
import android.content.SharedPreferences;
import android.widget.Button;

// Retrofit Package instance
import retrofit2.Retrofit;
import retrofit2.Response;
import retrofit2.Callback;
import retrofit2.http.GET;
import retrofit2.http.Query;
import retrofit2.http.Path;
import retrofit2.Call;

import retrofit2.converter.gson.GsonConverterFactory;

// Api Serialization Class
import com.google.gson.annotations.SerializedName;

import java.text.SimpleDateFormat;
import java.util.Date;

public class PunchWidgetProvider extends AppWidgetProvider {

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        PunchApplication PunchApp = ((PunchApplication) context.getApplicationContext());
        String WIDGET_ACTION = PunchApp.WIDGET_ACTION;
        for (int appWidgetId : appWidgetIds) {

            RemoteViews buttonView = new RemoteViews(context.getPackageName(), R.layout.punch_layout);

            Intent intent = new Intent(context, PunchWidgetProvider.class);
            intent.setAction(WIDGET_ACTION);

            PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, intent, 0);

            buttonView.setOnClickPendingIntent(
                    R.id.widget_punch, pendingIntent);

            // Set the text of button based on the current check in/out status
            if (PunchApp.isCheckedIn) {
                buttonView.setTextViewText(R.id.widget_punch, "Check Out");
            } else {
                buttonView.setTextViewText(R.id.widget_punch, "Check In");
            }

            appWidgetManager.updateAppWidget(appWidgetId, buttonView);
        }
    }

    // Widget receive

    @Override
    public void onReceive(Context context, Intent intent) {

        super.onReceive(context, intent);
        PunchApplication PunchApp = ((PunchApplication) context.getApplicationContext());
        if (intent.getAction().equals(PunchApp.WIDGET_ACTION)) {
            // Create a SimpleDateFormat object with the desired format
            SimpleDateFormat timeFormat = new SimpleDateFormat("hh:mm");

            // Get the current time as a Date object
            Date currentTime = new Date();
            // Format the current time using the SimpleDateFormat object
            String formattedTime = timeFormat.format(currentTime);
            // Do something when the button is clicked

            // Initialize the Shared preferences instance
            SharedPreferences prefs = context.getSharedPreferences(PunchApp.PREFS_NAME, Context.MODE_PRIVATE);

            String currentUser = prefs.getString(PunchApp.USER, "");
            ApiClient apiService = new ApiClient();
            apiService.getUser("TIME=" + formattedTime.toString(), currentUser).enqueue(
                    new Callback<ApiClient.AppScriptResponse>() {
                        @Override
                        public void onResponse(Call<ApiClient.AppScriptResponse> call,
                                Response<ApiClient.AppScriptResponse> response) {
                            if (response.isSuccessful()) {
                                // Do something with the response
                                ApiClient.AppScriptResponse user = response.body();
                                String status = user.getStatus();

                                System.out.println(status);
                            } else {
                                // Handle error
                                System.out.println("Bad Response");
                            }
                        }

                        @Override
                        public void onFailure(Call<ApiClient.AppScriptResponse> call, Throwable t) {
                            // Handle error
                            System.out.println("Someting Went Wrong");
                        }
                    });

            // Load the last isCheckedIn value from SharedPreferences
            PunchApp.isCheckedIn = !prefs.getBoolean(PunchApp.KEY_IS_CHECKED_IN, false);

            // Update the SharedPreferences with the new isCheckedIn value
            SharedPreferences.Editor editor = prefs.edit();
            editor.putBoolean(PunchApp.KEY_IS_CHECKED_IN, PunchApp.isCheckedIn);
            editor.apply();

            // Set the text of button based on the current check in/out status
            RemoteViews buttonView = new RemoteViews(context.getPackageName(),
                    R.layout.punch_layout);
            buttonView.setTextViewText(R.id.widget_punch,
                    PunchApp.isCheckedIn ? "CHECK OUT" : "CHECK IN");

            ComponentName watchWidget = new ComponentName(context, PunchWidgetProvider.class);
            AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
            appWidgetManager.updateAppWidget(watchWidget, buttonView);

        }
    }
}