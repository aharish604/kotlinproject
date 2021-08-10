package com.uniimarket.app.more.settings.view

import android.app.Dialog
import android.app.ProgressDialog
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.mindorks.placeholderview.ExpandablePlaceHolderView
import com.uniimarket.app.Dashboard.view.DashboardActivity
import com.uniimarket.app.Expandable.MultiCheckGenre
import com.uniimarket.app.Expandable.MultiCheckGenreAdapter
import com.uniimarket.app.categories.model.SubCategoriesData
import com.uniimarket.app.categories.presenter.SubCategoryPresenter
import com.uniimarket.app.home.model.CategoriesData
import com.uniimarket.app.home.model.ProductsData
import com.uniimarket.app.home.presenter.CategoriesPresenter
import com.uniimarket.app.more.MoreFragment
import com.uniimarket.app.more.settings.model.SettingsNotificationStatus
import com.uniimarket.app.more.settings.model.SubscribeCategoryData
import com.uniimarket.app.more.settings.presenter.NotificationStatusPresenter
import kotlinx.android.synthetic.main.child_layout.view.*
import kotlinx.android.synthetic.main.settings_fragment.*
import java.util.*
import kotlin.collections.ArrayList


class SettingsFragment : Fragment(),
    CategoriesPresenter.CategoriesListener,
    SubCategoryPresenter.SubCategoriesListener,
    NotificationStatusPresenter.NotificationStatusListener {


    var rl_change_password: RelativeLayout? = null
    var rb_category: RadioButton? = null
    var rb_user: RadioButton? = null
    var adapter: MultiCheckGenreAdapter? = null
    var rv_category: RecyclerView? = null
    var switch_app_notifications: Switch? = null
    var switch_show_notifications: Switch? = null
    var categoriesPresenter: CategoriesPresenter? = null
    var subCategoryPresenter: SubCategoryPresenter? = null

    var categoriesList: ArrayList<CategoriesData.Datum>? = null
    var subscribedList: ArrayList<SubscribeCategoryData.Datum?>? = null
    var subCategoryDataList: ArrayList<SubCategoriesData.Datum>? = null
    val hashMap: HashMap<String, ArrayList<SubCategoriesData.Datum>> =
        HashMap<String, ArrayList<SubCategoriesData.Datum>>() //define empty hashmap
    private var mDelayHandler: Handler? = null
    private val SPLASH_DELAY: Long = 3000 //3 seconds
    var i1: Int = 0
    var catName: String? = null
    var makeMultiCheckRockGenre: MultiCheckGenre? = null
    var list: List<MultiCheckGenre>? = ArrayList<MultiCheckGenre>()


    private var categoryMap: MutableMap<String, List<SubCategoriesData.Datum>>? = null
    private var subscribeCategoryDataMap: MutableMap<String?, List<SubscribeCategoryData.Datum?>>? =
        null

    private var subCategoriesDataList: List<SubCategoriesData.Datum>? = null
    private var categoriesDataList: List<CategoriesData.Datum>? = null

    private var expandablePlaceHolderView: ExpandablePlaceHolderView? = null

    var notificationStatusPresenter: NotificationStatusPresenter? = null

    var progressDialog: ProgressDialog? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // Inflate the layout for this fragment
        val view =
            inflater?.inflate(com.uniimarket.app.R.layout.settings_fragment, container, false)

        initialVariables(view)

        (context as DashboardActivity).ll_toolbar?.visibility = View.VISIBLE
        (context as DashboardActivity).ll_back?.visibility = View.VISIBLE

        (context as DashboardActivity).ll_back?.setOnClickListener {
            (context as DashboardActivity).replaceFragment(
                com.uniimarket.app.R.id.frame_layout,
                MoreFragment(), true, false
            )
        }

        var sharedPref: SharedPreferences? = context?.getSharedPreferences("uniimarket", 0)
        (context as DashboardActivity).imv_profile?.let {
            Glide.with(this)
                .load(sharedPref?.getString("profilepic", null))
                .placeholder(com.uniimarket.app.R.drawable.profile).into(it)
        }

        getUserNotificationStatus()

        val layoutManager = LinearLayoutManager(context)

        try {
//            expandablePlaceHolderView?.setOnClickListener {
//Log.
//            }
//            expandablePlaceHolderView?.setOnClickListener {
//
//            }
//            expandablePlaceHolderView?.expand(HeaderView(context, pair.key.toString()))
        } catch (e: Exception) {
            e.printStackTrace()
        }

        rl_change_password?.setOnClickListener {
            //            (context as DashboardActivity).replaceFragment(
//                R.id.frame_layout,
//                ChangePasswordFragment(), true, false

            startActivity(Intent(context, ChangePasswordActivity::class.java))
        }

//        getCategoriesList()
        getSubscribedNotifications()

//        adapter = MultiCheckGenreAdapter(makeMultiCheckGenres())
////        adapter = MultiCheckGenreAdapter(getMultiCheckGeneres())
//        rv_category!!.layoutManager = layoutManager
//        rv_category!!.adapter = adapter

        switch_app_notifications?.setOnClickListener {

            var switchShowNotifications: String = "0"

            switchShowNotifications = if (switch_show_notifications?.isChecked!!) {
                "1"
            } else {
                "0"
            }

            Log.e("app not", switch_app_notifications?.isChecked.toString())
            if (switch_app_notifications?.isChecked == true) {
//                switch_show_notifications?.isChecked = false

                changeSwitches(switchShowNotifications, "1")
            } else {
                switch_app_notifications?.isChecked = false
                changeSwitches(switchShowNotifications, "0")
            }
            rb_category?.isChecked = false
            rb_user?.isChecked = false
            expandablePlaceHolder!!.visibility = View.GONE

        }

//        switch_app_notifications?.setOnTouchListener(object:OnSwipeTouchListener(context) {
//            override fun onSwipeTop() {
//                Toast.makeText(context, "top", Toast.LENGTH_SHORT).show()
//            }
//            override fun onSwipeRight() {
//                switch_app_notifications?.isChecked = true
//                switch_show_notifications?.isChecked = false
////                Toast.makeText(context, "right", Toast.LENGTH_SHORT).show()
//                changeSwitches("0","1")
//                expandablePlaceHolder!!.visibility = View.VISIBLE
//            }
//            override fun onSwipeLeft() {
//                switch_app_notifications?.isChecked = false
//                switch_show_notifications?.isChecked = false
//                changeSwitches("0","0")
//                expandablePlaceHolder!!.visibility = View.GONE
////                Toast.makeText(context, "left", Toast.LENGTH_SHORT).show()
//            }
//            override fun onSwipeBottom() {
//                Toast.makeText(context, "bottom", Toast.LENGTH_SHORT).show()
//            }
//        })
//
//        switch_show_notifications?.setOnTouchListener(object:OnSwipeTouchListener(context) {
//            override fun onSwipeTop() {
//                Toast.makeText(context, "top", Toast.LENGTH_SHORT).show()
//            }
//            override fun onSwipeRight() {
//                switch_show_notifications?.isChecked = true
//                switch_app_notifications?.isChecked = false
////                Toast.makeText(context, "right", Toast.LENGTH_SHORT).show()
//                changeSwitches("1","0")
//            }
//            override fun onSwipeLeft() {
//                switch_show_notifications?.isChecked = false
//                switch_app_notifications?.isChecked = false
//                changeSwitches("0","0")
////                Toast.makeText(context, "left", Toast.LENGTH_SHORT).show()
//            }
//            override fun onSwipeBottom() {
//                Toast.makeText(context, "bottom", Toast.LENGTH_SHORT).show()
//            }
//        })

//        switch_show_notifications?.setOnTouchListener { v, event ->
//
//        }

        switch_show_notifications?.setOnClickListener {
            Log.e("show not", switch_show_notifications?.isChecked.toString())

            var switchAppNotifications: String = "0"

            switchAppNotifications = if (switch_app_notifications?.isChecked!!) {
                "1"
            } else {
                "0"
            }

            if (switch_show_notifications?.isChecked == true) {
                switch_app_notifications?.isChecked = false
                changeSwitches("1", switchAppNotifications)

            } else {
                switch_show_notifications?.isChecked = false
                changeSwitches("0", switchAppNotifications)
            }

            rb_category?.isChecked = false
            expandablePlaceHolder!!.visibility = View.GONE
        }

        rb_user?.setOnClickListener {

            if (switch_app_notifications?.isChecked!!) {
                if (rb_user?.isChecked == true) {
                    (context as DashboardActivity).replaceFragment(
                        com.uniimarket.app.R.id.frame_layout,
                        FriendsList(), true, false
                    )
                }
            } else {
                val dialog =
                    Dialog(context, android.R.style.Theme_DeviceDefault_Dialog_NoActionBar)
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
                dialog.setCancelable(false)

                dialog.setContentView(com.uniimarket.app.R.layout.notificaiton_popup)
                val body = dialog.findViewById(com.uniimarket.app.R.id.imv_cancel) as ImageView
                val tv_security_text =
                    dialog.findViewById(com.uniimarket.app.R.id.tv_security_text) as TextView
                tv_security_text.text = "Please Switch on the App Notifications"
                body.setOnClickListener {
                    dialog.dismiss()
                }
                rb_user?.isChecked = false
                dialog.show()
            }
        }

        rb_category?.setOnClickListener {
            if (switch_app_notifications?.isChecked!!) {
                if (rb_category?.isChecked == true) {
                    try {
                        if (subscribedList!!.size > 0) {
                            expandablePlaceHolder!!.visibility = View.VISIBLE
                        } else {
//                    mDelayHandler!!.postDelayed(mRunnable, SPLASH_DELAY)
                            expandablePlaceHolder!!.visibility = View.GONE
                        }
                    } catch (e: java.lang.Exception) {
                        e.printStackTrace()
                        expandablePlaceHolder!!.visibility = View.GONE
                    }
                } else {
                    expandablePlaceHolder!!.visibility = View.GONE
                }
            } else {
                expandablePlaceHolder!!.visibility = View.GONE
                val dialog =
                    Dialog(context, android.R.style.Theme_DeviceDefault_Dialog_NoActionBar)
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
                dialog.setCancelable(false)

                dialog.setContentView(com.uniimarket.app.R.layout.notificaiton_popup)
                val body = dialog.findViewById(com.uniimarket.app.R.id.imv_cancel) as ImageView
                val tv_security_text =
                    dialog.findViewById(com.uniimarket.app.R.id.tv_security_text) as TextView
                tv_security_text.text = "Please Switch on the App Notifications"
                body.setOnClickListener {
                    dialog.dismiss()
                }
                rb_category?.isChecked = false
                dialog.show()
            }
        }
        return view
    }

    private fun getSubscribedNotifications() {
        progressDialog?.setMessage("Loading...")
        progressDialog?.setCancelable(false)
        progressDialog?.show()
        notificationStatusPresenter?.getSubscribedNotifications()
    }

    private fun changeSwitches(showNotification: String, appNotification: String) {
        progressDialog?.setMessage("Loading...")
        progressDialog?.setCancelable(false)
        progressDialog?.show()
        notificationStatusPresenter?.updateSettingsNotifications(showNotification, appNotification)
    }

    private fun getUserNotificationStatus() {
        progressDialog?.setMessage("Loading...")
        progressDialog?.setCancelable(false)
        progressDialog?.show()
        notificationStatusPresenter?.getNotificationStatus()
    }

