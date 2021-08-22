package com.example.landingpage

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class MainActivity : AppCompatActivity() {
    private Button button;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        Button changeActivityBTN = findViewById(R.id.button_customer);

        changeActivityBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeActivity();
            }
        });

    }

    private void changeActivity() {
        Intent intent = new Intent (packageContext:this, newActivity.class);
        startAtivity(intent);
    }
}
