package com.uniimarket.app.Signup.view

import android.R
import android.app.AlertDialog
import android.app.Dialog
import android.app.ProgressDialog
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.view.Window
import android.view.WindowManager
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.textfield.TextInputEditText
import com.uniimarket.app.Signup.presenter.SignupPresenterKotlin
import java.util.regex.Matcher
import java.util.regex.Pattern


class SignUpActivity : AppCompatActivity(), SignupPresenterKotlin.SignupListener {


    var btn_signup: Button? = null
    var imv_cancel: ImageView? = null
    var et_first_name: TextInputEditText? = null
    var et_last_name: TextInputEditText? = null
    var et_unii_mail: TextInputEditText? = null
    var et_pswd: TextInputEditText? = null
    var et_cnf_pswd: TextInputEditText? = null
    var cb_terms_conditions: CheckBox? = null
    var tv_terms_conditions: TextView? = null
    var signupPresenter: SignupPresenterKotlin? = null
    var progressDialog: ProgressDialog? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        this.window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
        setContentView(com.uniimarket.app.R.layout.activity_sign_up)

        initialVariables()


        signupPresenter = SignupPresenterKotlin(this, this@SignUpActivity)
        imv_cancel?.setOnClickListener {
            onBackPressed()
        }


        tv_terms_conditions?.setOnClickListener {

            termsAndConditions()

        }

