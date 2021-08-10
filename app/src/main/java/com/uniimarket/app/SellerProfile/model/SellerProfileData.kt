package com.uniimarket.app.SellerProfile.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName



class SellerProfileData {

    @SerializedName("status")
    @Expose
    private var status: Boolean? = null
    @SerializedName("message")
    @Expose
    private var message: String? = null
    @SerializedName("records")
    @Expose
    private var records: Records? = null
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

    fun getRecords(): Records? {
        return records
    }

    fun setRecords(records: Records) {
        this.records = records
    }

    fun getData(): List<Datum>? {
        return data
    }

    fun setData(data: List<Datum>) {
        this.data = data
    }

    inner class Records {

        @SerializedName("id")
        @Expose
        internal var id: String? = null
        @SerializedName("firstname")
        @Expose
        internal var firstname: String? = null
        @SerializedName("lastname")
        @Expose
        internal var lastname: String? = null
        @SerializedName("age")
        @Expose
        internal var age: String? = null
        @SerializedName("email")
        @Expose
        internal var email: String? = null
        @SerializedName("unii_email")
        @Expose
        internal var uniiEmail: String? = null
        @SerializedName("password")
        @Expose
        internal var password: String? = null
        @SerializedName("profilepic")
        @Expose
        internal var profilepic: String? = null
        @SerializedName("bio")
        @Expose
        internal var bio: String? = null
        @SerializedName("university")
        @Expose
        internal var university: String? = null
        @SerializedName("university_year")
        @Expose
        internal var universityYear: String? = null
        @SerializedName("course")
        @Expose
        internal var course: String? = null
        @SerializedName("phone")
        @Expose
        internal var phone: String? = null
        @SerializedName("adress")
        @Expose
        internal var adress: String? = null
        @SerializedName("uotp")
        @Expose
        internal var uotp: String? = null
        @SerializedName("device_type")
        @Expose
        internal var deviceType: String? = null
        @SerializedName("device_token")
        @Expose
        internal var deviceToken: String? = null
        @SerializedName("device_id")
        @Expose
        internal var deviceId: String? = null
        @SerializedName("created_at")
        @Expose
        internal var createdAt: String? = null
        @SerializedName("status")
        @Expose
        internal var status: String? = null

        fun getId(): String? {
            return id
        }

        fun setId(id: String?) {
            this.id = id
        }

        fun getFirstname(): String? {
            return firstname
        }

        fun setFirstname(firstname: String?) {
            this.firstname = firstname
        }

        fun getLastname(): String? {
            return lastname
        }

        fun setLastname(lastname: String?) {
            this.lastname = lastname
        }

        fun getAge(): String? {
            return age
        }

        fun setAge(age: String?) {
            this.age = age
        }

        fun getEmail(): String? {
            return email
        }

        fun setEmail(email: String?) {
            this.email = email
        }

        fun getUniiEmail(): String? {
            return uniiEmail
        }

        fun setUniiEmail(uniiEmail: String?) {
            this.uniiEmail = uniiEmail
        }

        fun getPassword(): String? {
            return password
        }

        fun setPassword(password: String?) {
            this.password = password
        }

        fun getProfilepic(): String? {
            return profilepic
        }

        fun setProfilepic(profilepic: String?) {
            this.profilepic = profilepic
        }

        fun getBio(): String? {
            return bio
        }

        fun setBio(bio: String?) {
            this.bio = bio
        }

        fun getUniversity(): String? {
            return university
        }

        fun setUniversity(university: String?) {
            this.university = university
        }

        fun getUniversityYear(): String? {
            return universityYear
        }

        fun setUniversityYear(universityYear: String?) {
            this.universityYear = universityYear
        }

        fun getCourse(): String? {
            return course
        }

        fun setCourse(course: String?) {
            this.course = course
        }

        fun getPhone(): String? {
            return phone
        }

        fun setPhone(phone: String?) {
            this.phone = phone
        }

        fun getAdress(): String? {
            return adress
        }

        fun setAdress(adress: String?) {
            this.adress = adress
        }

        fun getUotp(): String? {
            return uotp
        }

        fun setUotp(uotp: String?) {
            this.uotp = uotp
        }

        fun getDeviceType(): String? {
            return deviceType
        }

        fun setDeviceType(deviceType: String?) {
            this.deviceType = deviceType
        }

        fun getDeviceToken(): String? {
            return deviceToken
        }

        fun setDeviceToken(deviceToken: String?) {
            this.deviceToken = deviceToken
        }

        fun getDeviceId(): String? {
            return deviceId
        }

        fun setDeviceId(deviceId: String?) {
            this.deviceId = deviceId
        }

        fun getCreatedAt(): String? {
            return createdAt
        }

        fun setCreatedAt(createdAt: String?) {
            this.createdAt = createdAt
        }

        fun getStatus(): String? {
            return status
        }

        fun setStatus(status: String?) {
            this.status = status
        }

    }


    class Datum {
        @SerializedName("id")
        @Expose
        var id: String? = null
        @SerializedName("uid")
        @Expose
        var uid: String? = null
        @SerializedName("name")
        @Expose
        var name: String? = null
        @SerializedName("uniemail")
        @Expose
        var uniemail: String? = null
        @SerializedName("date")
        @Expose
        var date: String? = null
        @SerializedName("latitude")
        @Expose
        var latitude: String? = null
        @SerializedName("longitude")
        @Expose
        var longitude: String? = null
        @SerializedName("location")
        @Expose
        var location: String? = null
        @SerializedName("product_name")
        @Expose
        var productName: String? = null
        @SerializedName("product_type")
        @Expose
        var productType: String? = null
        @SerializedName("cid")
        @Expose
        var cid: String? = null
        @SerializedName("scid")
        @Expose
        var scid: String? = null
        @SerializedName("price")
        @Expose
        var price: String? = null
        @SerializedName("setprice")
        @Expose
        var setprice: String? = null
        @SerializedName("product_pics")
        @Expose
        var productPics: String? = null
        @SerializedName("product_status")
        @Expose
        var productStatus: String? = null
        @SerializedName("purchase_from")
        @Expose
        var purchaseFrom: Any? = null
        @SerializedName("description")
        @Expose
        var description: String? = null
        @SerializedName("created_at")
        @Expose
        var createdAt: String? = null

    }
}