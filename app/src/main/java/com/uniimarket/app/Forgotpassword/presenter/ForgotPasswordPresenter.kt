package com.uniimarket.app.Forgotpassword.presenter

import android.util.Log
import com.uniimarket.app.Forgotpassword.model.ForgotEmailData
import com.uniimarket.app.Forgotpassword.view.ForgotEmailConfirmActivity
import com.uniimarket.app.network.ApiClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ForgotPasswordPresenter(var forgotPasswordListener:ForgotPasswordListener, var forgotEmailConfirmActivity: ForgotEmailConfirmActivity) {

    fun forgotEmail(mail: String) {

        val apiService = ApiClient.create()

//        Call<EstimatedProjectsData> call = apiService.getEstimatedProjectList(sharedPreferences.getString("uid", null));
        val call = apiService.getForgotMailResponse(mail)

        call.enqueue(object : Callback<ForgotEmailData> {
            override fun onResponse(call: Call<ForgotEmailData>, response: Response<ForgotEmailData>) {
                try {
                    Log.e("cancel project ", response.body()!!.getStatus().toString())

                    if (response.body()!!.getStatus().toString().contains("true")) {
                        Log.e("cancel project ", response.body()!!.getMessage())

                            forgotPasswordListener.forgotMailResponseSuccess(
                                "true",
                                response.body()!!.getMessage())
                    } else {
                        forgotPasswordListener.forgotMailResponseFailure("false", response.body()!!.getMessage())
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                    forgotPasswordListener.forgotMailResponseFailure(
                        "false",
                        "failure"
                    )
                }

            }

            override fun onFailure(call: Call<ForgotEmailData>, t: Throwable) {
                Log.e("TAG", t.toString())
                //                    Toast.makeText(getActivity(), t.toString(), Toast.LENGTH_SHORT).show();
            }
        })
    }


    interface ForgotPasswordListener {
        fun forgotMailResponseSuccess(status: String, message: String?)
        fun forgotMailResponseFailure(status: String, message: String?)

    }
}