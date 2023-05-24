package com.example.avo

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.avo.databinding.ActivityEcraprincipalBinding
import com.google.firebase.auth.FirebaseAuth

class Ecraprincipal : AppCompatActivity() {

    private lateinit var binding: ActivityEcraprincipalBinding
    private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEcraprincipalBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firebaseAuth = FirebaseAuth.getInstance()
        checkUser()

        binding.logoutbutton.setOnClickListener {
            firebaseAuth.signOut()
            checkUser()
        }

    }

    private fun checkUser()
    {
        val firebaseUser = firebaseAuth.currentUser

        if(firebaseUser != null)
        {
            val email = firebaseUser.email

            binding.emailshow.text = email
        }
        else{
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }
    }
}