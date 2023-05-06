package com.example.energysavingappnew.adapter

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.energysavingappnew.FilterItem
import com.example.energysavingappnew.activities.UpdateItemActivity
import com.example.energysavingappnew.databinding.RowItemBinding
import com.example.energysavingappnew.model.ItemModel
import com.google.firebase.database.FirebaseDatabase

class AdapterItem: RecyclerView.Adapter<AdapterItem.HolderItem>, Filterable {
    private val context: Context
    public var itemArrayList: ArrayList<ItemModel>
    private var filterList: ArrayList<ItemModel>

    private var filter: FilterItem? = null

    private lateinit var binding: RowItemBinding

    //constructor
    constructor(context: Context, itemArrayList: ArrayList<ItemModel>) {
        this.context = context
        this.itemArrayList = itemArrayList
        this.filterList = itemArrayList
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HolderItem {
        //inflate/bind row_item.xml
        binding = RowItemBinding.inflate(LayoutInflater.from(context), parent, false)

        return HolderItem(binding.root)
    }

    override fun getItemCount(): Int {
        return itemArrayList.size //number of items in list
    }

    override fun onBindViewHolder(holder: HolderItem, position: Int) {
        //Get Data, set data, handle clicks etc

        //get data
        val model = itemArrayList[position]
        val id = model.id
        val itemName = model.itemName
        val itemDesc = model.itemDesc
        val itemPrice = model.itemPrice
        val uid = model.uid

        //set data
        holder.itemNameTv.text = itemName
        holder.itemDescTv.text = itemDesc

        //handle click, delete category
        holder.deleteBtn.setOnClickListener {
            val builder = AlertDialog.Builder(context)
            builder.setTitle("Delete")
                .setMessage("Are you sure want to delete this item?")
                .setPositiveButton("Confirm"){a, d->
                    Toast.makeText(context, "Deleting...", Toast.LENGTH_SHORT).show()
                    deleteItem(model, holder)
                }
                .setNegativeButton("Cancel"){a, d->
                    a.dismiss()
                }
                .show()
        }
        holder.itemView.setOnClickListener {
            val intent = Intent(context, UpdateItemActivity::class.java)
            intent.putExtra("id",id)
            intent.putExtra("itemName",itemName)
            intent.putExtra("itemDesc",itemDesc)
            intent.putExtra("itemPrice",itemPrice)
            intent.putExtra("uid",uid)

            context.startActivity(intent)
        }
    }

    private fun deleteItem(model: ItemModel, holder: HolderItem) {
        //get id of item to declare
        val id = model.id
        //firebase DB > Items > itemId
        val ref = FirebaseDatabase.getInstance().getReference("Items")
        ref.child(id)
            .removeValue()
            .addOnSuccessListener {
                Toast.makeText(context, "Deleted...", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener{e->
                Toast.makeText(context, "Unable to delete due to ${e.message}", Toast.LENGTH_SHORT).show()

            }
    }




    //ViewHolder class to hold/init UI views for row_category.xml
    inner class HolderItem(itemView: View): RecyclerView.ViewHolder(itemView){
        //init ui views
        var itemNameTv: TextView = binding.itemNameTv
        var itemDescTv: TextView = binding.itemDescTv
        var deleteBtn: ImageButton = binding.deleteBtn
        var cardView: CardView = binding.cardView
    }

    override fun getFilter(): Filter {
        if (filter == null){
            filter = FilterItem(filterList, this)
        }
        return filter as FilterItem
    }


}