package com.example.myapp

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.ListView
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat.startActivity
import androidx.core.view.marginEnd
import database.DatabaseHelper
import model.Contact
import model.MyCustomAdapter

class MainActivity : AppCompatActivity() {

    var dbHandle = DatabaseHelper(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //Actually, I'm using this Home activity to display both "All Contacts" and "searched contacts" based on the condition
        var contacts = ArrayList<Contact>()
        if(!intent.hasExtra("First Name"))
        {
            contacts = dbHandle.getAllContacts() as ArrayList<Contact>
        }
        else
        {
            contacts = dbHandle.searchByName(intent.getStringExtra("First Name").toString().trim(), intent.getStringExtra("Last Name").toString().trim()) as ArrayList<Contact>
            var welcomeText: TextView = findViewById(R.id.welcomeText)
            var name = "${intent.getStringExtra("First Name")} ${intent.getStringExtra("Last Name")}".trim()
            welcomeText.text = "Search Results for \"$name\""
            var searchButton: ImageButton = findViewById(R.id.search_btn)
            searchButton.visibility = View.GONE
            var addButton: Button = findViewById(R.id.add_btn)
            addButton.visibility = View.GONE
            if(contacts.count() <= 0)
            {
                var searchText: TextView = findViewById(R.id.searchText)
                searchText.visibility = View.VISIBLE
            }
        }

        var contactsList: ListView = findViewById(R.id.contactsList)

        var addButton: Button = findViewById(R.id.add_btn)
        addButton.setOnClickListener(View.OnClickListener {
            var intent = Intent(this,insertContact::class.java)
            intent.putExtra("Mode","Insert")
            startActivity(intent)
        })

        var searchButton: ImageButton = findViewById(R.id.search_btn)
        searchButton.setOnClickListener(View.OnClickListener {
            var intent = Intent(this,insertContact::class.java)
            intent.putExtra("Mode","Search")
            startActivity(intent)
        })

        val contactAdapter: MyCustomAdapter = MyCustomAdapter(this, contacts)
        contactsList.adapter = contactAdapter

        contactsList.onItemClickListener = AdapterView.OnItemClickListener{adapterView: AdapterView<*>, view: View, pos: Int, id: Long ->
            val contact: Contact = adapterView.getItemAtPosition(pos) as Contact
            val dialIntent = Intent(Intent.ACTION_DIAL)
            dialIntent.data = Uri.parse("tel:" + "${contact.number}")
            startActivity(dialIntent)
        }

    }
}