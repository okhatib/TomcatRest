package com.test.rest;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Base64;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.URL;
import java.util.Scanner;

import javax.net.ssl.HttpsURLConnection;

/**
 * Created by Maro on 09/09/13.
 */
public class JSONMethods extends Activity {

    public final static String baseURL = "http://tomcatrest-skyhawk.rhcloud.com/rest/hello/cust";
    public final static String secureBaseURL = "https://tomcatrest-skyhawk.rhcloud.com/rest/hello/cust";

    private SharedPreferences mPrefs;

    String usernameTxt;
    String passwordTxt;

    TextView jsonUrlText;
    TextView jsonOutputText;
    EditText jsonIdIn;
    EditText jsonFirstNameIn;
    EditText jsonLastNameIn;
    EditText jsonStreetIn;
    EditText jsonStateIn;
    EditText jsonCityIn;
    EditText jsonZipIn;
    EditText jsonCountryIn;

    String inputJson;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.json_activity);

        mPrefs = PreferenceManager.getDefaultSharedPreferences(JSONMethods.this);

        usernameTxt = mPrefs.getString("username", "");
        passwordTxt = mPrefs.getString("password", "");

        jsonUrlText = (TextView)findViewById(R.id.jsonUrlText);
        jsonOutputText = (TextView)findViewById(R.id.jsonOutputText);

        jsonIdIn = (EditText)findViewById(R.id.jsonIDTxt);
        jsonFirstNameIn = (EditText)findViewById(R.id.jsonFirstNameTxt);
        jsonLastNameIn = (EditText)findViewById(R.id.jsonLastNameTxt);
        jsonStateIn = (EditText)findViewById(R.id.jsonStateTxt);
        jsonStreetIn = (EditText)findViewById(R.id.jsonStreetTxt);
        jsonCityIn = (EditText)findViewById(R.id.jsonCityTxt);
        jsonCountryIn = (EditText)findViewById(R.id.jsonCountryTxt);
        jsonZipIn = (EditText)findViewById(R.id.jsonZipTxt);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    public void jsonBtnGet_Click(View v)
    {
        GetJsonData getJson = new GetJsonData();

        String responseUrl = secureBaseURL + "/get/" + jsonIdIn.getText().toString();

        getJson.execute(new String[] { responseUrl });

        jsonUrlText.setText(responseUrl);
    }

    public void jsonBtnPost_Click(View v) throws JSONException
    {
        PostJsonData postJson = new PostJsonData();

        String responseUrl = secureBaseURL + "/post/";

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("firstName", jsonFirstNameIn.getText().toString());
        jsonObject.put("lastName", jsonLastNameIn.getText().toString());
        jsonObject.put("street", jsonStreetIn.getText().toString());
        jsonObject.put("state", jsonStateIn.getText().toString());
        jsonObject.put("city", jsonCityIn.getText().toString());
        jsonObject.put("zip", jsonZipIn.getText().toString());
        jsonObject.put("country", jsonCountryIn.getText().toString());

        inputJson = jsonObject.toString();

        postJson.execute(new String[] { responseUrl });

        jsonUrlText.setText(responseUrl);
    }

    public void jsonBtnPut_Click(View v) throws JSONException
    {
        PutJsonData postJson = new PutJsonData();

        String responseUrl = secureBaseURL + "/put/" + jsonIdIn.getText().toString();

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("firstName", jsonFirstNameIn.getText().toString());
        jsonObject.put("lastName", jsonLastNameIn.getText().toString());
        jsonObject.put("street", jsonStreetIn.getText().toString());
        jsonObject.put("state", jsonStateIn.getText().toString());
        jsonObject.put("city", jsonCityIn.getText().toString());
        jsonObject.put("zip", jsonZipIn.getText().toString());
        jsonObject.put("country", jsonCountryIn.getText().toString());

        inputJson = jsonObject.toString();

        postJson.execute(new String[] { responseUrl });

        jsonUrlText.setText(responseUrl);
    }

    public void jsonBtnDelete_Click(View v)
    {
        DeleteJsonData deleteJson = new DeleteJsonData();

        String responseUrl = secureBaseURL + "/delete/" + jsonIdIn.getText().toString();

        deleteJson.execute(new String[] { responseUrl });

        jsonUrlText.setText(responseUrl);
    }

    private class GetJsonData extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... urls) {
            String output = null;
            for (String url : urls) {
                try {
                    output = GetData(url);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return output;
        }

        public String GetData(String urlPath) throws IOException
        {
            URL url = new URL(urlPath);

            HttpsURLConnection connection = (HttpsURLConnection)url.openConnection();
            String encode = usernameTxt + ":" + passwordTxt;
            byte[] encodeBytes = encode.getBytes();
            String encoded = Base64.encodeToString(encodeBytes, Base64.DEFAULT);

            connection.setRequestProperty("Authorization", "Basic " + encoded);

            String output = "";

            Scanner inStream = new Scanner(connection.getInputStream());

            //process the stream and store it in StringBuilder
            while(inStream.hasNextLine())
                output+=(inStream.nextLine());

            /*
            HttpClient httpClient = new DefaultHttpClient();
            HttpGet getUrl = new HttpGet(urlPath);
            HttpResponse response = httpClient.execute(getUrl);

            HttpEntity httpEntity = response.getEntity();
            String output = EntityUtils.toString(httpEntity);
            */

            return output;
        }

        @Override
        protected void onPostExecute(String outputJson) {
            //outputText.setText(output);

            String output = "";

            try {
                JSONObject jsonObject = new JSONObject(outputJson);
                int id = jsonObject.getInt("id");
                String firstName = jsonObject.getString("firstName");
                String lastName = jsonObject.getString("lastName");
                String street = jsonObject.getString("street");
                String state = jsonObject.getString("state");
                String city = jsonObject.getString("city");
                String zip = jsonObject.getString("zip");
                String country = jsonObject.getString("country");

                jsonFirstNameIn.setText(firstName);
                jsonLastNameIn.setText(lastName);
                jsonStreetIn.setText(street);
                jsonStateIn.setText(state);
                jsonCityIn.setText(city);
                jsonZipIn.setText(zip);
                jsonCountryIn.setText(country);

                output = "ID: " + id + " -- First Name: " + firstName + " -- Last Name: " + lastName + " -- Street: " + street;
                output += " -- City: " + city + " -- State: " + state + " -- Zip: " + zip + " -- Country: " + country;

            } catch (JSONException e) {
                e.printStackTrace();
            }

            jsonOutputText.setText(output);
        }
    }

    private class DeleteJsonData extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... urls) {
            String output = null;
            for (String url : urls) {
                try {
                    output = DeleteData(url);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return output;
        }

        public String DeleteData(String urlPath) throws IOException
        {
            URL url = new URL(urlPath);

            HttpsURLConnection connection = (HttpsURLConnection)url.openConnection();
            String encode = usernameTxt + ":" + passwordTxt;
            byte[] encodeBytes = encode.getBytes();
            String encoded = Base64.encodeToString(encodeBytes, Base64.DEFAULT);

            connection.setRequestProperty("Authorization", "Basic " + encoded);
            connection.setRequestMethod("DELETE");

            String output = "";

            Scanner inStream = new Scanner(connection.getInputStream());

            //process the stream and store it in StringBuilder
            while(inStream.hasNextLine())
                output+=(inStream.nextLine());

            /*
            HttpClient httpClient = new DefaultHttpClient();
            HttpDelete deleteUrl = new HttpDelete(urlPath);
            HttpResponse response = httpClient.execute(deleteUrl);

            HttpEntity httpEntity = response.getEntity();
            String output = EntityUtils.toString(httpEntity);
            */

            return output;
        }

        @Override
        protected void onPostExecute(String output) {
            //outputText.setText(output);

            jsonOutputText.setText(output);
        }
    }

    private class PostJsonData extends AsyncTask<String, Void, String>{
        @Override
        protected String doInBackground(String... urls) {
            String output = null;
            for (String url : urls) {
                try {
                    output = PostData(url, inputJson);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return output;
        }

        public String PostData(String urlPath, String input) throws IOException
        {
            URL url = new URL(urlPath);

            HttpsURLConnection connection = (HttpsURLConnection)url.openConnection();
            String encode = usernameTxt + ":" + passwordTxt;
            byte[] encodeBytes = encode.getBytes();
            String encoded = Base64.encodeToString(encodeBytes, Base64.DEFAULT);
            connection.setRequestProperty("Content-type", "application/json");
            connection.setRequestProperty("Authorization", "Basic " + encoded);
            connection.setRequestMethod("POST");

            //send the POST out
            PrintWriter out = new PrintWriter(connection.getOutputStream());
            out.print(input);
            out.close();

            String output = "";

            Scanner inStream = new Scanner(connection.getInputStream());

            //process the stream and store it in StringBuilder
            while(inStream.hasNextLine())
                output+=(inStream.nextLine());

            /*
            HttpClient httpClient = new DefaultHttpClient();
            HttpPost postUrl = new HttpPost(urlPath);

            postUrl.setEntity(new StringEntity(input));
            postUrl.setHeader("Content-type", "application/json");

            HttpResponse response = httpClient.execute(postUrl);

            HttpEntity httpEntity = response.getEntity();
            String output = EntityUtils.toString(httpEntity);
            */

            return output;
        }

        @Override
        protected void onPostExecute(String output) {
            //outputText.setText(output);

            jsonOutputText.setText(output);
        }
    }

    private class PutJsonData extends AsyncTask<String, Void, String>{
        @Override
        protected String doInBackground(String... urls) {
            String output = null;
            for (String url : urls) {
                try {
                    output = PutData(url, inputJson);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return output;
        }

        public String PutData(String urlPath, String input) throws IOException
        {
            URL url = new URL(urlPath);

            HttpsURLConnection connection = (HttpsURLConnection)url.openConnection();
            String encode = usernameTxt + ":" + passwordTxt;
            byte[] encodeBytes = encode.getBytes();
            String encoded = Base64.encodeToString(encodeBytes, Base64.DEFAULT);
            connection.setRequestProperty("Content-type", "application/json");
            connection.setRequestProperty("Authorization", "Basic " + encoded);
            connection.setRequestMethod("PUT");

            //send the POST out
            PrintWriter out = new PrintWriter(connection.getOutputStream());
            out.print(input);
            out.close();

            String output = "";

            Scanner inStream = new Scanner(connection.getInputStream());

            //process the stream and store it in StringBuilder
            while(inStream.hasNextLine())
                output+=(inStream.nextLine());

            /*
            HttpClient httpClient = new DefaultHttpClient();
            HttpPut postUrl = new HttpPut(urlPath);

            postUrl.setEntity(new StringEntity(input));
            postUrl.setHeader("Content-type", "application/json");

            HttpResponse response = httpClient.execute(postUrl);

            HttpEntity httpEntity = response.getEntity();
            String output = EntityUtils.toString(httpEntity);
            */

            return output;
        }

        @Override
        protected void onPostExecute(String output) {
            //outputText.setText(output);

            jsonOutputText.setText(output);
        }
    }
}
