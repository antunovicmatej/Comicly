package com.example.comicly

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.comicly.databinding.LayoutListFavoriteBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.ktx.Firebase

class AdapterComicFavorite : RecyclerView.Adapter<AdapterComicFavorite.HolderComicFavorite>{

    private var context : Context

    public var comicArrayList : ArrayList<Comic>

    private lateinit var binding: LayoutListFavoriteBinding

    constructor(context: Context, comicArrayList: ArrayList<Comic>) {
        this.context = context
        this.comicArrayList = comicArrayList
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HolderComicFavorite {
        binding = LayoutListFavoriteBinding.inflate(LayoutInflater.from(context), parent, false)

        return HolderComicFavorite(binding.root)
    }

    override fun onBindViewHolder(holder: HolderComicFavorite, position: Int) {

        val model = comicArrayList[position]

        loadComicDetails(model, holder)

        holder.itemView.setOnClickListener{
            val intent = Intent(context, ComicPageActivity::class.java)
            intent.putExtra("uid", model.uid.toString())
            context.startActivity(intent)
        }

        holder.removeFavBtn.setOnClickListener{
            removeFromFavorite(context, model.uid.toString())
        }

    }

    private fun loadComicDetails(model: Comic, holder: AdapterComicFavorite.HolderComicFavorite) {
        val uid = model.uid

        val ref = FirebaseDatabase.getInstance().getReference("comics")
        ref.child(uid.toString()).addListenerForSingleValueEvent(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                val name = "${snapshot.child("name").value}"
                val author = "${snapshot.child("author").value}"
                val comicuid = "${snapshot.child("uid").value}"

                model.isFavorite = true
                model.name = name
                model.author = author
                model.uid = comicuid.toLong()

                holder.comicName.text = name
                holder.authorName.text = author
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }

    override fun getItemCount(): Int {
        return comicArrayList.size
    }

    inner class HolderComicFavorite(itemView: View) : RecyclerView.ViewHolder(itemView){

        var comicName = binding.comicName
        var authorName = binding.comicAuthor
        var removeFavBtn = binding.removeFavBtn

    }

    fun removeFromFavorite(context: Context, uid: String){
        val firebaseAuth = FirebaseAuth.getInstance()
        val ref = FirebaseDatabase.getInstance().getReference("users")
        ref!!.child(firebaseAuth.uid.toString()).child("favorites").child(uid)
            .removeValue().addOnSuccessListener {
                Toast.makeText(context, "Removed from favorite...", Toast.LENGTH_SHORT).show()

            }.addOnFailureListener{
                Toast.makeText(context, "Failed to remove from favorite...", Toast.LENGTH_SHORT).show()
            }
    }


}