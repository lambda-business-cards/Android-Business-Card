package com.example.android_business_card;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

// S04M03-4 start dao
public class BusinessCardDAO {
    private static final String BASE_URL = "https://swapi.co/api/";
    private static final String PERSON_URL = BASE_URL + "people/";
    private static final String STARSHIP_URL = BASE_URL + "starships/";

    // S04M03-5 write method to get person
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

    public static boolean loginBusinessCard(String strName, String strPassword) throws JSONException {
        String strURL="https://business-card-backend.herokuapp.com/api/users/login";
        String POST    = "POST";
        Map<String,String> map=new HashMap<String, String>();
        map.put("Content-Type","application/json");

        String strJSON="{\"username\":\""+strName+"\",\"password\",\""+strPassword+"\"}";
        JSONObject jsn=new JSONObject();
        try{
            jsn.put("username",strName);
            jsn.put("password",strPassword);
        }catch (Exception e){
            e.getMessage();
        }


        final String result = NetworkAdapter.httpRequest(strURL,NetworkAdapter.POST,jsn,map);

        if(result==""){
            return false;
        }else{
            return true;
        }

    }

}
