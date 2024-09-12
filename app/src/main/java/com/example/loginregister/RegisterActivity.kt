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

class RegisterActivity : AppCompatActivity() {

    lateinit var dbHelper: DatabaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        dbHelper = DatabaseHelper(this)

        val etName = findViewById<EditText>(R.id.et_name)
        val etEmail = findViewById<EditText>(R.id.et_email)
        val etPassword = findViewById<EditText>(R.id.et_pw)
        val etConfirmPassword = findViewById<EditText>(R.id.et_cpw)
        val btnRegister = findViewById<Button>(R.id.R_btn1)
        val backMain = findViewById<ImageView>(R.id.R_Img_1)

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

        backMain.setOnClickListener {
            val intent = Intent (this@RegisterActivity, MainActivity::class.java)
            startActivity(intent)
        }

        btnRegister.setOnClickListener {
            btnRegister.startAnimation(scaleUp)
            btnRegister.postDelayed({
                btnRegister.startAnimation(scaleDown)
            }, scaleUp.duration)

            val username = etName.text.toString().trim()
            val email = etEmail.text.toString().trim()
            val password = etPassword.text.toString().trim()
            val confirmPassword = etConfirmPassword.text.toString().trim()

            // Cek apakah username sudah ada di database
            if (dbHelper.isUsernameExists(username)) {
                Toast.makeText(this, "Username already exists", Toast.LENGTH_SHORT).show()
            }
            // Cek kekuatan password
            else if (!isPasswordStrong(password)) {
                Toast.makeText(
                    this,
                    "Password is too weak. Use at least 8 characters, including uppercase, number, and special character.",
                    Toast.LENGTH_LONG
                ).show()
            }
            // Cek apakah password dan konfirmasi password sama
            else if (password != confirmPassword) {
                Toast.makeText(this, "Passwords do not match", Toast.LENGTH_SHORT).show()
            }
            // Simpan data ke database
            else {
                dbHelper.addUser(username, email, password)
                Toast.makeText(this, "Registration successful!", Toast.LENGTH_SHORT).show()

                val intent = Intent(this@RegisterActivity, LoginActivity::class.java)
                startActivity(intent)
                finish()
            }
        }
    }

    // Validasi kekuatan password
    fun isPasswordStrong(password: String): Boolean {
        val passwordPattern = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@#\$%^&+=!]).{8,}$"
        val passwordMatcher = Regex(passwordPattern)
        return passwordMatcher.find(password) != null
    }


}
