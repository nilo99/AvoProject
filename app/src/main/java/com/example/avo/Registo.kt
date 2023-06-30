package com.example.avo

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Patterns
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class Registo : AppCompatActivity() {

    private lateinit var auth : FirebaseAuth
    private lateinit var database : FirebaseDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registo)

        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance()

        val upfname : EditText = findViewById(R.id.firstnameText)
        val upemail : EditText = findViewById(R.id.emailText)
        val uppassword : EditText = findViewById(R.id.passwordText)
        val upbutton : Button = findViewById(R.id.registoButton)

        val no_account : TextView = findViewById(R.id.signinText)

        no_account.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        upbutton.setOnClickListener {
            val fname = upfname.text.toString()
            val email = upemail.text.toString()
            val password = uppassword.text.toString()

            if(fname.isEmpty() ||email.isEmpty()||!Patterns.EMAIL_ADDRESS.matcher(email).matches() || password.isEmpty()||password.length < 6)
            {

                if(fname.isEmpty())
                {
                    upfname.error = "O Campo Primeiro Nome é Obrigatorio"
                }
                if(email.isEmpty())
                {
                    upemail.error = "O Campo E-mail é Obrigatorio"
                }
                if(password.isEmpty())
                {
                    uppassword.error = "O Campo Password é Obrigatorio"
                }
                Toast.makeText(this, "Inserir informaçoes validas", Toast.LENGTH_SHORT).show()
            }
            else if(!Patterns.EMAIL_ADDRESS.matcher(email).matches())
            {
                upemail.error ="Digite um E-mail Valido"
                Toast.makeText(this, "Digite um E-mail Valido", Toast.LENGTH_SHORT).show()
            }
            else if(password.length < 6)
            {
                uppassword.error ="Digite uma Password Com Mais de 6 Digitos"
                Toast.makeText(this, "Digite uma Password Com Mais de 6 Digitos", Toast.LENGTH_SHORT).show()
            } else {

                auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener {

                    if(it.isSuccessful) {
                        val databaseRef = database.reference.child("Users").child(auth.currentUser!!.uid)
                        val users : Users = Users(fname,email,auth.currentUser!!.uid)

                        databaseRef.setValue(users).addOnCompleteListener {
                            if(it.isSuccessful)
                            {
                                val intent = Intent(this, MainActivity::class.java)
                                startActivity(intent)
                            }else{
                                Toast.makeText(this, "Alguma coisa deu errado.Tente Novamente1", Toast.LENGTH_SHORT).show()
                            }
                        }
                    }else{
                        Toast.makeText(this, "Alguma coisa deu errado.Tente Novamente2", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }
}