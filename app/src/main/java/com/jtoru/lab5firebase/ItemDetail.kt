package com.jtoru.lab5firebase

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.view.Menu
import android.view.MenuItem
import android.widget.TextView
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class ItemDetail : AppCompatActivity() {

    lateinit var name : TextView
    lateinit var description : TextView
    lateinit var cost : TextView
    lateinit var database: DatabaseReference
    lateinit var item : Item

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_item_detail)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setDisplayShowHomeEnabled(true)
        supportActionBar?.title = "Item Detail"

        name = findViewById(R.id.textView5)
        description = findViewById(R.id.textView7)
        cost = findViewById(R.id.textView9)

        database = FirebaseDatabase.getInstance().reference

        item = this.intent.getSerializableExtra("item") as Item
        name.text = item.name
        description.text = item.description
        cost.text = item.amount
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.toolbar_item, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when(item?.itemId){
            R.id.delte_action -> {
                showDialog("Alert","Do you want delete it?.")
                return true
            }
            else -> {
                return super.onOptionsItemSelected(item)
            }
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    override fun onBackPressed() {
        finish()
    }

    private fun showDialog(title : String, message : String){
        val builder = AlertDialog.Builder(this)
        builder.setTitle(title)
        builder.setMessage(message)
        builder.setNegativeButton("Cancel", null)
        builder.setPositiveButton("Ok"){
            dialog, _ ->
            database.child("items").child(item.name!!).removeValue()
            dialog.dismiss()
            finish()
        }
        val dialog = builder.create()
        dialog.show()
    }
}
