package com.example.comicly

import android.Manifest.permission.*
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.ContactsContract
import android.provider.MediaStore
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import java.util.jar.Manifest

class ProfilePageActivity : AppCompatActivity() {

    lateinit var arrowIcon2 : ImageView
    lateinit var cameraIcon : ImageView
    lateinit var cameraImage : ImageView

    lateinit var favouriteListView : ListView
    val favouriteList = ArrayList<String>()
    lateinit var arrayAdapter: ArrayAdapter<String>

    lateinit var auth : FirebaseAuth
    var databaseReference : DatabaseReference? = null
    var database : FirebaseDatabase? = null

    companion object{
        private const val CAMERA_PERMISSION_CODE = 1
        private const val CAMERA_REQUEST_CODE = 2
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        arrowIcon2 = findViewById(R.id.arrowIcon2)
        cameraIcon = findViewById(R.id.cameraIcon)

        favouriteListView = findViewById(R.id.favouriteList)
        arrayAdapter = ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, favouriteList)
        favouriteListView.adapter = arrayAdapter

        arrowIcon2.setOnClickListener{
            val intent = Intent(this, HomePageActivity::class.java)
            startActivity(intent)
        }

        cameraIcon.setOnClickListener{
            if(ContextCompat.checkSelfPermission(this, CAMERA) == PackageManager.PERMISSION_GRANTED){
                val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                startActivity(intent)
            }else{
                ActivityCompat.requestPermissions(this, arrayOf(CAMERA),
                CAMERA_PERMISSION_CODE)
            }
        }


        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance()
        databaseReference = database?.reference!!.child("profile")

        loadProfile()

    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if(requestCode == CAMERA_PERMISSION_CODE){
            if(grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                startActivity(intent)
            }else{
                Toast.makeText(this,"Oops you just denied permission for camera.", Toast.LENGTH_LONG).show()
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        /*if(requestCode == CAMERA_REQUEST_CODE){
            val thumbNail : Bitmap = data!!.extras!!.get("data") as Bitmap
            cameraImage.setImageBitmap(thumbNail)
        }
        */
    }

    private fun loadProfile(){
        val user = auth.currentUser
        val userreference = databaseReference?.child(user?.uid!!)


        val email = findViewById<TextView>(R.id.email)
        email.text = user?.email
        userreference?.addValueEventListener(object: ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {

            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })
    }
}