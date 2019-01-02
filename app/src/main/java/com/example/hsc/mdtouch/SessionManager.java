package com.example.hsc.mdtouch;

import android.content.Context;
import android.content.SharedPreferences;

public class SessionManager {

    SharedPreferences pref;

    SharedPreferences.Editor edit;

    Context context;

    int PRIVATE = 0;

    private static final String PREF_NAME = "MySession";

    private static final String NAME = "name";
    private static final String USERNAME = "username";
    private static final String NUMBER = "number";
    private static final String DATA = "data";
    private static final String USERID = "userId";
    private static final String TYPE = "Type";
    private static final String LOGGED = "logged";

    public SessionManager(Context context){
        this.context = context;
        pref = context.getSharedPreferences(PREF_NAME,PRIVATE);
        edit = pref.edit();
    }

    public void createSession(String name,String username,String number,String data,String uId,String type,String logged){

        edit.putString(NAME,name);
        edit.putString(USERNAME,username);
        edit.putString(NUMBER,number);
        edit.putString(DATA,data);
        edit.putString(USERID,uId);
        edit.putString(TYPE,type);
        edit.putString(LOGGED,logged);
        edit.commit();

    }

    public void clearSession(){
        edit.clear();
        edit.commit();
    }

    public String getName(){
        return pref.getString(NAME,null);
    }

    public String getUsername(){
        return pref.getString(USERNAME,null);
    }

    public String getNumber(){
        return pref.getString(NUMBER,null);
    }

    public String getUserId(){
        return pref.getString(USERID,null);
    }

    public String getData(){
        return pref.getString(DATA,null);
    }

    public String getType(){
        return pref.getString(TYPE,null);
    }

    public String getStatus(){
        return pref.getString(LOGGED,null);
    }

}