package com.example.android_business_card;

import org.json.JSONException;
import org.json.JSONObject;

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
}
