package com.example.android_business_card;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;

// S04M03-1 Add Network Adapter
// S03M03-1 build and test firebase database
// S03M03-2 Add basic network adapter
public class NetworkAdapter {
    public static final String GET     = "GET";
    public static final String POST    = "POST";
    public static final String HEAD    = "HEAD";
    public static final String OPTIONS = "OPTIONS";
    public static final String PUT     = "PUT";
    public static final String DELETE  = "DELETE";
    public static final String TRACE   = "TRACE";

    static String httpRequest(String urlString) {
        return httpRequest(urlString, GET, null, null);
    }

    static String httpRequest(String urlString, String requestMethod) {
        return httpRequest(urlString, requestMethod, null, null);
    }

    static String httpRequest(String urlString, String requestMethod, JSONObject requestBody, Map<String, String> headerProperties) {
        String             result      = "";
        InputStream        inputStream = null;
        HttpsURLConnection connection  = null;

        try {
            URL url = new URL(urlString);
            connection = (HttpsURLConnection) url.openConnection();

            connection.setRequestMethod(requestMethod);

            if (headerProperties != null) {
                for (Map.Entry<String, String> property : headerProperties.entrySet()) {
                    connection.setRequestProperty(property.getKey(), property.getValue());
                }
            }

            // S03M03-10 add support for different types of request
            if((requestMethod.equals(POST) || requestMethod.equals(PUT)) && requestBody != null) {
                // S03M03-11 write body of post request
                connection.setDoInput(true);
                final OutputStream outputStream = connection.getOutputStream();
                outputStream.write(requestBody.toString().getBytes());
                outputStream.close();
            } else {
                try {
                    connection.connect();
                }catch (Exception e){
                    e.getMessage();
                }

            }

            final int responseCode = connection.getResponseCode();

          //debug  inputStream = connection.getInputStream();
            if (responseCode == HttpsURLConnection.HTTP_OK||responseCode == 201) {
                inputStream = connection.getInputStream();
                inputStream = connection.getInputStream();
                if (inputStream != null) {
                    BufferedReader reader  = new BufferedReader(new InputStreamReader(inputStream));
                    StringBuilder  builder = new StringBuilder();

                    String line;
                    do {
                        line = reader.readLine();
                        builder.append(line);
                    } while (line != null);
                    result = builder.toString();
                }
            }

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            if (connection != null) {
                connection.disconnect();
            }
        }
        return result;

    }


    public static final int TIMEOUT = 3000;


    public static Bitmap getBitmapFromUrl(String stringUrl) {
       //debug stringUrl="http://res.cloudinary.com/dhupmye0m/image/upload/v1556250023/jvdsiob81mf9pz0cr4u9.png";

        Bitmap result = null;
        InputStream stream = null;
        HttpURLConnection connection = null;
        try {
            URL url = new URL(stringUrl);
            connection = (HttpURLConnection) url.openConnection();
            connection.setReadTimeout(TIMEOUT);
            connection.setConnectTimeout(TIMEOUT);

            connection.connect();

            if(connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                stream = connection.getInputStream();
                if(stream != null) {
                    result = BitmapFactory.decodeStream(stream);
                }
            }

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if(connection != null) {
                connection.disconnect();
            }

            if(stream != null) {
                try {
                    stream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return result;
    }


}
