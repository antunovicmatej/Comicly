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

    lateinit var arrowIcon3 : ImageView
    lateinit var favBtn : Button
    lateinit var readBtn : Button

    var uid = ""

    private var isInMyFavorite = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityComicpageBinding.inflate(layoutInflater)
        setContentView(binding.root)

        uid = intent.getStringExtra("uid")!!

        favBtn = findViewById(R.id.favouriteBtn)
        readBtn = findViewById(R.id.readBtn)
        arrowIcon3 = findViewById(R.id.arrowIcon3)

        arrowIcon3.setOnClickListener{
            val intent = Intent(this, HomePageActivity::class.java)
            startActivity(intent)
        }

        loadComicDetails()

        binding.favouriteBtn.setOnClickListener{
            addToFavorite()
        }

        binding.readBtn.setOnClickListener{
            addToRead()
        }

    }

    private fun checkIsFavorite() {
        val ref = FirebaseDatabase.getInstance().getReference("users")
        ref!!.child(firebaseAuth.uid.toString()).child("favorites").child(uid)
            .addValueEventListener(object : ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    isInMyFavorite = snapshot.exists()
                }

                override fun onCancelled(error: DatabaseError) {
                }

            })
    }

    private fun loadComicDetails() {
        val ref = FirebaseDatabase.getInstance().getReference("comics")
        ref.child(uid).addListenerForSingleValueEvent(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                val name = "${snapshot.child("name").value}"
                val author = "${snapshot.child("author").value}"
                val description = "${snapshot.child("description").value}"
                val uid = "${snapshot.child("uid").value}"


                binding.name.text = name
                binding.author.text = author
                binding.description.text = description
            }

            override fun onCancelled(error: DatabaseError) {

            }

        })
    }

    fun addToFavorite(){
        firebaseAuth = FirebaseAuth.getInstance()

        val hashMap = HashMap<String, Any>()
        hashMap["uid"] = uid

        hashMap.put("uid", uid)

        val ref = FirebaseDatabase.getInstance().getReference("users")
        ref!!.child(firebaseAuth.uid.toString()).child("favorites").child(uid)
            .setValue(hashMap)
            .addOnSuccessListener {
                Toast.makeText(this, "Added to favorite...", Toast.LENGTH_SHORT).show()
            }.addOnFailureListener{
                Toast.makeText(this, "Failed to add to favorite...", Toast.LENGTH_SHORT).show()
            }
    }

    fun addToRead(){
        firebaseAuth = FirebaseAuth.getInstance()

        val hashMap = HashMap<String, Any>()
        hashMap["uid"] = uid

        hashMap.put("uid", uid)

        val ref = FirebaseDatabase.getInstance().getReference("users")
        ref!!.child(firebaseAuth.uid.toString()).child("read").child(uid)
            .setValue(hashMap)
            .addOnSuccessListener {
                Toast.makeText(this, "Added to read...", Toast.LENGTH_SHORT).show()
            }.addOnFailureListener{
                Toast.makeText(this, "Failed to add to read...", Toast.LENGTH_SHORT).show()
            }
    }

    public fun removeFromFavorite(context: Context, uid: String){
        val ref = FirebaseDatabase.getInstance().getReference("users")
        ref!!.child(firebaseAuth.uid.toString()).child("favorites").child(uid)
            .removeValue().addOnSuccessListener {
                Toast.makeText(this, "Removed from favorite...", Toast.LENGTH_SHORT).show()

            }.addOnFailureListener{
                Toast.makeText(this, "Failed to remove from favorite...", Toast.LENGTH_SHORT).show()
            }
    }

}