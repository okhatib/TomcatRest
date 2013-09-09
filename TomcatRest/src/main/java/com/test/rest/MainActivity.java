package com.test.rest;

import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.ByteArrayOutputStream;
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
            StatusLine statusLine = response.getStatusLine();
            String responseString = "";

            if(statusLine.getStatusCode() == HttpStatus.SC_OK){
                ByteArrayOutputStream out = new ByteArrayOutputStream();
                response.getEntity().writeTo(out);
                out.close();
                responseString = out.toString();
                //..more logic
            } else{
                //Closes the connection.
                response.getEntity().getContent().close();
                throw new IndexOutOfBoundsException(statusLine.getReasonPhrase());
            }

            return responseString;
        }

        /*private String getOutputFromUrl(String url) {
            String output = null;
            try {
                DefaultHttpClient httpClient = new DefaultHttpClient();
                HttpGet httpGet = new HttpGet(url);

                HttpResponse httpResponse = httpClient.execute(httpGet);
                HttpEntity httpEntity = httpResponse.getEntity();
                output = EntityUtils.toString(httpEntity);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            } catch (ClientProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return output;
        }*/

        @Override
        protected void onPostExecute(String output) {
            //outputText.setText(output);
            responseOutLbl.setText(output);
        }
    }

    public void GetDataBtn_Click(View v) throws IOException {

        GetUserData gud = new GetUserData();

        int radioId = radioGroup.getCheckedRadioButtonId();
        //View rButton = radioGroup.findViewById(radioId);
        //int index = radioGroup.indexOfChild(rButton);

        String responseUrl = "";
        //String responseText = "";
        String userName = nameTxt.getText().toString();
        String userAge = ageTxt.getText().toString();

        switch (radioId)
        {
            case R.id.radioBtn1:
                responseUrl = baseURL + "/rest/hello";
                gud.execute(new String[] { responseUrl });
                urlOutLbl.setText(responseUrl);
                break;
            case R.id.radioBtn2:
                responseUrl = baseURL + "/rest/hello/name/" + userName;
                gud.execute(new String[] { responseUrl });
                urlOutLbl.setText(responseUrl);
                break;
            case R.id.radioBtn3:
                responseUrl = baseURL + "/rest/hello/number/" + userAge;
                gud.execute(new String[] { responseUrl });
                urlOutLbl.setText(responseUrl);
                break;
            case R.id.radioBtn4:
                responseUrl = baseURL + "/rest/hello/login/" + userName;
                gud.execute(new String[] { responseUrl });
                urlOutLbl.setText(responseUrl);
                break;
            case R.id.radioBtn5:
                responseUrl = baseURL + "/rest/hello/matrix/2013;author=" + userName + ";age=" + userAge;
                gud.execute(new String[] { responseUrl });
                urlOutLbl.setText(responseUrl);
                break;
            case R.id.radioBtn6:
                responseUrl = baseURL + "/rest/hello/query?author=" + userName + "&age=" + userAge + "&orderBy=age&orderBy=name";
                gud.execute(new String[] { responseUrl });
                urlOutLbl.setText(responseUrl);
                break;
        }
    }
}
