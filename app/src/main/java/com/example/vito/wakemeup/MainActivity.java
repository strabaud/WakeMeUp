package com.example.vito.wakemeup;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.AlarmManager;
import android.app.Dialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Build;
import android.speech.tts.TextToSpeech;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.view.View;
import android.content.Intent;
import android.widget.ImageButton;
import android.widget.TimePicker;
import android.widget.Toast;
import android.widget.ToggleButton;

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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;


public class MainActivity extends AppCompatActivity implements View.OnClickListener, TextToSpeech.OnInitListener, LocationListener, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

                                                    /*

                                                    VARIABLES

                                                     */
    //TextView WeekDaysEdit;
    ImageButton SettingsEdit;
    ArrayList<Time> alarms;
    Button btnSpeak;
    public static TextToSpeech tts;

    //*********************************************
    AlarmManager alarmManager;
    ToggleButton weekToggleButton;
    ToggleButton weekEndToggleButton;
    PendingIntent pendingIntent;
    static MainActivity inst;


    GoogleApiClient mGoogleApiClient;
    //**************************************************
    private final int MY_PERMISSION_REQUEST_FINE_LOCATION = 101;

    // VARIABLES ENVOI RECUP DONNEES INTENT
    static final String EXTRA_TIME="Week";
    final String EXTRA_WE_TIME="Week-End";

    // VARIABLES LOCATION & WEATHER
    TextView longii;
    TextView latit;
    static TextView cityText;
    static TextView wakeUp;
    protected LocationManager locationManager;
    protected LocationListener locationListener;
    Location mLastLocation; // location
    double latitude; // latitude
    double longitude; // longitude
    static String City = "";
    String weatherFile = "a";
    String temperature = "";
    String MinTemp = "";
    String MaxTem = "";
    static String weather = "";
    static String finalTemp="";

    //VARIABLES TIMEPICKER
    private final int WEEK_TIME_DIALOG_ID = 200;
    private final int WEEK_END_TIME_DIALOG_ID = 300;
    TextView WeekDaysEdit;
    TextView weekEndDaysEdit;
    int hourWeek,minuteWeek,hourWeekEnd,minuteWeekEnd;

    //****************************************************
    String newsFile = "";
    static Map<String, String> map = new HashMap<String, String>();

    // VARIABLES DB

    String defaultActivity="CHOISIR";
    String defaultName="user";
    Hobbies hobbies;
    Hobbies hobbiesFromDB;
    TransactionsDB transactionsDB;

    static int a;
   static String userName;
   static String activity1;
   static String activity2;
   static String activity3;


    //// GESTION DES DEPRECATED POUR TEXT TO SPEECH
    public static void tts(String string){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            ttsGreater21(string);
        }
        else{
            ttsUnder20(string);
        }
    }

    public static void ttsSilence(int number){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            tts.playSilentUtterance(number, TextToSpeech.QUEUE_ADD, null);
        }
        else{
            tts.playSilence(number, TextToSpeech.QUEUE_ADD, null);
        }
    }

    @SuppressWarnings("deprecation")
    private static void ttsUnder20(String text) {
        HashMap<String, String> map = new HashMap<>();
        map.put(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID, "MessageId");
        tts.speak(text, TextToSpeech.QUEUE_ADD, map);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private static void ttsGreater21(String text) {
        String utteranceId= a + "";
        tts.speak(text, TextToSpeech.QUEUE_ADD, null, utteranceId);
    }
//////

    public static MainActivity instance() {
        return inst;
    }

    public static  TextView getWakeUpTextView(){
        return cityText;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        a= this.hashCode();
        //début récupération data SQLITE DB
        try
        {
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

            hobbies = new Hobbies(hobbiesFromDB.getName(),hobbiesFromDB.getActivity1(),hobbiesFromDB.getActivity2(),hobbiesFromDB.getActivity3());

        }
        else{

            //On cree une ligne
            hobbies = new Hobbies(defaultName,defaultActivity, defaultActivity, defaultActivity);

            //insert
            transactionsDB.insertHobbies(hobbies);


        }

        //Assignation données DB ACTIVITY a nos variables
        userName=hobbies.getName();
        activity1=hobbies.getActivity1();
        activity2=hobbies.getActivity2();
        activity3=hobbies.getActivity3();
        //FIN DB SQLITE



        wakeUp = (TextView) findViewById(R.id.wakeMeUpTextView) ;

        alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);

        mGoogleApiClient = new GoogleApiClient.Builder(this, this, this).addApi(LocationServices.API).build();


        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, MY_PERMISSION_REQUEST_FINE_LOCATION);
        }


        tts = new TextToSpeech(this, this);

        System.out.print("Passage dans Oncreate !");

        setContentView(R.layout.activity_main);

        weekToggleButton = (ToggleButton) findViewById(R.id.alarmToggle);
        weekToggleButton.setOnClickListener(this);
        weekEndToggleButton = (ToggleButton) findViewById(R.id.alarmToggle2);
        weekEndToggleButton.setOnClickListener(this);

        //Application des listener sur nos deux textView pour la gestion du timepicker
        WeekDaysEdit = (TextView) findViewById(R.id.WeekDays);
        WeekDaysEdit.setOnClickListener(this);
        weekEndDaysEdit = (TextView) findViewById(R.id.WeekEndDays);
        weekEndDaysEdit.setOnClickListener(this);

        //Application du listener pour l'image settings qui va ouvrir notre activité settings
        SettingsEdit = (ImageButton) findViewById(R.id.settings_button);
        SettingsEdit.setOnClickListener(this);

        //Application du listener sur notre bouton pour parler
        btnSpeak = (Button) findViewById(R.id.button_speak);
        btnSpeak.setOnClickListener(this);
        System.out.println("Passage dans OnCreate");

