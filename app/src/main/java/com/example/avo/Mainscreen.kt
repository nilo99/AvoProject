package com.example.avo

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.example.avo.Repository.MyAnnouncemnt
import com.example.avo.databinding.ActivityEcraprincipalBinding
import com.example.avo.databinding.ActivityMainscreenBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth

class Mainscreen : AppCompatActivity() {
    private lateinit var binding: ActivityMainscreenBinding
    private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainscreenBinding.inflate(layoutInflater)
        setContentView(binding.root)
        replaceFragment(HomeFragment())

        firebaseAuth = FirebaseAuth.getInstance()

        firebaseAuth.signOut()

        binding.teste.setOnItemSelectedListener{
            when(it.itemId){

                R.id.nav_home -> replaceFragment(HomeFragment())
                R.id.nav_settings -> replaceFragment(SettingsFragment())
                R.id.nav_search -> {
                    val intent = Intent(this, AdAnnouncement::class.java)
                    startActivity(intent)
                }

                else -> {

                }

            }
            true
        }

    }

    private fun replaceFragment(fragment: Fragment)
    {
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.body_container,fragment)
        fragmentTransaction.commit()
    }
}