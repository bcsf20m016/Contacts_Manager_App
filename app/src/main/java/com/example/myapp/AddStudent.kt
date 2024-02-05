package com.example.myapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.database.FirebaseDatabase
import model.Student

class AddStudent : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_student)

        var std_rollno:EditText = findViewById(R.id.std_rollno)
        var std_name:EditText = findViewById(R.id.std_name)
        var std_semester:EditText = findViewById(R.id.std_semester)
        var std_course:EditText = findViewById(R.id.std_course)
        var add_std_btn:Button = findViewById(R.id.add_std_btn)

        add_std_btn.setOnClickListener(View.OnClickListener {
            val name = std_name.text.toString().trim()
            val rollNo = std_rollno.text.toString().trim()
            val semester = std_semester.text.toString().trim()
            val course = std_course.text.toString().trim()
            submitData(Student(name, rollNo, semester, course))
        })
    }
    fun submitData(student: Student){
        val database = FirebaseDatabase.getInstance().reference
        database.child("Students").child(student.rollNo).setValue(student)
        database.get().addOnCompleteListener { task ->
            if (!task.isSuccessful) {
                Log.e("firebase", "Error getting data", task.exception)
                Toast.makeText(this@AddStudent, "Data not stored", Toast.LENGTH_SHORT).show()
            } else {
                Log.d("firebase", task.result.value.toString())
                Toast.makeText(this@AddStudent, "Your data is stored", Toast.LENGTH_SHORT).show()
            }
        }
    }
}