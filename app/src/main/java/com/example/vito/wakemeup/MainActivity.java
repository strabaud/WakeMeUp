package com.example.vito.wakemeup;

import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.os.Bundle;
import android.widget.EditText;
import android.view.View;
import android.content.Intent;


public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    // create a editText to get the click option
    EditText WeekDaysEdit;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        WeekDaysEdit = (EditText)findViewById(R.id.WeekDays);
        WeekDaysEdit.setOnClickListener(this);

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
        else
        {
            Log.e("Bonton","clic pas implémenté !");
        }
    }
}
