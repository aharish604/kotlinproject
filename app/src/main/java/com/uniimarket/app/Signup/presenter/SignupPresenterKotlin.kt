package com.uniimarket.app.Signup.presenter

import android.util.Log
import com.uniimarket.app.Helper
import com.uniimarket.app.Signup.model.SignupData
import com.uniimarket.app.Signup.view.SignUpActivity
import com.uniimarket.app.network.ApiClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SignupPresenterKotlin(var signupListener: SignupListener, var signUpActivity: SignUpActivity) {

    //    constructor(signUpActivity: SignUpActivity, signUpActivity1: SignUpActivity) : this()

    fun createAccount(firstName: String, lastName: String, uniiMail: String, password: String) {

        val apiService = ApiClient.create()

//        Call<EstimatedProjectsData> call = apiService.getEstimatedProjectList(sharedPreferences.getString("uid", null));
        val call = apiService.getSignUpResponse(firstName, lastName, uniiMail, password, "Android", Helper.fbToken)

        call.enqueue(object : Callback<SignupData> {
            override fun onResponse(call: Call<SignupData>, response: Response<SignupData>) {
                try {
                    Log.e("cancel project ", response.body()!!.getStatus().toString())

                    if (response.body()!!.getStatus().toString().contains("true")) {
                        Log.e("cancel project ", response.body()!!.getMessage())
                        signupListener.signUpResponse(
                            response.body()!!.getStatus().toString(),
                            response.body()!!.getMessage().toString()
                        )
                    } else {
                        signupListener.signUpResponse(
                            response.body()!!.getStatus().toString(),
                            response.body()!!.getMessage().toString()
                        )
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                    signupListener.signUpResponse(
                        "false",
                        "failure"
                    )
                }

            }

            override fun onFailure(call: Call<SignupData>, t: Throwable) {
                Log.e("TAG", t.toString())
                //                    Toast.makeText(getActivity(), t.toString(), Toast.LENGTH_SHORT).show();
            }
        })
    }


    interface SignupListener {
        fun signUpResponse(status: String, message: String)
    }
}
