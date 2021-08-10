package com.uniimarket.app.home.presenter

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import com.google.gson.Gson
import com.uniimarket.app.Helper
import com.uniimarket.app.categories.model.CategoriesTypeData
import com.uniimarket.app.network.ApiClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class BannerProductsPresenter(var bannerProductsListener: BannerProductsListener, context: Context?) {

    val sharedPref: SharedPreferences = context?.getSharedPreferences("uniimarket", Context.MODE_PRIVATE)!!

    fun getBannerProductsList() {
        val apiService = ApiClient.create()

//        Log.e("cid " + Helper.sliderProductData.cid, "scid " + Helper.sliderProductData.scid)

        var uid: String = sharedPref?.getString("id", null)

//        Call<EstimatedProjectsData> call = apiService.getEstimatedProjectList(sharedPreferences.getString("uid", null));
        val call = apiService.getBannerProducts(Helper.cid, Helper.scid, uid)

        call.enqueue(object : Callback<CategoriesTypeData> {
            override fun onResponse(call: Call<CategoriesTypeData>, response: Response<CategoriesTypeData>) = try {
                Log.e("categories list ", Gson().toJson(response.body()))

                if (response.body()!!.getStatus().toString().contains("true")) {
                    val categoriesList = ArrayList(response.body()!!.getData())
                    Log.e("cate mess", response.body()!!.getMessage())
                    if (categoriesList.size > 0) {
                        Log.e("cat name", categoriesList[0].name)
                        bannerProductsListener.bannerResponseSuccess(
                            "true",
                            response.body()!!.getMessage(),
                            categoriesList
                        )
                    } else {
                        bannerProductsListener.bannerResponseFailure("true", response.body()!!.getMessage())
                    }
                } else {
                    bannerProductsListener.bannerResponseFailure("true", response.body()!!.getMessage())
                }
            } catch (e: Exception) {
                e.printStackTrace()
                bannerProductsListener.bannerResponseFailure(
                    "false",
                    "failure"
                )
            }

            override fun onFailure(call: Call<CategoriesTypeData>, t: Throwable) {
                Log.e("TAG", t.toString())
                bannerProductsListener.bannerResponseFailure(
                    "false",
                    "failure"
                )
                //                    Toast.makeText(getActivity(), t.toString(), Toast.LENGTH_SHORT).show();
            }
        })

    }


    interface BannerProductsListener {

        fun bannerResponseSuccess(
            status: String,
            message: String?,
            signInList: java.util.ArrayList<CategoriesTypeData.Datum>
        )

        fun bannerResponseFailure(status: String, message: String?)
    }
}