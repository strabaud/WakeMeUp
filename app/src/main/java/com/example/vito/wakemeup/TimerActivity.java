package com.example.vito.wakemeup;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TimePicker;
import android.view.View;
import android.util.Log;

public class TimerActivity extends AppCompatActivity implements View.OnClickListener {

    private TimePicker timePicker1;
    private Button okButton;
    public int hour, min;
    public Intent intentResult;

    @TargetApi(Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timer);


        timePicker1 = (TimePicker) findViewById(R.id.timePicker1);
        okButton = (Button)findViewById(R.id.bt_Ok);

        if (Build.VERSION.SDK_INT >= 23){

            hour = timePicker1.getHour();
            min = timePicker1.getMinute();
        }
        else{
            hour = timePicker1.getCurrentHour();
            min = timePicker1.getCurrentMinute();
        }

         okButton.setOnClickListener(this);
    }

    // Methode associé à l'appui de l'EditText
    public void onClick(View view)
    {

        if(R.id.bt_Ok == view.getId())
        {
            //Alarms.getInstance().Create(hour, min);

            final Intent intent = getIntent();

            //String message = intent.getStringExtra("Week");
            String timeToReturn= toString(hour,min);
            intentResult= new Intent();
            intentResult.putExtra(timeToReturn,"Week");
            TimerActivity.this.setResult(TimerActivity.RESULT_OK,intentResult);
            TimerActivity.this.finish();
        }
        else
        {
            Log.e("Bouton","clic pas implémenté !");
        }

    }

    private String toString(int hour, int min){
        this.hour=hour;
        this.min=min;
        String string= this.hour+":"+this.min;
        return string;
    }

}
