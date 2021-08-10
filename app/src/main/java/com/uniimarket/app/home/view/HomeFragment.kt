package com.uniimarket.app.home.view

//import ss.com.bannerslider.Slider
//import ss.com.bannerslider.adapters.SliderAdapter
//import ss.com.bannerslider.viewholder.ImageSlideViewHolder

import android.app.AlertDialog
import android.app.ProgressDialog
import android.content.SharedPreferences
import android.os.Bundle
import android.provider.Settings.Secure
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.iid.FirebaseInstanceId
import com.uniimarket.app.Dashboard.view.DashboardActivity
import com.uniimarket.app.Dashboard.view.PathWaysFragment
import com.uniimarket.app.Helper
import com.uniimarket.app.PicassoImageLoadingService
import com.uniimarket.app.R
import com.uniimarket.app.categories.view.SubCategoriesFragment
import com.uniimarket.app.home.model.CategoriesData
import com.uniimarket.app.home.model.ProductsData
import com.uniimarket.app.home.presenter.BannerPresenter
import com.uniimarket.app.home.presenter.CategoriesPresenter
import com.uniimarket.app.home.presenter.CategoriesPresenter.CategoriesListener
import ss.com.bannerslider.Slider
import ss.com.bannerslider.adapters.SliderAdapter
import ss.com.bannerslider.viewholder.ImageSlideViewHolder
import java.util.*
import java.util.concurrent.*


class HomeFragment : Fragment(), CategoriesListener, BannerPresenter.BannerProductsListener {

    private var slider: Slider? = null
    var ll_clothes: LinearLayout? = null
    var ll_furniture: LinearLayout? = null
    var ll_accessories: LinearLayout? = null
    var ll_gaming: LinearLayout? = null
    var ll_uni_materials: LinearLayout? = null
    var ll_technologies: LinearLayout? = null
    var ll_other: LinearLayout? = null
    var ll_back: LinearLayout? = null
    var categoriesPresenter: CategoriesPresenter? = null
    var bannerPresenter: BannerPresenter? = null
    var rv_category: RecyclerView? = null
    var sharedPref: SharedPreferences? = null
    var progressDialog: ProgressDialog? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // Inflate the layout for this fragment
        val view = inflater?.inflate(com.uniimarket.app.R.layout.home_fragment, container, false)

        initialVariables(view)

        updateFirebaseToken()

        (context as DashboardActivity).ll_toolbar?.visibility = View.VISIBLE
        Slider.init(PicassoImageLoadingService(context))
        setupViews()

        (context as DashboardActivity).imv_search?.visibility = View.VISIBLE
        (context as DashboardActivity).imv_user_search?.visibility = View.GONE

        (context as DashboardActivity).ll_back?.visibility = View.VISIBLE

        (context as DashboardActivity).ll_back?.setOnClickListener {
            (context as DashboardActivity).replaceFragment(
                R.id.frame_layout,
                PathWaysFragment(), true, false
            )
        }

        var sharedPref: SharedPreferences? = context?.getSharedPreferences("uniimarket", 0)
        (context as DashboardActivity).imv_profile?.let {
            Glide.with(this)
                .load(sharedPref?.getString("profilepic", null))
                .placeholder(R.drawable.profile).into(it)
        }


//        try {
//            if (sharedPref?.getString("popup", null) == "true") {
//
//            } else {
//                openDialog()
//            }
//        } catch (e: Exception) {
//            e.printStackTrace()
//            openDialog()
//        }


//        (context as DashboardActivity).ll_back?.setOnClickListener {  }

        (ll_clothes as View?)!!.setOnClickListener { categoriesIntent("clothes") }

