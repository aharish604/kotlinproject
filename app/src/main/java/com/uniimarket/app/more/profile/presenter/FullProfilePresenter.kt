package com.uniimarket.app.more.profile.presenter

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import com.google.gson.Gson
import com.uniimarket.app.more.profile.model.FullProfileData
import com.uniimarket.app.network.ApiClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FullProfilePresenter(
    var context: Context?,
    var fullProfileListener: FullProfileListener
) {

    val sharedPref: SharedPreferences =
        context?.getSharedPreferences("uniimarket", Context.MODE_PRIVATE)!!

    fun editProfile(
        firstName: String,
        lastName: String,
        age: String,
        university: String,
        university_year: String,
        course: String,
        unii_email: String,
        phone: String,
        address: String,
        bio: String
    ) {

        var id: String = sharedPref?.getString("id", null)

        val apiService = ApiClient.create()

        val call = apiService.editProfile(
            firstName,
            lastName,
            age,
            university,
            course,
            phone,
            address,
            id,
            university_year,
            unii_email,
            bio
        )
        Log.e("uid", id)

        call.enqueue(object : Callback<FullProfileData> {
            override fun onResponse(
                call: Call<FullProfileData>,
                response: Response<FullProfileData>
            ) {
                Log.e("update profile res", Gson().toJson(response.body()))
                try {
                    Log.e("update profile status", response.body()!!.getStatus().toString())

                    if (response.body()!!.getStatus().toString().contains("true")) {
                        Log.e("cancel project ", response.body()!!.getMessage())

                        val profileData = response.body()!!.getRecords()

                        response.body()!!.getMessage()?.let {
                            profileData?.let { it1 ->
                                fullProfileListener.profileResponseSuccess(
                                    "true",
                                    it,
                                    it1
                                )
                            }
                        }

//                        val profileList = ArrayList(response.body()!!.getRecords())
//                        if (profileList.size > 0) {
//                            Log.e("first name", profileList[0].firstname+"-------")
//                            fullProfileListener.profileResponseSuccess(
//                                "true",
//                                response.body()!!.message,
//                                profileList
//                            )
//                        } else {
//                            fullProfileListener.profileResponseFailure("true", response.body()!!.message)
//                        }
                    } else {
                        response.body()!!.getMessage()
                            ?.let { fullProfileListener.profileResponseFailure("true", it) }
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                    fullProfileListener.profileResponseFailure(
                        "false",
                        "failure"
                    )
                }

            }

            override fun onFailure(call: Call<FullProfileData>, t: Throwable) {
                Log.e("TAG", t.toString())
                fullProfileListener.profileResponseFailure(
                    "false",
                    "failure"
                )
            }
        })

    }


    interface FullProfileListener {
        fun profileResponseSuccess(
            status: String,
            message: String,
            profileList: FullProfileData.Records
        )

        fun profileResponseFailure(status: String, message: String)
    }

}