package com.example.loginregister

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity

class ProfileActivity : AppCompatActivity() {

    private lateinit var dbHelper: DatabaseHelper
    private lateinit var etFullName: EditText
    private lateinit var etEmail: EditText
    private lateinit var imgProfile: ImageView
    private lateinit var btnUploadImage: Button
    private lateinit var btnSave: Button

    private lateinit var getImageResult: ActivityResultLauncher<Intent>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        dbHelper = DatabaseHelper(this)
        etFullName = findViewById(R.id.et_full_name)
        etEmail = findViewById(R.id.et_email)
        imgProfile = findViewById(R.id.img_profile)
        btnUploadImage = findViewById(R.id.btn_upload_image)
        btnSave = findViewById(R.id.btn_save)

        // Initialize ActivityResultLauncher
        getImageResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val imageUri: Uri? = result.data?.data
                imageUri?.let {
                    imgProfile.setImageURI(it)
                    imgProfile.tag = it.toString() // Store URI as a tag
                }
            }
        }

        val email = intent.getStringExtra("USER_EMAIL") ?: return

        val user = dbHelper.getUser(email)
        if (user != null) {
            etFullName.setText(user.fullName)
            etEmail.setText(user.email)
            user.profilePicture?.let {
                imgProfile.setImageURI(Uri.parse(it))
            }
        }

        btnUploadImage.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK).apply {
                type = "image/*"
            }
            getImageResult.launch(intent)
        }

        btnSave.setOnClickListener {
            val profilePicturePath = imgProfile.tag as? String // Assuming you store the path here
            if (profilePicturePath != null) {
                dbHelper.updateProfilePicture(email, profilePicturePath)
            }
            Toast.makeText(this, "Profile Updated", Toast.LENGTH_SHORT).show()
        }
    }
}

