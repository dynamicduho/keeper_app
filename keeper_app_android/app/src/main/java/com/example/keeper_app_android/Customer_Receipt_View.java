package com.example.keeper_app_android;

import androidx.appcompat.app.AppCompatActivity;

import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;
import android.os.Bundle;

import java.io.IOException;
import java.io.InputStream;

public class Customer_Receipt_View extends AppCompatActivity {

    String JSON_STRING = "{\"receipt\":{\"merchant\":\"Pho Xpress\",\"total\":12.97,\"item1\":\"Specialty Rice Noodle\"}}";
    String name, item1, paid;
    TextView merchant_name, item1_name, total_paid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_receipt_view);

        // get the reference of TextView's
        merchant_name = (TextView) findViewById(R.id.merchant);
        item1_name = (TextView) findViewById(R.id.item1);
        total_paid = (TextView) findViewById(R.id.total);

        try {
            // get JSONObject from JSON file
            JSONObject obj = new JSONObject(JSON_STRING);
            // fetch JSONObject named employee
            JSONObject receipt = obj.getJSONObject("receipt");
            // get merchant name and price
            name = receipt.getString("merchant");
            item1 = receipt.getString("item1");
            paid = String.valueOf(receipt.getDouble("total"));
            // set employee name and salary in TextView's
            merchant_name.setText("Merchant: "+name);
            item1_name.setText("Item: " + item1);
            total_paid.setText("Total: "+paid);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public String loadJSONFromAsset() {
        String json = null;
        try {
            InputStream is = getAssets().open("sampleReceipts.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;
    }

}