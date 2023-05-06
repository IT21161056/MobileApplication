package com.example.energysavingappnew.activities

import android.app.ProgressDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.energysavingappnew.databinding.ActivityUpdateItemBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class UpdateItemActivity : AppCompatActivity() {
    private var id = ""
    private var itemName = ""
    private var itemDesc = ""
    private var itemPrice = ""
    private var uid = ""

    //view binding
    private lateinit var binding: ActivityUpdateItemBinding

    //firebase auth
    private lateinit var firebaseAuth: FirebaseAuth

    //progress dialog
    private lateinit var progressDialog: ProgressDialog


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUpdateItemBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //init firebase auth
        firebaseAuth = FirebaseAuth.getInstance()

        //set data values to update boxes
        setValuesToViews()

        //configure progress dialog
        progressDialog = ProgressDialog(this)
        progressDialog.setTitle("Please wait...")
        progressDialog.setCanceledOnTouchOutside(false)

        //get data from intent that we passed from adapter
        val intent = intent
        id = intent.getStringExtra("id")!!
        itemName = intent.getStringExtra("itemName")!!
        itemDesc = intent.getStringExtra("itemDesc")!!
        uid = intent.getStringExtra("uid")!!

        //set item name to update layout title
        binding.updateSingleItemTvTitle.text = itemName

        //handle back button click
        binding.backBtn.setOnClickListener{
            onBackPressed() //goto previous screen
        }

        binding.UpdateBtn.setOnClickListener{
            validateData()
        }

    }

    private fun setValuesToViews(){
        binding.tvItemName.setText(intent.getStringExtra("itemName"))
        binding.tvItemDesc.setText(intent.getStringExtra("itemDesc"))
        binding.tvItemPrice.setText(intent.getStringExtra("itemPrice"))
    }
    private var name = ""
    private var desc = ""
    private var price = ""

    private fun validateData(){
        name = binding.tvItemName.text.toString().trim()
        desc = binding.tvItemDesc.text.toString().trim()
        price = binding.tvItemPrice.text.toString().trim()


        if (name.isEmpty()){
            Toast.makeText(this, "Enter your name...", Toast.LENGTH_SHORT).show()
        }

        else if(desc.isEmpty()){
            Toast.makeText(this, "Enter Password...", Toast.LENGTH_SHORT).show()
        }
        else if(price.isEmpty()){
            Toast.makeText(this, "Confirm Password...", Toast.LENGTH_SHORT).show()
        }else{
            updateItem()
        }

    }
    private fun updateItem(){
        //show progress
        progressDialog.setMessage("Updating item info")
        progressDialog.show()


        val hashMap = HashMap<String,Any>()
        hashMap["itemName"] = "$name"
        hashMap["itemDesc"] = "$desc"
        hashMap["itemPrice"] = "$price"

        // start updating
        val ref = FirebaseDatabase.getInstance().getReference("Items")
        ref.child(id).updateChildren(hashMap)
            .addOnSuccessListener {
                progressDialog.dismiss()
                Toast.makeText(this, "Update successfully", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener{e ->
                progressDialog.dismiss()
                Toast.makeText(this, "Update failed due to ${e.message}", Toast.LENGTH_SHORT).show()
            }
        startActivity(Intent(this, DashboardSupplierActivity::class.java))
        finish()
    }
}