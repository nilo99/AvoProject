package com.example.avo

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Patterns
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.example.avo.databinding.ActivityMainBinding
import com.google.firebase.auth.FirebaseAuth

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var firebaseAuth: FirebaseAuth

    private var email = ""
    private var password = ""




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firebaseAuth = FirebaseAuth.getInstance()
        checkUser()

        binding.signupText.setOnClickListener {
            startActivity(Intent(this, Regi2::class.java))
        }

        binding.loginButton.setOnClickListener {
            validateData()
        }
    }

    private fun validateData()
    {
        email = binding.username.text.toString().trim()
        password = binding.password.text.toString().trim()

        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches())
        {
            binding.username.error ="Invalid email format"
        }
        else if(TextUtils.isEmpty(password))
        {
            binding.password.error ="Please enter password"
        }

        else{
            firebaseLogin()
        }
    }

    private fun firebaseLogin()
    {
        firebaseAuth.signInWithEmailAndPassword(email, password)

            .addOnSuccessListener {
                val firebaseUser = firebaseAuth.currentUser
                val email = firebaseUser!!.email
                Toast.makeText(this, "Logged in as $email", Toast.LENGTH_SHORT).show()

                startActivity(Intent(this,Mainscreen::class.java))
                finish()
            }
            .addOnFailureListener { e->
                Toast.makeText(this, "Login failed duw to ${e.message}", Toast.LENGTH_SHORT).show()

            }
    }

    private fun checkUser()
    {
        val firebaseUser = firebaseAuth.currentUser
        if(firebaseUser != null)
        {
            startActivity(Intent(this, Ecraprincipal::class.java))
            finish()
        }
    }
}