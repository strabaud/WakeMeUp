package com.example.vito.wakemeup;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.os.Build;
import android.speech.tts.TextToSpeech;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.view.View;
import android.content.Intent;
import android.widget.ImageButton;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import javax.xml.parsers.DocumentBuilderFactory;



public class MainActivity extends AppCompatActivity implements View.OnClickListener, TextToSpeech.OnInitListener, LocationListener, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    // create a editText to get the click option
    TextView WeekDaysEdit;
    ImageButton SettingsEdit;
    ArrayList<Time> alarms;
    Button btnSpeak;
    TextToSpeech tts;

    GoogleApiClient mGoogleApiClient;

    TextView longii;
    TextView latit;
    TextView cityText;
    //**************************************************
    private final int MY_PERMISSION_REQUEST_FINE_LOCATION = 101;
    Location mLastLocation; // location
    double latitude; // latitude
    double longitude; // longitude
    //****************************************************

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }


        tts = new TextToSpeech(this, this);

        System.out.print("Passage dans Oncreate !");

        setContentView(R.layout.activity_main);

        WeekDaysEdit = (TextView) findViewById(R.id.WeekDays);
        WeekDaysEdit.setOnClickListener(this);

        SettingsEdit = (ImageButton) findViewById(R.id.imageButton1);
        SettingsEdit.setOnClickListener(this);

        btnSpeak = (Button) findViewById(R.id.button1);
        btnSpeak.setOnClickListener(this);

        DisplayAlarms();

    }

    private void speakOut() {
        //tts.speak("salut sylvain ! c'est l'heure de te réveillé ! voici les température pour aujourd'hui :", TextToSpeech.QUEUE_FLUSH, null);

        // create a new DocumentBuilderFactory
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        try
        {
            // use the factory to create a documentbuilder

        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

    protected void onStart() {
        mGoogleApiClient.connect();
        super.onStart();
    }

    protected void onStop() {
        mGoogleApiClient.disconnect();
        super.onStop();
    }

    // Methode associé à l'appui de l'EditText
    public void onClick(View view) {
        if (R.id.WeekDays == view.getId()) {
            Intent SetTimerActivity = new Intent(this, TimerActivity.class);
            //on passe l'intention au système
            startActivity(SetTimerActivity);
        } else if (R.id.imageButton1 == view.getId()) {
            Intent SetSettings = new Intent(this, Settings.class);
            //on passe l'intention au système
            startActivity(SetSettings);
        } else if (R.id.button1 == view.getId()) {
            speakOut();
        } else {
            Log.e("Bouton", "clic pas implémenté !");
        }
    }


    @Override
    public void onResume() {
        super.onResume();
        DisplayAlarms();
    }

    @Override
    public void onRestart() {
        super.onRestart();
        DisplayAlarms();
    }

    public void DisplayAlarms() {
        System.out.print("Passage dans la fonction Display baby");
        alarms = Alarms.getInstance().getAlarms();
        Time t1 = new Time(0, 0);

        if (alarms != null) {
            t1 = alarms.get(0);
            String hour = Integer.toString(t1.hour);
            String min = Integer.toString(t1.minutes);
            WeekDaysEdit.setText(hour + " : " + min);
        }
    }

    // init pour le textTo speech
    @Override
    public void onInit(int status) {
        if (status == TextToSpeech.SUCCESS) {

            int result = tts.setLanguage(Locale.FRANCE);

            if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                Log.e("TTS", "This Language is not supported");
            } else {
                btnSpeak.setEnabled(true);
                speakOut();
            }

        } else {

            Log.e("TTS", "Initilization Failed!");

        }
    }


    @Override
    public void onLocationChanged(Location location) {

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }


    @Override
    public void onConnected(Bundle connectionHint) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)
            {
                requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, MY_PERMISSION_REQUEST_FINE_LOCATION);
                return;
            }
        }
        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        if (mLastLocation != null)
        {
            latitude = mLastLocation.getLatitude();
            longitude = mLastLocation.getLongitude();

            longii = (TextView) findViewById(R.id.longitudeTextView);
            latit = (TextView) findViewById(R.id.latitudeTextView);
            cityText = (TextView) findViewById(R.id.CityTextView);

            longii.setText(Double.toString(longitude));
            latit.setText(Double.toString(latitude));

            Geocoder geocoder = new Geocoder(this, Locale.getDefault());
            try
            {
                List<Address> addresses = geocoder.getFromLocation(latitude, longitude, 1);
               Address A1 =  addresses.get(0);
               String city =  A1.getSubLocality();
                cityText.setText(city);

            } catch (IOException e)
            {
                e.printStackTrace();
            }


        }
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }
}
