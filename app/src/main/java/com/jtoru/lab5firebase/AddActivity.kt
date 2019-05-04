package com.jtoru.lab5firebase

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_add.*
import android.provider.MediaStore
import android.util.Base64
import android.graphics.Bitmap
import android.R.attr.bitmap
import java.io.ByteArrayOutputStream


class AddActivity : AppCompatActivity() {

    private lateinit var database: DatabaseReference
    private var fileUri : Uri? = null
    lateinit var img : ImageView
    var encodedImg = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add)
        img = findViewById(R.id.imageView)
        img.visibility = View.GONE
        database = FirebaseDatabase.getInstance().reference
        button.setOnClickListener {
            addItem()
        }

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.add_item, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when(item?.itemId){
            R.id.add_action -> {
                launchCamera()

                return true
            }
            else -> {
                return super.onOptionsItemSelected(item)
            }
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
        val item = Item(name,cost,description,encodedImg)
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

    private fun launchCamera(){
        val intent = Intent(Intent.ACTION_OPEN_DOCUMENT)
        intent.type = "image/*"
        startActivityForResult(intent, TAKE_PICTURE)
    }

    companion object {
        private const val TAKE_PICTURE = 101
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if(requestCode == TAKE_PICTURE){
            if(resultCode == Activity.RESULT_OK){
                fileUri = data?.data
                img.visibility = View.VISIBLE
                val bitmap = MediaStore.Images.Media.getBitmap(this.contentResolver, fileUri)
                img.setImageBitmap(bitmap)
                val byteArrayOutputStream = ByteArrayOutputStream()
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream)
                val byteArray = byteArrayOutputStream.toByteArray()
                encodedImg = Base64.encodeToString(byteArray, Base64.DEFAULT)

            }else{
                Toast.makeText(this, "Failed with take a picture", Toast.LENGTH_SHORT).show()
            }
        }
        super.onActivityResult(requestCode, resultCode, data)
    }
}
