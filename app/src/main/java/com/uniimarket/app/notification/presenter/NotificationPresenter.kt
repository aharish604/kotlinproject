package com.uniimarket.app.notification.presenter

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import com.google.gson.Gson
import com.uniimarket.app.network.ApiClient
import com.uniimarket.app.notification.modal.NotificationData
import com.uniimarket.app.notification.modal.UpdateNotificationStatusData
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class NotificationPresenter(var notificationListener: NotificationListener, var context: Context) {

    val sharedPref: SharedPreferences = context?.getSharedPreferences("uniimarket", Context.MODE_PRIVATE)!!

    fun getNotificationList() {

        var id: String = sharedPref?.getString("id", null)
        val apiService = ApiClient.create()

//        Call<EstimatedProjectsData> call = apiService.getEstimatedProjectList(sharedPreferences.getString("uid", null));
        val call = apiService.getNotificationList(id)

        call?.enqueue(object : Callback<NotificationData> {
            override fun onResponse(call: Call<NotificationData>, response: Response<NotificationData>) {
                try {
                    Log.e("noti project ", Gson().toJson(response.body()))

                    if (response.body()!!.getStatus().toString().contains("true")) {
                        val notificationDataList = ArrayList(response.body()!!.getData())
                        Log.e("noti mess", response.body()!!.getMessage())
                        if (notificationDataList.size > 0) {
                            Log.e("noti name", notificationDataList[0].message)
                            notificationListener.notificationResponseSuccess(
                                "true",
                                response.body()!!.getMessage(),
                                notificationDataList
                            )
                        } else {
                            notificationListener.notificationResponseFailure("true", response.body()!!.getMessage())
                        }
                    } else {
                        notificationListener.notificationResponseFailure("true", response.body()!!.getMessage())
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                    notificationListener.notificationResponseFailure(
                        "false",
                        "failure"
                    )
                }

            }

            override fun onFailure(call: Call<NotificationData>, t: Throwable) {
                Log.e("TAG", t.toString())
            }
        })

    }

    fun updateNotificationStatus(item: String, senderId: String?, userId: String?) {

        var id: String = sharedPref?.getString("id", null)
        val apiService = ApiClient.create()

        Log.e("lg id", id+"--")
        Log.e("sen id", senderId+"--")

//        Call<EstimatedProjectsData> call = apiService.getEstimatedProjectList(sharedPreferences.getString("uid", null));
        val call = apiService.updateNotificationStatus(id, senderId, item)

        call?.enqueue(object : Callback<UpdateNotificationStatusData> {
            override fun onResponse(
                call: Call<UpdateNotificationStatusData>,
                response: Response<UpdateNotificationStatusData>
            ) {
                try {
                    Log.e("noti status ", Gson().toJson(response.body()))

                    if (response.body()!!.getStatus().toString().contains("true")) {

                        response.body()!!.getMessage()?.let {
                            notificationListener.notificationStatusResponse("true",
                                it
                            )
                        }
                    } else {
                        response.body()!!.getMessage()?.let {
                            notificationListener.notificationStatusResponse("true",
                                it
                            )
                        }
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                    notificationListener.notificationStatusResponse(
                        "false",
                        "failure"
                    )
                }
            }

            override fun onFailure(call: Call<UpdateNotificationStatusData>, t: Throwable) {
                Log.e("TAG", t.toString())
            }
        })

    }


    interface NotificationListener {
        fun notificationResponseSuccess(
            status: String,
            message: String?,
            notificationDataList: java.util.ArrayList<NotificationData.Datum>
        )

        fun notificationResponseFailure(status: String, message: String?)
        fun notificationStatusResponse(status: String, message: String)

    }

}