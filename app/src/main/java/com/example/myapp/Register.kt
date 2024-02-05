package com.example.myapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class Register : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        val editTextUsername = findViewById<EditText>(R.id.editTextUsername)
        val editTextPassword = findViewById<EditText>(R.id.editTextPassword)
        val buttonRegister = findViewById<Button>(R.id.buttonRegister)

        buttonRegister.setOnClickListener {
            val username = editTextUsername.text.toString()
            val password = editTextPassword.text.toString()
            registerUser(username,password)
        }

    }
    private fun registerUser(email: String, password: String) {
        val auth: FirebaseAuth = FirebaseAuth.getInstance()
        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(this, object :
            OnCompleteListener<AuthResult?> {
                override fun onComplete(task: Task<AuthResult?>) {
                    if (task.isSuccessful)
                    {
                        // Sign in success, update UI with the signed-in user's information
                        val user: FirebaseUser? = auth.currentUser
                        if (user != null)
                        {
                            Log.d("Registration", user.email + " successfully Registered.")
                        }
                        Toast.makeText(this@Register, "Registration successful.", Toast.LENGTH_SHORT).show()
                        var intent = Intent(this@Register,Login::class.java)
                        startActivity(intent)
                    }
                    else {
                        // If sign in fails, display a message to the user.
                        Log.e("Registration", "failure", task.exception)
                        Toast.makeText(this@Register, "Registration failed. ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                    }
                }
            })
    }
}