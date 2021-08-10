package com.uniimarket.app.chat.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName



class ChatSendData {


    @SerializedName("status")
    @Expose
    private var status: Boolean? = null
    @SerializedName("message")
    @Expose
    private var message: String? = null

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
}