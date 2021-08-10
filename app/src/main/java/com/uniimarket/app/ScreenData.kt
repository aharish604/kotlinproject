package com.uniimarket.app

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName



class ScreenData {
    @SerializedName("status")
    @Expose
    private var status: Boolean? = null
    @SerializedName("message")
    @Expose
    private var message: String? = null
    @SerializedName("data")
    @Expose
    private var data: List<ScreenDatum>? = null

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

    fun getData(): List<ScreenDatum>? {
        return data
    }

    fun setData(data: List<ScreenDatum>) {
        this.data = data
    }
}