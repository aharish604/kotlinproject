package com.uniimarket.app.notification.utils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import com.uniimarket.app.firebase.MyFirebaseMessagingService;

public class OnBootBroadcastReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Intent i = new Intent("com.demo.FirebaseMessagingReceiveService");
        i.setClass(context, MyFirebaseMessagingService.class);
        context.startService(i);
    }
}