package com.example.energysavingappnew

import android.widget.Filter
import com.example.energysavingappnew.adapter.AdapterItem
import com.example.energysavingappnew.model.ItemModel

class FilterItem: Filter {

    //arraylist in which we want to search
    private var filterList: ArrayList<ItemModel>

    //adpter in which filter need to be implemented
    private var adapterItem: AdapterItem

    //constructor
    constructor(filterList: ArrayList<ItemModel>, adapterItem: AdapterItem) : super() {
        this.filterList = filterList
        this.adapterItem = adapterItem
    }

    override fun performFiltering(constraint: CharSequence?): FilterResults {
        var constraint = constraint
        val results = FilterResults()

        //value should note be null and not empty
        if (constraint != null && constraint.isNotEmpty()){
            //searched value is nor null not empty

            //change to upper case, or lower
            constraint = constraint.toString().uppercase()
            val filteredModels:ArrayList<ItemModel> = ArrayList()
            for (i in 0 until filterList.size){
                //validate
                if (filterList[i].itemName.uppercase().contains(constraint)){
                    //add to filtered list
                    filteredModels.add(filterList[i])
                }
            }
            results.count = filteredModels.size
            results.values = filteredModels
        }
        else{
            //search value is either null or empty
            results.count = filterList.size
            results.values = filterList
        }
        return results
    }

    override fun publishResults(constraint: CharSequence?, results: FilterResults) {
        //apply filter changes
        adapterItem.itemArrayList = results.values as ArrayList<ItemModel>

        //notify changes
        adapterItem.notifyDataSetChanged()
    }
}