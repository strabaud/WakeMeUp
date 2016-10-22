package com.example.vito.wakemeup;

import android.os.AsyncTask;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by jeanTindel on 20/10/2016.
 */
public class RetrieveXML extends AsyncTask<String,String,String> {


    @Override
    protected String doInBackground(String... url)
    {
        String URL1 = url[0];
        String Result = null ;
        BufferedReader br = null;
        try
        {
            HttpURLConnection conn = (HttpURLConnection)(new URL(URL1).openConnection());
            br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String result = br.readLine();

            System.out.println(result);

            String line;
            final StringBuilder sb = new StringBuilder();
            while ((line = br.readLine()) != null)
            {
                sb.append(line).append("\n");
            }

            Result =  sb.toString();
            return Result;
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return null;
        }
        finally
        {
            try
            {
                if (br != null) br.close();
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }

            return Result;
        }

    }

    @Override
    protected void onPostExecute(String result)
    {
        // execution of result of Long time consuming operation

    }
}
