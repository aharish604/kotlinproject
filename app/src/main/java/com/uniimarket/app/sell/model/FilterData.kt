package com.uniimarket.app.sell.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class FilterData {

    @SerializedName("status")
    @Expose
    private var status: Boolean? = null
    @SerializedName("message")
    @Expose
    private var message: String? = null
    @SerializedName("data")
    @Expose
    private var data: List<Datum>? = null

    fun getStatus(): Boolean? {
        return status
    }

    fun setStatus(status: Boolean?) {
        this.status = status
    }

    fun getMessage(): String? {
        return message
    }

    fun setMessage(message: String) {
        this.message = message
    }

    fun getData(): List<Datum>? {
        return data
    }

    fun setData(data: List<Datum>) {
        this.data = data
    }

    inner class Datum {

        @SerializedName("id")
        @Expose
        var id: String? = null
        @SerializedName("item_name")
        @Expose
        var itemName: String? = null
        @SerializedName("scid")
        @Expose
        var scid: String? = null
        @SerializedName("status")
        @Expose
        var status: String? = null

        var filterChecked: Boolean = false

    }
}