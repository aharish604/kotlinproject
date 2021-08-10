package com.uniimarket.app.home.view

import android.annotation.SuppressLint
import android.app.Dialog
import android.app.ProgressDialog
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.uniimarket.app.Dashboard.presenter.SearchPresenter
import com.uniimarket.app.Dashboard.view.DashboardActivity
import com.uniimarket.app.Helper
import com.uniimarket.app.R
import com.uniimarket.app.categories.model.CategoriesTypeData
import com.uniimarket.app.categories.model.SubCategoriesData
import com.uniimarket.app.categories.presenter.CategoryTypePresenter
import com.uniimarket.app.categories.presenter.SubCategoryPresenter
import com.uniimarket.app.categories.view.CategoriesPartAdapter
import com.uniimarket.app.categories.view.FilterPresenter
import com.uniimarket.app.categories.view.FilterProductsPartAdapter
import com.uniimarket.app.categories.view.SubCategoriesAdapter
import com.uniimarket.app.home.model.NewPostFilterData
import com.uniimarket.app.home.model.ProductsData
import com.uniimarket.app.home.presenter.BannerProductsPresenter
import com.uniimarket.app.sell.model.FilterData
import com.uniimarket.app.sell.view.FilterAdapter
import kotlinx.android.synthetic.main.sell_fragment.*

