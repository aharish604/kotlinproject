package com.uniimarket.app.more.posts.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName



class ProductDeleteData {

    @SerializedName("status")
    @Expose
    private var status: Boolean? = null
    @SerializedName("message")
    @Expose
    private var message: String? = null
    @SerializedName("data")
    @Expose
    private var data: Int? = null

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

    fun getData(): Int? {
        return data
    }

    fun setData(data: Int?) {
        this.data = data
    }
}