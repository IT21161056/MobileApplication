package com.example.energysavingappnew.activities

import android.app.ProgressDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.energysavingappnew.databinding.ActivityItemAddBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class ItemAddActivity : AppCompatActivity() {
    //view binding
    private lateinit var binding: ActivityItemAddBinding

    //firebase auth
    private lateinit var firebaseAuth: FirebaseAuth

    //progress dialog
    private lateinit var progressDialog: ProgressDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityItemAddBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //init firebase auth
        firebaseAuth = FirebaseAuth.getInstance()

        //configure progress dialog
        progressDialog = ProgressDialog(this)
        progressDialog.setTitle("Please wait...")
        progressDialog.setCanceledOnTouchOutside(false)

        //handle click, go back
        binding.backBtn.setOnClickListener {
            onBackPressed()
        }

        //handle click, begin upload item
        binding.submitBtn.setOnClickListener{
            validateData()
        }
    }

    private var itemName = ""
    private var itemDesc = ""
    private var itemPrice = ""

    private fun validateData() {
        //validate data

        //get data
        itemName = binding.etItemName.text.toString().trim()
        itemDesc = binding.etItemDesc.text.toString().trim()

        //validate data
        if (itemName.isEmpty()){
            Toast.makeText(this, "Enter Item", Toast.LENGTH_SHORT).show()
        }
        else{
            addItemFirebase()
        }

    }

    private fun addItemFirebase() {
        //show progress
        progressDialog.show()

        //get timestap
        val timestamp = System.currentTimeMillis()

        //setup data to add in firebase db
        val hashMap = HashMap<String,Any>() //second param is Any, because the value could be of any type
        hashMap["id"] = "$timestamp"  //put in string quotes because timestamp is in double, we need in string for id
        hashMap["itemName"] = itemName
        hashMap["itemDesc"] = itemDesc
        hashMap["itemPrice"] = itemPrice
        hashMap["timestamp"] = timestamp
        hashMap["uid"] = "${firebaseAuth.uid}"

        //add to firebase db: Database root > Items > itemId > item info
        val ref = FirebaseDatabase.getInstance().getReference("Items")
        ref.child("$timestamp")
            .setValue(hashMap)
            .addOnSuccessListener {
                //added successfully
                progressDialog.dismiss()
                Toast.makeText(this,"Added Successfully...", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener{ e->
                //failed to add
                progressDialog.dismiss()
                Toast.makeText(this, "Failed to add due to ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }
}