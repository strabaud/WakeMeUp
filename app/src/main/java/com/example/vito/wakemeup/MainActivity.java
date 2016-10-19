package com.example.vito.wakemeup;

import android.app.DialogFragment;
import android.speech.tts.TextToSpeech;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.view.View;
import android.content.Intent;
import android.widget.ImageButton;

import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Locale;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;


public class MainActivity extends AppCompatActivity implements View.OnClickListener,TextToSpeech.OnInitListener {

    // create a editText to get the click option
    TextView WeekDaysEdit;
    ImageButton SettingsEdit;
    ArrayList<Time> alarms;

    Button btnSpeak;

    TextToSpeech tts;

    String xmlUrl = "http://api.tameteo.com/index.php?api_lang=fr&localidad=26048&affiliate_id=fp7bnam325ul&v=2&h=1";

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        tts = new TextToSpeech(this, this);



        System.out.print("Passage dans Oncreate !");


        setContentView(R.layout.activity_main);

        WeekDaysEdit = (TextView)findViewById(R.id.WeekDays);
        WeekDaysEdit.setOnClickListener(this);

        SettingsEdit = (ImageButton)findViewById(R.id.imageButton1);
        SettingsEdit.setOnClickListener(this);

        btnSpeak = (Button) findViewById(R.id.button1);
        btnSpeak.setOnClickListener(this);

        DisplayAlarms();

    }

    private void speakOut()
    {
        tts.speak("salut sylvain ! c'est l'heure de te réveillé ! voici les température pour aujourd'hui :", TextToSpeech.QUEUE_FLUSH, null);

        // create a new DocumentBuilderFactory
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        try {
            // use the factory to create a documentbuilder
            DocumentBuilder builder = factory.newDocumentBuilder();

            // create a new document from input source
            FileInputStream fis = new FileInputStream(xmlUrl);

            int a =0;
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }

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
        else if(R.id.button1 == view.getId())
        {
            speakOut();
        }
        else
        {
            Log.e("Bouton","clic pas implémenté !");
        }
    }


    @Override
    public void onResume()
    {
        super.onResume();
        DisplayAlarms();
    }

    @Override
    public void onRestart()
    {
        super.onRestart();
        DisplayAlarms();
    }

    public void DisplayAlarms(){
        System.out.print("Passage dans la fonction Display baby");
        alarms = Alarms.getInstance().getAlarms();
        Time t1 = new Time(0,0);

        if(alarms!=null)
        {
            t1 = alarms.get(0);
            String hour  =  Integer.toString(t1.hour);
            String min = Integer.toString(t1.minutes);
            WeekDaysEdit.setText(hour + " : " + min);
        }
    }

    // init pour le textTo speech
    @Override
    public void onInit(int status) {
        if (status == TextToSpeech.SUCCESS) {

            int result = tts.setLanguage(Locale.FRANCE);

            if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED)
            {
                Log.e("TTS", "This Language is not supported");
            }
            else
            {
                btnSpeak.setEnabled(true);
                speakOut();
            }

        }
        else
        {

            Log.e("TTS", "Initilization Failed!");

        }
    }
}
