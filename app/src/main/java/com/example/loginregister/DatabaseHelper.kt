package com.example.loginregister

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME = "users.db"
        private const val DATABASE_VERSION = 1
        private const val TABLE_USERS = "users"
        private const val COLUMN_ID = "id"
        private const val COLUMN_USERNAME = "username"
        private const val COLUMN_EMAIL = "email"
        private const val COLUMN_PASSWORD = "password"
        private const val COLUMN_FULL_NAME = "full_name"
        private const val COLUMN_PROFILE_PICTURE = "profile_picture"
    }

    override fun onCreate(db: SQLiteDatabase) {
        val createUsersTable = ("CREATE TABLE $TABLE_USERS (" +
                "$COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT," +
                "$COLUMN_USERNAME TEXT," +
                "$COLUMN_EMAIL TEXT," +
                "$COLUMN_PASSWORD TEXT," +
                "$COLUMN_FULL_NAME TEXT," +
                "$COLUMN_PROFILE_PICTURE TEXT)")
        db.execSQL(createUsersTable)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_USERS")
        onCreate(db)
    }

    // Tambah pengguna baru
    fun addUser(username: String, email: String, password: String, fullName: String, profilePicture: String?): Long {
        val db = this.writableDatabase
        val values = ContentValues()
        values.put(COLUMN_USERNAME, username)
        values.put(COLUMN_EMAIL, email)
        values.put(COLUMN_PASSWORD, password)
        values.put(COLUMN_FULL_NAME, fullName)
        values.put(COLUMN_PROFILE_PICTURE, profilePicture)

        return db.insert(TABLE_USERS, null, values)
    }

    // Cek apakah username sudah ada
    fun isUsernameExists(username: String): Boolean {
        val db = this.readableDatabase
        val query = "SELECT * FROM $TABLE_USERS WHERE $COLUMN_USERNAME = ?"
        val cursor = db.rawQuery(query, arrayOf(username))
        val exists = cursor.count > 0
        cursor.close()
        return exists
    }

    fun checkUser(email: String, password: String): Boolean {
        val db = this.readableDatabase
        val query = "SELECT * FROM $TABLE_USERS WHERE $COLUMN_EMAIL = ? AND $COLUMN_PASSWORD = ?"
        val cursor = db.rawQuery(query, arrayOf(email, password))
        val exists = cursor.count > 0
        cursor.close()
        return exists
    }

    fun getUser(email: String): User? {
        val db = this.readableDatabase
        val cursor = db.rawQuery("SELECT * FROM $TABLE_USERS WHERE $COLUMN_EMAIL = ?", arrayOf(email))

        if (cursor.moveToFirst()) {
            val fullNameIndex = cursor.getColumnIndex(COLUMN_FULL_NAME)
            val profilePictureIndex = cursor.getColumnIndex(COLUMN_PROFILE_PICTURE)

            if (fullNameIndex != -1 && profilePictureIndex != -1) {
                val fullName = cursor.getString(fullNameIndex)
                val profilePicture = cursor.getString(profilePictureIndex)
                cursor.close()
                return User(fullName, email, profilePicture)
            } else {
                cursor.close()
                // Handle the case where the column is not found
                throw IllegalStateException("Column not found in cursor")
            }
        } else {
            cursor.close()
            return null
        }
    }


    fun updateProfilePicture(email: String, profilePicturePath: String) {
        val db = this.writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_PROFILE_PICTURE, profilePicturePath)
        }
        db.update(TABLE_USERS, values, "$COLUMN_EMAIL = ?", arrayOf(email))
    }

}
