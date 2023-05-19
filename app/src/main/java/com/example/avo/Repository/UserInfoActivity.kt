package com.example.avo.Repository

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import com.example.avo.R
import com.example.avo.Users

class UserInfoActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_info)

        val user = intent.getSerializableExtra("user") as? Users
        if (user != null) {
            // Use the user object to display the information in your activity
            // For example, set text in TextViews
            val nameTextView: TextView = findViewById(R.id.tv_name)
            nameTextView.text = user.fname

            val emailTextView: TextView = findViewById(R.id.tv_email)
            emailTextView.text = user.email

            val phoneTextView: TextView = findViewById(R.id.tv_phone)
            phoneTextView.text = user.uid
        }
    }
}