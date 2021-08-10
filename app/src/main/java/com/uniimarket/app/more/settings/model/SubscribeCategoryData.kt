package com.uniimarket.app.more.settings.model

import com.google.gson.annotations.Expose

import com.google.gson.annotations.SerializedName

class SubscribeCategoryData {

    @SerializedName("status")
    @Expose
    private var status: Boolean? = null
    @SerializedName("message")
    @Expose
    private var message: String? = null
    @SerializedName("data")
    @Expose
    private var data: List<Datum?>? = null

    fun getStatus(): Boolean? {
        return status
    }

    fun setStatus(status: Boolean?) {
        this.status = status
    }

    fun getMessage(): String? {
        return message
    }

    fun setMessage(message: String?) {
        this.message = message
    }

    fun getData(): List<Datum?>? {
        return data
    }

    fun setData(data: List<Datum?>?) {
        this.data = data
    }

    class Datum {
        @SerializedName("categorie_name")
        @Expose
        var categorieName: String? = null
        @SerializedName("categoriesid")
        @Expose
        var categoriesid: String? = null
        @SerializedName("sub_categorie_name")
        @Expose
        var subCategorieName: String? = null
        @SerializedName("subcategorieid")
        @Expose
        var subcategorieid: String? = null
        @SerializedName("status")
        @Expose
        var status: String? = null

    }

}