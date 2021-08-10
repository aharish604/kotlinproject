package com.uniimarket.app.Forgotpassword.view

import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.view.Window
import android.view.WindowManager
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.textfield.TextInputEditText
import com.uniimarket.app.Forgotpassword.presenter.ForgotPasswordPresenter
import com.uniimarket.app.R

class ForgotEmailConfirmActivity : AppCompatActivity(), ForgotPasswordPresenter.ForgotPasswordListener {

    var btn_submit: Button? = null
    var et_unii_mail: TextInputEditText? = null
    var forgotPasswordPresenter: ForgotPasswordPresenter? = null
    var progressDialog: ProgressDialog? = null
    var imv_cancel: ImageView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        this.window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)
        setContentView(R.layout.activity_forgot_email_confirm)

        initialVariables()

        imv_cancel?.setOnClickListener {
            finish()
        }

        btn_submit?.setOnClickListener {
            when {
                et_unii_mail?.text.toString().trim().isEmpty() -> {
//                    et_unii_mail?.error = "Please enter Email"
                    Toast.makeText(this, "Please enter email", Toast.LENGTH_LONG).show()
                }

                else -> {
                    progressDialog?.show()
                    forgotPasswordPresenter?.forgotEmail(et_unii_mail?.text.toString().trim())

                }
            }
        }
    }

    private fun initialVariables() {
        progressDialog = ProgressDialog(this)
        progressDialog?.setMessage("Loading....")
        btn_submit = findViewById(R.id.btn_submit)
        et_unii_mail = findViewById(R.id.et_unii_mail)
        imv_cancel = findViewById(R.id.imv_cancel)
        forgotPasswordPresenter = ForgotPasswordPresenter(this, this@ForgotEmailConfirmActivity)

    }

    override fun forgotMailResponseSuccess(status: String, message: String?) {

        val builder1 = AlertDialog.Builder(this)
        builder1.setTitle("Email Confirmation")
        builder1.setMessage("We've sent a code to your registered mail id")
        builder1.setCancelable(true)

        builder1.setPositiveButton(
            "OK"
        ) { dialog, id ->

            var intent: Intent = Intent(this@ForgotEmailConfirmActivity, ForgotPasswordActivity::class.java)
            intent.putExtra("email", et_unii_mail?.text.toString())
            startActivity(intent)
            finish()
            try {
                progressDialog?.dismiss()
            }catch (e: Exception){
                e.printStackTrace()
            }
            dialog.cancel()
        }

//        builder1.setNegativeButton(
//            "No"
//        ) { dialog, id ->
//            progressDialog?.dismiss()
//            dialog.cancel()
//        }

        val alert11 = builder1.create()
        alert11.show()
        alert11.getButton(AlertDialog.BUTTON_POSITIVE).isAllCaps = false
        alert11.getButton(AlertDialog.BUTTON_NEGATIVE).isAllCaps = false

    }

    override fun forgotMailResponseFailure(status: String, message: String?) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
        try {
            progressDialog?.dismiss()
        }catch (e: Exception){
            e.printStackTrace()
        }
    }

}
