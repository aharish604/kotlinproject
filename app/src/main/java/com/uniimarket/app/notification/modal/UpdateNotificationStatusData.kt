package com.uniimarket.app.notification.modal

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName



class UpdateNotificationStatusData {

    @SerializedName("message")
    @Expose
    private var message: String? = null
    @SerializedName("status")
    @Expose
    private var status: Boolean? = null

    fun getMessage(): String? {
        return message
    }

    fun setMessage(message: String) {
        this.message = message
    }

    fun getStatus(): Boolean? {
        return status
    }

    fun setStatus(status: Boolean?) {
        this.status = status
    }
}