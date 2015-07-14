package com.byteshaft.locationlogger.utils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

public class WebServiceHelpers {

    private static final String APP_NAME = "LocationLogger";
    private static final String SESSION_URL = (
            "http://mirastatgps1.cloudapp.net:80/rest/user/session"
    );
    private static final String DATABASE_URL = (
            "http://mirastatgps1.cloudapp.net:80/rest/TestThree/TableThree"
    );

    public static String getSessionId(String email, String password)
            throws IOException, JSONException {

        String data = String.format("{\"email\" : \"%s\", \"password\" : \"%s\"}", email, password);
        HttpURLConnection connection = openConnectionForUrl(SESSION_URL);
        sendRequestData(connection, data);
        JSONObject jsonObj = readResponse(connection);
        return (String) jsonObj.get("session_id");
    }

    public static void writeRecords(String sessionId, ArrayList<HashMap> records)
            throws IOException, JSONException {

        String data = getJsonStringForRecords(records);
        HttpURLConnection connection = openConnectionForUrl(DATABASE_URL);
        connection.setRequestProperty("X-DreamFactory-Session-Token", sessionId);
        connection.connect();
        sendRequestData(connection, data);
        JSONObject jsonObj = readResponse(connection);
        System.out.println(jsonObj.toString());
    }

    private static String getJsonStringForRecords(ArrayList<HashMap> records) {
        StringBuilder builder = new StringBuilder();
        builder.append("{ \"record\": [ ");
        for (HashMap map : records) {
            String longitude = (String) map.get("longitude");
            String latitude = (String) map.get("latitude");
            String timeStamp = (String) map.get("time_stamp");
            String userId = (String) map.get("user_id");
            String jsonObjectString = getJsonObjectString(latitude, longitude, timeStamp, userId);
            builder.append(jsonObjectString);
            builder.append(", ");
        }
        builder.deleteCharAt(builder.length() - 2);
        builder.append("] }");
        return builder.toString();
    }

    private static String getJsonObjectString(String latitude, String longitude, String timeStamp,
                                              String userId) {

        return String.format(
                "{\"lat\": %s, \"lon\": %s, \"PulseDateTime\": \"%s\", \"userident\": \"%s\"}",
                latitude, longitude, timeStamp, userId
        );
    }

    public static void writeRecord(String session_id, String latitude, String longitude,
                                   String timeStamp, String userId)
            throws IOException, JSONException {

        String data = getJsonObjectString(latitude, longitude, timeStamp, userId);
        HttpURLConnection connection = openConnectionForUrl(DATABASE_URL);
        connection.setRequestProperty("X-DreamFactory-Session-Token", session_id);
        connection.connect();
        sendRequestData(connection, data);
        JSONObject jsonObj = readResponse(connection);
        System.out.println(jsonObj.get("ID"));
    }

    private static HttpURLConnection openConnectionForUrl(String path)
            throws IOException {

        URL url = new URL(path);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestProperty("Content-Type", "application/json");
        connection.setRequestProperty("X-DreamFactory-Application-Name", APP_NAME);
        connection.setRequestMethod("POST");
        return connection;
    }

    private static void sendRequestData(HttpURLConnection connection, String body)
            throws IOException {

        byte[] outputInBytes = body.getBytes("UTF-8");
        OutputStream os = connection.getOutputStream();
        os.write(outputInBytes);
        os.close();
    }

    private static JSONObject readResponse(HttpURLConnection connection)
            throws IOException, JSONException {

        InputStream is = connection.getInputStream();
        BufferedReader rd = new BufferedReader(new InputStreamReader(is));
        String line;
        StringBuilder response = new StringBuilder();
        while((line = rd.readLine()) != null) {
            response.append(line);
            response.append('\r');
        }
        return new JSONObject(response.toString());
    }
}
