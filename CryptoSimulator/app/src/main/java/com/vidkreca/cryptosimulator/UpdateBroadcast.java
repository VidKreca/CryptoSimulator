package com.vidkreca.cryptosimulator;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.vidkreca.data.Cryptocurrency;
import com.vidkreca.data.ProtocolMessages.Single;

public class UpdateBroadcast extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        String symbol = intent.getExtras().getString("symbol");
        symbol = (symbol != null) ? symbol : "UNKNOWN";
        final double fiat_price = intent.getExtras().getDouble("fiat_price");
        final double crypto_value = intent.getExtras().getDouble("crypto_value");

        final String title = String.format("Check how your recent purchase of %.6f%s is doing", crypto_value, symbol);

        App app = (App) context.getApplicationContext();
        app.getApi().getSingle(new VolleyCallback() {
            @Override
            public void onSuccess(String json) {
                Single response = API.gson.fromJson(json, Single.class);
                Cryptocurrency crypto = response.GetCryptocurrency();

                /*double current_fiat_value = crypto.getPrice() * crypto_value;
                double change_fiat_value = current_fiat_value / fiat_price;
                // change_fiat_value == 1.1 if we have 10% profit
                // get that 10% number
                Boolean gain = false;
                if (change_fiat_value < 1) {
                    change_fiat_value = (1 - change_fiat_value) * 100;
                } else {
                    gain = true;
                    change_fiat_value = (change_fiat_value - 1) * 100;
                }
                String description = String.format("%s %.2f", (gain) ? "up" : "down", change_fiat_value);*/

                String description = String.format("Daily change: %.2f", crypto.change_percentage);
                createNotification(context, title, description);
            }

            @Override
            public void onError(String message) {
                createNotification(context, title, "");
            }
        }, symbol);


    }


    private void createNotification(Context context, String title, String description) {
        // Build notification
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "CryptoSimulator")
                .setSmallIcon(R.drawable.ic_portfolio)
                .setContentTitle(title)
                .setContentText(description)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        PendingIntent contentIntent = PendingIntent.getActivity(context, 0, new Intent(context, MainActivity.class), PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(contentIntent);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);

        notificationManager.notify(200, builder.build());
    }
}
