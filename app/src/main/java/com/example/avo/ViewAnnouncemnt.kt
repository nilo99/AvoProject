package com.example.avo

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.PopupMenu
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener


class ViewAnnouncemnt : AppCompatActivity() {

    private lateinit var textCity: TextView
    private lateinit var textInfo: TextView
    private lateinit var txtrua: TextView
    private lateinit var txtdia: TextView
    private lateinit var textcuidados: TextView
    private lateinit var txtprice : TextView
    private var id : String? = null
    private lateinit var buttonSubmit: Button
    private lateinit var backButtonImageView: ImageView

    companion object {
        private const val REQUEST_CALL_PHONE_PERMISSION = 1
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_announcemnt)

        textCity = findViewById(R.id.textCity)
        textInfo = findViewById(R.id.textinfo)
        txtrua = findViewById(R.id.txtdescricao)
        txtdia = findViewById(R.id.txtdatajoin)
        textcuidados = findViewById(R.id.textcriaido)
        txtprice = findViewById(R.id.textCost)

        buttonSubmit = findViewById(R.id.buttonSubmit)
        backButtonImageView = findViewById(R.id.anterior)

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.CALL_PHONE), REQUEST_CALL_PHONE_PERMISSION)
        }

        backButtonImageView.setOnClickListener {
            finish() // Close the current activity and return to the previous one
        }

        buttonSubmit.setOnClickListener { view ->
            showPopupMenu(view)
        }

        val extras = intent.extras
        if (extras != null) {
            id = extras.getString("testedeid")
        }

        id?.let { announcementId ->
            val databaseRef = FirebaseDatabase.getInstance().reference.child("announcements").child(announcementId)
            databaseRef.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        val cityName = snapshot.child("fname").getValue(String::class.java)
                        val info = snapshot.child("about").getValue(String::class.java)
                        val nome = snapshot.child("address").getValue(String::class.java)
                        val cuidados = snapshot.child("numberPeople").getValue(String::class.java)
                        val preco = snapshot.child("price").getValue(String::class.java)

                        var count = 0
                        val checkboxValues = listOf(
                            snapshot.child("checkbox1Value").getValue(Boolean::class.java),
                            snapshot.child("checkbox2Value").getValue(Boolean::class.java),
                            snapshot.child("checkbox3Value").getValue(Boolean::class.java),
                            snapshot.child("checkbox4Value").getValue(Boolean::class.java),
                            snapshot.child("checkbox5Value").getValue(Boolean::class.java),
                            snapshot.child("checkbox6Value").getValue(Boolean::class.java),
                            snapshot.child("checkbox7Value").getValue(Boolean::class.java)
                        )

                        for (value in checkboxValues) {
                            if (value != null && value) {
                                count++
                            }
                        }

                        textCity.text = cityName
                        textInfo.text = info
                        txtrua.text = nome
                        txtdia.text = count.toString()
                        textcuidados.text = cuidados
                        txtprice.text = preco + " €/hora"
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    showToast("Failed to load announcement")
                }
            })
        }
    }


    private fun showPopupMenu(view: View) {
        val popupMenu = PopupMenu(this, view)
        popupMenu.inflate(R.menu.popup_menu)

        id?.let { announcementId ->
            val databaseRef = FirebaseDatabase.getInstance().reference.child("announcements").child(announcementId)
            databaseRef.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        val emailValue = snapshot.child("email").getValue(String::class.java)
                        val phoneValue = snapshot.child("phone").getValue(String::class.java)

                        val menu = popupMenu.menu
                        menu.findItem(R.id.menu_email)?.isVisible = !emailValue.isNullOrEmpty()
                        menu.findItem(R.id.menu_phone)?.isVisible = !phoneValue.isNullOrEmpty()

                        Log.d("PhoneValue", "Phone Value: $phoneValue") // Debugging statement

                        popupMenu.setOnMenuItemClickListener { menuItem ->
                            when (menuItem.itemId) {
                                R.id.menu_email -> {
                                    emailValue?.let { email ->
                                        val intent = Intent(Intent.ACTION_SEND).apply {
                                            type = "text/plain"
                                            putExtra(Intent.EXTRA_EMAIL, arrayOf(email))
                                        }
                                        if (intent.resolveActivity(packageManager) != null) {
                                            startActivity(intent)
                                        } else {
                                            showToast("No email app found")
                                        }
                                    }
                                    true
                                }
                                R.id.menu_phone -> {
                                    phoneValue?.let { phone ->
                                        val intent = Intent(Intent.ACTION_VIEW).apply {
                                            data = Uri.parse("tel:$phone")
                                        }
                                        startActivity(intent)
                                    }
                                    true
                                }
                                else -> false
                            }
                        }
                        popupMenu.show()
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    showToast("Failed to load contact information")
                }
            })
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CALL_PHONE_PERMISSION) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted, you can make the phone call here
            } else {
                showToast("Phone call permission denied")
            }
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}
