package com.uniimarket.app.chat.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName



class ChatViewData {

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
        @SerializedName("from_id")
        @Expose
        var fromId: String? = null
        @SerializedName("to_id")
        @Expose
        var toId: String? = null
        @SerializedName("description")
        @Expose
        var description: String? = null
        @SerializedName("create_at")
        @Expose
        var createAt: String? = null
        @SerializedName("image")
        @Expose
        var image: String? = null

    }


}