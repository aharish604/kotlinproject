package com.uniimarket.app.more.profile.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class ProfilePicData {
    @SerializedName("status")
    @Expose
    private var status: Boolean? = null
    @SerializedName("records")
    @Expose
    private var records: Records? = null
    @SerializedName("message")
    @Expose
    private var message: String? = null

    fun getStatus(): Boolean? {
        return status
    }

    fun setStatus(status: Boolean?) {
        this.status = status
    }

    fun getRecords(): Records? {
        return records
    }

    fun setRecords(records: Records) {
        this.records = records
    }

    fun getMessage(): String? {
        return message
    }

    fun setMessage(message: String) {
        this.message = message
    }

    inner class Records {

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
        var deviceType: Any? = null
        @SerializedName("device_token")
        @Expose
        var deviceToken: Any? = null
        @SerializedName("device_id")
        @Expose
        var deviceId: Any? = null
        @SerializedName("created_at")
        @Expose
        var createdAt: String? = null

    }
}