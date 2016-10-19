package com.example.vito.wakemeup;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class Settings extends AppCompatActivity implements View.OnClickListener{

    private Button OkButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        OkButton = (Button)findViewById(R.id.bt_Ok);
        OkButton.setOnClickListener(this);
    }

    public void onClick(View view)
    {

        if(R.id.bt_Ok == view.getId())
        {
            this.finish();
        }
        else
        {
            Log.e("Bouton","clic pas implémenté !");
        }
    }
}
