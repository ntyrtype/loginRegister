package com.example.loginregister

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import android.view.animation.ScaleAnimation
import android.view.animation.Animation

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val img1: ImageView = findViewById(R.id.img_1)
        val img2: ImageView = findViewById(R.id.img_2)
        val text1: TextView = findViewById(R.id.text_1)
        val text2: TextView = findViewById(R.id.text_2)
        val btn1: Button = findViewById(R.id.btn_1)
        val btn2: Button = findViewById(R.id.btn_2)

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

        btn1.setOnClickListener {
            btn1.startAnimation(scaleUp) // Jalankan animasi zoom-in
            btn1.postDelayed({
                btn1.startAnimation(scaleDown) // Jalankan animasi zoom-out setelah selesai

                // Pindah ke LoginActivity setelah animasi selesai
                btn1.postDelayed({
                    val intent = Intent(this@MainActivity, LoginActivity::class.java)
                    startActivity(intent)
                }, scaleDown.duration)
            }, scaleUp.duration)
        }

        btn2.setOnClickListener {
            btn2.startAnimation(scaleUp) // Jalankan animasi zoom-in
            btn2.postDelayed({
                btn2.startAnimation(scaleDown) // Jalankan animasi zoom-out setelah selesai

                // Pindah ke LoginActivity setelah animasi selesai
                btn2.postDelayed({
                    val intent = Intent(this@MainActivity, RegisterActivity::class.java)
                    startActivity(intent)
                }, scaleDown.duration)
            }, scaleUp.duration)

        }
    }
}