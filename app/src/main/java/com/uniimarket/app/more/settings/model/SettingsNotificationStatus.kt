package com.uniimarket.app.more.settings.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName


class SettingsNotificationStatus {

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
        @SerializedName("id")
        @Expose
        var id: String? = null
        @SerializedName("all_notifications")
        @Expose
        var allNotifications: String? = null
        @SerializedName("app_notifications")
        @Expose
        var appNotifications: String? = null
        @SerializedName("uid")
        @Expose
        var uid: String? = null

    }
}