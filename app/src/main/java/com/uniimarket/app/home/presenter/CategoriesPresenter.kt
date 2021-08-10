package com.uniimarket.app.home.presenter

import android.content.Context
import android.util.Log
import com.uniimarket.app.Helper
import com.uniimarket.app.home.model.CategoriesData
import com.uniimarket.app.home.model.TokenUpdateData
import com.uniimarket.app.network.ApiClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CategoriesPresenter(var categoriesListener: CategoriesListener, var context: Context?) {


    fun updateToken(
        androidId: String,
        uid: String?,
        email: String?,
        token: String
    ) {

        val apiService = ApiClient.create()

        Log.e("deviceid", androidId);
        Log.e("uid", uid);
        Log.e("em", email);
        Log.e("token", Helper.fbToken);


//        Call<TokenUpdateData> call = apiService.updateFBToken(sharedPreferences.getString("uid", null));
        val call = apiService.updateFBToken(uid, "Android", Helper.fbToken, androidId, email)

        call.enqueue(object : Callback<TokenUpdateData> {
            override fun onResponse(
                call: Call<TokenUpdateData>,
                response: Response<TokenUpdateData>
            ) {
                try {
                    Log.e("categories list ", response.body()?.getData().toString())

                    if (response.body()!!.getStatus().toString().contains("true")) {

                        categoriesListener.tokenSuccess(
                            "true",
                            response.body()!!.getMessage()
                        )
                    } else {
                        categoriesListener.tokenSuccess(
                            "false",
                            response.body()!!.getMessage()
                        )
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                    categoriesListener.tokenSuccess(
                        "false",
                        "failure"
                    )
                }

            }

            override fun onFailure(call: Call<TokenUpdateData>, t: Throwable) {
                Log.e("TAG", t.toString())
                //                    Toast.makeText(getActivity(), t.toString(), Toast.LENGTH_SHORT).show();
            }
        })

    }

    fun getCategories() {

        val apiService = ApiClient.create()

//        Call<EstimatedProjectsData> call = apiService.getEstimatedProjectList(sharedPreferences.getString("uid", null));
        val call = apiService.getCategories()

        call.enqueue(object : Callback<CategoriesData> {
            override fun onResponse(
                call: Call<CategoriesData>,
                response: Response<CategoriesData>
            ) {
                try {
                    Log.e("categories list ", response.body().toString())

                    if (response.body()!!.getStatus().toString().contains("true")) {
                        val categoriesList = ArrayList(response.body()!!.getData())
                        Log.e("cate mess", response.body()!!.getMessage())
                        if (categoriesList.size > 0) {
                            Log.e("cat name", categoriesList[0].name)
                            categoriesListener.categoriesResponseSuccess(
                                "true",
                                response.body()!!.getMessage(),
                                categoriesList
                            )
                        } else {
                            categoriesListener.categoriesResponseFailure(
                                "true",
                                response.body()!!.getMessage()
                            )
                        }
                    } else {
                        categoriesListener.categoriesResponseFailure(
                            "true",
                            response.body()!!.getMessage()
                        )
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                    categoriesListener.categoriesResponseFailure(
                        "false",
                        "failure"
                    )
                }

            }

            override fun onFailure(call: Call<CategoriesData>, t: Throwable) {
                Log.e("TAG", t.toString())
                //                    Toast.makeText(getActivity(), t.toString(), Toast.LENGTH_SHORT).show();
            }
        })

    }

    fun getSliderPrducts() {
//        val apiService = ApiClient.create()
//
////        Call<EstimatedProjectsData> call = apiService.getEstimatedProjectList(sharedPreferences.getString("uid", null));
//        val call = apiService.getSlideProducts()
//
//        call.enqueue(object : Callback<ProductsData> {
//            override fun onResponse(call: Call<ProductsData>, response: Response<ProductsData>) {
//                try {
//                    Log.e("cancel project ", response.body()!!.getStatus().toString())
//
//                    if (response.body()!!.getStatus().toString().contains("true")) {
//                        val productDataList = ArrayList(response.body()!!.getData())
//                        Log.e("product message", response.body()!!.getMessage())
//                        if (productDataList.size > 0) {
//                            Log.e("product name", productDataList!![0].productPics)
//                            categoriesListener.productResponseSuccess(
//                                "true",
//                                response.body()!!.getMessage(),
//                                productDataList
//                            )
//                        } else {
//                            categoriesListener.categoriesResponseFailure("true", response.body()!!.getMessage())
//                        }
//                    } else {
//                        categoriesListener.productResponseFailure("true", response.body()!!.getMessage())
//                    }
//                } catch (e: Exception) {
//                    e.printStackTrace()
//                    categoriesListener.productResponseFailure(
//                        "false",
//                        "failure"
//                    )
//                }
//
//            }
//
//            override fun onFailure(call: Call<ProductsData>, t: Throwable) {
//                Log.e("TAG", t.toString())
//                //                    Toast.makeText(getActivity(), t.toString(), Toast.LENGTH_SHORT).show();
//            }
//        })

    }

//    fun getSliderBanner() {
//        val apiService = ApiClient.create()
//
//        val sharedPref: SharedPreferences =
//            context?.getSharedPreferences("uniimarket", Context.MODE_PRIVATE)!!
//        var id: String = sharedPref?.getString("id", null)
//        Log.e("uid", id)
////        Call<EstimatedProjectsData> call = apiService.getEstimatedProjectList(sharedPreferences.getString("uid", null));
//        val call = apiService.getSlideBanners(id)
//
//        call.enqueue(object : Callback<ProductsData> {
//            override fun onResponse(call: Call<ProductsData>, response: Response<ProductsData>) {
//                try {
//                    Log.e("cancel project ", response.body()!!.getStatus().toString())
//
//                    if (response.body()!!.getStatus().toString().contains("true")) {
//                        val productDataList = ArrayList(response.body()!!.getData())
//                        Log.e("product message", response.body()!!.getMessage())
//                        if (productDataList.size > 0) {
//                            Log.e("product name", productDataList!![0]?.productPics)
//                            categoriesListener.productResponseSuccess(
//                                "true",
//                                response.body()!!.getMessage(),
//                                productDataList
//                            )
//                        } else {
//                            categoriesListener.productResponseFailure(
//                                "true",
//                                response.body()!!.getMessage()
//                            )
//                        }
//                    } else {
//                        categoriesListener.productResponseFailure(
//                            "true",
//                            response.body()!!.getMessage()
//                        )
//                    }
//                } catch (e: Exception) {
//                    e.printStackTrace()
//                    categoriesListener.productResponseFailure(
//                        "false",
//                        "failure"
//                    )
//                }
//
//            }
//
//            override fun onFailure(call: Call<ProductsData>, t: Throwable) {
//                Log.e("TAG", t.toString())
//                //                    Toast.makeText(getActivity(), t.toString(), Toast.LENGTH_SHORT).show();
//            }
//        })
//
//    }


    interface CategoriesListener {
        fun categoriesResponseSuccess(
            status: String,
            message: String?,
            signInList: java.util.ArrayList<CategoriesData.Datum>
        )

        fun categoriesResponseFailure(status: String, message: String?)
        fun tokenSuccess(s: String, message: String?)
//        fun productResponseSuccess(
//            status: String,
//            message: String?,
//            productDataList: java.util.ArrayList<ProductsData.Datum>
//        )

//        fun productResponseFailure(status: String, message: String?)
//        fun productResponseSuccess(
//            s: String,
//            message: String?,
//            productDataList: java.util.ArrayList<ProductsData.Datum?>
//        )
    }
}