        btn_signup?.setOnClickListener {

            var email: String = et_unii_mail?.text.toString().trim();

            val separated = email.split("@").toTypedArray()

            when {
                et_first_name?.text.toString().trim() == "" -> {
//                    et_first_name?.error = "Enter the First Name to Register"
                    Toast.makeText(this, "Enter the first name to register", Toast.LENGTH_LONG)
                        .show()
                }
                et_last_name?.text.toString().trim() == "" -> {
//                    et_last_name?.error = "Enter the Last Name to Register"
                    Toast.makeText(this, "Enter the last name to register", Toast.LENGTH_LONG)
                        .show()
                }
                et_unii_mail?.text.toString().trim() == "" -> {
//                    et_unii_mail?.error = "Enter the Uni Email to Register"
                    Toast.makeText(this, "Enter the Uni Email to Register", Toast.LENGTH_LONG)
                        .show()
                }


                et_pswd?.text.toString().trim() == "" -> {
//                    et_pswd?.error = "Enter Password to Register"
                    Toast.makeText(this, "Enter password to register", Toast.LENGTH_LONG).show()
                }

                et_pswd?.text.toString().trim().length < 8 || et_pswd?.text.toString().trim().length > 24
                -> {
//                    et_pswd?.error = "Password length should be minimum 8 and maximum 24"
                    Toast.makeText(
                        this,
                        "Password length should be minimum 8 and maximum 24",
                        Toast.LENGTH_LONG
                    ).show()
                }

                et_cnf_pswd?.text.toString().trim() == "" -> {
//                    et_cnf_pswd?.error = "Enter Confirm Password to Register"
                    Toast.makeText(this, "Enter confirm password to register", Toast.LENGTH_LONG)
                        .show()
                }
                et_pswd?.text.toString().trim() != et_cnf_pswd?.text.toString().trim() -> {
//                    et_cnf_pswd?.error =
//                        "Password and Confirm password should be same"
                    Toast.makeText(
                        this,
                        "Password and confirm password should be same",
                        Toast.LENGTH_LONG
                    ).show()
                }
                cb_terms_conditions?.isChecked != true ->
                    Toast.makeText(
                        this,
                        "Please accept the Terms and Conditions",
                        Toast.LENGTH_LONG
                    ).show()

//                if (checkEmail(et_unii_mail?.text.toString())) {
//                    if (separated[1] == "brighton.ac.uk") {
//                        Toast.makeText(
//                            this,
//                            "Please register with your valid university email id",
//                            Toast.LENGTH_LONG
//                        ).show()
//                    }
//                } else {
//                    Toast.makeText(
//                        this,
//                        "Please enter correct email to register",
//                        Toast.LENGTH_LONG
//                    ).show()
//                }

                else -> {

                    if (!isEmailValid1(et_unii_mail?.text.toString().trim())) {
                        Log.e("if", "false")
                        Toast.makeText(
                            this,
                            "Please enter correct email to register",
                            Toast.LENGTH_LONG
                        ).show()
//                    if (!checkEmail(et_unii_mail?.text.toString())) {
//                        Toast.makeText(
//                            this,
//                            "Please enter correct email to register",
//                            Toast.LENGTH_LONG
//                        ).show()
                    } else {
                        if (separated[1] != "uni.brighton.ac.uk") {
                            Toast.makeText(
                                this,
                                "Please register with your valid university email id",
                                Toast.LENGTH_LONG
                            ).show()
                        } else {
                            progressDialog?.setMessage("Registering a user ...")
                            progressDialog?.setCancelable(false)
                            progressDialog?.show()
                            signupPresenter!!.createAccount(
                                et_first_name?.text.toString().trim(),
                                et_last_name?.text.toString().trim(),
                                et_unii_mail?.text.toString().trim().toLowerCase(),
                                et_pswd?.text.toString().trim()
                            )
                        }

                        Log.e("else", "else")
                    }

                }
            }

        }

    }

    fun isEmailValid(email: String): Boolean {
        return Patterns.EMAIL_ADDRESS.toRegex().matches(email);
    }

    private fun termsAndConditions() {

        val dialog = Dialog(this, R.style.Theme_DeviceDefault_Dialog_NoActionBar)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(false)

        dialog.setContentView(com.uniimarket.app.R.layout.terms_conditions_popup)
        val body = dialog.findViewById(com.uniimarket.app.R.id.imv_cancel) as ImageView
//        body.text = title
//        val yesBtn = dialog .findViewById(R.id.yesBtn) as Button
//        val noBtn = dialog .findViewById(R.id.noBtn) as TextView
        body.setOnClickListener {
            dialog.dismiss()
        }

        dialog.show()


    }

    private fun checkEmail(email: String): Boolean {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    private fun initialVariables() {

        btn_signup = findViewById(com.uniimarket.app.R.id.btn_signup)
        imv_cancel = findViewById(com.uniimarket.app.R.id.imv_cancel)
        et_first_name = findViewById(com.uniimarket.app.R.id.et_first_name)
        et_last_name = findViewById(com.uniimarket.app.R.id.et_last_name)
        et_unii_mail = findViewById(com.uniimarket.app.R.id.et_unii_mail)
        et_pswd = findViewById(com.uniimarket.app.R.id.et_pswd)
        et_cnf_pswd = findViewById(com.uniimarket.app.R.id.et_cnf_pswd)
        cb_terms_conditions = findViewById(com.uniimarket.app.R.id.cb_terms_conditions)
        tv_terms_conditions = findViewById(com.uniimarket.app.R.id.tv_terms_conditions)
        progressDialog = ProgressDialog(this)
    }


    override fun signUpResponse(status: String, message: String) {
        progressDialog?.dismiss()
        if (status == "true") {

            val builder = AlertDialog.Builder(this@SignUpActivity)
            builder.setTitle("Unii Market Registration")
            builder.setMessage("You've registered successfully")
                .setPositiveButton("Ok") { dialog, id -> finish() }
            //Creating dialog box
            val alert = builder.create()
            alert.show()
            alert.getButton(androidx.appcompat.app.AlertDialog.BUTTON_POSITIVE).isAllCaps = false

        } else if (status == "false") {

            if (message == "failure") {

                val builder = AlertDialog.Builder(this@SignUpActivity)
                builder.setTitle("Unii Market Registration")
                builder.setMessage("Something Went Wrong")
                    .setPositiveButton("Ok") { dialog, id -> }
                //Creating dialog box
                val alert = builder.create()
                alert.show()
                alert.getButton(androidx.appcompat.app.AlertDialog.BUTTON_POSITIVE).isAllCaps =
                    false
            } else {
                val builder = AlertDialog.Builder(this@SignUpActivity)
                builder.setTitle("Unii Market Registration")
                builder.setMessage(message)
                    .setPositiveButton("Ok") { dialog, id -> }
                //Creating dialog box
                val alert = builder.create()
                alert.show()
                alert.getButton(androidx.appcompat.app.AlertDialog.BUTTON_POSITIVE).isAllCaps =
                    false
            }
        }
    }

    fun isEmailValid1(email: String): Boolean {
        var isValid = false
//        val expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$"
//        val expression = "[a-zA-Z0-9._-]+@[a-z]+.[a-z]+"
//        val expression =
//            "(?:[a-z0-9!#\$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#\$%&'*+/=?^_`{|}~-]+)*|\"(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])*\")@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\\[(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?|[a-z0-9-]*[a-z0-9]:(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21-\\x5a\\x53-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])+)\\])"
        val expression =
            "(?:[a-z0-9_`]+(?:\\.[a-z0-9!#\$%&'*+/=?^_`{|}~-]+)*|\"(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])*\")@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\\[(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?|[a-z0-9-]*[a-z0-9]:(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21-\\x5a\\x53-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])+)\\])"
        val inputStr: CharSequence = email
        val pattern: Pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE)
        val matcher: Matcher = pattern.matcher(inputStr)
        if (matcher.matches()) {
            isValid = true
        }

//        email.matches("[a-zA-Z0-9._-]+@[a-z]+.[a-z]+")
        return isValid
    }
}
