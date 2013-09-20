package com.test.rest;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.TextView;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * Created by Maro on 20/09/13.
 */
public class XmlActivity extends Activity {

    TextView xmlInput;
    TextView xmlOutput;

    String in;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_xml);

        xmlInput = (TextView)findViewById(R.id.xmlInput);
        xmlOutput = (TextView)findViewById(R.id.xmlOutput);

        in = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                "<products>\n" +
                "\t<product>     \n" +
                "\t\t<productname>Jeans</productname>\n" +
                "\t\t<productcolor>red</productcolor>\n" +
                "\t\t<productquantity>5</productquantity>\n" +
                "\t</product>\n" +
                "\t<product>     \n" +
                "\t\t<productname>Tshirt</productname>\n" +
                "\t\t<productcolor>blue</productcolor>\n" +
                "\t\t<productquantity>3</productquantity>\n" +
                "\t</product>\n" +
                "\t<product>     \n" +
                "\t\t<productname>shorts</productname>\n" +
                "\t\t<productcolor>green</productcolor>\n" +
                "\t\t<productquantity>4</productquantity>\n" +
                "\t</product>\n" +
                "</products>";

        xmlInput.setText(in);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.xml, menu);
        return true;
    }

    public void ParseXml(View v)
    {
        XmlPullParserFactory pullParserFactory;
        try {
            pullParserFactory = XmlPullParserFactory.newInstance();
            XmlPullParser parser = pullParserFactory.newPullParser();

            InputStream in_s = new ByteArrayInputStream(in.getBytes());
            parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
            parser.setInput(in_s, null);

            parseXML(parser);

        } catch (XmlPullParserException e) {

            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    class Product
    {

        public String name;
        public String quantity;
        public String color;

    }

    private void parseXML(XmlPullParser parser) throws XmlPullParserException,IOException
    {
        ArrayList<Product> products = null;

        int eventType = parser.getEventType();
        Product currentProduct = null;

        while (eventType != XmlPullParser.END_DOCUMENT){
            String name = null;
            switch (eventType){
                case XmlPullParser.START_DOCUMENT:
                    products = new ArrayList();
                    break;
                case XmlPullParser.START_TAG:
                    name = parser.getName();
                    if (name.equals("product")){
                        currentProduct = new Product();
                    } else if (currentProduct != null){
                        if (name.equals("productname")){
                            currentProduct.name = parser.nextText();
                        } else if (name.equals("productcolor")){
                            currentProduct.color = parser.nextText();
                        } else if (name.equals("productquantity")){
                            currentProduct.quantity= parser.nextText();
                        }
                    }
                    break;
                case XmlPullParser.END_TAG:
                    name = parser.getName();
                    if (name.equalsIgnoreCase("product") && currentProduct != null){
                        products.add(currentProduct);
                    }
            }
            eventType = parser.next();
        }

        String content = "";
        Iterator<Product> it = products.iterator();
        while(it.hasNext())
        {
            Product currProduct  = it.next();
            content = content + "Product :" +  currProduct.name + "\n";
            content = content + "Quantity :" +  currProduct.quantity + "\n";
            content = content + "Color :" +  currProduct.color + "\n\n";

        }

        //content = content + "nnnProduct :" +  currentProduct.name + "n";
        //content = content + "Quantity :" +  currentProduct.quantity + "n";
        //content = content + "Color :" +  currentProduct.color + "n";

        xmlOutput.setText(content);
    }
}
