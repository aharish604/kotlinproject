package com.uniimarket.app.notification.modal

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName


class NotificationData {


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
        @SerializedName("user_id")
        @Expose
        var userId: String? = null
        @SerializedName("device_token")
        @Expose
        var deviceToken: String? = null
        @SerializedName("sender_id")
        @Expose
        var senderId: String? = null
        @SerializedName("ntype")
        @Expose
        var ntype: String? = null
        @SerializedName("ndetails")
        @Expose
        var ndetails: String? = null
        @SerializedName("ntime")
        @Expose
        var ntime: String? = null
        @SerializedName("nstatus")
        @Expose
        var nstatus: String? = null
        @SerializedName("badgecount")
        @Expose
        var badgecount: String? = null
        @SerializedName("idref")
        @Expose
        var idref: Any? = null
        @SerializedName("message")
        @Expose
        var message: String? = null
        @SerializedName("created_at")
        @Expose
        var createdAt: String? = null
        @SerializedName("firstname")
        @Expose
        var firstname: String? = null
        @SerializedName("profilepic")
        @Expose
        var profilepic: String? = null

    }
}
