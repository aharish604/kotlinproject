package com.uniimarket.app.more.posts.presenter

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import com.google.gson.Gson
import com.uniimarket.app.more.posts.model.PurchaseData
import com.uniimarket.app.network.ApiClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PurchasePresenter (var myPurchaseListener: PurchaseListener, var context: Context?) {

    val sharedPref: SharedPreferences = context?.getSharedPreferences("uniimarket", Context.MODE_PRIVATE)!!

    fun getPurchaseList() {

        var id: String = sharedPref?.getString("id", null)
        val apiService = ApiClient.create()

        val call = apiService.getPurchaseData(id, "sale")

        call?.enqueue(object : Callback<PurchaseData> {
            override fun onResponse(call: Call<PurchaseData>, response: Response<PurchaseData>) {
                try {
                    Log.e("purchase status", response.body()!!.getStatus().toString())

                    if (response.body()!!.getStatus().toString().contains("true")) {
                        val notificationDataList = ArrayList(response.body()!!.getData())
                        Log.e("fav mess", response.body()!!.getMessage())
                        if (notificationDataList.size > 0) {
                            myPurchaseListener.purchaseResponseSuccess(
                                "true",
                                response.body()!!.getMessage(),
                                notificationDataList
                            )
                        } else {
                            myPurchaseListener.purchasseResponseFailure("true", response.body()!!.getMessage())
                        }
                    } else {
                        myPurchaseListener.purchasseResponseFailure("true", response.body()!!.getMessage())
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                    myPurchaseListener.purchasseResponseFailure(
                        "false",
                        "failure"
                    )
                }
            }

            override fun onFailure(call: Call<PurchaseData>, t: Throwable) {
                Log.e("TAG", t.toString())
            }
        })
    }

    fun getSoldList() {

        var id: String = sharedPref?.getString("id", null)
        val apiService = ApiClient.create()

        val call = apiService.getSoldData(id,"sold")

        call?.enqueue(object : Callback<PurchaseData> {
            override fun onResponse(call: Call<PurchaseData>, response: Response<PurchaseData>) {
                try {
                    Log.e("purchase status", response.body()!!.getStatus().toString())

                    if (response.body()!!.getStatus().toString().contains("true")) {
                        val notificationDataList = ArrayList(response.body()!!.getData())
                        Log.e("fav mess", response.body()!!.getMessage())
                        if (notificationDataList.size > 0) {
                            myPurchaseListener.purchaseResponseSuccess(
                                "true",
                                response.body()!!.getMessage(),
                                notificationDataList
                            )
                        } else {
                            myPurchaseListener.purchasseResponseFailure("true", response.body()!!.getMessage())
                        }
                    } else {
                        myPurchaseListener.purchasseResponseFailure("true", response.body()!!.getMessage())
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                    myPurchaseListener.purchasseResponseFailure(
                        "false",
                        "failure"
                    )
                }
            }

            override fun onFailure(call: Call<PurchaseData>, t: Throwable) {
                Log.e("TAG", t.toString())
            }
        })
    }

    fun getPostedList() {
        var id: String = sharedPref?.getString("id", null)
        val apiService = ApiClient.create()

        val call = apiService.getPostedData(id,"Active")

        call?.enqueue(object : Callback<PurchaseData> {
            override fun onResponse(call: Call<PurchaseData>, response: Response<PurchaseData>) {
                try {
                    Log.e("posted status", Gson().toJson(response.body()))

                    if (response.body()!!.getStatus().toString().contains("true")) {
                        val notificationDataList = ArrayList(response.body()!!.getData())
                        Log.e("fav mess", response.body()!!.getMessage())
                        if (notificationDataList.size > 0) {
                            myPurchaseListener.purchaseResponseSuccess(
                                "true",
                                response.body()!!.getMessage(),
                                notificationDataList
                            )
                        } else {
                            myPurchaseListener.purchasseResponseFailure("true", response.body()!!.getMessage())
                        }
                    } else {
                        myPurchaseListener.purchasseResponseFailure("false", response.body()!!.getMessage())
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                    myPurchaseListener.purchasseResponseFailure(
                        "false",
                        "failure"
                    )
                }

            }

            override fun onFailure(call: Call<PurchaseData>, t: Throwable) {
                Log.e("TAG", t.toString())
            }
        })

    }


    interface PurchaseListener {
        fun purchaseResponseSuccess(
            status: String,
            message: String?,
            notificationDataList: java.util.ArrayList<PurchaseData.Datum>
        )

        fun purchasseResponseFailure(status: String, message: String?)
    }
}