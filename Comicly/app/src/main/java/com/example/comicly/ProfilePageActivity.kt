package com.example.comicly

import android.Manifest.permission.*
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.ContactsContract
import android.provider.MediaStore
import android.widget.*
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.contract.ActivityResultContracts.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.comicly.databinding.ActivityProfileBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import java.util.jar.Manifest

class ProfilePageActivity : AppCompatActivity() {

    lateinit var arrowIcon2 : ImageView
    lateinit var cameraIcon : ImageView
    lateinit var imageView : ImageView

    private lateinit var binding : ActivityProfileBinding

    lateinit var firebaseAuth : FirebaseAuth
    lateinit var storageReference : StorageReference
    var databaseReference : DatabaseReference? = null
    var database : FirebaseDatabase? = null

    private lateinit var comicArrayList: ArrayList<Comic>
    private lateinit var comicReadArrayList: ArrayList<Comic>
    private lateinit var adapterComicFavorite: AdapterComicFavorite
    private lateinit var adapterComicRead: AdapterComicRead

    lateinit var activityResultLauncher : ActivityResultLauncher<Intent>

    companion object{
        private const val CAMERA_PERMISSION_CODE = 1
        private const val CAMERA_REQUEST_CODE = 2
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        storageReference = FirebaseStorage.getInstance().getReference()

        arrowIcon2 = findViewById(R.id.arrowIcon2)
        cameraIcon = findViewById(R.id.cameraIcon)
        imageView = findViewById(R.id.iv_image)

        arrowIcon2.setOnClickListener{
            val intent = Intent(this, HomePageActivity::class.java)
            startActivity(intent)
        }

        val getAction = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
            val bitmap = it?.data?.extras?.get("data") as Bitmap
            imageView.setImageBitmap(bitmap)
        }

        cameraIcon.setOnClickListener{
            if(ContextCompat.checkSelfPermission(this, CAMERA) == PackageManager.PERMISSION_GRANTED){
                val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                getAction.launch(intent)
            }else{
                ActivityCompat.requestPermissions(this, arrayOf(CAMERA),
                CAMERA_PERMISSION_CODE)
            }
        }


        firebaseAuth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance()
        databaseReference = database?.reference!!.child("profile")

        loadProfile()
        loadFavoriteComics()
        loadReadComics()
    }

    private fun loadFavoriteComics() {

        comicArrayList = ArrayList()

        val ref = FirebaseDatabase.getInstance().getReference("users")
        ref.child(firebaseAuth.uid!!).child("favorites")
            .addValueEventListener(object : ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    comicArrayList.clear()
                    for(ds in snapshot.children){
                        val uid = "${ds.child("uid").value}"

                        val comic = Comic()
                        comic.uid = uid.toLong()

                        comicArrayList.add(comic)
                    }
                    adapterComicFavorite = AdapterComicFavorite(this@ProfilePageActivity, comicArrayList)
                    binding.favouriteList.adapter = adapterComicFavorite
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }

            })
    }

    private fun loadReadComics() {

        comicReadArrayList = ArrayList()

        val ref = FirebaseDatabase.getInstance().getReference("users")
        ref.child(firebaseAuth.uid!!).child("read")
            .addValueEventListener(object : ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    comicReadArrayList.clear()
                    for(ds in snapshot.children){
                        val uid = "${ds.child("uid").value}"

                        val comic = Comic()
                        comic.uid = uid.toLong()

                        comicReadArrayList.add(comic)
                    }
                    adapterComicRead = AdapterComicRead(this@ProfilePageActivity, comicReadArrayList)
                    binding.readList.adapter = adapterComicRead
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }

            })
    }

    private fun loadProfile(){

        val ref = FirebaseDatabase.getInstance().getReference("users")

        ref.child(firebaseAuth.uid!!)
            .addValueEventListener(object: ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    val name = "${snapshot.child("name").value}"
                    val email = "${snapshot.child("email").value}"
                    val uid = "${snapshot.child("uid").value}"

                    binding.fullName.text = name
                    binding.email.text = email
                }

                override fun onCancelled(error: DatabaseError) {

                }
            })
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

            }else{
                Toast.makeText(this,"Oops you just denied permission for camera.", Toast.LENGTH_LONG).show()
            }
        }
    }


}