        return view
    }

    private fun updateFirebaseToken() {
        var token = ""
        val android_id = Secure.getString(
            context!!.contentResolver,
            Secure.ANDROID_ID
        )

        FirebaseInstanceId.getInstance().instanceId
            .addOnCompleteListener(OnCompleteListener { task ->
                if (!task.isSuccessful) {
                    Log.e("pn", "getInstanceId failed", task.exception)
                    return@OnCompleteListener
                }

                // Get new Instance ID token
                token = task.result!!.token

                Log.e("SA FB Token", token)
                Helper.fbToken = token
                //                        Toast.makeText(SplashActivity.this, msg, Toast.LENGTH_SHORT).show();
            })

//        categoriesPresenter?.updateToken(
//            android_id, sharedPref?.getString("id", null),
//            sharedPref?.getString("email", null), token
//        )

    }


    override fun onResume() {
        super.onResume()
//        (context as DashboardActivity).ll_back?.visibility = View.INVISIBLE
        threadPool()

    }

    private fun threadPool() {
        progressDialog = ProgressDialog(context)
        progressDialog?.setMessage("Fetching data...")
        progressDialog?.setCancelable(false)
        progressDialog?.show()

        val callable1 = Callable<Void> {
            //            categoriesPresenter?.getSliderPrducts()
            bannerPresenter?.getSliderBanner()

            null
        }

        val callable2 = Callable<Void> {

            categoriesPresenter?.getCategories()
            null
        }


        //add to a list
        val taskList = ArrayList<Callable<Void>>()
        taskList.add(callable1)
        taskList.add(callable2)

        //create a pool executor with 3 threads
        val executor = Executors.newFixedThreadPool(2)

        try {
            //start the threads
            val futureList = executor.invokeAll(taskList)

            for (voidFuture in futureList) {
                try {
                    //check the status of each future.  get will block until the task
                    //completes or the time expires
                    voidFuture.get(100, TimeUnit.MILLISECONDS)
                } catch (e: ExecutionException) {
                    System.err.println("Error executing task " + e.message)
                } catch (e: TimeoutException) {
                    System.err.println("Timed out executing task" + e.message)
                }

            }
//            progressDialog.dismiss()

        } catch (ie: InterruptedException) {
            ie.printStackTrace()
            //do something if you care about interruption;
//            progressDialog.dismiss()
        }


        //  progressDialog.dismiss();
    }


    private fun categoriesIntent(s: String) {
//        startActivity(Intent(context, CategoriesActivity::class.java))
//     1ction.addToBackStack(true.toString())


        (context as DashboardActivity).addFragment(
            com.uniimarket.app.R.id.frame_layout,
            SubCategoriesFragment(), true, false
        )

//
//        ((DashboardActivity) mContext).addFragment(new ForumFragment(), true, false);
    }

    private fun initialVariables(view: View?) {

        slider = view?.findViewById(com.uniimarket.app.R.id.banner_slider1)
        ll_clothes = view?.findViewById(com.uniimarket.app.R.id.ll_clothes)
        ll_furniture = view?.findViewById(com.uniimarket.app.R.id.ll_furniture)
        ll_accessories = view?.findViewById(com.uniimarket.app.R.id.ll_accessories)
        ll_gaming = view?.findViewById(com.uniimarket.app.R.id.ll_gaming)
        ll_uni_materials = view?.findViewById(com.uniimarket.app.R.id.ll_uni_materials)
        ll_technologies = view?.findViewById(com.uniimarket.app.R.id.ll_technologies)
        ll_other = view?.findViewById(com.uniimarket.app.R.id.ll_other)
        ll_back = view?.findViewById(com.uniimarket.app.R.id.ll_back)
        rv_category = view?.findViewById(com.uniimarket.app.R.id.rv_category)
        categoriesPresenter = CategoriesPresenter(this, context)
        bannerPresenter = BannerPresenter(this, context)
        sharedPref = context?.getSharedPreferences("uniimarket", 0)
    }


    private fun setupViews() {
//        setupToolbar()
//        setupPageIndicatorChooser()
//        setupSettingsUi()
//        slider = findViewById(R.id.banner_slider1)

        //delay for testing empty view functionality
//        slider?.postDelayed(Runnable {
//            slider?.setAdapter(MainSliderAdapter())
//            slider?.setSelectedSlide(0)
//        }, 1000)

    }

    override fun categoriesResponseSuccess(
        status: String,
        message: String?,
        categoriesList: ArrayList<CategoriesData.Datum>
    ) {
        Log.e("cat o/p", categoriesList[0].name + "")

        val mng_layout: GridLayoutManager = GridLayoutManager(context, 3)

//        mng_layout.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
//            override fun getSpanSize(position: Int): Int {
//
//                if (categoriesList.size % 3 == 0)
//                    return 3
//                else
//                    if (position % 3 == 0)
//                        return 3
//                    else
//
//                        return if (position % 3 == 0)
//                            3
//                        else
//                            3
//            }
//        }
//        myRecyclerView.setLayoutManager(mng_layout)

        rv_category?.layoutManager = mng_layout

        rv_category?.adapter =
            CategoriesAdapter(categoriesList, context, this)

        try {
            progressDialog?.dismiss()
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    override fun categoriesResponseFailure(status: String, message: String?) {
        val builder = AlertDialog.Builder(context)
        builder.setTitle("Categories")
        builder.setMessage(message)
            .setPositiveButton("Retry") { dialog, id -> categoriesPresenter?.getCategories() }
            .setNegativeButton("Cancel") { dialog, id -> }
        //Creating dialog box
        val alert = builder.create()
        alert.show()
        alert.getButton(androidx.appcompat.app.AlertDialog.BUTTON_POSITIVE).isAllCaps = false
        alert.getButton(androidx.appcompat.app.AlertDialog.BUTTON_NEGATIVE).isAllCaps = false
        try {
            progressDialog?.dismiss()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun tokenSuccess(s: String, message: String?) {

        Log.e("token update", s+"")
    }

    override fun productResponseSuccess(
        status: String,
        message: String?,
        productDataList: java.util.ArrayList<ProductsData.Datum?>
    ) {

        Log.e("banner size", "" + productDataList.size)

        slider?.postDelayed(Runnable {
            slider?.setAdapter(MainSliderAdapter(productDataList))
            slider?.setSelectedSlide(0)
        }, 1000)


    }

    override fun productResponseFailure(status: String, message: String?) {

    }


    inner class MainSliderAdapter(var productDataList: java.util.ArrayList<ProductsData.Datum?>) :
        SliderAdapter() {


        override fun getItemCount(): Int {
            return productDataList.size
        }


        override fun onBindImageSlide(position: Int, viewHolder: ImageSlideViewHolder) {
//            when (position) {
////                0 -> viewHolder.bindImageSlide("https://assets.materialup.com/uploads/dcc07ea4-845a-463b-b5f0-4696574da5ed/preview.jpg")
////                1 -> viewHolder.bindImageSlide("https://assets.materialup.com/uploads/20ded50d-cc85-4e72-9ce3-452671cf7a6d/preview.jpg")
////                2 -> viewHolder.bindImageSlide("https://assets.materialup.com/uploads/76d63bbc-54a1-450a-a462-d90056be881b/preview.png")
//            }

            for (x in 0 until productDataList.size) {

                Log.e("img url", productDataList[position]?.banner_photo)
//                Log.e("productImage", productDataList[position].productPics)
                viewHolder.bindImageSlide(productDataList[position]?.banner_photo)
            }
//
//            viewHolder.imageView.setOnClickListener {
//                Log.e("slide pos", "" + position)
//            }

            slider?.setOnSlideClickListener {
                Log.e("slide pos", "" + position)
                Helper.homeScreen = "new_products"
                Helper.sliderProductData = productDataList[position]!!
                Helper.cid = productDataList[position]!!.cid.toString()
                Helper.scid = productDataList[position]!!.scid.toString()
                Helper.searchProductData.clear()
                (context as DashboardActivity).replaceFragment(
                    R.id.frame_layout,
                    ProductsFragment(), true, false
                )

            }
        }

    }

}
