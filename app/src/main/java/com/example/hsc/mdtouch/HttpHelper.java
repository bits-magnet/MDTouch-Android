package com.example.hsc.mdtouch;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
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