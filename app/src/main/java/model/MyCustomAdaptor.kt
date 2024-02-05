package model

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat.startActivity
import com.example.myapp.R
import com.example.myapp.insertContact
import database.DatabaseHelper

class MyCustomAdapter (context: Context, contactList: List<Contact>) : ArrayAdapter<Contact>(context, 0, contactList) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {

        // convertView which is recyclable view
        var currentItemView = convertView

        // of the recyclable view is null then inflate the custom layout for the same
        if (currentItemView == null) {
            currentItemView =
                LayoutInflater.from(context).inflate(R.layout.rowitem, parent, false)
        }

        // get the position of the view from the ArrayAdapter
        val contact: Contact? = getItem(position)

        val firstName = currentItemView!!.findViewById<TextView>(R.id.firstName)
        val lastName = currentItemView!!.findViewById<TextView>(R.id.lastName)
        val phoneNumber = currentItemView.findViewById<TextView>(R.id.phone_no)

        val editButton = currentItemView.findViewById<ImageView>(R.id.edit_btn)
        val deleteButton = currentItemView.findViewById<ImageView>(R.id.delete_btn)

        editButton.setOnClickListener {
            var intent = Intent(context, insertContact::class.java)
            intent.putExtra("Mode","Edit")
            intent.putExtra("Id",contact?.id)
            intent.putExtra("First Name", contact?.firstName)
            intent.putExtra("Last Name", contact?.lastName)
            intent.putExtra("Phone Number", contact?.number)
            context.startActivity(intent)
        }

        deleteButton.setOnClickListener {
            showDeleteConfirmationDialog(position)
        }


        assert(contact != null)
        if (contact != null) {
            firstName.setText(contact.firstName)
            lastName.setText(contact.lastName)
            phoneNumber.setText(contact.number)
        }

        // then return the recyclable view
        return currentItemView
    }

    private fun showDeleteConfirmationDialog(position: Int) {
        val builder = AlertDialog.Builder(context)

        builder.setTitle("Confirm Deletion")
        builder.setMessage("Are you sure you want to delete this Contact?")

        builder.setPositiveButton("Yes") { dialog, which ->
            // delete operation for the item at the specified position
            var contact = getItem(position)
            remove(contact)
            var db = DatabaseHelper(context)

            if (contact != null) {
                db.deleteContact(contact)
            }
            notifyDataSetChanged()
        }

        builder.setNegativeButton("No") { dialog, which ->
        }

        val dialog: AlertDialog = builder.create()

        // Show the dialog
        dialog.show()
    }
}
