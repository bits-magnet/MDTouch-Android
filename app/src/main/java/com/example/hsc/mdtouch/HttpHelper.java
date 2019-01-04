package com.example.hsc.mdtouch;

import android.util.Log;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class HttpHelper {

    public HttpHelper(){

    }

    public String get(String reqUrl){

        String response = null;

        try{

            URL url = new URL(reqUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            InputStream in = new BufferedInputStream(connection.getInputStream());
            response = convertToString(in);

        } catch (IOException ignored) {

        }

        return response;

    }

    public void put(String reqUrl,int value){

        try{

            URL url = new URL(reqUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setDoOutput(true);
            conn.setRequestMethod("PUT");
            conn.setRequestProperty("Content-Type","application/json");

            OutputStreamWriter out = new OutputStreamWriter(conn.getOutputStream());
            out.write("{ \"status\" : "+value+" }");
            out.flush();
            out.close();

            Log.i("TAG", this.get(reqUrl));
            Log.i("TAG",""+conn.getResponseCode());

        } catch (IOException ignored) {

        }

    }

    public String convertToString(InputStream in){

        BufferedReader reader = new BufferedReader(new InputStreamReader(in));
        StringBuilder sb = new StringBuilder();

        String line;

        try{
            while((line = reader.readLine()) != null){
                sb.append(line);
            }

        } catch (IOException ignored) {

        }finally {
            try{
                in.close();
            } catch (IOException ignored) {
            }
        }

        return sb.toString();
    }

}