package com.pad1.notificationapps;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    private NotificationManager mNotif;
    private final static String CHANNEL_ID = "primary-channel";
    private int NOTIFICATION_ID = 0;
    Button btn1,btn2,btn3;

    private NotificationReceiver mReceiver = new NotificationReceiver();
    private final static String ACTION_UPDATE_NOTIFICATION = "ACTION_UPDATE_NOTIFICATION";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btn1 = findViewById(R.id.btn1);

        mNotif = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            NotificationChannel mNotifChannel = new NotificationChannel(CHANNEL_ID, "app notif", NotificationManager.IMPORTANCE_HIGH);
            mNotif.createNotificationChannel(mNotifChannel);
        }

        findViewById(R.id.btn1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendNotification();
            }
        });

        findViewById(R.id.btn2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateNotification();
            }
        });

        findViewById(R.id.btn3).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cancelNotification();
            }
        });

        registerReceiver(mReceiver, new IntentFilter(ACTION_UPDATE_NOTIFICATION));

    }

    private NotificationCompat.Builder getNotificationBuiler(){

        Intent intent = new Intent(this, MainActivity2.class);
        PendingIntent notifPendingIntent = PendingIntent.getActivity(this, NOTIFICATION_ID, intent , PendingIntent.FLAG_IMMUTABLE);
        NotificationCompat.Builder notifyBuilder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle("NOTIFIKASI UNTUK TIDUR")
                .setContentText("KAMU HARUS TIDUR SEKARANG JUGA!!!")
                .setSmallIcon(R.drawable.ic_baseline_android_24)
                .setContentIntent(notifPendingIntent);
        ;
        return notifyBuilder;


    }

    private void sendNotification(){
        Intent updateIntent = new Intent(ACTION_UPDATE_NOTIFICATION);
        PendingIntent updatePendingIntent = PendingIntent.getBroadcast(this, NOTIFICATION_ID, updateIntent, PendingIntent.FLAG_IMMUTABLE);

        NotificationCompat.Builder notifyBuilder = getNotificationBuiler();
        notifyBuilder.addAction(R.drawable.ic_baseline_android_24, "Update Notification", updatePendingIntent);

        mNotif.notify(NOTIFICATION_ID ,notifyBuilder.build());
    }

    public void updateNotification() {

        Bitmap androidImage = BitmapFactory
                .decodeResource(getResources(), R.drawable.wisnuturuawikwok);


        NotificationCompat.Builder notifyBuilder = getNotificationBuiler();


        notifyBuilder.setStyle(new NotificationCompat.BigPictureStyle()
                .bigPicture(androidImage)
                .setBigContentTitle("HP KAMU AKAN APA HAYO"));


        mNotif.notify(NOTIFICATION_ID, notifyBuilder.build());


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(mReceiver);
    }

    public void cancelNotification() {
        // Cancel the notification.
        mNotif.cancel(NOTIFICATION_ID);

    }

    public class NotificationReceiver extends BroadcastReceiver {

        public NotificationReceiver() {

        }
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if(action.equals(ACTION_UPDATE_NOTIFICATION)){
                updateNotification();
            }

        }
    }

}