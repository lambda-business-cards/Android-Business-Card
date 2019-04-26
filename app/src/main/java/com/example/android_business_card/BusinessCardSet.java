package com.example.android_business_card;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.v7.app.AlertDialog;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static android.content.Context.MODE_PRIVATE;

// S04M03-4 start dao
public class BusinessCardSet implements Parcelable {

    protected BusinessCardSet(Parcel in) {
        alBusinessCard = in.createTypedArrayList(BusinessCard.CREATOR);
        strUserID = in.readString();
        strUserName = in.readString();
        strToken = in.readString();
        strPassword = in.readString();
    }

    public static final Creator<BusinessCardSet> CREATOR = new Creator<BusinessCardSet>() {
        @Override
        public BusinessCardSet createFromParcel(Parcel in) {
            return new BusinessCardSet(in);
        }

        @Override
        public BusinessCardSet[] newArray(int size) {
            return new BusinessCardSet[size];
        }
    };

    public ArrayList<BusinessCard> getAlBusinessCard() {
        return alBusinessCard;
    }

    public void setAlBusinessCard(ArrayList<BusinessCard> alBusinessCard) {
        this.alBusinessCard = alBusinessCard;
    }

    public static Creator<BusinessCardSet> getCREATOR() {
        return CREATOR;
    }

    public String getStrUserID() {
        return strUserID;
    }

    public void setStrUserID(String strUserID) {
        this.strUserID = strUserID;
    }

    public String getStrUserName() {
        return strUserName;
    }

    public void setStrUserName(String strUserName) {
        this.strUserName = strUserName;
    }

    public String getStrToken() {
        return strToken;
    }

    public void setStrToken(String strToken) {
        this.strToken = strToken;
    }

    public String getStrPassword() {
        return strPassword;
    }

