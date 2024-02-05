package database

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import model.Contact

class DatabaseHelper(context: Context): SQLiteOpenHelper(context, DBName, null, DBVERSION) {
    companion object{
        private val DBName = "ContactsList"
        private val DBVERSION = 1
        private val TableName = "CONTACTS"
        private val Id = "ID"
        private val FirstName = "FirstName"
        private val LastName = "LastName"
        private val ContactNo = "ContactNo"
    }
    override fun onCreate(db: SQLiteDatabase?){
        var query = ("CREATE TABLE $TableName ($Id INTEGER PRIMARY KEY AUTOINCREMENT, $FirstName TEXT, $LastName TEXT, $ContactNo TEXT)")
        db?.execSQL(query)

//        //Adding some dummy data
//        insertContact(Contact("Saif","Ullah","0322-9117616"))
//        insertContact(Contact("Ali","Hassan","0341-4070886"))
//        insertContact(Contact("Muhammad","Bilal","0324-8445443"))
//        insertContact(Contact("Muhammad","Awais","0343-1704951"))
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        var query = "DROP TABLE IF EXISTS $TableName"
        db?.execSQL(query)
        onCreate(db)
    }
    //From here, our CRUD operations will start
    fun getAllContacts():List<Contact>{
        val contacts = ArrayList<Contact>()
        var db = writableDatabase
        var query = "SELECT * FROM $TableName"
        var cursor =  db.rawQuery(query,null)
        if(cursor != null)
        {
            if (cursor.moveToFirst()) {
                do {
                    var contct = Contact()
                    contct.id = Integer.parseInt(cursor.getString(0))
                    contct.firstName = cursor.getString(1)
                    contct.lastName = cursor.getString(2)
                    contct.number = cursor.getString(3)
                    contacts.add(contct)
                } while (cursor.moveToNext())
            }
        }
        cursor.close()
        db.close()
        return contacts
    }
    fun insertContact(contactNo: Contact): Boolean
    {
        val db = writableDatabase
        val values = ContentValues()
        values.put(FirstName, contactNo.firstName)
        values.put(LastName, contactNo.lastName)
        values.put(ContactNo, contactNo.number)
        val result = db.insert(TableName,null,values)
        db.close()
        return(Integer.parseInt("$result") != -1)
    }
    fun searchByName(firstName: String, lastName:String):List<Contact>
    {
        val contacts = ArrayList<Contact>()
        var db = writableDatabase
        val query = "SELECT * FROM $TableName WHERE LOWER($FirstName)=LOWER('$firstName') or lower($LastName)=lower('$lastName') or lower($FirstName)=lower('$lastName') or lower($LastName)=lower('$firstName')"
        var cursor = db.rawQuery(query,null)
        if(cursor != null)
        {
            if (cursor.moveToFirst()) {
                do {
                    var contct = Contact()
                    contct.id = Integer.parseInt(cursor.getString(0))
                    contct.firstName = cursor.getString(1)
                    contct.lastName = cursor.getString(2)
                    contct.number = cursor.getString(3)
                    contacts.add(contct)
                } while (cursor.moveToNext())
            }
        }
        cursor.close()
        db.close()
        return contacts
    }
    fun deleteContact(contactNo: Contact): Boolean
    {
        val db = writableDatabase
        var result = db.delete(TableName, ContactNo + "=?", arrayOf(contactNo.number));
        db.close()
        return result > 0
    }
    fun deleteTable()
    {
        var query = "DROP TABLE IF EXISTS $TableName"
        var db = writableDatabase
        db.rawQuery(query,null)
        db.close()
    }
    fun updateContact(contactNo: Contact): Boolean
    {
        val db = writableDatabase
        val values = ContentValues()
        values.put(FirstName,contactNo.firstName)
        values.put(LastName,contactNo.lastName)
        values.put(ContactNo,contactNo.number)
        var result = db.update(TableName, values, Id + "=?", arrayOf(contactNo.id.toString()))
        db.close()
        return result > 0
    }
}