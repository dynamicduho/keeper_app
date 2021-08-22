package com.example.keeper_app_android;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {
    private Button merchant_button;
    private Button customer_button;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        merchant_button = (Button)findViewById(R.id.merchant_button);
        merchant_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openMerchant_Scan();
            }
        });

        customer_button = (Button)findViewById(R.id.customer_button);
        customer_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openCustomer_Archive();
            }
        });
    }
    public void openMerchant_Scan() {
        Intent intent = new Intent(this, Merchant_Scan.class);
        startActivity(intent);
    }

    public void openCustomer_Archive() {
        Intent intent = new Intent(this, Customer_Archive.class);
        startActivity(intent);
    }
}