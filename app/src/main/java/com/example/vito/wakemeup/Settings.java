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


        //On cree instance pour transactions
        TransactionsDB transactionsDB= new TransactionsDB(this);

        //On cree une ligne pour tester
        Hobbies hobbies = new Hobbies("PORN", "METEO", "SPORT");

        //ouverture db pour insert
        transactionsDB.open();
        //insert
        transactionsDB.insertHobbies(hobbies);


        Hobbies hobbiesFromDB = transactionsDB.getHobbiesById(hobbies.getId());

        if(hobbiesFromDB != null){
            //On affiche les infos du livre dans un Toast
            //Toast.makeText(this, hobbiesFromDB.toString(), Toast.LENGTH_LONG).show();
            //On modifie le titre du livre
            hobbies.setActivity1("RELIGION");
            //Puis on met à jour la BDD
            transactionsDB.updateHobbies(hobbiesFromDB.getId(), hobbiesFromDB);
        }

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
