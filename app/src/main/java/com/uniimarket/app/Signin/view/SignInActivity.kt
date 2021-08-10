package com.uniimarket.app.Signin.view

import android.Manifest
import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.ProgressDialog
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.uniimarket.app.Dashboard.view.DashboardActivity
import com.uniimarket.app.Forgotpassword.view.ForgotEmailConfirmActivity
import com.uniimarket.app.R
import com.uniimarket.app.Signin.model.SignInData
import com.uniimarket.app.Signin.presenter.SigninPresenter
import com.uniimarket.app.Signup.view.SignUpActivity

class SignInActivity : AppCompatActivity(), SigninPresenter.SigninListener {


    var et_ti_mail: EditText? = null
    var et_ti_pswd: EditText? = null
    var btn_login: Button? = null
    var tv_forgot_password: TextView? = null
    var ll_signup: LinearLayout? = null
    var signinPresenter: SigninPresenter? = null
    private var PRIVATE_MODE = 0
    private val PREF_NAME = "uniimarket"
    var sharedPref: SharedPreferences? = null
    var MyVersion = Build.VERSION.SDK_INT
    var progressDialog: ProgressDialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        this.window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
        setContentView(R.layout.activity_sign_in)

        initialVariables()


        if (MyVersion > Build.VERSION_CODES.LOLLIPOP_MR1) {
            if (!checkIfAlreadyhavePermission()) {
                requestForSpecificPermission()
            }
        }


        (btn_login as View?)!!.setOnClickListener {

            when {
                et_ti_mail?.text?.trim()?.isEmpty()!! ->
//                    et_ti_mail?.error = "Please enter Email"
                    Toast.makeText(
                        this@SignInActivity,
                        "Please enter Uni Email",
                        Toast.LENGTH_LONG
                    ).show()
                et_ti_pswd?.text?.trim()?.isEmpty()!! ->
//                    et_ti_pswd?.error = "Please enter Password"

                    Toast.makeText(this@SignInActivity, "Enter password", Toast.LENGTH_LONG).show()
                else -> {
                    progressDialog?.setMessage("Verifying user ...")
                    progressDialog?.setCancelable(false)
                    progressDialog?.show()
                    signinPresenter?.signIn(
                        et_ti_mail?.text!!.trim().toString(),
                        et_ti_pswd?.text!!.trim().toString()
                    )

                }
            }
        }

        ll_signup?.setOnClickListener {
            startActivity(Intent(this@SignInActivity, SignUpActivity::class.java))
        }

        tv_forgot_password?.setOnClickListener {
            startActivity(Intent(this@SignInActivity, ForgotEmailConfirmActivity::class.java))
        }

    }

    private fun checkIfAlreadyhavePermission(): Boolean {
        val result =
            ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
        return result == PackageManager.PERMISSION_GRANTED
    }

    private fun requestForSpecificPermission() {
        ActivityCompat.requestPermissions(
            this,
            arrayOf(
                Manifest.permission.GET_ACCOUNTS,
                Manifest.permission.RECEIVE_SMS,
                Manifest.permission.READ_SMS,
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_NETWORK_STATE,
                Manifest.permission.CHANGE_NETWORK_STATE,
                Manifest.permission.CAMERA
            ),
            101
        )
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        when (requestCode) {
            101 -> if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //granted
            } else {
                //not granted
            }
            else -> super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        }
    }

    @SuppressLint("CommitPrefEdits")
    private fun initialVariables() {
        et_ti_mail = findViewById<EditText>(R.id.et_ti_mail)
        et_ti_pswd = findViewById<EditText>(R.id.et_ti_pswd)
        btn_login = findViewById<Button>(R.id.btn_login)
        tv_forgot_password = findViewById<TextView>(R.id.tv_forgot_password)
        ll_signup = findViewById<LinearLayout>(R.id.ll_signup)
        progressDialog = ProgressDialog(this)
        sharedPref = getSharedPreferences("uniimarket", 0)

        signinPresenter = SigninPresenter(this, this@SignInActivity)

    }

    override fun signinResponse(status: String, message: String) {
        startActivity(Intent(this@SignInActivity, DashboardActivity::class.java))
        finish()

    }

    override fun signinResponseSuccess(
        status: String,
        message: String?,
        signInList: SignInData.Data?
    ) {
        progressDialog?.dismiss()
        Log.e("profile pic", signInList?.profilepic)
        with(sharedPref!!.edit()) {
            putString("first", "true")
            putString("id", signInList?.id)
            putString("firstname", signInList?.firstname)
            putString("lastname", signInList?.lastname)
            putString("age", signInList?.age)
            putString("email", signInList?.email)
            putString("password", signInList?.password)
            putString("profilepic", signInList?.profilepic)
            putString("university", signInList?.university)
            putString("university_year", signInList?.universityYear)
            putString("course", signInList?.course)
            putString("phone", signInList?.phone)
            putString("adress", signInList?.adress as String?)
            putString("uotp", signInList?.uotp)
            putString("device_type", signInList?.deviceType as String?)
            putString("device_token", signInList?.deviceToken as String?)
            putString("device_id", signInList?.deviceId as String?)
            putString("created_at", signInList?.createdAt)
            putString("bio", signInList?.bio)

            commit()

            startActivity(Intent(this@SignInActivity, DashboardActivity::class.java))
            finish()
        }

    }

    override fun signinResponseFailure(status: String, message: String?) {
        progressDialog?.dismiss()
        val builder = AlertDialog.Builder(this@SignInActivity)
        builder.setTitle("Sign In")
        builder.setMessage(message)
            .setPositiveButton("Ok") { dialog, id -> }
        //Creating dialog box
        val alert = builder.create()
        alert.show()
        alert.getButton(androidx.appcompat.app.AlertDialog.BUTTON_POSITIVE).isAllCaps = false
    }
}
