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
            //ouverture db
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

        }
        else{

            //On cree une ligne pour tester
            hobbies = new Hobbies("CHOISIR", "CHOISIR", "CHOISIR");

            //insert
            transactionsDB.insertHobbies(hobbies);


        }


        OkButton = (Button)findViewById(R.id.bt_Ok);
        OkButton.setOnClickListener(this);


        //SPINNERS
        ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,HobbiesList);
        spinner1 = (Spinner) findViewById(R.id.spinner1);
        setSpinText(spinner1,adapter1,hobbies.getActivity1());

        ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,HobbiesList);
        spinner2 = (Spinner) findViewById(R.id.spinner2);
        setSpinText(spinner2,adapter2,hobbies.getActivity2());

        ArrayAdapter<String> adapter3 = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,HobbiesList);
        spinner3 = (Spinner) findViewById(R.id.spinner3);
        setSpinText(spinner3,adapter3,hobbies.getActivity3());

    }

    // MAJ du text des spin avec les données de la DB ou celles par défaut
    public void setSpinText(Spinner spinner, ArrayAdapter adapter, String string)
    {
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setSelection(adapter.getPosition(string));
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