    public void setStrPassword(String strPassword) {
        this.strPassword = strPassword;
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public SharedPreferences getPreferences() {
        return preferences;
    }

    public void setPreferences(SharedPreferences preferences) {
        this.preferences = preferences;
    }

    public static String getBaseUrl() {
        return BASE_URL;
    }

    public static String getPersonUrl() {
        return PERSON_URL;
    }

    public static String getStarshipUrl() {
        return STARSHIP_URL;
    }

    private ArrayList<BusinessCard> alBusinessCard;

    public ArrayList<BusinessCard> getBusinessCardSet(){
        return alBusinessCard;
    }


    public void setBusinessCardSet(ArrayList<BusinessCard> alb ){
        alBusinessCard=alb;
    }

    public BusinessCardSet(Context context) {
        this.context=context;
        preferences = context.getSharedPreferences("BusinessCard", MODE_PRIVATE);
        readPreferanceOfLogin();
        BusinessCard bc=getPerson(1);
        alBusinessCard=new ArrayList<BusinessCard>(100);
        alBusinessCard.add(bc);
        alBusinessCard.add(getPerson(2));
    }

    public void add(BusinessCard bc){
        alBusinessCard.add(bc);

    }
    public BusinessCard get(int index){
        return alBusinessCard.get(index);
    }

    public int size(){
        return alBusinessCard.size();
    }



    public static BusinessCard getPerson(int id) {
        final String result = NetworkAdapter.httpRequest(PERSON_URL + id);

        BusinessCard object = null;
        try {
            JSONObject json = new JSONObject(result);

            object = new BusinessCard(id, json.getString("name"));
            object.setStrImageURL(DrawableResolver.CHARACTER);
            object.setStrContactName(json.getString("hair_color"));
            object.setStrAddress(json.getString("homeworld"));
            object.setStrWebURL(json.getString("url"));

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return object;
    }


    //Network and Sharedpreferences
    // preferences
    private  String strUserID;
    private  String strUserName="";
    private  String strToken="";
    private  String strPassword="";
    private  Context context;
    private  SharedPreferences preferences;


    public void readPreferanceOfLogin(){
        if(this.context==null)return;
        if(preferences==null){

        }else{
            strPassword=preferences.getString("USERPASSWORD","");
            strUserName=preferences.getString("USERNAME","");
            strUserID=preferences.getString("USERID","");
            strToken=preferences.getString("TOKEN","");
        }

        //initial test
     //   if(strPassword.equals(""))strPassword="test";
     //   if(strUserName.equals(""))strUserName="test1";
      //  writePreferanceOfLogin();
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
            editor.clear();

            editor.commit();
        }


    }
    ////////////////Network API
    /////Initial dummy
    private static final String BASE_URL = "https://swapi.co/api/";
    private static final String PERSON_URL = BASE_URL + "people/";
    private static final String STARSHIP_URL = BASE_URL + "starships/";

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
            Notification.send(context,"Login error in Business card organizer" ,"Please check your username and password");
            AlertDialog dialog = new AlertDialog.Builder(context).setTitle("Error").setMessage("Login failed!").create();
//                dialog.getWindow().getAttributes().windowAnimations = R.style.AnimationSpin;
            dialog.getWindow().getAttributes().windowAnimations = R.style.AnimationTopBottom;
            dialog.show();
            return false;
        }else{
            try{
                jsn=new JSONObject(result);
            }catch (Exception e){

                Notification.send(context,"Error",e.getMessage());
            }
            strUserID=jsn.get("user_id").toString();
            strUserName=jsn.get("username").toString();
            strToken=jsn.get("token").toString();
            writePreferanceOfLogin();
            return true;
        }
    }

    public boolean saveProfile(){
        String strURL="https://business-card-backend.herokuapp.com/api/cards";
        String POST    = "POST";
        Map<String,String> map=new HashMap<String, String>();
        map.put("Content-Type","application/json");
        map.put("Authorization",strToken);

        JSONObject jsn=new JSONObject();
        try{
            jsn.put("business_name", alBusinessCard.get(0).getStrName());
            jsn.put("contact_name", alBusinessCard.get(0).getStrContactName());
            jsn.put("email", alBusinessCard.get(0).getStrEmail());
        }catch (Exception e){
            e.getMessage();
        }
        final String result = NetworkAdapter.httpRequest(strURL,NetworkAdapter.POST,jsn,map);

        if(result==""){
            Notification.send(context,"Login error in Business card organizer" ,"Please check your username and password");
            return false;
        }else{
            if(saveResult(0,result)){
                return saveResult(1,result);
            }else{
                return false;
            }
        }
    }

    public boolean saveResult(int iIndex, String result){
        try{
            JSONObject jsn=new JSONObject(result);
            alBusinessCard.get(iIndex).setStrName(jsn.getString("business_name"));
            alBusinessCard.get(iIndex).setStrContactName(jsn.getString("contact_name"));
            alBusinessCard.get(iIndex).setStrEmail(jsn.getString("email"));
            alBusinessCard.get(iIndex).setStrPhone(jsn.getString("phone"));
            alBusinessCard.get(iIndex).setStrAddress(jsn.getString("address"));
            alBusinessCard.get(iIndex).setStrFax(jsn.getString("fax"));
            alBusinessCard.get(iIndex).setStrWebURL(jsn.getString("web_url"));
        }catch (Exception e){
            Notification.send(context,"Error",e.getMessage());
            return false;
        }
        return true;
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
        dest.writeTypedList(alBusinessCard);
        dest.writeString(strUserID);
        dest.writeString(strUserName);
        dest.writeString(strToken);
        dest.writeString(strPassword);
    }


    public void setStringUser(String[] strSet){
        int i=0;
        alBusinessCard.get(0).setStrName(strSet[i]);
        alBusinessCard.get(1).setStrContactName(strSet[i++]);//Link to person in compnay info
        strUserName=strSet[i++];
        strPassword=strSet[i++];
        alBusinessCard.get(0).isChecked = Boolean.valueOf(strSet[i++]);
        alBusinessCard.get(0).setStrTitle("personal");
        alBusinessCard.get(0).setStrImageURL(strSet[i++]);
        alBusinessCard.get(0).setStrAddress(strSet[i++]);
        alBusinessCard.get(0).setStrPhone(strSet[i++]);
        alBusinessCard.get(0).setStrEmail(strSet[i++]);
        alBusinessCard.get(0).setStrWebURL(strSet[i++]);
        alBusinessCard.get(0).setStrFax(strSet[i++]);
        alBusinessCard.get(0).setStrQRcodeURL(strSet[i++]);
        alBusinessCard.get(0).setStrWebURL(strSet[i++]);
        alBusinessCard.get(1).isChecked = Boolean.valueOf(strSet[i++]);
        alBusinessCard.get(0).setStrContactName(strSet[i]); //Company name in personal info
        alBusinessCard.get(1).setStrName(strSet[i++]);
        alBusinessCard.get(1).setStrEmail(strSet[i++]);
        alBusinessCard.get(1).setStrImageURL(strSet[i++]);
        alBusinessCard.get(1).setStrTitle(strSet[i++]);
        alBusinessCard.get(1).setStrAddress(strSet[i++]);
        alBusinessCard.get(1).setStrPhone(strSet[i++]);
        alBusinessCard.get(1).setStrWebURL(strSet[i++]);
        alBusinessCard.get(1).setStrFax(strSet[i++]);
        alBusinessCard.get(1).setStrQRcodeURL(strSet[i++]);
    }

    public String[] getStringUser(){
        String[] strSet=new String[22];
        int i=0;
        strSet[i++]=alBusinessCard.get(0).getStrName();
        strSet[i++]=strUserName;
        strSet[i++]=strPassword;

        strSet[i++]=Boolean.toString(alBusinessCard.get(0).isChecked);
        strSet[i++]=alBusinessCard.get(0).getStrEmail();
        strSet[i++]=alBusinessCard.get(0).getStrImageURL();
        strSet[i++]=alBusinessCard.get(0).getStrAddress();
        strSet[i++]=alBusinessCard.get(0).getStrPhone();
        strSet[i++]=alBusinessCard.get(0).getStrWebURL();
        strSet[i++]=alBusinessCard.get(0).getStrFax();
        strSet[i++]=alBusinessCard.get(0).getStrQRcodeURL();
        strSet[i++]=Boolean.toString(alBusinessCard.get(1).isChecked);
        strSet[i++]=alBusinessCard.get(1).getStrName();
        strSet[i++]=alBusinessCard.get(1).getStrEmail();
        strSet[i++]=alBusinessCard.get(1).getStrImageURL();
        strSet[i++]=alBusinessCard.get(1).getStrTitle();
        strSet[i++]=alBusinessCard.get(1).getStrAddress();
        strSet[i++]=alBusinessCard.get(1).getStrPhone();
        strSet[i++]=alBusinessCard.get(1).getStrWebURL();
        strSet[i++]=alBusinessCard.get(1).getStrFax();
        strSet[i++]=alBusinessCard.get(1).getStrQRcodeURL();//22

        return strSet;
    }

}