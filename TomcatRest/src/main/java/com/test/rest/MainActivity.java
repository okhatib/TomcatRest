package com.test.rest;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import java.io.IOException;

public class MainActivity extends Activity {

    public final static String baseURL = "http://tomcatrest-skyhawk.rhcloud.com/";

    RadioGroup radioGroup;
    EditText nameTxt;
    EditText ageTxt;
    TextView urlOutLbl;
    TextView responseOutLbl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        radioGroup = (RadioGroup)findViewById(R.id.RadioGrp);
        nameTxt = (EditText)findViewById(R.id.NameTxt);
        ageTxt = (EditText)findViewById(R.id.AgeTxt);
        urlOutLbl = (TextView)findViewById(R.id.urlOutputLbl);
        responseOutLbl = (TextView)findViewById(R.id.outputTextLbl);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    private class GetUserData extends AsyncTask<String, Void, String> {
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
            HttpClient httpClient = new DefaultHttpClient();
            HttpGet getUrl = new HttpGet(urlPath);
            HttpResponse response = httpClient.execute(getUrl);

            HttpEntity httpEntity = response.getEntity();
            String output = EntityUtils.toString(httpEntity);

            return output;
        }

        @Override
        protected void onPostExecute(String output) {
            //outputText.setText(output);
            responseOutLbl.setText(output);
        }
    }

    public void GoToJsonActivity_Click(View v)
    {
        Intent i = new Intent(MainActivity.this,JSONMethods.class);
        startActivity(i);
    }

    public void GetDataBtn_Click(View v) throws IOException {

        GetUserData gud = new GetUserData();

        int radioId = radioGroup.getCheckedRadioButtonId();

        String responseUrl;
        String userName = nameTxt.getText().toString();
        String userAge = ageTxt.getText().toString();

        switch (radioId)
        {
            case R.id.radioBtnHelloWorld:
                responseUrl = baseURL + "/rest/hello";
                gud.execute(new String[] { responseUrl });
                urlOutLbl.setText(responseUrl);
                break;
            case R.id.radioBtnHelloName:
                responseUrl = baseURL + "/rest/hello/name/" + userName;
                gud.execute(new String[] { responseUrl });
                urlOutLbl.setText(responseUrl);
                break;
            case R.id.radioBtnSelectedNumber:
                responseUrl = baseURL + "/rest/hello/number/" + userAge;
                gud.execute(new String[] { responseUrl });
                urlOutLbl.setText(responseUrl);
                break;
            case R.id.radioBtnEnteredPassword:
                responseUrl = baseURL + "/rest/hello/login/" + userName;
                gud.execute(new String[] { responseUrl });
                urlOutLbl.setText(responseUrl);
                break;
            case R.id.radioBtnMatrixParam:
                responseUrl = baseURL + "/rest/hello/matrix/2013;author=" + userName + ";age=" + userAge;
                gud.execute(new String[] { responseUrl });
                urlOutLbl.setText(responseUrl);
                break;
            case R.id.radioBtnQueryParam:
                responseUrl = baseURL + "/rest/hello/query?author=" + userName + "&age=" + userAge + "&orderBy=age&orderBy=name";
                gud.execute(new String[] { responseUrl });
                urlOutLbl.setText(responseUrl);
                break;
        }
    }
}
