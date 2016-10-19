package com.example.vito.wakemeup;

import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.os.Bundle;
import android.widget.EditText;
import android.view.View;
import android.content.Intent;
import android.widget.ImageButton;

import java.util.ArrayList;


public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    // create a editText to get the click option
    EditText WeekDaysEdit;
    ImageButton SettingsEdit;
    ArrayList<Time> alarms;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        WeekDaysEdit = (EditText)findViewById(R.id.WeekDays);
        WeekDaysEdit.setOnClickListener(this);

        SettingsEdit = (ImageButton)findViewById(R.id.imageButton1);
        SettingsEdit.setOnClickListener(this);

    }

    // Methode associé à l'appui de l'EditText
    public void onClick(View view)
    {

        if(R.id.WeekDays == view.getId())
        {
            Intent SetTimerActivity = new Intent(this,TimerActivity.class);
            //on passe l'intention au système
            startActivity(SetTimerActivity);

        }
        else if(R.id.imageButton1 == view.getId()){
            Intent SetSettings = new Intent(this,Settings.class);
            //on passe l'intention au système
            startActivity(SetSettings);
        }

        else
        {
            Log.e("Bouton","clic pas implémenté !");
        }
    }
    /*
    @Override
    public void onResume()
    {
        super.onResume();
        alarms = Alarms.getInstance().getAlarms();
        Time t1 = new Time(0,0);
        t1 = alarms.get(0);
        String hour  =  Integer.toString(t1.hour);
        String min = Integer.toString(t1.minutes);

        WeekDaysEdit.setText(hour + " : " + min);
    }
*/
}