//    private fun getMultiCheckGeneres(): List<MultiCheckGenre> {
//
//        return Arrays.asList(
//            makeMultiCheckRockGenre(),
//            makeMultiCheckJazzGenre(),
//            makeMultiCheckClassicGenre(),
//            makeMultiCheckSalsaGenre(),
//            makeMulitCheckBluegrassGenre()
//        )
//    }

//    fun makeMultiCheckRockGenre(): MultiCheckGenre {
////        return MultiCheckGenre("Rock", makeRockArtists(), R.drawable.ic_launcher_background)
//    }

    private fun getCategoriesList() {
        categoriesPresenter?.getCategories()
    }

    private fun initialVariables(view: View?) {
        switch_show_notifications =
            view?.findViewById(com.uniimarket.app.R.id.switch_show_notifications)
        switch_app_notifications =
            view?.findViewById(com.uniimarket.app.R.id.switch_app_notifications)
        rv_category = view?.findViewById(com.uniimarket.app.R.id.rv_category) as RecyclerView
        rb_category = view?.findViewById(com.uniimarket.app.R.id.rb_category)
        rl_change_password = view?.findViewById(com.uniimarket.app.R.id.rl_change_password)
        rb_user = view?.findViewById(com.uniimarket.app.R.id.rb_user)
        expandablePlaceHolderView =
            view?.findViewById(com.uniimarket.app.R.id.expandablePlaceHolder)
        categoriesPresenter = CategoriesPresenter(this, context)
        subCategoryPresenter = SubCategoryPresenter(this, context)
        notificationStatusPresenter = NotificationStatusPresenter(this, context)
        mDelayHandler = Handler()
        categoryMap = HashMap<String, List<SubCategoriesData.Datum>>()
        subscribeCategoryDataMap = HashMap<String?, List<SubscribeCategoryData.Datum?>>()

        progressDialog = ProgressDialog(context)
    }

    override fun categoriesResponseSuccess(
        status: String,
        message: String?,
        categoriesList: ArrayList<CategoriesData.Datum>
    ) {
        progressDialog?.dismiss()
        this.categoriesList = categoriesList
        for (i in 0 until categoriesList?.size!!) {
            Log.e("i cat", "" + i1)
            catName = categoriesList!![i].name
            subCategoryPresenter?.getSubCategoryList(categoriesList?.get(i)?.id)
        }
//        mDelayHandler!!.postDelayed(mRunnable, SPLASH_DELAY)
    }

    internal val mRunnable: Runnable = Runnable {
        //        if (!isFinishing) {
        for (i in 0 until categoriesList?.size!!) {
            Log.e("i cat", "" + i1)
            catName = categoriesList!![i].name
            subCategoryPresenter?.getSubCategoryList(categoriesList?.get(i)?.id)
        }
    }

    override fun categoriesResponseFailure(status: String, message: String?) {
        progressDialog?.dismiss()
    }

    override fun tokenSuccess(s: String, message: String?) {


    }

    override fun productResponseSuccess(
        status: String,
        message: String?,
        productDataList: ArrayList<ProductsData.Datum>
    ) {
        progressDialog?.dismiss()
    }

    override fun productResponseFailure(status: String, message: String?) {
        progressDialog?.dismiss()
    }

    override fun subcategoriesResponseSuccess(
        status: String,
        message: String?,
        subCategoryDataList: ArrayList<SubCategoriesData.Datum>
    ) {
        progressDialog?.dismiss()

//        Log.e("i sub cat", "" + i1)
//
//        for (movie in subCategoryDataList) {
//            var movieList1: MutableList<SubCategoriesData.Datum>? =
//                this!!.categoryMap?.get(movie.categoriesName) as MutableList<SubCategoriesData.Datum>?
//            if (movieList1 == null) {
//                Log.e("map list", "null")
//                movieList1 = java.util.ArrayList<SubCategoriesData.Datum>()
//            }
//            movieList1.add(movie)
////            this!!.categoryMap?.set(movie.cid, movieList1)
//            movie.categoriesName?.let { categoryMap?.put(it, movieList1) }
//        }
//
//        Log.d("Map", categoryMap.toString())
//        val it = categoryMap?.entries?.iterator()
//        while (it?.hasNext()!!) {
//            val pair = it.next() as Map.Entry<*, *>
//            Log.d("Key", pair.key.toString())
//            expandablePlaceHolderView?.addView(HeaderView(context, pair.key.toString()))
//            val movieList1 = pair.value as List<SubCategoriesData.Datum>
//            for (movie in movieList1) {
//                expandablePlaceHolderView?.addView(ChildView(context, movie))
//            }
//            it.remove()
//        }
//
//        expandablePlaceHolderView?.setOnClickListener {
//            Log.e("chi", view?.id.toString())
////            expandablePlaceHolderView?.expand(com.uniimarket.app.R.layout.header_layout)
//
//            Log.e("frag", expandablePlaceHolderView?.child_name.toString())
////
////            try {
////                Log.e("pos", com.uniimarket.app.R.layout.header_layout.toString())
////            } catch (e: Exception) {
////                e.printStackTrace()
////            }
//
//        }


//
//        this.subCategoryDataList = subCategoryDataList
//
//        makeMultiCheckRockGenre =
//            catName?.let { MultiCheckGenre(it, subCategoryDataList, R.drawable.ic_launcher_background) }
//
//        list.a
//
//        catName?.let { hashMap.put(it, subCategoryDataList) }

    }

    override fun subcategoriesResponseFailure(status: String, message: String?) {
        progressDialog?.dismiss()
    }

    override fun notificationSuccessResponse(
        status: String,
        message: String,
        body: SettingsNotificationStatus
    ) {
        progressDialog?.dismiss()

        var showNotification: String = body.getData()?.allNotifications.toString()
        var appNotification: String = body.getData()?.appNotifications.toString()

        if (showNotification == "1") {
//            switch_app_notifications?.isChecked = true
            switch_show_notifications?.isChecked = true
        }

        if (appNotification == "1") {
//            switch_show_notifications?.isChecked = true
            switch_app_notifications?.isChecked = true
        }


    }

    override fun notificationFailureResponse(status: String, message: String) {
        progressDialog?.dismiss()
    }

    override fun subscribeSuccessResponse(
        s: String,
        message: String?,
        subCategoryDataList: java.util.ArrayList<SubscribeCategoryData.Datum?>
    ) {
        progressDialog?.dismiss()
        this.subscribedList = subCategoryDataList

        Log.e("sub size", "" + subCategoryDataList.size)

//        for (movie in subCategoryDataList) {
//            var movieList1: MutableList<SubscribeCategoryData.Datum?>? =
//                this!!.subscribeCategoryDataMap?.get(movie?.categorieName) as MutableList<SubscribeCategoryData.Datum?>?
//            if (movieList1 == null) {
//                Log.e("map list", "null")
//                movieList1 = java.util.ArrayList<SubscribeCategoryData.Datum?>()
//            }
//            movieList1.add(movie)
////            this!!.categoryMap?.set(movie.cid, movieList1)
//            movie?.categorieName?.let { subscribeCategoryDataMap?.put(it, movieList1) }
//        }
//
//
//        Log.d("Map", subscribeCategoryDataMap.toString())
//        val it = subscribeCategoryDataMap?.entries?.iterator()
//        while (it?.hasNext()!!) {
//            val pair = it.next() as Map.Entry<*, *>
//            Log.d("Key", pair.key.toString())
//            expandablePlaceHolderView?.addView(HeaderView(context, pair.key.toString()))
//            val movieList1 = pair.value as List<SubscribeCategoryData.Datum>
//            for (movie in movieList1) {
//                expandablePlaceHolderView?.addView(ChildView(context, movie))
//            }
//            it.remove()
//        }
//
//        expandablePlaceHolderView?.setOnClickListener {
//            Log.e("chi", view?.id.toString())
////            expandablePlaceHolderView?.expand(com.uniimarket.app.R.layout.header_layout)
//
//            Log.e("frag", expandablePlaceHolderView?.child_name.toString())
////
////            try {
////                Log.e("pos", com.uniimarket.app.R.layout.header_layout.toString())
////            } catch (e: Exception) {
////                e.printStackTrace()
////            }
//
//        }


        for (movie in subCategoryDataList) {
            var movieList1: MutableList<SubscribeCategoryData.Datum?>? =
                this!!.subscribeCategoryDataMap?.get(movie?.categorieName) as? MutableList<SubscribeCategoryData.Datum?>
            if (movieList1 == null) {
                movieList1 = java.util.ArrayList<SubscribeCategoryData.Datum?>()
            }
            movieList1.add(movie)

            this!!.subscribeCategoryDataMap?.set(movie?.categorieName, movieList1)
            movie?.categorieName?.let { subscribeCategoryDataMap?.put(it, movieList1) }

//            subscribeCategoryDataMap!![movie?.categorieName?] = movieList1
        }


        Log.d("Map", subscribeCategoryDataMap.toString())
        val it: MutableIterator<*> = subscribeCategoryDataMap!!.entries.iterator()
        while (it.hasNext()) {
            val pair =
                it.next() as Map.Entry<*, *>
            Log.d("Key", pair.key.toString())
            expandablePlaceHolderView!!.addView(HeaderView(context, pair.key.toString()))
            val movieList1: List<SubscribeCategoryData.Datum> =
                pair.value as List<SubscribeCategoryData.Datum>
            for (movie in movieList1) {
                expandablePlaceHolderView!!.addView(ChildView(context, movie))
            }
            it.remove()
        }


        expandablePlaceHolderView?.setOnClickListener {
            Log.e("chi", view?.id.toString())
//            expandablePlaceHolderView?.expand(com.uniimarket.app.R.layout.header_layout)

            Log.e("frag", expandablePlaceHolderView?.child_name.toString())
//
//            try {
//                Log.e("pos", com.uniimarket.app.R.layout.header_layout.toString())
//            } catch (e: Exception) {
//                e.printStackTrace()
//            }

        }

    }

    override fun subscribeFailureResponse(s: String, message: String?) {
        progressDialog?.dismiss()
    }


}
