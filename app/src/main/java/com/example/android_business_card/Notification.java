package com.example.android_business_card;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Build;
import android.support.v4.app.NotificationCompat;

public class Notification {
    public static void send(Context context, String strTitle, String strContent){

        String channelId = "CHANNEL_ID";
        String name = "CHANNEL_NAME";

        int importance = NotificationManager.IMPORTANCE_HIGH;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationManager notificationManager = (NotificationManager) context.getSystemService( Context.NOTIFICATION_SERVICE );


            String description ="Logon error";



            NotificationChannel notificationChannel = new NotificationChannel( channelId, name, importance );

            notificationChannel.setDescription( description );

            notificationManager.createNotificationChannel( notificationChannel );

            NotificationCompat.Builder notification = new NotificationCompat.Builder(

                    context, channelId ).setPriority( 4 ).setContentTitle(strTitle )
                    .setContentText(strContent  ).setColor( 3 )
                    .setSmallIcon( android.R.drawable.stat_notify_error ).setDefaults( 1 );

            notificationManager.notify( 1, notification.build() );
        }
    }
}
