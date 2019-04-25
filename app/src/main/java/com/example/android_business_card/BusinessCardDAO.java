package com.example.android_business_card;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.v7.app.AlertDialog;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import static android.content.Context.MODE_PRIVATE;

// S04M03-4 start dao
public class BusinessCardDAO implements Parcelable {
    public BusinessCardDAO(Context context) {
        this.context=context;
        preferences = context.getSharedPreferences("BusinessCard", MODE_PRIVATE);
    }

    private static final String BASE_URL = "https://swapi.co/api/";
    private static final String PERSON_URL = BASE_URL + "people/";
    private static final String STARSHIP_URL = BASE_URL + "starships/";


    protected BusinessCardDAO(Parcel in) {
    }

    public static final Creator<BusinessCardDAO> CREATOR = new Creator<BusinessCardDAO>() {
        @Override
        public BusinessCardDAO createFromParcel(Parcel in) {
            return new BusinessCardDAO(in);
        }

        @Override
        public BusinessCardDAO[] newArray(int size) {
            return new BusinessCardDAO[size];
        }
    };

    public static BusinessCard getPerson(int id) {
        final String result = NetworkAdapter.httpRequest(PERSON_URL + id);

        BusinessCard object = null;
        try {
            JSONObject json = new JSONObject(result);

            object = new BusinessCard(id, json.getString("name"));
            object.setCategory(DrawableResolver.CHARACTER);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return object;
    }

    public static BusinessCard getStarship(int id) {
        final String result = NetworkAdapter.httpRequest(STARSHIP_URL + id);

        BusinessCard object = null;
        try {
            JSONObject json = new JSONObject(result);

            object = new BusinessCard(id, json.getString("name"));
            object.setCategory(DrawableResolver.STARSHIP);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return object;
    }

    //Network and Sharedpreferences
    // preferences
    private static String strUserID;
    private static String strUserName="";
    private static String strToken="";
    private static String strPassword="";
    private static Context context;
    private static SharedPreferences preferences;


    public void readPreferanceOfLogin(){
        if(this.context==null)return;
        if(preferences==null){

        }else{
            strPassword=preferences.getString("PASSWORD","");
            strUserName=preferences.getString("USERNAME","");
            strUserID=preferences.getString("USERID","");
            strToken=preferences.getString("TOKEN","");
        }
        if(strPassword.equals(""))strPassword="test";

    }

    public void writePreferanceOfLogin(){
        if(this.context==null)return;
        if(preferences==null){
            strUserName="test1";
            strPassword="test";
        }else{
            SharedPreferences.Editor editor = preferences.edit();
            editor.putString("USERPASSWORD",strPassword);
            editor.putString("USERNAME",strUserName);
            editor.putString("USERID",strUserID);
            editor.putString("TOKEN",strToken);

            editor.commit();
        }
    }

    public boolean loginBusinessCard() {
        if(strUserName.equals("")||strPassword.equals("")){
            readPreferanceOfLogin();
        }

        try {
            return loginBusinessCard(strUserName,strPassword);

        } catch (Exception e) {
            return false;
        }
    }


    public boolean loginBusinessCard(String strName, String strPassword) throws JSONException {
        String strURL="https://business-card-backend.herokuapp.com/api/users/login";
        String POST    = "POST";
        Map<String,String> map=new HashMap<String, String>();
        map.put("Content-Type","application/json");

        JSONObject jsn=new JSONObject();
        try{
            jsn.put("username",strName);
            jsn.put("password",strPassword);
        }catch (Exception e){
            e.getMessage();
        }
        final String result = NetworkAdapter.httpRequest(strURL,NetworkAdapter.POST,jsn,map);

        if(result==""){

            AlertDialog dialog = new AlertDialog.Builder(context).setTitle("Error").setMessage("Login failed!").create();
//                dialog.getWindow().getAttributes().windowAnimations = R.style.AnimationSpin;
            dialog.getWindow().getAttributes().windowAnimations = R.style.AnimationTopBottom;
            dialog.show();
            return false;
        }else{
            try{
                jsn=new JSONObject(result);
            }catch (Exception e){
                e.getMessage();
            }
            strUserID=jsn.get("user_id").toString();
            strUserName=jsn.get("username").toString();
            strToken=jsn.get("token").toString();
            writePreferanceOfLogin();
            return true;
        }
    }
    public String[] getStringUserInfo(){
        String[] strTemp=new String[20];

        return strTemp;
    }
    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
    }
}
