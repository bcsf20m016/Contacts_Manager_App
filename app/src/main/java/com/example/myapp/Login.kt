package com.example.myapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth

class Login : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val editTextUsername = findViewById<EditText>(R.id.editTextUsername)
        val editTextPassword = findViewById<EditText>(R.id.editTextPassword)
        val buttonLogin = findViewById<Button>(R.id.buttonLogin)
        val buttonRegister = findViewById<Button>(R.id.buttonRegister)

        buttonLogin.setOnClickListener {
            val username = editTextUsername.text.toString()
            val password = editTextPassword.text.toString()
            authenticateUser(username,password)
        }

        buttonRegister.setOnClickListener(View.OnClickListener {
            var intent = Intent(this,Register::class.java)
            startActivity(intent)
        })

    }
    private fun authenticateUser(email:String, password:String)
    {
        val auth: FirebaseAuth = FirebaseAuth.getInstance()
        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener(this) { task ->
            if (task.isSuccessful)
            {
                // Sign in success, update UI with the signed-in user's information
                val user = auth.currentUser
                Log.d("Login", user!!.email + "Successfully Logged in.")
                Toast.makeText(this@Login, "Authentication successful", Toast.LENGTH_SHORT).show()
                var intent = Intent(this@Login,AddStudent::class.java)
                startActivity(intent)
            }
            else
            {
                // If sign in fails, display a message to the user.
                Log.e("Login", "Failure", task.exception)
                Toast.makeText(this@Login, "Authentication failed.", Toast.LENGTH_SHORT).show()
            }
    }
}
}