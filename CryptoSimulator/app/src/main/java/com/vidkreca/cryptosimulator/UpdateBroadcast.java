package com.vidkreca.cryptosimulator;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

public class UpdateBroadcast extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        String symbol = intent.getExtras().getString("symbol");
        symbol = (symbol != null) ? symbol : "UNKNOWN";

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "CryptoSimulator")
                .setSmallIcon(R.drawable.ic_portfolio)
                .setContentTitle("Check how your recent " + symbol + " trade is doing")
                .setContentText("Did you time the market correctly?")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        PendingIntent contentIntent = PendingIntent.getActivity(context, 0, new Intent(context, MainActivity.class), PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(contentIntent);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);

        notificationManager.notify(200, builder.build());
    }
}
