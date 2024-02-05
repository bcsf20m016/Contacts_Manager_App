package com.example.myapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import database.DatabaseHelper
import model.Contact


//The following code is written to ensure the phone number pattern to 03XX-XXXXXXX
class PhoneNumberWatcher(private val editText: EditText) : TextWatcher {

    companion object {
        private const val PHONE_NUMBER_PATTERN = "^03[0-9]{2}-[0-9]{7}$"
    }

    override fun beforeTextChanged(charSequence: CharSequence?, start: Int, count: Int, after: Int) {
        // Not needed for this example
    }

    override fun onTextChanged(charSequence: CharSequence?, start: Int, before: Int, count: Int) {
        // Not needed for this example
    }

    override fun afterTextChanged(editable: Editable?) {
        validatePhoneNumber(editable.toString())
    }

    private fun validatePhoneNumber(phoneNumber: String) {
        if (!phoneNumber.matches(Regex(PHONE_NUMBER_PATTERN))) {
            editText.error = "Invalid number. Format must be 03XX-XXXXXXX"
        } else {
            editText.error = null
        }
    }
}

class insertContact : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_insert_contact)

        var title: TextView = findViewById(R.id.insertScreen)
        var firstName: EditText = findViewById(R.id.firstName)
        var lastName: EditText = findViewById(R.id.lastName)

        var number: EditText = findViewById(R.id.number)
        val phoneNumberWatcher = PhoneNumberWatcher(number)
        number.addTextChangedListener(phoneNumberWatcher)

        var btn: Button = findViewById(R.id.insertUpdate_btn)

        if(intent.getStringExtra("Mode") == "Edit")
        {
            title.setText("Update Existing Contact")
            firstName.setText(intent.getStringExtra("First Name"))
            lastName.setText(intent.getStringExtra("Last Name"))
            number.setText(intent.getStringExtra("Phone Number"))
            btn.setText("Update")

            btn.setOnClickListener(View.OnClickListener {

                if(firstName.text.isEmpty())
                {
                    firstName.error = "First Name is required"
                }
                else if(lastName.text.isEmpty())
                {
                    lastName.error = "Last Name is required"
                }
                else
                {
                    var contact: Contact = Contact()
                    contact.id = intent.getIntExtra("Id",10)
                    contact.firstName = firstName.text.toString()
                    contact.lastName = lastName.text.toString()
                    contact.number = number.text.toString()
                    var db: DatabaseHelper = DatabaseHelper(this)
                    var result = db.updateContact(contact)
                    if(result)
                        Toast.makeText(this,"Contact Updated Successfully",Toast.LENGTH_SHORT).show()
                    else
                        Toast.makeText(this,"Failed to update Contact",Toast.LENGTH_SHORT).show()
                    var intent = Intent(this,MainActivity::class.java)
                    startActivity(intent)
                }
            })
        }
        else if(intent.getStringExtra("Mode") == "Insert")
        {
            btn.setOnClickListener(View.OnClickListener {

                if(firstName.text.isEmpty())
                {
                    firstName.error = "First Name is required"
                }
                else if(lastName.text.isEmpty())
                {
                    lastName.error = "Last Name is required"
                }
                else
                {
                    var contact: Contact = Contact()
                    contact.firstName = firstName.text.toString().trim()
                    contact.lastName = lastName.text.toString().trim()
                    contact.number = number.text.toString().trim()
                    var db: DatabaseHelper = DatabaseHelper(this)
                    var result = db.insertContact(contact)
                    if(result)
                        Toast.makeText(this,"Contact Added Successfully",Toast.LENGTH_SHORT).show()
                    else
                        Toast.makeText(this,"Failed to add Contact",Toast.LENGTH_SHORT).show()
                    var intent = Intent(this,MainActivity::class.java)
                    startActivity(intent)
                }
            })
        }
        else
        {
            title.text = "Search Contact"
            number.visibility = View.GONE
            btn.text = "Search"

            btn.setOnClickListener(View.OnClickListener {
                if(firstName.text.isEmpty() &&  lastName.text.isEmpty())
                {
                    firstName.error = "First Name is required"
                }
                else
                {
                    var intent = Intent(this,MainActivity::class.java)
                    intent.putExtra("First Name", firstName.text.toString())
                    intent.putExtra("Last Name", lastName.text.toString())
                    startActivity(intent)
                }
            })
        }
    }
}