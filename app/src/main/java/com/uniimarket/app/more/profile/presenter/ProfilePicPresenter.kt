package com.uniimarket.app.more.profile.presenter

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import com.google.gson.Gson
import com.uniimarket.app.more.profile.model.ProfilePicData
import com.uniimarket.app.network.ApiClient
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File

class ProfilePicPresenter(
    var context: Context?,
    var fullProfilePicListener: FullProfilePicListener
) {
    val sharedPref: SharedPreferences = context?.getSharedPreferences("uniimarket", Context.MODE_PRIVATE)!!

    fun updateProfilePic(file: File) {

        var id: String = sharedPref?.getString("id", null)

        val apiService = ApiClient.create()

        val uid = RequestBody.create(MediaType.parse("text/plain"), id)
        var fileToUpload: MultipartBody.Part? = null
        try {
//            val file = File(filepath)
            val mFile = RequestBody.create(MediaType.parse("image/*"), file)
            fileToUpload = MultipartBody.Part.createFormData("profilepic", file?.name, mFile)
        } catch (e: Exception) {
            e.printStackTrace()
        }

        val call = apiService.updateProfilePic(uid, fileToUpload)

        call.enqueue(object : Callback<ProfilePicData> {

            override fun onResponse(call: Call<ProfilePicData>, response: Response<ProfilePicData>) {
                try {
                    Log.e("sell response ", Gson().toJson(response.body()) + "")

                    if (response.body()!!.getStatus().toString().contains("true")) {
                        Log.e("sell mess ", response.body()!!.getMessage())

                        fullProfilePicListener.profilePicSuccess("true",
                            response.body()!!.getMessage().toString(),
                            response.body()!!.getRecords())
//                        response.body()!!.getMessage()?.let { sellListener.sellResponse("true", it) }

                    } else {
                        fullProfilePicListener.profilePicFailure("true",
                            response.body()!!.getMessage().toString())
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                    fullProfilePicListener.profilePicFailure("true",
                        response.body()!!.getMessage().toString())
                }

            }

            override fun onFailure(call: Call<ProfilePicData>, t: Throwable) {
                Log.e("TAG", t.toString())
//                sellListener.sellResponse(
//                    "false", "failure"
//                )
                fullProfilePicListener.profilePicFailure("false",
                   "failure")
            }
        })

    }


    interface FullProfilePicListener {
        fun profilePicSuccess(status: String, message: String, profileData: ProfilePicData.Records?)
        fun profilePicFailure(status: String, message: String)
    }

}