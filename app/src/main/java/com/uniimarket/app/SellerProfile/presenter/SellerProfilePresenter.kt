package com.uniimarket.app.SellerProfile.presenter

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import com.google.gson.Gson
import com.uniimarket.app.Helper
import com.uniimarket.app.SellerProfile.model.NotificationRequestList
import com.uniimarket.app.SellerProfile.model.SellerNotificationStatusData
import com.uniimarket.app.SellerProfile.model.SellerProfileData
import com.uniimarket.app.SellerProfile.model.SellerUpdateNotificationsData
import com.uniimarket.app.network.ApiClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SellerProfilePresenter(
    var sellerProfileListener: SellerProfileListener,
    var context: Context?
) {

    val sharedPref: SharedPreferences =
        context?.getSharedPreferences("uniimarket", Context.MODE_PRIVATE)!!

    fun getSellerProfileDetails(uid: String?) {

        val apiService = ApiClient.create()

//        Call<EstimatedProjectsData> call = apiService.getEstimatedProjectList(sharedPreferences.getString("uid", null));
//        var uid: String
//        uid = try {
//            Helper.productData.uid.toString()
//        } catch (e: Exception) {
//               Helper.productData.uid.toString()
//        }
        Log.e("uid", uid)

        val call = apiService.getSellerProfile(uid)

        call?.enqueue(object : Callback<SellerProfileData> {
            override fun onResponse(
                call: Call<SellerProfileData>,
                response: Response<SellerProfileData>
            ) {
                try {
                    Log.e("seller profile det", Gson().toJson(response.body()))

                    if (response.body()?.getStatus()!!) {

                        response.body()?.getStatus()?.let {
                            sellerProfileListener.sellProfileResponseSuccess(
                                it,
                                response.body()?.getMessage(), response.body()!!.getRecords(),
                                response.body()!!.getData() as ArrayList<SellerProfileData.Datum>?
                            )
                        }
                    } else {
                        response.body()?.getStatus()?.let {
                            sellerProfileListener.sellProfileResponseFailure(
                                it,
                                response.body()?.getMessage()
                            )
                        }
                    }


                } catch (e: Exception) {
                    e.printStackTrace()

                    response.body()?.getStatus()?.let {
                        sellerProfileListener.sellProfileResponseFailure(
                            false,
                            response.body()?.getMessage()
                        )
                    }
                }

            }

            override fun onFailure(call: Call<SellerProfileData>, t: Throwable) {
                Log.e("TAG", t.toString())
                //                    Toast.makeText(getActivity(), t.toString(), Toast.LENGTH_SHORT).show();
            }
        })

    }

    fun getNotificationStatus(uid: String) {
        Log.e("se pr", uid + "")
        val apiService = ApiClient.create()

//        Call<EstimatedProjectsData> call = apiService.getEstimatedProjectList(sharedPreferences.getString("uid", null));
//        var uid: String
//        uid = try {
//            Helper.productData.uid.toString()
//        } catch (e: Exception) {
//            Helper.productData.uid.toString()
//        }
        var id: String = sharedPref?.getString("id", null)

//        val call = apiService.getNotificationStatus(id, uid, "things")
        val call = apiService.getNotificationStatus(id, uid, "user")

        call?.enqueue(object : Callback<SellerNotificationStatusData> {
            override fun onResponse(
                call: Call<SellerNotificationStatusData>,
                response: Response<SellerNotificationStatusData>
            ) {
                try {
                    Log.e("seller profile", Gson().toJson(response.body()))

                    if (response.body()?.getStatus()!!) {

                        response.body()?.getStatus()?.let {
                            sellerProfileListener.sellNotificationResponseSuccess(
                                it, response.body()?.getData()
                            )
                        }
                    } else {
                        response.body()?.getStatus()?.let {
                            sellerProfileListener.sellNotificationResponseFailure(
                                it,
                                "0"
                            )
                        }
                    }

                } catch (e: Exception) {
                    e.printStackTrace()
                    response.body()?.getStatus()?.let {
                        sellerProfileListener.sellProfileResponseFailure(
                            false,
                            "0"
                        )
                    }
                }
            }

            override fun onFailure(call: Call<SellerNotificationStatusData>, t: Throwable) {
                Log.e("TAG", t.toString())
                //                    Toast.makeText(getActivity(), t.toString(), Toast.LENGTH_SHORT).show();
            }
        })

    }

    fun updateNotificationStatus(uid: String) {

        val apiService = ApiClient.create()

//        Call<EstimatedProjectsData> call = apiService.getEstimatedProjectList(sharedPreferences.getString("uid", null));
//        var uid: String
//        uid = try {
//            Helper.productData.uid.toString()
//        } catch (e: Exception) {
//            Helper.productData.uid.toString()
//        }

        var cid: String
        cid = try {
            Helper.productData.cid.toString()
        } catch (e: Exception) {
            "0"
        }

        var scid: String
        scid = try {
            Helper.productData.scid.toString()
        } catch (e: Exception) {
            "0"
        }

        var id: String = sharedPref?.getString("id", null)

        val call = apiService.getUpdateNotificationStatusUser(
            id,
            uid,
            "0",
            "0",
            "user"
        )

        Log.e("id $id", "uid $uid")

        call?.enqueue(object : Callback<SellerUpdateNotificationsData> {
            override fun onResponse(
                call: Call<SellerUpdateNotificationsData>,
                response: Response<SellerUpdateNotificationsData>
            ) {
                try {
                    Log.e("seller update profile", Gson().toJson(response.body()))

                    if (response.body()?.getStatus()!!) {

                        response.body()?.getStatus()?.let {
                            sellerProfileListener.sellUpdateNotificationResponse(
                                it,
                                response.body()?.getMessage(),
                                response.body()?.getData()
                            )
                        }
                    } else {
                        response.body()?.getStatus()?.let {
                            sellerProfileListener.sellNotificationResponseFailure(
                                it,
                                response.body()?.getMessage()
                            )
                        }
                    }

                } catch (e: Exception) {
                    e.printStackTrace()

                    response.body()?.getStatus()?.let {
                        sellerProfileListener.sellProfileResponseFailure(
                            false,
                            response.body()?.getMessage()
                        )
                    }
                }
            }

            override fun onFailure(call: Call<SellerUpdateNotificationsData>, t: Throwable) {
                Log.e("TAG", t.toString())
                //                    Toast.makeText(getActivity(), t.toString(), Toast.LENGTH_SHORT).show();
            }
        })
    }

    fun notificationRequestList(uid: String?) {

        Log.e("uid", " " + uid)
        val apiService = ApiClient.create()

//        Call<EstimatedProjectsData> call = apiService.getEstimatedProjectList(sharedPreferences.getString("uid", null));
//        var uid: String
//        uid = try {
//            Helper.productData.uid.toString()
//        } catch (e: Exception) {
//            Helper.productFavouriteData.uid.toString()
//        }


        var id: String = sharedPref?.getString("id", null)

        val call = apiService.sendRequestList(id, uid.toString())

        call?.enqueue(object : Callback<NotificationRequestList> {
            override fun onResponse(
                call: Call<NotificationRequestList>, response: Response<NotificationRequestList>
            ) {
                try {
                    Log.e("seller notif req ", Gson().toJson(response.body()))

                    if (response.body()?.getStatus()!!) {

                        response.body()?.getStatus()?.let {
                            sellerProfileListener.notificationRequestResponse(
                                it,
                                response.body()?.getMessage(),
                                response.body()?.getData()
                            )
                        }
                    } else {
                        response.body()?.getStatus()?.let {
                            sellerProfileListener.notificationRequestResponseFailure(
                                it,
                                response.body()?.getMessage()
                            )
                        }
                    }

                } catch (e: Exception) {
                    e.printStackTrace()

                    response.body()?.getStatus()?.let {
                        sellerProfileListener.sellProfileResponseFailure(
                            false,
                            response.body()?.getMessage()
                        )
                    }
                }

            }

            override fun onFailure(call: Call<NotificationRequestList>, t: Throwable) {
                Log.e("TAG", t.toString())
                //                    Toast.makeText(getActivity(), t.toString(), Toast.LENGTH_SHORT).show();
            }
        })

    }


    interface SellerProfileListener {
        fun sellProfileResponseSuccess(
            status: Boolean,
            message: String?,
            subCategoryDataList: SellerProfileData.Records?,
            productList: ArrayList<SellerProfileData.Datum>?
        )

        fun sellProfileResponseFailure(status: Boolean, message: String?)
        fun sellNotificationResponseFailure(status: Boolean, message: String?)
        fun sellNotificationResponseSuccess(status: Boolean, data: String?)
        fun sellUpdateNotificationResponse(status: Boolean, message: String?, data: String?)
        fun notificationRequestResponse(status: Boolean, message: String?, data: String?)
        fun notificationRequestResponseFailure(status: Boolean, message: String?)
    }
}