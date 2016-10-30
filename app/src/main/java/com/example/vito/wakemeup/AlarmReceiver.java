package com.example.vito.wakemeup;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.support.v4.content.WakefulBroadcastReceiver;

/**
 * Created by jeanTindel on 29/10/2016.
 */

public class AlarmReceiver extends WakefulBroadcastReceiver {
    @Override
    public void onReceive(final Context context, Intent intent) {
        //this will update the UI with message
       // MainActivity inst = MainActivity.instance();
        // apelle la fonction qui annonce la meteo et les news
        //inst.speakOut();
        MainActivity.getWakeUpTextView().setText("REVEILLES TOI !!!!!");
        //MainActivity.speakOut();
        //this will send a notification message
       /* ComponentName comp = new ComponentName(context.getPackageName(),
                AlarmService.class.getName());
        startWakefulService(context, (intent.setComponent(comp)));
        setResultCode(Activity.RESULT_OK);*/
    }
}
