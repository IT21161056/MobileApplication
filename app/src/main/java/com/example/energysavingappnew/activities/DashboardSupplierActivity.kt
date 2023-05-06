package com.example.energysavingappnew.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import com.example.energysavingappnew.adapter.AdapterItem
import com.example.energysavingappnew.databinding.ActivityDashboardSupplierBinding
import com.example.energysavingappnew.model.ItemModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class DashboardSupplierActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDashboardSupplierBinding
    //firebase auth
    private lateinit var firebaseAuth: FirebaseAuth

    //arrayList to hold item
    private lateinit var itemArrayList: ArrayList<ItemModel>

    //adapter
    private lateinit var adapterItem: AdapterItem

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDashboardSupplierBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //init firebase Auth
        firebaseAuth = FirebaseAuth.getInstance()
        checkUser()
        loadItems()

        //search
        binding.searchEt.addTextChangedListener(object: TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(s: CharSequence?, p1: Int, p2: Int, p3: Int) {
                //called as and when user type anything
                try {
                    adapterItem.filter.filter(s)
                }
                catch (e: Exception){

                }
            }

            override fun afterTextChanged(s: Editable?) {

            }

        })

        //handle click, logout
        binding.logoutBtn.setOnClickListener{
            firebaseAuth.signOut()
            checkUser()
        }

        //handle click, start add category page
        binding.addItemBtn.setOnClickListener{
            startActivity(Intent(this, ItemAddActivity::class.java))
        }
    }

    private fun loadItems() {
        //init arrayList
        itemArrayList = ArrayList()

        //get all items from firebase data.. Firebase DB > Items
        val ref = FirebaseDatabase.getInstance().getReference("Items")
        ref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot){
                //clear list before starting adding data into it
                itemArrayList.clear()
                for (ds in snapshot.children){
                    //get data as model
                    var itemModel = ds.getValue(ItemModel::class.java)

                    //add to arraylist
                    itemArrayList.add(itemModel!!)
                }
                //setup adapter
                adapterItem = AdapterItem(this@DashboardSupplierActivity, itemArrayList)
                //set adapter to recyclerview
                binding.itemRv.adapter = adapterItem
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }

    private fun checkUser() {
        //get current user
        val firebaseUser = firebaseAuth.currentUser
        if (firebaseUser == null){
            //not logged in, goto main screen
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }
        else{
            //logged in, get and user info
            val email = firebaseUser.email
            //set to textview of toolbar
            binding.subTitleTv.text = email
        }

    }
}