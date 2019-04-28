package com.jtoru.lab5firebase

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.widget.Toast
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_add.*

class AddActivity : AppCompatActivity() {

    private lateinit var database: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add)
        database = FirebaseDatabase.getInstance().reference
        button.setOnClickListener {
            addItem()
        }

    }

    fun addItem(){
        val name = name_input.text.toString()
        val description = description_input.text.toString()
        val cost = cost_input.text.toString()
        if (TextUtils.isEmpty(name)) {
            name_input.error = "Required"
            return
        }
        if (TextUtils.isEmpty(description)) {
            description_input.error = "Required"
            return
        }
        if (TextUtils.isEmpty(cost)) {
            cost_input.error = "Required"
            return
        }
        button.isEnabled = false
        val item = Item(name,cost,description,"image")
        database.child("items").child(name).setValue(item)
            .addOnSuccessListener {
                Toast.makeText(this@AddActivity, "Addded Correctly", Toast.LENGTH_SHORT).show()
                button.isEnabled = true
                finish()
            }
            .addOnFailureListener {
                Toast.makeText(this@AddActivity, "Error with the process", Toast.LENGTH_SHORT).show()
                button.isEnabled = true

            }



    }
}
