package com.uniimarket.app.categories.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class Datum {

    @SerializedName("id")
    @Expose
    var id: String? = null
    @SerializedName("uid")
    @Expose
    var uid: String? = null
    @SerializedName("name")
    @Expose
    var name: String? = null
    @SerializedName("uniemail")
    @Expose
    var uniemail: String? = null
    @SerializedName("date")
    @Expose
    var date: String? = null
    @SerializedName("latitude")
    @Expose
    var latitude: String? = null
    @SerializedName("longitude")
    @Expose
    var longitude: String? = null
    @SerializedName("location")
    @Expose
    var location: String? = null
    @SerializedName("product_name")
    @Expose
    var productName: String? = null
    @SerializedName("cid")
    @Expose
    var cid: String? = null
    @SerializedName("scid")
    @Expose
    var scid: String? = null
    @SerializedName("gender")
    @Expose
    var gender: String? = null
    @SerializedName("price")
    @Expose
    var price: String? = null
    @SerializedName("setprice")
    @Expose
    var setprice: String? = null
    @SerializedName("product_pics")
    @Expose
    var productPics: String? = null
    @SerializedName("product_status")
    @Expose
    var productStatus: String? = null
    @SerializedName("purchase_from")
    @Expose
    var purchaseFrom: Any? = null
    @SerializedName("description")
    @Expose
    var description: String? = null
    @SerializedName("created_at")
    @Expose
    var createdAt: String? = null
}