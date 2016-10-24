package com.example.vito.wakemeup;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.pm.PackageManager;
import android.icu.util.ValueIterator;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Build;
import android.speech.tts.TextToSpeech;
import android.speech.tts.Voice;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.view.View;
import android.content.Intent;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xmlpull.v1.XmlPullParser;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;


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
    protected LocationManager locationManager;
    protected LocationListener locationListener;
    Location mLastLocation; // location
    double latitude; // latitude
    double longitude; // longitude
    String City = "";
   // String strURL = "http://api.openweathermap.org/data/2.5/weather?q=Paris&appid=288c4c3f50e07e9188bdef93c039687c";
    String weatherFile = "a";
    String temperature = "";
    String MinTemp ="";
    String MaxTem ="";
    String weather="";
    //****************************************************
    String newsFile = "";
    Map<String, String> map = new HashMap<String, String>();


//// GESTION DES DEPRECATED
    private void tts(String string){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            ttsGreater21(string);
        }
        else{
            ttsUnder20(string);
        }
    }
    private void ttsSilence(int number){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            tts.playSilentUtterance(number, TextToSpeech.QUEUE_ADD, null);
        }
        else{
            tts.playSilence(number, TextToSpeech.QUEUE_ADD, null);
        }
    }

    @SuppressWarnings("deprecation")
    private void ttsUnder20(String text) {
        HashMap<String, String> map = new HashMap<>();
        map.put(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID, "MessageId");
        tts.speak(text, TextToSpeech.QUEUE_ADD, map);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void ttsGreater21(String text) {
        String utteranceId=this.hashCode() + "";
        tts.speak(text, TextToSpeech.QUEUE_ADD, null, utteranceId);
    }
//////


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        mGoogleApiClient = new GoogleApiClient.Builder(this, this, this).addApi(LocationServices.API).build();
        /*if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }*/

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, MY_PERMISSION_REQUEST_FINE_LOCATION);
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
        System.out.println("Passage dans OnCreate");


        //longii.setText(Double.toString(longitude));
        //latit.setText(Double.toString(latitude));
        //cityText.setText(City);

    }

    private void speakOut()
    {
        Double tempDouble = Double.valueOf(temperature);
        int tempInt = tempDouble.intValue();
        String finalTemp = String.valueOf(tempInt);
        String W = weather;
        String wakeUp = "Bonjour sylvain ! c'est l'heure de te réveiller ! Actuellement, il fait " + finalTemp + " degrés dehors, et le temps est " + W;
        String newsToday ="Voici les nouvelles pour aujourd'hui ";
        String title = "Titre de l'article ";
        String description = "Description ";
        int averageWaitingTime=1000;

        /*
        A VOIR SI ON UTILISE OU PAS
        //Voice v1 = tts.getVoice();
        //String name = v1.getName();
         */


/// Watch deprecated functions after VARIABLES DECLARATIONS to understand those functions
            tts(wakeUp);
            ttsSilence(averageWaitingTime);
            tts(newsToday);
            ttsSilence(averageWaitingTime);

        int i =1;
        for (Map.Entry<String, String> entry : map.entrySet())
        {
            tts(title +" "+Integer.toString(i)+" "+ entry.getKey());
            ttsSilence(averageWaitingTime);
            tts(description +" "+Integer.toString(i)+" "+ entry.getValue());
            ttsSilence(averageWaitingTime);
            i++;
        }

    }

    protected void onStart() {
        super.onStart();
        mGoogleApiClient.connect();

    }

    protected void onStop() {
        super.onStop();
        mGoogleApiClient.disconnect();

    }

    // Methode associé à l'appui de l'EditText
    public void onClick(View view) {
        if (R.id.WeekDays == view.getId()) {
            Intent SetTimerActivity = new Intent(this, TimerActivity.class);
            //on passe l'intention au système
            startActivity(SetTimerActivity);
        }
        else if (R.id.imageButton1 == view.getId()) {
            Intent SetSettings = new Intent(this, Settings.class);
            //on passe l'intention au système
            startActivity(SetSettings);
        }
        else if (R.id.button1 == view.getId()) {
            speakOut();
        }
        else {
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
    public void onInit(int status)
    {

        if (status == TextToSpeech.SUCCESS) {

            int result = tts.setLanguage(Locale.FRANCE);

            if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                Log.e("TTS", "This Language is not supported");
            } else {
                btnSpeak.setEnabled(true);

            }

        } else {

            Log.e("TTS", "Initilization Failed!");

        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode)
        {
            case MY_PERMISSION_REQUEST_FINE_LOCATION:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                {
                    // All good!
                }
                else
                {
                    Toast.makeText(this, "Need your location!", Toast.LENGTH_SHORT).show();
                }break;
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
    public void onConnected(Bundle connectionHint)
    {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
        {
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

            longii.setText("Longitude : "+Double.toString(longitude));
            latit.setText("Latitude : "+Double.toString(latitude));

            Geocoder geocoder = new Geocoder(this, Locale.getDefault());
            try
            {
                List<Address> addresses = geocoder.getFromLocation(latitude, longitude, 1);
               Address A1 =  addresses.get(0);
                City =  A1.getLocality();
                cityText.setText("City : "+City);
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED)
        {
            Location lastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);

            latitude = lastLocation.getLatitude();
            longitude = lastLocation.getLongitude();
            Geocoder geocoder = new Geocoder(this, Locale.getDefault());
            try
            {
                List<Address> addresses = geocoder.getFromLocation(latitude, longitude, 1);
                Address A1 =  addresses.get(0);
                if(A1.getSubLocality()!=null){
                    City = A1.getSubLocality();
                }
                else{
                    City = A1.getLocality();
                }
                new WeatherTask().execute();

            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
            //String url = String.format("http://api.openweathermap.org/data/2.5/weather?lat=%f&lon=%f&units=%s&appid=%s", lat, lon, units, APP_ID);
            //new GetWeatherTask(textView).execute(url);
        }

    }


    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }

    public class WeatherTask extends AsyncTask<String, Void, String>
    {

        @Override
        protected String doInBackground(String... params) {
            try
            {
                if(City != null)
                {

                    String UrlWeather ="http://api.openweathermap.org/data/2.5/weather?q="+City+"&units=metric&lang=fr&appid=288c4c3f50e07e9188bdef93c039687c";
                    URL url = new URL(UrlWeather);
                    HttpURLConnection con = (HttpURLConnection) url.openConnection();
                    con.setRequestMethod("GET");
                    con.connect();
                    BufferedReader bf = new BufferedReader(new InputStreamReader(con.getInputStream()));
                    String value = bf.readLine();
                    weatherFile = value;

                    /*************************************************************************************************/
                    String UrlNews = "http://www.lemonde.fr/technologies/rss_full.xml";
                    URL url2 = new URL(UrlNews);
                    con = (HttpURLConnection) url2.openConnection();
                    DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
                    DocumentBuilder builder = factory.newDocumentBuilder();
                    Document doc = builder.parse(con.getInputStream());
                    NodeList nodes = doc.getElementsByTagName("item");
                    for (int i = 0; i < nodes.getLength(); i++)
                    {
                        Element element = (Element) nodes.item(i);
                        NodeList titleNode = element.getElementsByTagName("title");
                        Element line = (Element) titleNode.item(0);
                        String Title = line.getTextContent();
                        //
                        NodeList descriptionNode = element.getElementsByTagName("description");
                        Element descriptionLine = (Element) descriptionNode.item(0);
                        String description = descriptionLine.getTextContent();

                        map.put(Title,description);

                    }

                }


            }
            catch (IOException e)
            {
                e.printStackTrace();
            } catch (ParserConfigurationException e) {
                e.printStackTrace();
            } catch (SAXException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s){
            super.onPostExecute(s);

            try
            {
                JSONObject jObject = new JSONObject(weatherFile);
                JSONObject main = jObject.getJSONObject("main");
                temperature = main.getString("temp");
                MinTemp = main.getString("temp_min");
                MaxTem = main.getString("temp_max");
                JSONArray weatherObj = jObject.getJSONArray("weather");
                JSONObject j = weatherObj.getJSONObject(0);

                weather = j.getString("description");




            }
            catch (JSONException e)
            {
                e.printStackTrace();
            }

        }
    }
}
