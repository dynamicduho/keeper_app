package com.example.keeper_app_android;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;

import java.io.File;

public class Customer_Archive extends AppCompatActivity {
    private Button button_view_receipt;
    private Button button_customer_upload;

    ArrayList<String> merchant_names = new ArrayList<>();
    ArrayList<String> receiptIDs = new ArrayList<>();
    ArrayList<Double> total_prices = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_archive);

        // navigate to receipt
        button_view_receipt = (Button) findViewById(R.id.button_view_receipt);
        button_view_receipt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openReceiptView();
            }
        });

        // recyclerview ref
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        // set linearlayoutmanager default vertical
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(linearLayoutManager);

        try {
            // get JSONObject from JSON file
            JSONObject obj = new JSONObject(loadJSONFromAsset());
            // fetch JSONArray named users
            JSONArray receiptArray = obj.getJSONArray("receipts");
            // implement for loop for getting users list data
            for (int i = 0; i < receiptArray.length(); i++) {
                // create a JSONObject for fetching single receipt data
                JSONObject receiptDetail = receiptArray.getJSONObject(i);
                // fetch details and store it in arraylist
                merchant_names.add(receiptDetail.getString("merchant"));
                receiptIDs.add(receiptDetail.getString("receiptID"));
                total_prices.add(receiptDetail.getDouble("item_total"));

                // create a object for getting item data from JSONObject
                     // JSONObject items = receiptDetail.getJSONObject("items");
                // store items_names in arraylist
                    //  items_names.add(items.getString("item)name"));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        //  call the constructor of CustomAdapter to send the reference and data to Adapter
        CustomAdapter customAdapter = new CustomAdapter(Customer_Archive.this, merchant_names, receiptIDs, total_prices);
        recyclerView.setAdapter(customAdapter); // set the Adapter to RecyclerView

        button_customer_upload = (Button)findViewById(R.id.button_customer_upload);
        button_customer_upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadReceipt();
            }
        });


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

    public void openReceiptView() {
        Intent intent = new Intent(this, Customer_Receipt_View.class);
        startActivity(intent);
    }

    public void uploadReceipt() {
        /* String url = "http://yourserver";
        File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath(),
                "yourfile");
        try {
            HttpClient httpclient = new DefaultHttpClient();

            HttpPost httppost = new HttpPost(url);

            InputStreamEntity reqEntity = new InputStreamEntity(
                    new FileInputStream(file), -1);
            reqEntity.setContentType("binary/octet-stream");
            reqEntity.setChunked(true); // Send in multiple parts if needed
            httppost.setEntity(reqEntity);
            HttpResponse response = httpclient.execute(httppost);
            //Do something with response...

        } catch (Exception e) {
            // show error
        }*/
    }


//    public void openReceipt() {
//        String fileName = "sampleReceipts.json";
//
//        // Retrieve the path to the user's public pictures directory
//        File path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
//        path.mkdirs();
//
//        // Create a new file using the specified directory and name
//        File fileToOpen = new File(path, fileName);
//        fileToOpen.setReadable(true, false);
//
//
//
//    }

}