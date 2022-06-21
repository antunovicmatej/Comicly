package com.example.comicly

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.comicly.databinding.ActivityComicpageBinding
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import java.util.*
import kotlin.collections.HashMap

class ComicPageActivity : AppCompatActivity(){

    private lateinit var firebaseAuth: FirebaseAuth
    var databaseReference : DatabaseReference? = null

    private lateinit var binding: ActivityComicpageBinding

    lateinit var hashMap : HashMap<String, String>

    lateinit var arrowIcon3 : ImageView
    lateinit var favBtn : Button

    var uid = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityComicpageBinding.inflate(layoutInflater)
        setContentView(binding.root)

        uid = intent.getStringExtra("uid")!!

        favBtn = findViewById(R.id.favouriteBtn)
        arrowIcon3 = findViewById(R.id.arrowIcon3)

        arrowIcon3.setOnClickListener{
            val intent = Intent(this, HomePageActivity::class.java)
            startActivity(intent)
        }


        loadComicDetails()

        binding.favouriteBtn.setOnClickListener(object : View.OnClickListener {
            override fun onClick(p0: View?) {
                addToFavorite(this@ComicPageActivity, uid)
            }
        })

    }

    private fun loadComicDetails() {
        val ref = FirebaseDatabase.getInstance().getReference("comics")
        ref.child(uid).addListenerForSingleValueEvent(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                val name = "${snapshot.child("name").value}"
                val author = "${snapshot.child("author").value}"
                val uid = "${snapshot.child("uid").value}"

                binding.name.text = name
                binding.author.text = author
            }

            override fun onCancelled(error: DatabaseError) {

            }

        })
    }

    fun addToFavorite(context : Context, comicId: String){
        firebaseAuth = FirebaseAuth.getInstance()
        var timestamp : Long = System.currentTimeMillis()


        hashMap.put("comicId", comicId)

        databaseReference = FirebaseDatabase.getInstance().getReference("comics")
        databaseReference!!.child(firebaseAuth.uid.toString()).child("favorites").child(comicId)
            .setValue(hashMap)
            .addOnSuccessListener (object : OnSuccessListener<Void>{
                override fun onSuccess(s: Void?) {
                }

            })



    }

}