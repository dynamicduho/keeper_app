package com.example.keeper_app_android;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;

import java.io.File;

public class Customer_Archive extends AppCompatActivity {

    private Button button_customer_upload;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_archive);

        button_customer_upload = (Button)findViewById(R.id.button_customer_upload);
        button_customer_upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadReceipt();
            }
        });


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
}