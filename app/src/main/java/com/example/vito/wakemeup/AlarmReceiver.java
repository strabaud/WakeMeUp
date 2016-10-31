package com.example.vito.wakemeup;


import android.content.Context;
import android.content.Intent;
import android.speech.tts.TextToSpeech;
import android.support.v4.content.WakefulBroadcastReceiver;
import android.util.Log;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * Created by jeanTindel on 29/10/2016.
 */

public class AlarmReceiver extends WakefulBroadcastReceiver implements  TextToSpeech.OnInitListener{
    TextToSpeech tts;
    int averageWaitingTime=1000;
    // init pour le textTo speech
    @Override
    public void onInit(int status)
    {

        if (status == TextToSpeech.SUCCESS) {

            int result = tts.setLanguage(Locale.FRANCE);

            if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                Log.e("TTS", "This Language is not supported");
            } else {
                //btnSpeak.setEnabled(true);

            }

        } else {

            Log.e("TTS", "Initilization Failed!");

        }
    }


    @Override
    public void onReceive(final Context context, Intent intent) {


        try
        {
            String wakeUp = "Bonjour "+MainActivity.userName+" c'est l'heure de te réveiller ! Actuellement, il fait " + MainActivity.finalTemp + " degrés dans la ville de " +MainActivity.City+" , et le temps est "+MainActivity.weather;
            String newsToday = "Voici les nouvelles pour aujourd'hui";
            String title = "Titre de l'article ";
            String description = "Description ";
            MainActivity.tts(wakeUp);
            MainActivity.ttsSilence(averageWaitingTime);
            MainActivity.tts(newsToday);
            MainActivity.ttsSilence(averageWaitingTime);

            int i =1;
            for (Map.Entry<String, String> entry : MainActivity.newsMap.entrySet()) // getKey() de entry permet d'avoir
            {
                // TITRE
                MainActivity.tts(title +" "+Integer.toString(i));
                MainActivity.ttsSilence(averageWaitingTime);
                MainActivity.tts(entry.getKey());

                MainActivity.ttsSilence(averageWaitingTime);

                // DESCRIPTION
                MainActivity.tts(description +" "+Integer.toString(i));
                MainActivity.ttsSilence(averageWaitingTime);
                MainActivity.tts(entry.getValue());
                MainActivity.ttsSilence(averageWaitingTime);
                i++;
            }
            for (Map.Entry<String, String> entry : MainActivity.technoMap.entrySet()) // getKey() de entry permet d'avoir
            {
                // TITRE
                MainActivity.tts(title +" "+Integer.toString(i));
                MainActivity.ttsSilence(averageWaitingTime);
                MainActivity.tts(entry.getKey());

                MainActivity.ttsSilence(averageWaitingTime);

                // DESCRIPTION
                MainActivity.tts(description +" "+Integer.toString(i));
                MainActivity.ttsSilence(averageWaitingTime);
                MainActivity.tts(entry.getValue());
                MainActivity.ttsSilence(averageWaitingTime);
                i++;
            }
            for (Map.Entry<String, String> entry : MainActivity.scienceMap.entrySet()) // getKey() de entry permet d'avoir
            {
                // TITRE
                MainActivity.tts(title +" "+Integer.toString(i));
                MainActivity.ttsSilence(averageWaitingTime);
                MainActivity.tts(entry.getKey());

                MainActivity.ttsSilence(averageWaitingTime);

                // DESCRIPTION
                MainActivity.tts(description +" "+Integer.toString(i));
                MainActivity.ttsSilence(averageWaitingTime);
                MainActivity.tts(entry.getValue());
                MainActivity.ttsSilence(averageWaitingTime);
                i++;
            }
        }catch (Exception e){

        }
    }
}
