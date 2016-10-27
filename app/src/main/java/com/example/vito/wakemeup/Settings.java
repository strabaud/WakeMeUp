package com.example.vito.wakemeup;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

public class Settings extends AppCompatActivity implements View.OnClickListener{

    private Button OkButton;


    // VARIABLES GESTION LISTE DES ACTIVITES
    private Spinner spinner1;
    private Spinner spinner2;
    private Spinner spinner3;
    String[] HobbiesList = new String[]{
            "CHOISIR","SPORT","METEO","ACTUALITE"
    };
    String defaultActivity="CHOISIR";
    Hobbies hobbies;
    Hobbies hobbiesFromDB;
    TransactionsDB transactionsDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        try {
            transactionsDB = new TransactionsDB(this);
            transactionsDB.open();
        }
        catch (Exception e){

        }

        try {
            hobbiesFromDB = transactionsDB.getHobbiesById(1);
        }
        catch (Exception e){

        }

        if(hobbiesFromDB != null){

            hobbies = new Hobbies(hobbiesFromDB.getActivity1(),hobbiesFromDB.getActivity2(),hobbiesFromDB.getActivity3());
            //hobbies = new Hobbies("CHOISIR", "CHOISIR", "CHOISIR");

        }
        else{

            //hobbies = new Hobbies("Activité 1", "Activité 2", "Activité 3");
            //On cree une ligne pour tester
            hobbies = new Hobbies("CHOISIR", "CHOISIR", "CHOISIR");

            //ouverture db pour insert
            //insert
            transactionsDB.insertHobbies(hobbies);


        }


        OkButton = (Button)findViewById(R.id.bt_Ok);
        OkButton.setOnClickListener(this);


        //SPINNER
        ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,HobbiesList);
        spinner1 = (Spinner) findViewById(R.id.spinner1);
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner1.setAdapter(adapter1);
        spinner1.setSelection(adapter1.getPosition(hobbies.getActivity1()));

        ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,HobbiesList);
        spinner2 = (Spinner) findViewById(R.id.spinner2);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner2.setAdapter(adapter2);
        spinner2.setSelection(adapter2.getPosition(hobbies.getActivity2()));

        ArrayAdapter<String> adapter3 = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,HobbiesList);
        spinner3 = (Spinner) findViewById(R.id.spinner3);
        adapter3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner3.setAdapter(adapter3);
        spinner3.setSelection(adapter3.getPosition(hobbies.getActivity3()));

    }

    public void setSpinText(Spinner spin, String text)
    {
        for(int i= 0; i < spin.getAdapter().getCount(); i++)
        {
            if(spin.getAdapter().getItem(i).toString().contains(text))
            {
                spin.setSelection(i);
            }
        }

    }

    public void onClick(View view)
    {

        if(R.id.bt_Ok == view.getId())
        {

            Hobbies finalHobbies = new Hobbies(spinner1.getSelectedItem().toString(),spinner2.getSelectedItem().toString(),spinner3.getSelectedItem().toString());
            transactionsDB.updateHobbies(1,finalHobbies);
            //Log.println(Log.INFO,"tag",hobbiesFromDB.getActivity1());

            this.finish();
        }
        else
        {
            Log.e("Bouton","clic pas implémenté !");
        }
    }


}
