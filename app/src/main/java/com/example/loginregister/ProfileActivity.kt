package com.example.loginregister

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.squareup.picasso.Picasso

class ProfileActivity : AppCompatActivity() {

    private lateinit var dbHelper: DatabaseHelper
    private lateinit var etFullName: EditText
    private lateinit var etEmail: EditText
    private lateinit var imgProfile: ImageView
    private lateinit var btnUploadImage: Button
    private lateinit var btnSave: Button

    private lateinit var getImageResult: ActivityResultLauncher<Intent>
    private val REQUEST_PERMISSION_CODE = 100

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
            } else {
                Log.d("ProfileActivity", "Image selection failed or was canceled")
            }
        }


        // Get email from Intent and retrieve user data
        val email = intent.getStringExtra("USER_EMAIL") ?: return
        if (email != null) {
            val user = dbHelper.getUser(email)
            if (user != null) {
                etFullName.setText(user.fullName) // Should be the username
                etEmail.setText(user.email)
                user.profilePicture?.let {
                    imgProfile.setImageURI(Uri.parse(it))
                }
            } else {
                Toast.makeText(this, "User not found", Toast.LENGTH_SHORT).show()
            }
        } else {
            Toast.makeText(this, "Email not provided", Toast.LENGTH_SHORT).show()
        }

        // Check storage permission
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), REQUEST_PERMISSION_CODE)
        }

        btnUploadImage.setOnClickListener {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                val intent = Intent(Intent.ACTION_PICK).apply {
                    type = "image/*"
                }
                getImageResult.launch(intent)
            } else {
                Toast.makeText(this, "Storage permission is required to upload images.", Toast.LENGTH_SHORT).show()
            }
        }

        btnSave.setOnClickListener {
            val profilePicturePath = imgProfile.tag as? String
            if (profilePicturePath != null) {
                dbHelper.updateProfilePicture(email, profilePicturePath)
                Toast.makeText(this, "Profile Updated", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "No image selected", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_PERMISSION_CODE && grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(this, "Permission granted", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show()
        }
    }
}

