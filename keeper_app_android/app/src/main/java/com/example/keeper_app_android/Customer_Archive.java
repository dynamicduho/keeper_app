package com.example.keeper_app_android;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;

import java.io.File;

public class Customer_Archive extends AppCompatActivity {
    private Button button_view_receipt;
    private Button button_customer_upload;
    private Button button_GET_receipt;

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
                try {
                    //use the async "class" like a function
                    new sendPostRequest1().execute(loadJSONFromAsset());
                    Toast.makeText(getApplicationContext(), "Receipt has been uploaded!", Toast.LENGTH_SHORT).show();
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), "Failed!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        button_GET_receipt = (Button)findViewById(R.id.button_GET_receipt);
        button_GET_receipt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    //use the async "class" like a function
                    new sendGetRequest().execute();
                    Toast.makeText(getApplicationContext(), "Received receipt!", Toast.LENGTH_SHORT).show();
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), "Failed!", Toast.LENGTH_SHORT).show();
                }
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




    class sendPostRequest1 extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... strings) {
            try {
                String dataToSend = strings[0];
                // data we want to send
                String post_data="receipt=" + dataToSend;

                // sends the http request
                String dbLink = "https://keeper-database.herokuapp.com/uploadReceipt";
                URL dbURL = new URL(dbLink);
                HttpURLConnection dbConn = (HttpURLConnection)dbURL.openConnection();
                dbConn.setRequestMethod("POST");
                dbConn.setDoOutput(true);

                OutputStream outputStream = dbConn.getOutputStream();
                outputStream.write(post_data.getBytes());
                outputStream.flush();
                outputStream.close();

                // converts response received to string
                String inputLine = "";
                BufferedReader BReader = new BufferedReader(
                        new InputStreamReader(dbConn.getInputStream()));
                StringBuilder response = new StringBuilder();
                while ((inputLine = BReader.readLine()) != null) {
                    response.append(inputLine);
                }
                BReader.close();
                String output = response.toString();

                // testing code
                // System.out.println("response is: "+ output);
                return output;
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }
    }

    // sends a get request to server and returns a string for the server's response
    class sendGetRequest extends AsyncTask<Void, Void, String> {
        @Override
        protected String doInBackground(Void... params) {
            try {
                // sends the http request
                String dbLink = "https://keeper-database.herokuapp.com/getReceipts";
                URL dbURL = new URL(dbLink);
                HttpURLConnection dbConn = (HttpURLConnection)dbURL.openConnection();
                dbConn.setRequestMethod("GET");

                // converts response received to string
                String inputLine = "";
                BufferedReader BReader = new BufferedReader(
                        new InputStreamReader(dbConn.getInputStream()));
                StringBuilder response = new StringBuilder();
                while ((inputLine = BReader.readLine()) != null) {
                    response.append(inputLine);
                }
                BReader.close();
                String output = response.toString();

                // testing code
                // System.out.println("response is: "+ output);
                return output;
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }
        protected void onPostExecute(String output) {
            Toast.makeText(getApplicationContext(), output, Toast.LENGTH_SHORT).show();
            System.out.print(output);
        }
    }





    public static String sendPostRequest(String dataToSend) throws Exception {
        // data we want to send
        String post_data = "receipt=" + dataToSend;

        // sends the http request
        String dbLink = "https://keeper-database.herokuapp.com/uploadReceipt";
        URL dbURL = new URL(dbLink);
        HttpURLConnection dbConn = (HttpURLConnection) dbURL.openConnection();
        dbConn.setRequestMethod("POST");
        dbConn.setDoOutput(true);

        OutputStream outputStream = dbConn.getOutputStream();
        outputStream.write(post_data.getBytes());
        outputStream.flush();
        outputStream.close();

        // converts response received to string
        String inputLine = "";
        BufferedReader BReader = new BufferedReader(
                new InputStreamReader(dbConn.getInputStream()));
        StringBuilder response = new StringBuilder();
        while ((inputLine = BReader.readLine()) != null) {
            response.append(inputLine);
        }
        BReader.close();
        String output = response.toString();

        // testing code
        // System.out.println("response is: "+ output);
        return output;
    }
    /* public void uploadReceipt() {
        String url = "http://yourserver";
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
