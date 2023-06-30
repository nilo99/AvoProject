package com.example.avo

import android.content.Context
import android.location.Geocoder
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.InputFilter
import android.view.View
import android.widget.Button
import android.widget.CheckBox
import android.widget.TextView
import android.widget.Toast
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import org.w3c.dom.Text
import java.io.IOException

class AdAnnouncement : AppCompatActivity() {
    private lateinit var checkBoxEmail: CheckBox
    private lateinit var checkBoxPhone: CheckBox
    private lateinit var textEmailInputLayout: TextInputLayout
    private lateinit var textPhoneInputLayout: TextInputLayout
    private lateinit var latitudeTextView: TextView
    private lateinit var longitudeTextView: TextView
    private lateinit var geocoder: Geocoder
    private lateinit var text:Text

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ad_announcemnt)

        checkBoxEmail = findViewById(R.id.checkBoxEmail)
        checkBoxPhone = findViewById(R.id.checkBoxPhone)
        textEmailInputLayout = findViewById(R.id.textEmailInputLayout)
        textPhoneInputLayout = findViewById(R.id.textPhoneInputLayout)

        textEmailInputLayout.visibility = View.GONE
        textPhoneInputLayout.visibility = View.GONE

        latitudeTextView = findViewById(R.id.latitudeTextView) // Initialize latitudeTextView
        longitudeTextView = findViewById(R.id.longitudeTextView)

        latitudeTextView.visibility = View.GONE
        longitudeTextView.visibility = View.GONE



        checkBoxEmail.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                textEmailInputLayout.visibility = View.VISIBLE
            } else {
                textEmailInputLayout.visibility = View.GONE
            }
        }

        checkBoxPhone.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                textPhoneInputLayout.visibility = View.VISIBLE
            } else {
                textPhoneInputLayout.visibility = View.GONE
            }
        }

        val textNameField = findViewById<TextInputEditText>(R.id.textNameField)
        val minLength = 20

        val filter = InputFilter { source, start, end, _, _, _ ->
            val newLength = (textNameField.text?.length ?: 0) - (end - start) + source.length
            if (newLength >= minLength) {
                null
            } else {
                ""
            }
        }

        geocoder = Geocoder(this)

        val saveButton = findViewById<Button>(R.id.saveButton)
        saveButton.setOnClickListener {
            val address = findViewById<TextInputEditText>(R.id.locfield).text.toString()
            val latLng = getLatitudeLongitude(address)
            if (latLng != null) {
                val latitude = latLng.first.toString()
                val longitude = latLng.second.toString()
                saveAdAnnouncement(address, latitude, longitude)
            }
        }
    }

    private fun getLatitudeLongitude(address: String): Pair<Double, Double>? {
        try {
            val addresses = geocoder.getFromLocationName(address, 1)
            if (addresses.isNotEmpty()) {
                val latitude = addresses[0].latitude
                val longitude = addresses[0].longitude
                return Pair(latitude, longitude)
            } else {
                Toast.makeText(this, "Endereço não encontrado", Toast.LENGTH_SHORT).show()
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return null
    }

    private fun saveAdAnnouncement(address: String, latitude: String, longitude: String) {
        val sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
        val userId = sharedPreferences.getString("userId", null)
        val textNameField = findViewById<TextInputEditText>(R.id.textNameField)
        val aboutField = findViewById<TextInputEditText>(R.id.aboutfield)
        val priceField = findViewById<TextInputEditText>(R.id.priceField)
        val price = priceField.text.toString().trim()
        val checkbox1 = findViewById<CheckBox>(R.id.checkbox1)
        val checkbox2 = findViewById<CheckBox>(R.id.checkbox2)
        val checkbox3 = findViewById<CheckBox>(R.id.checkbox3)
        val checkbox4 = findViewById<CheckBox>(R.id.checkbox4)
        val checkbox5 = findViewById<CheckBox>(R.id.checkbox5)
        val checkbox6 = findViewById<CheckBox>(R.id.checkbox6)
        val checkbox7 = findViewById<CheckBox>(R.id.checkbox7)
        val about = textNameField.text.toString().trim()
        val numberPeople = aboutField.text.toString().trim()
        val checkbox1Value = checkbox1.isChecked
        val checkbox2Value = checkbox2.isChecked
        val checkbox3Value = checkbox3.isChecked
        val checkbox4Value = checkbox4.isChecked
        val checkbox5Value = checkbox5.isChecked
        val checkbox6Value = checkbox6.isChecked
        val checkbox7Value = checkbox7.isChecked
        val email = textEmailInputLayout.editText?.text.toString()
        val phone = textPhoneInputLayout.editText?.text.toString()

        // Verifique se todos os campos foram preenchidos antes de salvar
        if (about.isEmpty() || about.isEmpty()) {
            Toast.makeText(this, "Preencha todos os campos", Toast.LENGTH_SHORT).show()
            return
        }

        if (price.isEmpty()) {
            Toast.makeText(this, "Enter the price", Toast.LENGTH_SHORT).show()
            return
        }

        // Retrieve the fname value from the Users table
        val database = FirebaseDatabase.getInstance()
        val usersRef = database.getReference("Users")
        usersRef.child(userId!!).addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                // Check if the user exists in the database
                if (dataSnapshot.exists()) {
                    // Retrieve the fname value
                    val fname = dataSnapshot.child("fname").getValue(String::class.java)

                    // Use the fname value as needed
                    if (fname != null) {
                        // Store the fname value in SharedPreferences
                        val editor = sharedPreferences.edit()
                        editor.putString("fname", fname)
                        editor.apply()

                        // Create an Announcement object with the fname value
                        val ref = database.getReference("announcements")
                        val key = ref.push().key
                        if (key != null) {
                            val adId = key // Get the ad ID from the generated key

                            val announcement = Announcement(
                                about,
                                numberPeople,
                                checkbox1Value,
                                checkbox2Value,
                                checkbox3Value,
                                checkbox4Value,
                                checkbox5Value,
                                checkbox6Value,
                                checkbox7Value,
                                address,
                                latitude,
                                longitude,
                                fname,
                                userId,
                                adId,
                                price,
                                email,
                                phone
                            )

                            ref.child(key).setValue(announcement)
                                .addOnSuccessListener {
                                    Toast.makeText(this@AdAnnouncement, "Anúncio salvo com sucesso", Toast.LENGTH_SHORT).show()
                                    finish()
                                }
                                .addOnFailureListener {
                                    Toast.makeText(this@AdAnnouncement, "Falha ao salvar o anúncio", Toast.LENGTH_SHORT).show()
                                }
                        } else {
                            Toast.makeText(this@AdAnnouncement, "Falha ao gerar a chave do anúncio", Toast.LENGTH_SHORT).show()
                        }
                    } else {
                        // Handle the case when fname is null
                        Toast.makeText(this@AdAnnouncement, "fname not found", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    // Handle the case when the user does not exist
                    Toast.makeText(this@AdAnnouncement, "User not found", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Handle any errors that occurred during the database operation
                Toast.makeText(this@AdAnnouncement, "Database error: ${databaseError.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    data class Announcement(
        val about: String,
        val numberPeople: String,
        val checkbox1Value: Boolean,
        val checkbox2Value: Boolean,
        val checkbox3Value: Boolean,
        val checkbox4Value: Boolean,
        val checkbox5Value: Boolean,
        val checkbox6Value: Boolean,
        val checkbox7Value: Boolean,
        val address: String,
        val latitude: String,
        val longitude: String,
        val fname: String,
        val uid: String,
        val adid:String,
        val price: String,
        val email : String? ,
        val phone : String?
    )
}
