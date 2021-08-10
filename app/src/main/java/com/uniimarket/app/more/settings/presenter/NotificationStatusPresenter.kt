package com.uniimarket.app.more.settings.presenter

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import com.google.gson.Gson
import com.uniimarket.app.more.settings.model.SettingsNotificationStatus
import com.uniimarket.app.more.settings.model.SubscribeCategoryData
import com.uniimarket.app.network.ApiClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class NotificationStatusPresenter(var notificationStatusListener: NotificationStatusListener, var context: Context?) {

    val sharedPref: SharedPreferences = context?.getSharedPreferences("uniimarket", Context.MODE_PRIVATE)!!
    val apiService = ApiClient.create()


    fun getNotificationStatus() {

        var uid: String = sharedPref?.getString("id", null)

        val call = apiService.getSettingsNotificationStatus(uid)

        call.enqueue(object : Callback<SettingsNotificationStatus> {

            override fun onResponse(call: Call<SettingsNotificationStatus>, response: Response<SettingsNotificationStatus>) {
                try {
                    Log.e("sell response ", Gson().toJson(response.body()) + "")

                    if (response.body()!!.getStatus().toString().contains("true")) {
                        Log.e("sell mess ", response.body()!!.getMessage())

                        notificationStatusListener.notificationSuccessResponse("true",response.message(),
                            response.body()!!
                        )


                    } else {
                        notificationStatusListener.notificationFailureResponse("false",response.message())
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                    notificationStatusListener.notificationFailureResponse("false",response.message())
                }

            }

            override fun onFailure(call: Call<SettingsNotificationStatus>, t: Throwable) {
                Log.e("TAG", t.toString())
                notificationStatusListener.notificationFailureResponse("false","Something went wrong, Please try again")
            }
        })

    }

    fun updateSettingsNotifications(showNotification: String, appNotification: String) {

        var uid: String = sharedPref?.getString("id", null)

        val call = apiService.updateSettingsNotificationStatus(uid, showNotification, appNotification)

        call.enqueue(object : Callback<SettingsNotificationStatus> {

            override fun onResponse(call: Call<SettingsNotificationStatus>, response: Response<SettingsNotificationStatus>) {
                try {
                    Log.e("sell response ", Gson().toJson(response.body()) + "")

                    if (response.body()!!.getStatus().toString().contains("true")) {
                        Log.e("sell mess ", response.body()!!.getMessage())

                        notificationStatusListener.notificationSuccessResponse("true",response.message(),
                            response.body()!!
                        )


                    } else {
                        notificationStatusListener.notificationFailureResponse("false",response.message())
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                    notificationStatusListener.notificationFailureResponse("false",response.message())
                }

            }

            override fun onFailure(call: Call<SettingsNotificationStatus>, t: Throwable) {
                Log.e("TAG", t.toString())
                notificationStatusListener.notificationFailureResponse("false","Something went wrong, Please try again")
            }
        })

    }

    fun getSubscribedNotifications() {

        var uid: String = sharedPref?.getString("id", null)

        val call = apiService.getSubscribedNotifications(uid)

        call.enqueue(object : Callback<SubscribeCategoryData> {

            override fun onResponse(call: Call<SubscribeCategoryData>, response: Response<SubscribeCategoryData>) {
                try {
                    Log.e("sell response ", Gson().toJson(response.body()) + "")

//                    if (response.body()!!.getStatus().toString().contains("true")) {
//                        Log.e("sell mess ", response.body()!!.getMessage())
//
//
//
//                        val notificationDataList = ArrayList(response.body()!!.getData())
//                        Log.e("fav mess", response.body()!!.getMessage())
//                        if (notificationDataList.size > 0) {
//                            notificationStatusListener.subscribeSuccessResponse(
//                                "true",
//                                response.body()!!.getMessage().toString(),
//                                notificationDataList
//                            )
//                        } else {
//                            response.body()!!.getMessage()?.let {
//                                notificationStatusListener.subscribeFailureResponse("true",
//                                    it
//                                )
//                            }
//                        }
//
//
//
//                    } else {
//                        notificationStatusListener.subscribeFailureResponse("false",response.message())
//                    }



                    if (response.body()!!.getStatus().toString().contains("true")) {
                        val notificationDataList = ArrayList(response.body()!!.getData())
                        Log.e("noti mess", response.body()!!.getMessage())
                        if (notificationDataList.size > 0) {
                            Log.e("noti name", notificationDataList[0]?.categorieName)
                            notificationStatusListener.subscribeSuccessResponse(
                                "true",
                                response.body()!!.getMessage(),
                                notificationDataList
                            )
                        } else {
                            notificationStatusListener.subscribeFailureResponse("false", response.body()!!.getMessage())
                        }
                    } else {
                        notificationStatusListener.subscribeFailureResponse("false", response.body()!!.getMessage())
                    }

                } catch (e: Exception) {
                    e.printStackTrace()
                    notificationStatusListener.subscribeFailureResponse("false",response.message())
                }

            }

            override fun onFailure(call: Call<SubscribeCategoryData>, t: Throwable) {
                Log.e("TAG", t.toString())
                notificationStatusListener.subscribeFailureResponse("false","Something went wrong, Please try again")
            }
        })

    }


    interface NotificationStatusListener {
        fun notificationSuccessResponse(
            status: String,
            message: String,
            body: SettingsNotificationStatus
        )


        fun notificationFailureResponse(
            status: String,
            message: String
        )

        fun subscribeSuccessResponse(
            s: String,
            message: String?,
            notificationDataList: java.util.ArrayList<SubscribeCategoryData.Datum?>
        )

        fun subscribeFailureResponse(s: String, message: String?)


    }
}