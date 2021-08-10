package com.uniimarket.app.categories.presenter

import android.content.Context
import android.util.Log
import com.uniimarket.app.categories.model.SubCategoriesData
import com.uniimarket.app.home.model.ProductsData
import com.uniimarket.app.network.ApiClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SubCategoryPresenter(var categoriesListener: SubCategoriesListener, var context: Context?) {

    fun getSubCategoryList(id: String?) {

        val apiService = ApiClient.create()

//        Call<EstimatedProjectsData> call = apiService.getEstimatedProjectList(sharedPreferences.getString("uid", null));
        val call = apiService.getSubCategories(id.toString())

        call?.enqueue(object : Callback<SubCategoriesData> {
            override fun onResponse(call: Call<SubCategoriesData>, response: Response<SubCategoriesData>) {
                try {
                    Log.e("cancel project ", response.body()!!.getStatus().toString())

                    if (response.body()!!.getStatus().toString().contains("true")) {
                        val categoriesList = ArrayList(response.body()!!.getData())
                        Log.e("sub cate mess", response.body()!!.getMessage())
                        if (categoriesList.size > 0) {
                            Log.e("sub cate name", categoriesList[0].name)
                            categoriesListener.subcategoriesResponseSuccess(
                                "true",
                                response.body()!!.getMessage(),
                                categoriesList
                            )
                        } else {
                            categoriesListener.subcategoriesResponseFailure("true", response.body()!!.getMessage())
                        }
                    } else {
                        categoriesListener.subcategoriesResponseFailure("true", response.body()!!.getMessage())
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                    categoriesListener.subcategoriesResponseFailure(
                        "false",
                        "failure"
                    )
                }

            }

            override fun onFailure(call: Call<SubCategoriesData>, t: Throwable) {
                Log.e("TAG", t.toString())
                //                    Toast.makeText(getActivity(), t.toString(), Toast.LENGTH_SHORT).show();
            }
        })

    }

    fun getSubTypeCategoryList(subCategoryId: String) {


        val apiService = ApiClient.create()
//
////        Call<EstimatedProjectsData> call = apiService.getEstimatedProjectList(sharedPreferences.getString("uid", null));
//        val call = apiService.getSubCategories(subCategoryId.toString())
//
//        call?.enqueue(object : Callback<SubCategoriesData> {
//            override fun onResponse(call: Call<SubCategoriesData>, response: Response<SubCategoriesData>) {
//                try {
//                    Log.e("cancel project ", response.body()!!.getStatus().toString())
//
//                    if (response.body()!!.getStatus().toString().contains("true")) {
//                        val categoriesList = ArrayList(response.body()!!.getData())
//                        Log.e("sub cate mess", response.body()!!.getMessage())
//                        if (categoriesList.size > 0) {
//                            Log.e("sub cate name", categoriesList[0].name)
//                            categoriesListener.subcategoriesResponseSuccess(
//                                "true",
//                                response.body()!!.getMessage(),
//                                categoriesList
//                            )
//                        } else {
//                            categoriesListener.subcategoriesResponseFailure("true", response.body()!!.getMessage())
//                        }
//                    } else {
//                        categoriesListener.subcategoriesResponseFailure("true", response.body()!!.getMessage())
//                    }
//                } catch (e: Exception) {
//                    e.printStackTrace()
//                    categoriesListener.subcategoriesResponseFailure(
//                        "false",
//                        "failure"
//                    )
//                }
//
//            }
//
//            override fun onFailure(call: Call<SubCategoriesData>, t: Throwable) {
//                Log.e("TAG", t.toString())
//                //                    Toast.makeText(getActivity(), t.toString(), Toast.LENGTH_SHORT).show();
//            }
//        })

    }


    interface SubCategoriesListener {
        fun subcategoriesResponseSuccess(
            status: String,
            message: String?,
            subCategoryDataList: java.util.ArrayList<SubCategoriesData.Datum>
        )

        fun subcategoriesResponseFailure(status: String, message: String?)
        fun productResponseSuccess(
            status: String,
            message: String?,
            productDataList: java.util.ArrayList<ProductsData.Datum>
        )

        fun productResponseFailure(status: String, message: String?)
    }
}