class ProductsFragment : Fragment(), BannerProductsPresenter.BannerProductsListener,
    SubCategoriesAdapter.AdapterOnClick,
    SubCategoryPresenter.SubCategoriesListener, FilterPresenter.FilterListener,
    CategoryTypePresenter.CategoryTypeListener, FilterAdapter.FilterClickListener,
    SearchPresenter.SearchListener {


    var ll_filter_sorting: LinearLayout? = null
    var filterPresenter: FilterPresenter? = null
    var subCategoryPresenter: SubCategoryPresenter? = null
    var categoryTypePresenter: CategoryTypePresenter? = null
    var rv_category_part: RecyclerView? = null
    var bannerProductsPresenter: BannerProductsPresenter? = null
    var imv_filter: ImageView? = null
    var imv_sorting: ImageView? = null
    var tv_category_type: TextView? = null
    var filterDialog: BottomSheetDialog? = null
    var shortingDialog: BottomSheetDialog? = null

    var tv_category_title: TextView? = null
    var rv_category: RecyclerView? = null
    var btn_apply: Button? = null
    var tv_no_filter: TextView? = null
    var rl_low_to_high: RelativeLayout? = null
    var rl_high_to_low: RelativeLayout? = null
    var rl_negotiable: RelativeLayout? = null
    var rl_exchange: RelativeLayout? = null
    var rl_distance: RelativeLayout? = null
    var rb_low_to_high: RadioButton? = null
    var rb_high_to_low: RadioButton? = null
    var rb_negotiable: RadioButton? = null
    var rb_exchange: RadioButton? = null
    var rb_distance: RadioButton? = null
    var tv_seek_bar_status: TextView? = null
    var btn_done: Button? = null
    var seekBar: SeekBar? = null
    var sortingValue: String? = ""
    var distance: String? = ""
    var subCategoryId: String? = ""
    var filterId: String? = ""
    var sharedPref: SharedPreferences? = null
    var filterListData: ArrayList<FilterData.Datum> = ArrayList()
    var newPostFilterListData: ArrayList<NewPostFilterData.Datum?> = ArrayList()
    var searchPresenter: SearchPresenter? = null
    var tv_no_data_found: TextView? = null
    var imv_no_data_found: ImageView? = null

    var progressDialog: ProgressDialog? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // Inflate the layout for this fragment
        val view =
            inflater?.inflate(com.uniimarket.app.R.layout.products_fragment, container, false)

        Log.e("prod"," called")

        (context as DashboardActivity).ll_back?.visibility = View.VISIBLE

        (context as DashboardActivity).ll_back?.setOnClickListener {
            (context as DashboardActivity).replaceFragment(
                R.id.frame_layout,
                HomeFragment(), true, false
            )
        }

        initialVariables(view)
        (context as DashboardActivity).ll_toolbar?.visibility = View.VISIBLE

        var sharedPref: SharedPreferences? = context?.getSharedPreferences("uniimarket", 0)
        (context as DashboardActivity).imv_profile?.let {
            Glide.with(this)
                .load(sharedPref?.getString("profilepic", null))
                .placeholder(R.drawable.profile).into(it)
        }
        Log.e("sp", sharedPref?.getString("id", null))

        filterPresenter = FilterPresenter(this, context)
//        filterPresenter?.getFilter(Helper.sliderProductData.scid)


        imv_filter?.setOnClickListener {

            init_filter_bottomsheet()

        }

        imv_sorting?.setOnClickListener {
            init_sorting_bottomsheet()
        }

        getSliderProducts()

        filterPresenter?.getNewPostFilter()

        return view
    }

    @SuppressLint("WrongConstant")
    private fun init_sorting_bottomsheet() {

        val modalbottomsheet =
            layoutInflater.inflate(com.uniimarket.app.R.layout.sorting_bottomsheet, null)

        shortingDialog = BottomSheetDialog(activity!!)
        shortingDialog?.setContentView(modalbottomsheet)
        shortingDialog?.setCanceledOnTouchOutside(true)
        shortingDialog?.setCancelable(true)
        try {
            shortingDialog?.show()
        } catch (e: Exception) {
            e.printStackTrace()
        }

        rl_low_to_high = modalbottomsheet.findViewById(com.uniimarket.app.R.id.rl_low_to_high)
        rl_high_to_low = modalbottomsheet.findViewById(com.uniimarket.app.R.id.rl_high_to_low)
        rl_negotiable = modalbottomsheet.findViewById(com.uniimarket.app.R.id.rl_negotiable)
        rl_exchange = modalbottomsheet.findViewById(com.uniimarket.app.R.id.rl_exchange)
        rb_low_to_high = modalbottomsheet.findViewById(com.uniimarket.app.R.id.rb_low_to_high)
        rb_high_to_low = modalbottomsheet.findViewById(com.uniimarket.app.R.id.rb_high_to_low)
        rb_negotiable = modalbottomsheet.findViewById(com.uniimarket.app.R.id.rb_negotiable)
        rb_exchange = modalbottomsheet.findViewById(com.uniimarket.app.R.id.rb_exchange)
        rl_distance = modalbottomsheet.findViewById(com.uniimarket.app.R.id.rl_distance)
        rb_distance = modalbottomsheet.findViewById(R.id.rb_distance)
        seekBar = modalbottomsheet.findViewById(R.id.seekBar)
        tv_seek_bar_status =
            modalbottomsheet.findViewById(com.uniimarket.app.R.id.tv_seek_bar_status)
        btn_done = modalbottomsheet.findViewById(R.id.btn_done)

        rl_low_to_high?.setOnClickListener {
            sortingValue = "LTH"
            tv_set_price?.text = "LTH"
            shortingDialog!!.hide()

            seekBar?.visibility = View.GONE
            tv_seek_bar_status?.visibility = View.GONE
            btn_done?.visibility = View.GONE
            distance = "0"
            sortingCategories()

        }

        rl_high_to_low?.setOnClickListener {
            sortingValue = "HTL"
            tv_set_price?.text = "HTL"
            shortingDialog!!.hide()

            seekBar?.visibility = View.GONE
            tv_seek_bar_status?.visibility = View.GONE
            btn_done?.visibility = View.GONE
            distance = "0"
            sortingCategories()
        }

        rl_negotiable?.setOnClickListener {
            sortingValue = "Negotiable"
            tv_set_price?.text = "Negotiable"
            shortingDialog!!.hide()

            seekBar?.visibility = View.GONE
            tv_seek_bar_status?.visibility = View.GONE
            btn_done?.visibility = View.GONE
            distance = "0"
            sortingCategories()
        }

        rl_exchange?.setOnClickListener {
            sortingValue = "Exchange"
            tv_set_price?.text = "Exchange"
            shortingDialog!!.hide()

            seekBar?.visibility = View.GONE
            tv_seek_bar_status?.visibility = View.GONE
            btn_done?.visibility = View.GONE
            distance = "0"
            sortingCategories()
        }


        rb_low_to_high?.setOnClickListener {
            sortingValue = "LTH"
            tv_set_price?.text = "LTH"
            shortingDialog!!.hide()

            seekBar?.visibility = View.GONE
            tv_seek_bar_status?.visibility = View.GONE
            btn_done?.visibility = View.GONE
            distance = "0"
            sortingCategories()

        }

        rb_high_to_low?.setOnClickListener {
            sortingValue = "HTL"
            tv_set_price?.text = "HTL"
            shortingDialog!!.hide()

            seekBar?.visibility = View.GONE
            tv_seek_bar_status?.visibility = View.GONE
            btn_done?.visibility = View.GONE
            distance = "0"
            sortingCategories()
        }

        rb_negotiable?.setOnClickListener {
            sortingValue = "Negotiable"
            tv_set_price?.text = "Negotiable"
            shortingDialog!!.hide()

            seekBar?.visibility = View.GONE
            tv_seek_bar_status?.visibility = View.GONE
            btn_done?.visibility = View.GONE
            distance = "0"
            sortingCategories()
        }

        rb_exchange?.setOnClickListener {
            sortingValue = "Exchange"
            tv_set_price?.text = "Exchange"
            shortingDialog!!.hide()

            seekBar?.visibility = View.GONE
            tv_seek_bar_status?.visibility = View.GONE
            btn_done?.visibility = View.GONE
            distance = "0"
            sortingCategories()
        }

        rb_distance?.setOnClickListener {
            sortingValue = "Distance"
            tv_set_price?.text = "Distance"

            seekBar?.visibility = View.VISIBLE
            tv_seek_bar_status?.visibility = View.VISIBLE
            btn_done?.visibility = View.VISIBLE

//            sortingCategories()
        }

        rl_distance?.setOnClickListener {
            sortingValue = "Distance"
            tv_set_price?.text = "Distance"
            rb_distance?.isChecked
            seekBar?.visibility = View.VISIBLE
            tv_seek_bar_status?.visibility = View.VISIBLE
            btn_done?.visibility = View.VISIBLE

//            sortingCategories()
        }

        seekBar?.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            internal var seekBarProgress = 0

            @SuppressLint("SetTextI18n")
            override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                seekBarProgress = progress
                tv_seek_bar_status?.text = "" + seekBarProgress + " miles"
            }

            override fun onStartTrackingTouch(seekBar: SeekBar) {

            }

            override fun onStopTrackingTouch(seekBar: SeekBar) {
                sortingValue = seekBarProgress.toString()
                distance = "" + seekBarProgress

                tv_seek_bar_status?.setText("" + seekBarProgress + " miles")

//                Toast.makeText(context, "SeekBar Touch Stop ", Toast.LENGTH_SHORT).show()
            }
        })

        btn_done?.setOnClickListener {
            shortingDialog!!.hide()
            sortingCategories()
        }
    }

    private fun sortingCategories() {
        Log.e("cat id", Helper.sliderProductData.cid)
        Log.e("sub cat id", Helper.sliderProductData.scid)

        Log.e("lat", "" + Helper.latitude)
        Log.e("lng", "" + Helper.longitude)

        categoryTypePresenter?.getSortingCategories(
            Helper.sliderProductData.cid,
            Helper.sliderProductData.scid,
            Helper.latitude,
            Helper.longitude,
            sortingValue,
            distance
        )

    }


    @SuppressLint("WrongConstant")
    private fun init_filter_bottomsheet() {

        val modalbottomsheet =
            layoutInflater.inflate(com.uniimarket.app.R.layout.filter_bottomsheet, null)

        filterDialog = BottomSheetDialog(activity!!)
        filterDialog?.setContentView(modalbottomsheet)
        filterDialog?.setCanceledOnTouchOutside(true)
        filterDialog?.setCancelable(true)



        tv_category_title =
            modalbottomsheet?.findViewById(com.uniimarket.app.R.id.tv_category_title)
        rv_category = modalbottomsheet?.findViewById(com.uniimarket.app.R.id.rv_category)
        btn_apply = modalbottomsheet?.findViewById(R.id.btn_apply)
        tv_no_filter = modalbottomsheet?.findViewById(R.id.tv_no_filter)
        tv_category_title?.text = "Filters"
        val mng_layout: LinearLayoutManager =
            LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        rv_category?.layoutManager = mng_layout
        rv_category?.adapter =
            NewPostFilterAdapter(newPostFilterListData, context, this)
        filterDialog?.show()
//        filterPresenter = FilterPresenter(this, context)
////        filterPresenter?.getFilter(Helper.sliderProductData.scid)
//        filterPresenter?.getNewPostFilter()

        btn_apply?.setOnClickListener {

            var filterIds: String = ""

            for (i in 0 until newPostFilterListData.size) {

                if (newPostFilterListData[i]!!.filterChecked) {
                    Log.e("filt name", "" + newPostFilterListData[i]?.id)
                    filterIds = newPostFilterListData[i]?.id + ","
                }
            }

            if (filterIds.isNotEmpty()) {

                filterDialog?.hide()
                filterId = filterIds.substring(0, filterIds.length - 1)

//                categoryTypePresenter?.getFilterCategories(
//                    Helper.sliderProductData.cid,
//                    Helper.sliderProductData.scid,
//                    filterId
//                )

                categoryTypePresenter?.getNewPostFilter(
                    filterId!!
                )
            } else {
                Toast.makeText(context, "Please Select at least one filter", Toast.LENGTH_LONG)
                    .show()
            }

        }
    }

    private fun getSliderProducts() {
        progressDialog?.setMessage("Loading...")
        progressDialog?.setCancelable(false)
        progressDialog?.show()

        if (Helper.searchProduct.isNotEmpty()) {
            searchPresenter?.getSearchProductList(Helper.searchProduct)
            Helper.searchProduct = ""
        } else {
            bannerProductsPresenter?.getBannerProductsList()
            ll_filter_sorting?.visibility = View.VISIBLE

            tv_category_type?.text = "New Posts"
        }
    }

    private fun initialVariables(view: View?) {

        rv_category_part = view?.findViewById(com.uniimarket.app.R.id.rv_category_part)
        imv_filter = view?.findViewById(com.uniimarket.app.R.id.imv_filter)
        imv_sorting = view?.findViewById(com.uniimarket.app.R.id.imv_sorting)
        ll_filter_sorting = view?.findViewById(R.id.ll_filter_sorting)
        imv_no_data_found = view?.findViewById(R.id.imv_no_data_found)
        subCategoryPresenter = SubCategoryPresenter(this, context)
        filterPresenter = FilterPresenter(this, context)
        categoryTypePresenter = CategoryTypePresenter(this, context)
        bannerProductsPresenter = BannerProductsPresenter(this, context)
        tv_category_type = view?.findViewById(R.id.tv_category_type)
        searchPresenter = SearchPresenter(this, context)
        tv_no_data_found = view?.findViewById(R.id.tv_no_data_found)

        progressDialog = ProgressDialog(context)
    }

    override fun bannerResponseSuccess(
        status: String,
        message: String?,
        productList: ArrayList<CategoriesTypeData.Datum>
    ) {

        try {
            progressDialog?.dismiss()
        }catch (e: Exception){
            e.printStackTrace()
        }
        val mng_layout: GridLayoutManager = GridLayoutManager(context, 2)
        rv_category_part?.layoutManager = mng_layout

        if (productList.size > 0) {
            imv_no_data_found?.visibility = View.GONE
            tv_no_data_found?.visibility = View.GONE
            rv_category_part?.visibility = View.VISIBLE
            rv_category_part?.adapter =
                BannerProductsAdapter(productList, context)
        } else {
            imv_no_data_found?.visibility = View.VISIBLE
            tv_no_data_found?.visibility = View.VISIBLE
            rv_category_part?.visibility = View.GONE
        }

    }

    override fun bannerResponseFailure(status: String, message: String?) {
        try {
            progressDialog?.dismiss()
        }catch (e: Exception){
            e.printStackTrace()
        }
        imv_no_data_found?.visibility = View.VISIBLE
        tv_no_data_found?.visibility = View.VISIBLE
        rv_category_part?.visibility = View.GONE
    }


    override fun subcategoriesResponseSuccess(
        status: String,
        message: String?,
        subCategoryDataList: ArrayList<SubCategoriesData.Datum>
    ) {

        rv_category_part?.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        rv_category_part?.adapter =
            SubCategoriesAdapter(subCategoryDataList, this, context)

        this.subCategoryId = subCategoryDataList[0].name


        categoryTypePresenter?.getFilterCategories(
            Helper.categoriesData.id,
            subCategoryDataList[0].id,
            ""
        )
    }

    override fun subcategoriesResponseFailure(status: String, message: String?) {

    }

    override fun productResponseSuccess(
        status: String,
        message: String?,
        productDataList: ArrayList<ProductsData.Datum>
    ) {

    }

    override fun productResponseFailure(status: String, message: String?) {

    }

    @SuppressLint("WrongConstant")
    override fun filterResponseSuccess(
        status: String,
        message: String?,
        filterList: ArrayList<FilterData.Datum>
    ) {
        rv_category?.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        rv_category?.adapter =
            FilterAdapter(filterList, context, this)
        filterListData = filterList
    }

    override fun newPostFilterResponseSuccess(
        status: String,
        message: String?,
        filterDataList: ArrayList<NewPostFilterData.Datum?>
    ) {
        tv_no_filter?.visibility = View.GONE
        rv_category?.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)

        newPostFilterListData = filterDataList
        rv_category?.adapter =
            NewPostFilterAdapter(filterDataList, context, this)
    }

    override fun filterResponseFailure(status: String, message: String?) {
        rv_category_part?.visibility = View.GONE
        tv_no_filter?.visibility = View.VISIBLE
        tv_no_filter?.text = "No Filter's Found"
//        imv_no_data_found?.visibility = View.VISIBLE
//        tv_no_data_found?.visibility = View.VISIBLE
    }

    override fun categoryTypeResponseSuccess(
        status: String,
        message: String?,
        categoryTypeDataList: ArrayList<CategoriesTypeData.Datum>
    ) {
        rv_category_part?.visibility = View.VISIBLE
//        imv_no_data_found?.visibility = View.GONE
//        tv_no_data_found?.visibility = View.GONE

        rv_category_part?.layoutManager = GridLayoutManager(context, 2)
        rv_category_part?.adapter =
            CategoriesPartAdapter(categoryTypeDataList, context)
    }

    override fun categoryTypeResponseFailure(status: String, message: String?) {
        rv_category_part?.visibility = View.GONE
//        imv_no_data_found?.visibility = View.VISIBLE
//        tv_no_data_found?.visibility = View.VISIBLE
    }

    override fun filterTypeResponseSuccess(
        status: String,
        message: String?,
        categoryTypeDataList: ArrayList<CategoriesTypeData.Datum>
    ) {
//        rv_category_part?.visibility = View.VISIBLE
////        imv_no_data_found?.visibility = View.GONE
////        tv_no_data_found?.visibility = View.GONE
//
//        rv_category_part?.layoutManager = GridLayoutManager(context, 2)
//        rv_category_part?.adapter =
//            FilterProductsPartAdapter(categoryTypeDataList, context)
    }

    override fun filterTypeResponseFailure(status: String, message: String?) {
        rv_category_part?.visibility = View.GONE
//        imv_no_data_found?.visibility = View.VISIBLE
//        tv_no_data_found?.visibility = View.VISIBLE
    }

    override fun newPostFilterTypeResponseSuccess(
        s: String,
        message: String?,
        categoriesList: java.util.ArrayList<CategoriesTypeData.Datum>
    ) {

        rv_category_part?.visibility = View.VISIBLE
//        imv_no_data_found?.visibility = View.GONE
//        tv_no_data_found?.visibility = View.GONE

        rv_category_part?.layoutManager = GridLayoutManager(context, 2)
        rv_category_part?.adapter =
            FilterProductsPartAdapter(categoriesList, context)
    }

    override fun onClickFilter(id: String?, name: String?) {
        filterDialog?.hide()
        filterId = id
//        tv_gender?.text = name
        Log.e(
            "cat id " + Helper.categoriesData.id,
            "sub cat id -- " + subCategoryId + " filter id -- " + filterId
        )
        categoryTypePresenter?.getFilterCategories(
            Helper.categoriesData.id,
            subCategoryId,
            filterId
        )

    }

    override fun onClick(item: String, subCategoryId: String) {
        Toast.makeText(context, item, Toast.LENGTH_SHORT).show()
//        tv_category_part?.text = subCategoryName
//        this.subCategoryId = subCategoryId
//
//        categoryTypePresenter?.getFilterCategories(
//            Helper.categoriesData.id,
//            subCategoryId,
//            ""
//        )
    }

    override fun searchResponseSuccess(
        status: String,
        message: String?,
        productDataList: ArrayList<CategoriesTypeData.Datum>
    ) {
        try {
            progressDialog?.dismiss()
        }catch (e: Exception){
            e.printStackTrace()
        }
        if (productDataList.size > 0) {
            val mng_layout: GridLayoutManager = GridLayoutManager(context, 2)
            rv_category_part?.layoutManager = mng_layout

            tv_category_type?.text = "Searched Products"

            rv_category_part?.adapter =
                BannerProductsAdapter(productDataList, context)

            ll_filter_sorting?.visibility = View.GONE
        } else {
            bannerProductsPresenter?.getBannerProductsList()
            ll_filter_sorting?.visibility = View.VISIBLE

            tv_category_type?.text = "New Posts"
        }
//
//        Helper.searchProductData = productDataList
//
//
//        val firstFragment = ProductsFragment()
//        val manager = supportFragmentManager
//        val transaction = manager.beginTransaction()
//        transaction.add(R.id.frame_layout, firstFragment)
//        transaction.addToBackStack(true.toString())
//        transaction.commit()


    }

    override fun searchResponseFailure(status: String, message: String?) {

        try {
            progressDialog?.dismiss()
        }catch (e: Exception){
            e.printStackTrace()
        }

        val dialog = Dialog(context, android.R.style.Theme_DeviceDefault_Dialog_NoActionBar)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(false)

        dialog.setContentView(R.layout.notificaiton_popup)
        val body = dialog.findViewById(R.id.imv_cancel) as ImageView
//        body.text = title
//        val yesBtn = dialog .findViewById(R.id.yesBtn) as Button
        val tv_security_text = dialog.findViewById(R.id.tv_security_text) as TextView
        tv_security_text.text = message
        body.setOnClickListener {
            //            notificationPresenter?.getNotificationList()
            dialog.dismiss()
        }

        dialog.show()
    }
}
