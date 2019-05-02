package com.jtoru.lab5firebase

import android.app.Activity
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import android.view.ViewGroup
import android.widget.Toast
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.IdpResponse
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.Query
import android.support.v7.widget.helper.ItemTouchHelper
import android.support.v4.view.accessibility.AccessibilityEventCompat.setAction
import android.support.design.widget.Snackbar
import android.support.annotation.NonNull



class MainActivity : AppCompatActivity() {

    lateinit var recyclerView : RecyclerView
    lateinit var database: DatabaseReference
    private lateinit var manager: LinearLayoutManager
    private var adapter : FirebaseRecyclerAdapter<Item, ItemViewHolder>? = null

    companion object {

        private const val RC_SIGN_IN = 123
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        this.supportActionBar?.title = "Home"

        authScreen()
        val fab = findViewById<FloatingActionButton>(R.id.fab)
        fab.setOnClickListener {
            val intent = Intent(this, AddActivity::class.java)
            startActivity(intent)
        }

        recyclerView = findViewById(R.id.items_recycler_view)
        database = FirebaseDatabase.getInstance().reference
        manager = LinearLayoutManager(this)
        manager.reverseLayout = true
        manager.stackFromEnd = true
        recyclerView.layoutManager = manager
        queryInfo()
        //enableSwipeToDeleteAndUndo()

    }

    private fun enableSwipeToDeleteAndUndo() {
        val swipeToDeleteCallback = object : SwipeToDeleteCallback(this) {
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, i: Int) {


                val position = viewHolder.adapterPosition

            }
        }

        val itemTouchhelper = ItemTouchHelper(swipeToDeleteCallback)
        itemTouchhelper.attachToRecyclerView(recyclerView)
    }

    fun queryInfo(){
        val itemQuery = getQuery(database)
        val options =  FirebaseRecyclerOptions.Builder<Item>()
            .setQuery(itemQuery, Item::class.java)
            .build()
        adapter = object : FirebaseRecyclerAdapter<Item, ItemViewHolder>(options){
            override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
                val inflater = LayoutInflater.from(parent.context)
                return ItemViewHolder(inflater.inflate(R.layout.item_row, parent,false))
            }

            override fun onBindViewHolder(holder: ItemViewHolder, position: Int, model: Item) {
                val itemRef = getRef(position)
                holder.itemView.setOnClickListener {
                    val intent = Intent(this@MainActivity, ItemDetail::class.java)
                    intent.putExtra("item",model)
                    startActivity(intent)
                }
                holder.bindToItem(model)
            }
        }
        recyclerView.adapter = adapter
    }

    fun getQuery(databaseReference: DatabaseReference): Query{
        return databaseReference.child("items")
    }

    override fun onStart() {
        super.onStart()
        adapter?.startListening()
    }

    override fun onStop() {
        super.onStop()
        adapter?.stopListening()
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == RC_SIGN_IN) {
            val response = IdpResponse.fromResultIntent(data)

            if (resultCode == Activity.RESULT_OK) {
                // Successfully signed in
                val user = FirebaseAuth.getInstance().currentUser
                // ...
            } else {
                // Sign in failed. If response is null the user canceled the
                // sign-in flow using the back button. Otherwise check
                // response.getError().getErrorCode() and handle the error.
                // ...
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.toolbar_sign_out, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when(item?.itemId){
            R.id.help_action -> {
                signOut()
                return true
            }
            else -> {
                return super.onOptionsItemSelected(item)
            }
        }
    }

    private fun authScreen(){
        val providers = arrayListOf(
            AuthUI.IdpConfig.EmailBuilder().build(),
            AuthUI.IdpConfig.PhoneBuilder().build(),
            AuthUI.IdpConfig.GoogleBuilder().build())

// Create and launch sign-in intent
        startActivityForResult(
            AuthUI.getInstance()
                .createSignInIntentBuilder()
                .setAvailableProviders(providers)
                .build(),
            RC_SIGN_IN)
    }

    private fun signOut() {
        // [START auth_fui_signout]
        AuthUI.getInstance()
            .signOut(this)
            .addOnCompleteListener {
                // ...
                authScreen()
            }
        // [END auth_fui_signout]
    }
}
