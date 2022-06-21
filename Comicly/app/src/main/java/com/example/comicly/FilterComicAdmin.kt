package com.example.comicly

import android.widget.Filter

class FilterComicAdmin : Filter{

    var filterList : ArrayList<Comic>

    var adapterComicAdmin : AdapterComicAdmin

    constructor(filterList: ArrayList<Comic>, adapterComicAdmin: AdapterComicAdmin){
        this.filterList = filterList
        this.adapterComicAdmin = adapterComicAdmin
    }

    override fun performFiltering(constraint: CharSequence?): FilterResults {
        var constraint:CharSequence? = constraint
        val results = FilterResults()

        if(constraint != null && constraint.isNotEmpty()){
            constraint = constraint.toString().lowercase()
            val filteredModels = ArrayList<Comic>()
            for(i in filterList.indices){
                if(filterList[i].name?.lowercase()!!.contains(constraint)){
                    filteredModels.add(filterList[i])
                }
            }
            results.count = filteredModels.size
            results.values = filteredModels
        }else{
            results.count = filterList.size
            results.values = filterList
        }
        return results
    }

    override fun publishResults(constraint: CharSequence?, results: FilterResults) {
        adapterComicAdmin.comicArrayList = results.values as ArrayList<Comic>

        adapterComicAdmin.notifyDataSetChanged()
    }

}