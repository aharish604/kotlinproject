package com.uniimarket.app.chat.model

import com.google.gson.annotations.Expose

import com.google.gson.annotations.SerializedName




class UserInfoData {

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
        @SerializedName("firstname")
        @Expose
        var firstname: String? = null
        @SerializedName("lastname")
        @Expose
        var lastname: String? = null
        @SerializedName("age")
        @Expose
        var age: String? = null
        @SerializedName("email")
        @Expose
        var email: String? = null
        @SerializedName("unii_email")
        @Expose
        var uniiEmail: String? = null
        @SerializedName("password")
        @Expose
        var password: String? = null
        @SerializedName("profilepic")
        @Expose
        var profilepic: String? = null
        @SerializedName("university")
        @Expose
        var university: String? = null
        @SerializedName("university_year")
        @Expose
        var universityYear: String? = null
        @SerializedName("course")
        @Expose
        var course: String? = null
        @SerializedName("phone")
        @Expose
        var phone: String? = null
        @SerializedName("adress")
        @Expose
        var adress: String? = null
        @SerializedName("uotp")
        @Expose
        var uotp: String? = null
        @SerializedName("device_type")
        @Expose
        var deviceType: String? = null
        @SerializedName("device_token")
        @Expose
        var deviceToken: String? = null
        @SerializedName("device_id")
        @Expose
        var deviceId: Any? = null
        @SerializedName("created_at")
        @Expose
        var createdAt: String? = null

    }
}