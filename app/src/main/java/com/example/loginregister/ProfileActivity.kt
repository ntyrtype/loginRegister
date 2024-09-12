package com.example.loginregister

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity


class ProfileActivity : AppCompatActivity() {

    private lateinit var dbHelper: DatabaseHelper
    private lateinit var etFullName: EditText
    private lateinit var etEmail: EditText
    private lateinit var imgProfile: ImageView
    private lateinit var btnUploadImage: Button
    private lateinit var btnSave: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        dbHelper = DatabaseHelper(this)
        etFullName = findViewById(R.id.et_full_name)
        etEmail = findViewById(R.id.et_email)
        imgProfile = findViewById(R.id.img_profile)
        btnUploadImage = findViewById(R.id.btn_upload_image)
        btnSave = findViewById(R.id.btn_save)

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
            // Implement image upload logic
            // For example, open image picker
            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"
            startActivityForResult(intent, IMAGE_PICK_REQUEST_CODE)
        }

        btnSave.setOnClickListener {
            val profilePicturePath = imgProfile.tag as? String // Assuming you store the path here
            if (profilePicturePath != null) {
                dbHelper.updateProfilePicture(email, profilePicturePath)
            }
            Toast.makeText(this, "Profile Updated", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == IMAGE_PICK_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            val imageUri: Uri? = data?.data
            imgProfile.setImageURI(imageUri)
            // Save the image path or URI for later use
            imageUri?.let {
                imgProfile.tag = it.toString()
            }
        }
    }

    companion object {
        private const val IMAGE_PICK_REQUEST_CODE = 1
    }
}
