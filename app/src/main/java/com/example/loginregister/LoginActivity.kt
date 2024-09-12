package com.example.loginregister

import android.content.Intent
import android.os.Bundle
import android.view.animation.Animation
import android.view.animation.ScaleAnimation
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class LoginActivity : AppCompatActivity() {

    lateinit var dbHelper: DatabaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        dbHelper = DatabaseHelper(this)

        val etEmail = findViewById<EditText>(R.id.L_et_email)
        val etPassword = findViewById<EditText>(R.id.L_et_pw)
        val btnLogin = findViewById<Button>(R.id.L_btn_login)
        val backMain = findViewById<ImageView>(R.id.L_Img_1)

        val scaleUp = ScaleAnimation(
            1f, 1.1f, // Mulai dari 100% ukuran hingga 110% ukuran
            1f, 1.1f, // Mulai dari 100% ukuran hingga 110% ukuran
            Animation.RELATIVE_TO_SELF, 0.5f, // Pivot X di tengah
            Animation.RELATIVE_TO_SELF, 0.5f  // Pivot Y di tengah
        )
        scaleUp.duration = 200 // Durasi animasi dalam milidetik

        val scaleDown = ScaleAnimation(
            1.1f, 1f, // Kembali ke ukuran 100%
            1.1f, 1f, // Kembali ke ukuran 100%
            Animation.RELATIVE_TO_SELF, 0.5f,
            Animation.RELATIVE_TO_SELF, 0.5f
        )
        scaleDown.duration = 200

        // Handle Login Button Click
        btnLogin.setOnClickListener {
            btnLogin.startAnimation(scaleUp)
            btnLogin.postDelayed({
                btnLogin.startAnimation(scaleDown)
            }, scaleUp.duration)

            val email = etEmail.text.toString().trim()
            val password = etPassword.text.toString().trim()

            // Cek apakah email dan password terdaftar di database
            if (dbHelper.checkUser(email, password)) {
                Toast.makeText(this, "Login Successful!", Toast.LENGTH_SHORT).show()

                // Pindah ke halaman berikutnya jika login berhasil
                val intent = Intent(this@LoginActivity, DashboardActivity::class.java)
                startActivity(intent)
                finish() // Menutup LoginActivity agar tidak bisa kembali
            } else {
                Toast.makeText(this, "Invalid Email or Password", Toast.LENGTH_SHORT).show()
            }
        }

        // Pindah ke MainActivity
        backMain.setOnClickListener{
            val intent = Intent (this@LoginActivity, MainActivity::class.java)
            startActivity(intent)
        }
    }
}
