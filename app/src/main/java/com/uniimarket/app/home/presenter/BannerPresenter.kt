package com.uniimarket.app.home.presenter

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import com.uniimarket.app.home.model.ProductsData
import com.uniimarket.app.network.ApiClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class BannerPresenter (var bannerProductsListener: BannerProductsListener, var context: Context?) {

    val sharedPref: SharedPreferences = context?.getSharedPreferences("uniimarket", Context.MODE_PRIVATE)!!

    fun getSliderBanner() {
        val apiService = ApiClient.create()

        val sharedPref: SharedPreferences =
            context?.getSharedPreferences("uniimarket", Context.MODE_PRIVATE)!!
        var id: String = sharedPref?.getString("id", null)
        Log.e("uid", id)
//        Call<EstimatedProjectsData> call = apiService.getEstimatedProjectList(sharedPreferences.getString("uid", null));
        val call = apiService.getSlideBanners(id)

        call.enqueue(object : Callback<ProductsData> {
            override fun onResponse(call: Call<ProductsData>, response: Response<ProductsData>) {
                try {
                    Log.e("cancel project ", response.body()!!.getStatus().toString())

                    if (response.body()!!.getStatus().toString().contains("true")) {
                        val productDataList = ArrayList(response.body()!!.getData())
                        Log.e("product message", response.body()!!.getMessage())
                        if (productDataList.size > 0) {
                            Log.e("product name", productDataList!![0]?.banner_photo)
                            bannerProductsListener.productResponseSuccess(
                                "true",
                                response.body()!!.getMessage(),
                                productDataList
                            )
                        } else {
                            bannerProductsListener.productResponseFailure(
                                "true",
                                response.body()!!.getMessage()
                            )
                        }
                    } else {
                        bannerProductsListener.productResponseFailure(
                            "true",
                            response.body()!!.getMessage()
                        )
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                    bannerProductsListener.productResponseFailure(
                        "false",
                        "failure"
                    )
                }

            }

            override fun onFailure(call: Call<ProductsData>, t: Throwable) {
                Log.e("TAG", t.toString())
                //                    Toast.makeText(getActivity(), t.toString(), Toast.LENGTH_SHORT).show();
            }
        })

    }


    interface BannerProductsListener {

        fun productResponseFailure(status: String, message: String?)
        fun productResponseSuccess(
            s: String,
            message: String?,
            productDataList: java.util.ArrayList<ProductsData.Datum?>
        )
    }
}