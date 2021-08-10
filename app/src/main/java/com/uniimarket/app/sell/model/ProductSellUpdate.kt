package com.uniimarket.app.sell.model

import com.google.gson.annotations.Expose

import com.google.gson.annotations.SerializedName




class ProductSellUpdate {

    @SerializedName("status")
    @Expose
    private var status: Boolean? = null
    @SerializedName("message")
    @Expose
    private var message: String? = null
    @SerializedName("data")
    @Expose
    private var data: Boolean? = null

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

    fun getData(): Boolean? {
        return data
    }

    fun setData(data: Boolean?) {
        this.data = data
    }

}