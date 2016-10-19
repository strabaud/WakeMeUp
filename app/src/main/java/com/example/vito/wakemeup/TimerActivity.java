package com.example.vito.wakemeup;

import android.annotation.TargetApi;
import android.os.Build;
import android.support.annotation.MainThread;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TimePicker;
import android.view.View;
import android.content.Intent;
import android.util.Log;

public class TimerActivity extends AppCompatActivity implements View.OnClickListener {

    private TimePicker timePicker1;
    private Button OkButton;
    public int hour, min;

    @TargetApi(Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timer);

        timePicker1 = (TimePicker) findViewById(R.id.timePicker1);
        OkButton = (Button)findViewById(R.id.bt_Ok);

        if (Build.VERSION.SDK_INT >= 23){

            hour = timePicker1.getHour();
            min = timePicker1.getMinute();
        }
        else{
            hour = timePicker1.getCurrentHour();
            min = timePicker1.getCurrentMinute();
        }

         OkButton.setOnClickListener(this);
    }

    // Methode associé à l'appui de l'EditText
    public void onClick(View view)
    {

        if(R.id.bt_Ok == view.getId())
        {
            Alarms.getInstance().Create(hour, min);
            this.finish();
        }
        else
        {
            Log.e("Bouton","clic pas implémenté !");
        }
    }

}
