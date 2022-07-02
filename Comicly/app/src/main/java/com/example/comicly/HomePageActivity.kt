package com.example.comicly

import android.content.DialogInterface
import android.content.Intent
import android.graphics.drawable.ClipDrawable.HORIZONTAL
import android.os.Binder
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.comicly.databinding.ActivityHomepageBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import java.lang.Exception

class HomePageActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHomepageBinding
    private lateinit var comicArrayList: ArrayList<Comic>
    private lateinit var adapterComicAdmin: AdapterComicAdmin

    private lateinit var auth: FirebaseAuth

    lateinit var recyclerView : RecyclerView
    lateinit var manager: LinearLayoutManager

    private companion object{
        const val TAG = "COMIC_LIST_ADMIN_TAG"
    }

    lateinit var arrowIcon : ImageView
    lateinit var profileIcon : ImageView
    lateinit var barTitle : TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomepageBinding.inflate(layoutInflater)
        setContentView(binding.root)

        arrowIcon = findViewById(R.id.arrowIcon)
        profileIcon = findViewById(R.id.profileIcon)
        barTitle = findViewById(R.id.barTitle)

        arrowIcon.setOnClickListener{
            val intent = Intent(this, SignInActivity::class.java)
            startActivity(intent)
        }

        profileIcon.setOnClickListener{
            val intent = Intent(this, ProfilePageActivity::class.java)
            startActivity(intent)
        }

        loadComicList()

        binding.searchField.addTextChangedListener(object: TextWatcher{
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(s: CharSequence?, p1: Int, p2: Int, p3: Int) {
                try {
                    adapterComicAdmin.filter!!.filter(s)
                }
                catch (e: Exception){
                    Log.d(TAG, "onDataChange: ${e.message}")
                }
            }

            override fun afterTextChanged(p0: Editable?) {

            }

        })


    }

    private fun loadComicList() {
        comicArrayList = ArrayList()

        val ref = FirebaseDatabase.getInstance().getReference("comics")
        ref.orderByChild("name").addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                for(ds in snapshot.children){
                    val model = ds.getValue(Comic::class.java)
                    if (model != null) {
                        comicArrayList.add(model)
                        Log.d(TAG, "onDataChange: ${model.name} ${model.uid}")
                    }
                }
                adapterComicAdmin = AdapterComicAdmin(this@HomePageActivity, comicArrayList)
                binding.comicsRv.adapter = adapterComicAdmin
            }

            override fun onCancelled(error: DatabaseError) {

            }

        })
    }
}