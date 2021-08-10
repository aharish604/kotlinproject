package com.uniimarket.app.categories.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName



class AddFavouritesData {

    @SerializedName("status")
    @Expose
    private var status: Boolean? = null
    @SerializedName("message")
    @Expose
    private var message: String? = null
    @SerializedName("data")
    @Expose
    private var data: String? = null

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

    fun getData(): String? {
        return data
    }

    fun setData(data: String) {
        this.data = data
    }
}