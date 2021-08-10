package com.uniimarket.app.more.contact.presenter

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import com.uniimarket.app.more.contact.model.ContactUsData
import com.uniimarket.app.network.ApiClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ContactUsPresenter(var contactUsListener: ContactUsListener, var context: Context?) {

    val sharedPref: SharedPreferences = context?.getSharedPreferences("uniimarket", Context.MODE_PRIVATE)!!

    fun contactUs(userName: String, uniiMail: String, university: String, course: String, query: String) {

        var uId: String = sharedPref?.getString("id", null)
        val apiService = ApiClient.create()

        val call =
            apiService.getContactUs(userName, uniiMail, university, course, query, uId)

        call?.enqueue(object : Callback<ContactUsData> {
            override fun onResponse(call: Call<ContactUsData>, response: Response<ContactUsData>) {
                try {
                    Log.e("contact status ", response.body()!!.getStatus().toString())

                    if (response.body()!!.getStatus().toString().contains("true")) {
                        contactUsListener.contactUsListenerResponse("true", response.body()!!.getMessage())
                    } else {
                        contactUsListener.contactUsListenerResponse("false", response.body()!!.getMessage())
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                    contactUsListener.contactUsListenerResponse(
                        "false",
                        "failure"
                    )
                }

            }

            override fun onFailure(call: Call<ContactUsData>, t: Throwable) {
                Log.e("failure ", t.toString())
            }
        })

    }


    interface ContactUsListener {
        fun contactUsListenerResponse(
            status: String,
            message: String?
        )
    }
}