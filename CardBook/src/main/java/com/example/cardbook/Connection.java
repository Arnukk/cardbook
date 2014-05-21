package com.example.cardbook;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import org.apache.http.NameValuePair;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

public class Connection {

    public String Method;
    public String URL;
    public Connection(String Method, String URL){
        this.Method = Method;
        this.URL = URL;
    }
    // Given a URL, this method establishes an HttpUrlConnection and retrieves
    // the web page content as an InputStream, which it returns as
    // a string.
    public Integer downloadUrl(List<NameValuePair> params) throws IOException {
        InputStream is = null;
        // Only display the first 500 characters of the retrieved
        // web page content.
        int len = 500;
        try {
            URL url = new URL(this.URL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(10000); /* milliseconds */
            conn.setConnectTimeout(15000);/* milliseconds */
            conn.setRequestMethod(this.Method);
            conn.setDoInput(true);
            conn.setRequestProperty("Content-Type","application/x-www-form-urlencoded");
                conn.setRequestProperty("Content-Length", Integer.toString(getQuery(params).length()));
                conn.setDoOutput(true);
                OutputStream os = conn.getOutputStream();
                BufferedWriter writer = new BufferedWriter(
                        new OutputStreamWriter(os, "UTF-8"));
                writer.write(getQuery(params));
                writer.flush();
                writer.close();
                os.close();
            conn.connect();
            int response = conn.getResponseCode();
            StringBuffer responset = new StringBuffer();
            try{
                is = conn.getInputStream();
                if (is != null){
                    BufferedReader rd = new BufferedReader(new InputStreamReader(is));
                    String line;
                    while((line = rd.readLine()) != null) {
                        responset.append(line);
                        responset.append('\r');
                    }
                    rd.close();
                }
            }catch (Exception e) {
                e.printStackTrace();
            }

            Log.i("++++++++++++++++++++++++++++++++++++++++++++++++", Integer.toString(response));
            Log.i("++++++++++++++++++++++++++++++++++++++++++++++++", responset.toString());
            return response;

            // Makes sure that the InputStream is closed after the app is
            // finished using it.
        } finally {
            if (is != null) {
                is.close();
            }
        }

    }

    private String getQuery(List<NameValuePair> params) throws UnsupportedEncodingException
    {
        StringBuilder result = new StringBuilder();
        boolean first = true;

        for (NameValuePair pair : params)
        {
            if (first)
                first = false;
            else
                result.append("&");

            result.append(URLEncoder.encode(pair.getName(), "UTF-8"));
            result.append("=");
            result.append(URLEncoder.encode(pair.getValue(), "UTF-8"));
        }

        return result.toString();
    }


    private String convertStreamToString(InputStream is) {
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();

        String line = null;
        try {
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return sb.toString();
    }

    // Given a URL, this method establishes an HttpUrlConnection and retrieves
    // the web page content as an InputStream, which it returns as
    // a string.
    public String getVisitcarddetails() throws IOException {
        InputStream is = null;
        // Only display the first1 500 characters of the retrieved
        // web page content.
        int len = 1500;
        try {
            URL url = new URL(this.URL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(10000); /* milliseconds */
            conn.setConnectTimeout(15000);/* milliseconds */
            conn.setRequestMethod(this.Method);
            conn.setDoInput(true);
            // Starts the query
            conn.connect();
            String data = null;
            try{
                is = conn.getInputStream();
                data = convertStreamToString(is);
            }catch (Exception e){
                e.printStackTrace();
            }
            return data;

            // Makes sure that the InputStream is closed after the app is
            // finished using it.
        } finally {
            if (is != null) {
                is.close();
            }
        }

    }

    public String getbump(List<NameValuePair> params) throws IOException {
        InputStream is = null;
        // Only display the first1 500 characters of the retrieved
        // web page content.
        int len = 1500;
        try{
            URL url = new URL(this.URL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(10000); /* milliseconds */
            conn.setConnectTimeout(15000);/* milliseconds */
            conn.setRequestMethod(this.Method);
            conn.setDoInput(true);
            conn.setRequestProperty("Content-Type","application/x-www-form-urlencoded");
            conn.setRequestProperty("Content-Length", Integer.toString(getQuery(params).length()));
            conn.setDoOutput(true);
            OutputStream os = conn.getOutputStream();
            BufferedWriter writer = new BufferedWriter(
                    new OutputStreamWriter(os, "UTF-8"));
            writer.write(getQuery(params));
            writer.flush();
            writer.close();
            os.close();
            conn.connect();
            String data = null;
            try{
                is = conn.getInputStream();
                data = convertStreamToString(is);
            }catch (Exception e) {
                e.printStackTrace();
            }
            return data;
        } finally {
            if (is != null) {
                is.close();
            }
        }
    }

}