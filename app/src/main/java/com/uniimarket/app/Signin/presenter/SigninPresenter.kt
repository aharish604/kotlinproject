package com.uniimarket.app.Signin.presenter

import android.util.Log
import com.uniimarket.app.Helper
import com.uniimarket.app.Signin.model.SignInData
import com.uniimarket.app.Signin.view.SignInActivity
import com.uniimarket.app.network.ApiClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SigninPresenter(var signinListener: SigninListener, var signinActivity: SignInActivity) {

    fun signIn(mail: String, password: String) {

        val apiService = ApiClient.create()

//        Call<EstimatedProjectsData> call = apiService.getEstimatedProjectList(sharedPreferences.getString("uid", null));
        val call = apiService.getSignInResponse(mail, password, "Android", Helper.fbToken)

        call.enqueue(object : Callback<SignInData> {
            override fun onResponse(call: Call<SignInData>, response: Response<SignInData>) {
                try {
                    Log.e("cancel project ", response.body()!!.getStatus().toString())

                    if (response.body()!!.getStatus().toString().contains("true")) {
                        Log.e("cancel project ", response.body()!!.getMessage())
//                        val signInList = ArrayList(response.body()!!.getData())
//                        if (signInList.size > 0) {
//                            Log.e("project name", signInList[0].firstname)
                            signinListener.signinResponseSuccess(
                                "true",
                                response.body()!!.getMessage(),
                                response.body()!!.getData()
                            )
//                        } else {
//                            signinListener.signinResponseFailure("true", response.body()!!.getMessage())
//                        }
                    } else {
                        signinListener.signinResponseFailure("true", response.body()!!.getMessage())
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                    signinListener.signinResponseFailure(
                        "false",
                        "failure"
                    )
                }

            }

            override fun onFailure(call: Call<SignInData>, t: Throwable) {
                Log.e("TAG", t.toString())
                //                    Toast.makeText(getActivity(), t.toString(), Toast.LENGTH_SHORT).show();
            }
        })
    }


    interface SigninListener {
        fun signinResponse(status: String, message: String)
        fun signinResponseSuccess(status: String, message: String?, signInList: SignInData.Data?)
        fun signinResponseFailure(status: String, message: String?)
    }
}