package com.uniimarket.app.more.settings.view

import android.R
import android.app.Dialog
import android.app.ProgressDialog
import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import android.view.Window
import android.widget.ImageView
import android.widget.RadioButton
import android.widget.TextView
import com.google.gson.Gson
import com.mindorks.placeholderview.annotations.Click
import com.mindorks.placeholderview.annotations.Layout
import com.mindorks.placeholderview.annotations.Resolve
import com.mindorks.placeholderview.annotations.View
import com.uniimarket.app.SellerProfile.model.SellerUpdateNotificationsData
import com.uniimarket.app.more.settings.model.SubscribeCategoryData
import com.uniimarket.app.network.ApiClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


@Layout(com.uniimarket.app.R.layout.child_layout)
class ChildView(var mContext: Context?, var movie: SubscribeCategoryData.Datum) {


    private val TAG = "ChildView"

    @View(com.uniimarket.app.R.id.child_name)
    internal var textViewChild: TextView? = null

    @View(com.uniimarket.app.R.id.child_desc)
    internal var textViewDesc: TextView? = null

    @View(com.uniimarket.app.R.id.child_image)
    internal var childImage: ImageView? = null

    @View(com.uniimarket.app.R.id.rb_child)
    internal var rb_child: RadioButton? = null

    var progressDialog: ProgressDialog? = ProgressDialog(mContext)

//    private val mContext: Context
//    private val movie: Movie

//    fun ChildView(mContext: Context, movie: Movie): ??? {
//        this.mContext = mContext
//        this.movie = movie
//    }

    @Resolve
    private fun onResolve() {
        Log.e(TAG, "onResolve " + movie.subCategorieName + "")
        textViewChild!!.text = movie.subCategorieName

//        mContext?.let {
//            Glide.with(it).load(movie.image).apply(RequestOptions.circleCropTransform())
//                .into(childImage!!)
//        }

//        rb_child?.isChecked = movie.status == "1"
        rb_child?.isChecked = movie.status == "1"


    }

    @Click(com.uniimarket.app.R.id.child_name)
    fun onItemClick() {
        //        mExpandablePlaceHolderView.addChildView(mParentPosition, new ChildItem(mExpandablePlaceHolderView));
//        mExpandablePlaceHolderView.removeView(this)
        Log.e("child ", "click " + movie.subCategorieName + "---")
        rb_child?.isChecked = rb_child?.isChecked != true
        progressDialog?.setMessage("Submitting Data...")
        progressDialog?.setCancelable(false)
        progressDialog?.show()
        updateNotificationStatus(movie.categoriesid, movie.subcategorieid)


    }


    @Click(com.uniimarket.app.R.id.rb_child)
    fun onRadioButtonItemClick() {
        //        mExpandablePlaceHolderView.addChildView(mParentPosition, new ChildItem(mExpandablePlaceHolderView));
//        mExpandablePlaceHolderView.removeView(this)
        Log.e("child ", "click " + movie.subCategorieName + "---")
//        rb_child?.isChecked = rb_child?.isChecked != true
        progressDialog?.setMessage("Submitting Data...")
        progressDialog?.setCancelable(false)
        progressDialog?.show()
        updateNotificationStatus(movie.categoriesid, movie.subcategorieid)


    }

    fun updateNotificationStatus(cid: String?, scid: String?) {

        val apiService = ApiClient.create()

////        Call<EstimatedProjectsData> call = apiService.getEstimatedProjectList(sharedPreferences.getString("uid", null));
//        var uid: String
//        uid = try {
//            Helper.productData.uid.toString()
//        } catch (e: Exception) {
//            Helper.productData.uid.toString()
//        }
//
//        var cid: String
//        cid = try {
//            Helper.productData.cid.toString()
//        } catch (e: Exception) {
//            Helper.productData.cid.toString()
//        }
//
//        var scid: String
//        scid = try {
//            Helper.productData.scid.toString()
//        } catch (e: Exception) {
//            Helper.productData.scid.toString()
//        }

        val sharedPref: SharedPreferences =
            mContext?.getSharedPreferences("uniimarket", Context.MODE_PRIVATE)!!
        var id: String = sharedPref?.getString("id", null)

        val call = apiService.getUpdateNotificationStatus(
            id,
            "",
            cid,
            scid,
            "things"
        )

//        Log.e("id $id", "uid $uid")

        call?.enqueue(object : Callback<SellerUpdateNotificationsData> {
            override fun onResponse(
                call: Call<SellerUpdateNotificationsData>,
                response: Response<SellerUpdateNotificationsData>
            ) {
                progressDialog?.dismiss()
                try {
                    Log.e("seller profile", Gson().toJson(response.body()))

                    if (response.body()?.getStatus()!!) {
                        response.body()!!.getMessage()?.let { status("true", it) }

                        rb_child?.isChecked = response.body()!!.getData() == "1"


//                        response.body()?.getStatus()?.let {
//                            sellerProfileListener.sellUpdateNotificationResponse(
//                                it,
//                                response.body()?.getMessage(),
//                                response.body()?.getData()
//                            )
//                        }
                    } else {
                        response.body()!!.getMessage()?.let { status("false", it) }
//                        response.body()?.getStatus()?.let {
//                            sellerProfileListener.sellNotificationResponseFailure(it, response.body()?.getMessage())
//                        }
                    }

                } catch (e: Exception) {
                    e.printStackTrace()
                    status("false", "Please try again")
//                    response.body()?.getStatus()?.let {
//                        sellerProfileListener.sellProfileResponseFailure(
//                            false,
//                            response.body()?.getMessage()
//                        )
//                    }
                }

            }

            override fun onFailure(call: Call<SellerUpdateNotificationsData>, t: Throwable) {
                Log.e("TAG", t.toString())
                progressDialog?.dismiss()
                status("false", "Something went wrong")
                //                    Toast.makeText(getActivity(), t.toString(), Toast.LENGTH_SHORT).show();
            }
        })
    }

    fun status(status: String, message: String) {
        val dialog = Dialog(mContext, R.style.Theme_DeviceDefault_Dialog_NoActionBar)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(false)

        dialog.setContentView(com.uniimarket.app.R.layout.notificaiton_popup)
        val body = dialog.findViewById(com.uniimarket.app.R.id.imv_cancel) as ImageView
//        body.text = title
//        val yesBtn = dialog .findViewById(R.id.yesBtn) as Button
        val tv_security_text =
            dialog.findViewById(com.uniimarket.app.R.id.tv_security_text) as TextView
        tv_security_text.text = message
        body.setOnClickListener {
            dialog.dismiss()
        }

        dialog.show()

    }

}
