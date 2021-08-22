package com.example.landingpage

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val button = findViewById<Button>(R.id.button_customer)
        button.setOnClickListener {
            val intent = Intent(this, CustomerArchive::class.java)

            print("Hello")
            startActivity(intent)


        }
    }
}



