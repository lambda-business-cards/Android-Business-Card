package com.example.android_business_card;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.v7.app.AlertDialog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static android.content.Context.MODE_PRIVATE;
import static com.example.android_business_card.NetworkAdapter.GET;

// S04M03-4 start dao
public class BusinessCardSet implements Parcelable {


    protected BusinessCardSet(Parcel in) {
        alBusinessCard = in.createTypedArrayList(BusinessCard.CREATOR);
        strBaseURL = in.readString();
        strCards = in.readString();
        strEach = in.readString();
        strUserID = in.readString();
        strUserName = in.readString();
        strToken = in.readString();
        strPassword = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedList(alBusinessCard);
        dest.writeString(strBaseURL);
        dest.writeString(strCards);
        dest.writeString(strEach);
        dest.writeString(strUserID);
        dest.writeString(strUserName);
        dest.writeString(strToken);
        dest.writeString(strPassword);
    }

    @Override
    public int describeContents() {
        return 0;
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



    private ArrayList<BusinessCard> alBusinessCard;

    public ArrayList<BusinessCard> getBusinessCardSet() {
        return alBusinessCard;
    }


    public void setBusinessCardSet(ArrayList<BusinessCard> alb) {
        alBusinessCard = alb;
    }

    public BusinessCardSet(Context context) {
        this.context = context;
        preferences = context.getSharedPreferences("BusinessCard", MODE_PRIVATE);
        readPreferanceOfLogin();
        alBusinessCard = new ArrayList<BusinessCard>(100);
        getPeople();

        BusinessCard bc = getPerson(0);

        //alBusinessCard.add(bc);
        if(size()<3)return;
       // alBusinessCard.add(getPerson(1));
    }

    public void add(BusinessCard bc) {
        alBusinessCard.add(bc);

    }

    public BusinessCard get(int index) {
        return alBusinessCard.get(index);
    }

    public int size() {
        return alBusinessCard.size();
    }
    private static String strBaseURL="https://business-card-backend.herokuapp.com/";
    private static String strRegister="api/users/register";
    private static String strLogin="api/users/login";
    private static String strCards="api/cards";
    private static String strEach="/";


    public BusinessCardSet getPeople() {
        Map<String,String> map=new HashMap<String, String>();
        map.put("Content-Type","application/json");
        map.put("Authorization",strToken);
        final String result = NetworkAdapter.httpRequest(strBaseURL+strCards,NetworkAdapter.GET,null,map);

        try {
            JSONObject json = new JSONObject(result);
            JSONArray jsa = json.getJSONArray( "created");
            JSONObject jsonTemp;

            for(int i=0;i<jsa.length();i++){

                BusinessCard bc=new BusinessCard(jsa.getJSONObject(i));
                if(size()<2){
                    alBusinessCard.add(bc);
                }else{
                    if(isProfile(bc)){

                    }else{
                        alBusinessCard.add(bc);
                    }
                }

            }
            jsa=json.getJSONArray(  "saved");
            for(int i=0;i<jsa.length();i++){
                BusinessCard bc=new BusinessCard(jsa.getJSONObject(i));
                if(size()<2){
                    alBusinessCard.add(bc);
                }else{
                    if(isProfile(bc)){

                    }else{
                        alBusinessCard.add(bc);
                    }
                }


            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return this;
    }
    private boolean isProfile(BusinessCard bc){
        if(alBusinessCard.get(0).getId()==bc.getId()||
                alBusinessCard.get(1).getId()==bc.getId()){
            return true;
        }
        return false;
    }

    public BusinessCard getPerson(int i){

        if(size()==0){

            BusinessCard bc=new BusinessCard(1,"Akagi");
            alBusinessCard.add(bc);
        }
        return alBusinessCard.get(i);

    }

    //Network and Sharedpreferences
    // preferences
    private String strUserID;
    private String strUserName = "";
    private String strToken = "";
    private String strPassword = "";
    private Context context;
    private SharedPreferences preferences;


    public void readPreferanceOfLogin() {
        if (this.context == null) return;
        if (preferences == null) {

        } else {
            strPassword = preferences.getString("USERPASSWORD", "");
            strUserName = preferences.getString("USERNAME", "");
            strUserID = preferences.getString("USERID", "");
            strToken = preferences.getString("TOKEN", "");
        }
    }

    public void writePreferanceOfLogin() {

        if (this.context == null) return;
        if (preferences == null) {
            strUserName = "test1";
            strPassword = "test";
        } else {
            SharedPreferences.Editor editor = preferences.edit();
            editor.putString("USERPASSWORD", strPassword);
            editor.putString("USERNAME", strUserName);
            editor.putString("USERID", strUserID);
            editor.putString("TOKEN", strToken);
            // editor.clear();

            editor.commit();
        }
    }

    public void clearSharepreferences(){
        preferences.edit().clear();
        preferences.edit().commit();
    }
    ////////////////Network API

    public void registerLogin(String username, String email, String password, String phone) {
        String strURL=strBaseURL+strRegister;
        JSONObject jsn = new JSONObject();
        try {
            jsn.put("username", username);
            jsn.put("emai", email);
            jsn.put("password", password);
            jsn.put("phone", phone);
        }catch(Exception e) {
            e.getMessage();
        }
        Map<String,String> map=new HashMap<String, String>();
        map.put("Content-Type","application/json");
        final String result = NetworkAdapter.httpRequest(strURL,NetworkAdapter.POST,jsn,map);
        writePreferanceOfLogin();
    }



    public boolean loginAPI(String username,  String password){
        String strURL=strBaseURL+strLogin;
        JSONObject jsn = new JSONObject();
        try {
            jsn.put("username", username);
            jsn.put("password", password);

        }catch(Exception e) {
            e.getMessage();
        }
        Map<String,String> map=new HashMap<String, String>();
        map.put("Content-Type","application/json");
        final String result = NetworkAdapter.httpRequest(strURL,NetworkAdapter.POST,jsn,map);
        if(result==""){
            Notification.send(context,"Login error in Business card organizer" ,"Please check your username and password");
            AlertDialog dialog = new AlertDialog.Builder(context).setTitle("Error").setMessage("Login failed!").create();
            dialog.getWindow().getAttributes().windowAnimations = R.style.AnimationTopBottom;
            dialog.show();
            return false;
        }else{
            try{
                jsn=new JSONObject(result);
                strUserID=jsn.get("user_id").toString();
                strUserName=jsn.get("username").toString();
                strToken=jsn.get("token").toString();
                writePreferanceOfLogin();
            }catch (Exception e){

                Notification.send(context,"Error",e.getMessage());
            }

            return true;
        }

    }


    public boolean loginBusinessCard(String strName, String strPassword) throws JSONException {
        String strURL=strBaseURL+strLogin;
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

    public boolean saveProfile(){ //0 and 1 are reserved for owner
        if (updateAPI(0,alBusinessCard.get(0))) {
            updateAPI(1,alBusinessCard.get(1));
            writePreferanceOfLogin();
            return true;
        } else{
            return false;
        }
    }


    public void delete(int iIndex) {
        String strURL = strBaseURL + strCards + strEach + Integer.toString(iIndex);
        String POST = "DELETE";
        Map<String, String> map = new HashMap<String, String>();
        map.put("Content-Type", "application/json");
        map.put("Authorization", strToken);

        final String result = NetworkAdapter.httpRequest(strURL, POST, null, map);

        if (result == "") {

        }
    }


    public boolean getAPI(int iIndex){
        String strURL=strBaseURL+strCards+strEach+Integer.toString(iIndex);
        String POST    = "POST";
        Map<String,String> map=new HashMap<String, String>();
        map.put("Content-Type","application/json");
        map.put("Authorization",strToken);

        final String result = NetworkAdapter.httpRequest(strURL+Integer.toString(iIndex), GET,null,map);

        if(result==""){
            Notification.send(context,"Login error in Business card organizer" ,"Please check your username and password");
            return false;
        }else{
            try{
                JSONObject jsn=new JSONObject(result);
                alBusinessCard.get(iIndex).setStrName(jsn.getString("business_name"));
                alBusinessCard.get(iIndex).setStrContactName(jsn.getString("contact_name"));
                alBusinessCard.get(iIndex).setStrEmail(jsn.getString("email"));
                alBusinessCard.get(iIndex).setStrTitle(jsn.getString("title"));
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
    }

    public BusinessCard addAPI(BusinessCard bc){
        String strURL=strBaseURL+strCards;
        String POST    = "POST";
        Map<String,String> map=new HashMap<String, String>();
        map.put("Content-Type","application/json");
        map.put("Authorization",strToken);

        JSONObject jsn=new JSONObject();
        try{
            jsn.put("business_name", bc.getStrName());
            jsn.put("contact_name", bc.getStrContactName());
            jsn.put("email", bc.getStrEmail());
            jsn.put("phone", bc.getStrPhone());
            jsn.put("title", bc.getStrAddress());
            jsn.put("fax", bc.getStrFax());
            jsn.put("web_url", bc.getStrWebURL());


        }catch (Exception e){
            e.getMessage();
        }
        final String result = NetworkAdapter.httpRequest(strURL,NetworkAdapter.POST,jsn,map);


        if(result==""){
            Notification.send(context,"Login error in Business card organizer" ,"Please check your username and password");
            return bc;
        }else{
            try{
                jsn=new JSONObject(result);

               // getAPI()
                Notification.send(context, "added business card",jsn.getString("message"));
                return bc;
            }catch (Exception e){
                Notification.send(context,"Error",e.getMessage());
                return bc;
            }

        }

    }

    public boolean updateAPI(int iIndex,BusinessCard bc) {

       String strURL = strBaseURL+strCards+strEach+Integer.toString(alBusinessCard.get(iIndex).getId());
        String POST = "POST";
        Map<String, String> map = new HashMap<String, String>();
        map.put("Content-Type", "application/json");
        map.put("Authorization", strToken);

        JSONObject jsn = new JSONObject();
        try {
            jsn.put("business_name", bc.getStrName());
            jsn.put("contact_name", bc.getStrContactName());
            jsn.put("title", bc.getStrContactName());

            jsn.put("email", bc.getStrEmail());
            jsn.put("phone", bc.getStrPhone());
            jsn.put("title", bc.getStrAddress());
            jsn.put("fax", bc.getStrFax());
            jsn.put("web_url", bc.getStrWebURL());


        } catch (Exception e) {
            e.getMessage();
        }
        final String result = NetworkAdapter.httpRequest(strURL, NetworkAdapter.PUT, jsn, map);

        if (result == "") {
            Notification.send(context, "Login error in Business card organizer", "Please check your username and password");
            return false;
        } else {
            try {
                jsn = new JSONObject(result);
                return (jsn.getString("message").equals("Success"));
            } catch (Exception e) {
                Notification.send(context, "Error", e.getMessage());
                return false;
            }

        }
    }

    public void saveAll(){
        for(int i=0;i<size();i++){
            updateAPI(i,alBusinessCard.get(i));

        }

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
        alBusinessCard.get(0).setStrEmail(strSet[i++]);
        alBusinessCard.get(0).setStrPhone(strSet[i++]);
        alBusinessCard.get(0).setStrAddress(strSet[i++]);
        alBusinessCard.get(0).setStrFax(strSet[i++]);
        alBusinessCard.get(0).setStrWebURL(strSet[i++]);
        alBusinessCard.get(0).setStrQRcodeURL(strSet[i++]);
        alBusinessCard.get(1).isChecked = Boolean.valueOf(strSet[i++]);
        alBusinessCard.get(0).setStrContactName(strSet[i]); //Company name in personal info
        alBusinessCard.get(1).setStrName(strSet[i++]);
        alBusinessCard.get(1).setStrEmail(strSet[i++]);
        alBusinessCard.get(1).setStrPhone(strSet[i++]);
        alBusinessCard.get(1).setStrImageURL(strSet[i++]);
        alBusinessCard.get(1).setStrTitle(strSet[i++]);
        alBusinessCard.get(1).setStrAddress(strSet[i++]);
        alBusinessCard.get(1).setStrFax(strSet[i++]);
        alBusinessCard.get(1).setStrWebURL(strSet[i++]);
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
        strSet[i++]=alBusinessCard.get(0).getStrPhone();
        strSet[i++]=alBusinessCard.get(0).getStrImageURL();
        strSet[i++]=alBusinessCard.get(0).getStrAddress();
        strSet[i++]=alBusinessCard.get(0).getStrFax();
        strSet[i++]=alBusinessCard.get(0).getStrWebURL();
        strSet[i++]=alBusinessCard.get(0).getStrQRcodeURL();
        strSet[i++]=Boolean.toString(alBusinessCard.get(1).isChecked);
        strSet[i++]=alBusinessCard.get(1).getStrName();
        strSet[i++]=alBusinessCard.get(1).getStrEmail();
        strSet[i++]=alBusinessCard.get(1).getStrPhone();
        strSet[i++]=alBusinessCard.get(1).getStrImageURL();
        strSet[i++]=alBusinessCard.get(1).getStrTitle();
        strSet[i++]=alBusinessCard.get(1).getStrAddress();
        strSet[i++]=alBusinessCard.get(1).getStrFax();
        strSet[i++]=alBusinessCard.get(1).getStrWebURL();
        strSet[i++]=alBusinessCard.get(1).getStrQRcodeURL();//22
        return strSet;
    }

}
