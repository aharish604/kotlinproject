package com.uniimarket.app.more.posts.presenter

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import com.uniimarket.app.more.posts.model.ProductDeleteData
import com.uniimarket.app.network.ApiClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ProductDeletePresenter(var productDeleteListener: ProductDeleteListener, var context: Context?) {


    val sharedPref: SharedPreferences = context?.getSharedPreferences("uniimarket", Context.MODE_PRIVATE)!!

    fun deletePost(id: String?) {


        var uid: String = sharedPref?.getString("id", null)
        val apiService = ApiClient.create()

        val call = apiService.getDeleteProduct(id, uid)

        call?.enqueue(object : Callback<ProductDeleteData> {
            override fun onResponse(call: Call<ProductDeleteData>, response: Response<ProductDeleteData>) {
                try {
                    Log.e("purchase status", response.body()!!.getStatus().toString())

                    if (response.body()!!.getStatus().toString().contains("true")) {
//                        val notificationDataList = ArrayList(response.body()!!.getData())
                        Log.e("fav mess", response.body()!!.getMessage())
//                        if (notificationDataList.size > 0) {
//                            myPurchaseListener.purchaseResponseSuccess(
//                                "true",
//                                response.body()!!.getMessage(),
//                                notificationDataList
//                            )
//                        } else {
//                            myPurchaseListener.purchasseResponseFailure("true", response.body()!!.getMessage())
//                        }

                        productDeleteListener.productDeleteResponseSuccess(
                            "true",
                            response.body()!!.getMessage(),
                            response.body()!!.getData()
                        )
                    } else {
                        productDeleteListener.productDeleteResponseSuccess(
                            "true",
                            response.body()!!.getMessage(),
                            response.body()!!.getData()
                        )
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                    productDeleteListener.productDeleteResponseFailure(
                        "false",
                        "failure"
                    )
                }

            }

            override fun onFailure(call: Call<ProductDeleteData>, t: Throwable) {
                Log.e("TAG", t.toString())
            }
        })

    }

    fun itemSold(id: String?, sale: String, friendId: String) {


        var uid: String = sharedPref?.getString("id", null)
        val apiService = ApiClient.create()

        val call = apiService.productStatusUpdate(id, uid,sale, friendId)

        call?.enqueue(object : Callback<ProductDeleteData> {
            override fun onResponse(call: Call<ProductDeleteData>, response: Response<ProductDeleteData>) {
                try {
                    Log.e("purchase sold", response.body()!!.getStatus().toString())

                    if (response.body()!!.getStatus().toString().contains("true")) {
//                        val notificationDataList = ArrayList(response.body()!!.getData())
                        Log.e("fav mess", response.body()!!.getMessage())
//                        if (notificationDataList.size > 0) {
//                            myPurchaseListener.purchaseResponseSuccess(
//                                "true",
//                                response.body()!!.getMessage(),
//                                notificationDataList
//                            )
//                        } else {
//                            myPurchaseListener.purchasseResponseFailure("true", response.body()!!.getMessage())
//                        }

                        productDeleteListener.productSoldResponseSuccess(
                            "true",
                            response.body()!!.getMessage(),
                            response.body()!!.getData()
                        )
                    } else {
                        productDeleteListener.productSoldResponseSuccess(
                            "true",
                            response.body()!!.getMessage(),
                            response.body()!!.getData()
                        )
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                    productDeleteListener.productSoldResponseFailure(
                        "false",
                        "failure"
                    )
                }

            }

            override fun onFailure(call: Call<ProductDeleteData>, t: Throwable) {
                Log.e("TAG", t.toString())
            }
        })
    }

    interface ProductDeleteListener {
        fun productDeleteResponseSuccess(
            status: String,
            message: String?,
            data: Int?
        )

        fun productDeleteResponseFailure(status: String, message: String?)

        fun productSoldResponseSuccess(
            status: String,
            message: String?,
            data: Int?
        )

        fun productSoldResponseFailure(status: String, message: String?)
    }

}