/*

No more used
        longii.setText(Double.toString(longitude));
        latit.setText(Double.toString(latitude));
        cityText.setText(City);
*/
    }

    protected void onStart() {
        super.onStart();
        mGoogleApiClient.connect();

    }

    protected void onStop() {
        super.onStop();
        mGoogleApiClient.disconnect();

    }

    // Methode associé
    public void onClick(View view) {

        switch (view.getId()){
            case R.id.WeekDays:
                /*Intent SetTimerActivity = new Intent(MainActivity.this, TimerActivity.class);
                //on passe l'intention au système
                EditText weekDays = (EditText)findViewById(R.id.WeekDays);
                Bundle bundle = new Bundle();
                bundle.putString("Week", weekDays.toString());

                SetTimerActivity.putExtras(bundle);
                startActivityForResult(SetTimerActivity,1);*/
                showDialog(WEEK_TIME_DIALOG_ID);
                break;

            case R.id.WeekEndDays:
                showDialog(WEEK_END_TIME_DIALOG_ID);
/*
                Intent SetTimerActivity2 = new Intent(MainActivity.this, TimerActivity.class);
                //SetTimerActivity2.putExtra(EXTRA_WE_TIME,weekEnd.getText().toString());
                //on passe l'intention au système
                startActivity(SetTimerActivity2);*/
                break;

            case R.id.settings_button:
                Intent SetSettings = new Intent(this, Settings.class);
                //on passe l'intention au système
                startActivity(SetSettings);
                break;

            case R.id.button_speak:
                speakOut();
                break;

            case R.id.alarmToggle:
                //button on
                if(weekToggleButton.isChecked())
                {
                    Calendar calendar = Calendar.getInstance();
                    //regle l'heure du reveil pour la semaine
                    calendar.set(Calendar.HOUR_OF_DAY,hourWeek);
                    //regle les minute du reveil pour la semaine
                    calendar.set(Calendar.MINUTE,minuteWeek);
                    Intent myIntent = new Intent(MainActivity.this,AlarmReceiver.class);
                    pendingIntent = PendingIntent.getBroadcast(MainActivity.this, 0, myIntent, 0);
                    // set alarm
                    alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
                }else {
                    alarmManager.cancel(pendingIntent);
                    //Log.d("MyActivity", "Alarm Off");
                }

                break;

            case R.id.alarmToggle2:
                break;

            default:
                Log.e("Bouton", "clic pas implémenté !");
                break;
        }

    }


    @Override
    public void onResume() {
        super.onResume();

        try {
            hobbiesFromDB = transactionsDB.getHobbiesById(1);
        }
        catch (Exception e){

        }

        hobbies = new Hobbies(hobbiesFromDB.getName(),hobbiesFromDB.getActivity1(),hobbiesFromDB.getActivity2(),hobbiesFromDB.getActivity3());
        userName=hobbies.getName();
        activity1=hobbies.getActivity1();
        activity2=hobbies.getActivity2();
        activity3=hobbies.getActivity3();
        DisplayAlarms();
    }

    @Override
    public void onRestart() {
        super.onRestart();
        DisplayAlarms();
    }

    public void DisplayAlarms()
    {
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
                if(A1.getSubLocality()!=null){
                    City = A1.getSubLocality();
                }
                else{
                    City = A1.getLocality();
                }
                cityText.setText(City);
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
            try
            {
                new WeatherTask().execute();
            }
            catch (Exception e)
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

                            /*
                            *
                            RECUPERATION DONNEES INTENT
                             *
                             **/
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1)
            if( resultCode==RESULT_OK ) {
                String s = data.getExtras().getString("Week");
                EditText weekDays = (EditText)findViewById(R.id.WeekDays);
                weekDays.setText(s);
            }
            else{

            }
        else{

        }

    }

                            /*

                            SPEAKOUT

                             */
    public void speakOut()
    {

        String W = weather;
        String wakeUp = "Bonjour "+userName+" c'est l'heure de te réveiller ! Actuellement, il fait " + finalTemp + " degrés dans la ville de " +City+" , et le temps est " + W;
        String newsToday ="Tu as choisi d'avoir des informations sur "+activity1+" "+activity2+" "+activity3+" Voici les nouvelles pour aujourd'hui ";
        String title = "Titre de l'article ";
        String description = "Description ";
        int averageWaitingTime=1000;
        int shortWaintingTime=500;

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
        for (Map.Entry<String, String> entry : map.entrySet()) // getKey() de entry permet d'avoir
        {
            // TITRE
            tts(title +" "+Integer.toString(i));
            ttsSilence(shortWaintingTime);
            tts(entry.getKey());

            ttsSilence(averageWaitingTime);

            // DESCRIPTION
            tts(description +" "+Integer.toString(i));
            ttsSilence(shortWaintingTime);
            tts(entry.getValue());
            ttsSilence(averageWaitingTime);
            i++;
        }

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
                            for (int i = 0; i < nodes.getLength(); i++) {
                                Element element = (Element) nodes.item(i);
                                NodeList titleNode = element.getElementsByTagName("title");
                                Element line = (Element) titleNode.item(0);
                                String Title = line.getTextContent();
                                //
                                NodeList descriptionNode = element.getElementsByTagName("description");
                                Element descriptionLine = (Element) descriptionNode.item(0);
                                String description = descriptionLine.getTextContent();

                                map.put(Title, description);

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

                Double tempDouble = Double.valueOf(temperature);
                int tempInt = tempDouble.intValue();
                finalTemp = String.valueOf(tempInt);

                weather = j.getString("description");




            }
            catch (JSONException e)
            {
                e.printStackTrace();
            }

        }
    }

    /*


    */
    //fonctions de gestion de l  r&cup&ration des heures
    protected Dialog onCreateDialog(int id){
        switch (id){
            case WEEK_TIME_DIALOG_ID:
                return new TimePickerDialog(this,timePickerListener1,hourWeek,minuteWeek,false);
            case WEEK_END_TIME_DIALOG_ID:
                return new TimePickerDialog(this, timePickerListener2,hourWeekEnd,minuteWeekEnd,false);
        }
        return null;
    }

    private TimePickerDialog.OnTimeSetListener timePickerListener1= new TimePickerDialog.OnTimeSetListener() {
        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minuteSelected) {
            hourWeek = hourOfDay;
            minuteWeek = minuteSelected;
            WeekDaysEdit.setText(hourWeek+":"+minuteWeek);
        }
    };

    private TimePickerDialog.OnTimeSetListener timePickerListener2= new TimePickerDialog.OnTimeSetListener() {
        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minuteSelected) {
            hourWeek = hourOfDay;
            minuteWeek = minuteSelected;
            weekEndDaysEdit.setText(hourWeek+":"+minuteWeek);
        }
    };
}
