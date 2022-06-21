package com.example.comicly

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.RecyclerView
import com.example.comicly.databinding.LayoutListBinding

class AdapterComicAdmin : RecyclerView.Adapter<AdapterComicAdmin.HolderComicAdmin>, Filterable {

    private var context : Context

    public var comicArrayList : ArrayList<Comic>
    private val filterList: ArrayList<Comic>

    private lateinit var binding: LayoutListBinding

    private var filter: FilterComicAdmin? = null

    constructor(context: Context, comicArrayList: ArrayList<Comic>) : super() {
        this.context = context
        this.comicArrayList = comicArrayList
        this.filterList = comicArrayList
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HolderComicAdmin {
        binding = LayoutListBinding.inflate(LayoutInflater.from(context), parent, false)

        return HolderComicAdmin(binding.root)
    }

    override fun onBindViewHolder(holder: HolderComicAdmin, position: Int) {
        val model = comicArrayList[position]
        val uid = model.uid
        val name = model.name
        val author = model.author

        holder.comicName.text = name
        holder.authorName.text = author

        holder.itemView.setOnClickListener{
            val intent = Intent(context, ComicPageActivity::class.java)
            intent.putExtra("uid", uid.toString())
            context.startActivity(intent)
        }

    }

    override fun getItemCount(): Int {
        return comicArrayList.size
    }

    override fun getFilter(): Filter {
        if(filter == null){
            filter = FilterComicAdmin(filterList, this)
        }

        return filter as FilterComicAdmin
    }

    inner class HolderComicAdmin(itemView: View) : RecyclerView.ViewHolder(itemView){

        val comicName = binding.comicName
        val authorName = binding.comicAuthor

    }
}