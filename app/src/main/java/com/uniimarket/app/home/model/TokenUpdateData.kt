package com.uniimarket.app.home.model

import com.google.gson.annotations.Expose

import com.google.gson.annotations.SerializedName


class TokenUpdateData {

    @SerializedName("status")
    @Expose
    private var status: Boolean? = null
    @SerializedName("message")
    @Expose
    private var message: String? = null
    @SerializedName("data")
    @Expose
    private var data: Data? = null

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

    fun getData(): Data? {
        return data
    }

    fun setData(data: Data?) {
        this.data = data
    }

    class Data {
        @SerializedName("device_type")
        @Expose
        var deviceType: String? = null
        @SerializedName("device_token")
        @Expose
        var deviceToken: String? = null
        @SerializedName("device_id")
        @Expose
        var deviceId: String? = null

    }
}