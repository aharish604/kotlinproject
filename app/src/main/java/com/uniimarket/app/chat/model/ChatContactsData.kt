package com.uniimarket.app.chat.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable


class ChatContactsData {

    @SerializedName("status")
    @Expose
    private var status: Boolean? = null
    @SerializedName("data")
    @Expose
    private var data: List<Datum>? = null

    fun getStatus(): Boolean? {
        return status
    }

    fun setStatus(status: Boolean?) {
        this.status = status
    }

    fun getData(): List<Datum>? {
        return data
    }

    fun setData(data: List<Datum>) {
        this.data = data
    }

    inner class Datum : Serializable {

        @SerializedName("id")
        @Expose
        var id: String? = null
        @SerializedName("firstname")
        @Expose
        var firstname: String? = null
        @SerializedName("profilepic")
        @Expose
        var profilepic: String? = null
        @SerializedName("email")
        @Expose
        var email: String? = null

    }

}