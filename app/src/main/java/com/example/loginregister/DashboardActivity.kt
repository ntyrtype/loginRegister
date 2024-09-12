package com.example.loginregister

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.widget.Button

class DashboardActivity : AppCompatActivity() {

    lateinit var dbHelper: DatabaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)

        dbHelper = DatabaseHelper(this)

        val btnProfile = findViewById<Button>(R.id.btn_profile)

        btnProfile.setOnClickListener {
            // Arahkan ke ProfileActivity
            val intent = Intent(this, ProfileActivity::class.java)
            startActivity(intent)
        }
    }
}
