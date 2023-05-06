package com.example.energysavingappnew.model

class ItemModel {
    //validate, must match as in firebase
    var id:String = ""
    var itemName:String = ""
    var itemDesc:String = ""
    var itemPrice:String = ""
    var uid:String = ""
    var timestamp:Long = 0


    //empty constructor, required by firebase
    constructor()

    constructor(id: String, itemName: String, itemDesc: String,itemPrice: String, uid: String, timestamp:Long){
        this.id = id
        this.itemName = itemName
        this.itemDesc = itemDesc
        this.itemPrice = itemPrice
        this.uid = uid
        this.timestamp = timestamp
    }
}