package com.uniimarket.app.categories.view

import android.annotation.SuppressLint
import android.app.ProgressDialog
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.uniimarket.app.Dashboard.view.DashboardActivity
import com.uniimarket.app.Helper
import com.uniimarket.app.R
import com.uniimarket.app.categories.model.CategoriesTypeData
import com.uniimarket.app.categories.model.SubCategoriesData
import com.uniimarket.app.categories.presenter.CategoryTypePresenter
import com.uniimarket.app.categories.presenter.SubCategoryPresenter
import com.uniimarket.app.home.model.NewPostFilterData
import com.uniimarket.app.home.model.ProductsData
import com.uniimarket.app.home.view.HomeFragment
import com.uniimarket.app.sell.model.FilterData
import com.uniimarket.app.sell.view.FilterAdapter
import kotlinx.android.synthetic.main.sell_fragment.*


class SubCategoriesFragment : Fragment(), SubCategoriesAdapter.AdapterOnClick,
    SubCategoryPresenter.SubCategoriesListener, FilterPresenter.FilterListener,
    CategoryTypePresenter.CategoryTypeListener, FilterAdapter.FilterClickListener {


    var filterPresenter: FilterPresenter? = null
    var rv_category_types: RecyclerView? = null
    var rv_category_part: RecyclerView? = null
    var tv_category_type: TextView? = null
    var tv_category_part: TextView? = null
    var imv_filter: ImageView? = null
    var imv_sorting: ImageView? = null

    var subCategoryPresenter: SubCategoryPresenter? = null
    var categoryTypePresenter: CategoryTypePresenter? = null
    var filterDialog: BottomSheetDialog? = null
    var shortingDialog: BottomSheetDialog? = null
    var tv_category_title: TextView? = null
    var rv_category: RecyclerView? = null
    var imv_no_data_found: ImageView? = null
    var tv_no_data_found: TextView? = null
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
    var btn_apply: Button? = null
    var tv_no_filter: TextView? = null
    var sharedPref: SharedPreferences? = null
    var progressDialog: ProgressDialog? = null
    var filterListData: ArrayList<FilterData.Datum> = ArrayList()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view =
            inflater?.inflate(com.uniimarket.app.R.layout.activity_categories, container, false)

        initialVariables(view)

        (context as DashboardActivity).ll_toolbar?.visibility = View.VISIBLE

        var sharedPref: SharedPreferences? = context?.getSharedPreferences("uniimarket", 0)
        (context as DashboardActivity).imv_profile?.let {
            Glide.with(this)
                .load(sharedPref?.getString("profilepic", null))
                .placeholder(R.drawable.profile).into(it)
        }

        filterPresenter = FilterPresenter(this, context)



        (context as DashboardActivity).ll_back?.visibility = View.VISIBLE
        (context as DashboardActivity).ll_back?.setOnClickListener {

            (context as DashboardActivity).replaceFragment(
                com.uniimarket.app.R.id.frame_layout,
                HomeFragment(), false, false
            )
        }
        try {
            tv_category_type?.text = Helper.categoriesData.name

            getSubCategoriesList(Helper.categoriesData.id)
        } catch (e: Exception) {
            e.printStackTrace()
        }

        imv_filter?.setOnClickListener {

            init_filter_bottomsheet()

        }

        imv_sorting?.setOnClickListener {
            init_sorting_bottomsheet()
        }

        (context as DashboardActivity).imv_profile?.let {
            Glide.with(this)
                .load(sharedPref?.getString("profilepic", null))
                .placeholder(R.drawable.profile).into(it)
        }


        Log.e("scid", subCategoryId)

        filterPresenter?.getFilter(subCategoryId)

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
        seekBar = modalbottomsheet.findViewById(com.uniimarket.app.R.id.seekBar)
        tv_seek_bar_status =
            modalbottomsheet.findViewById(com.uniimarket.app.R.id.tv_seek_bar_status)
        btn_done =
            modalbottomsheet.findViewById(R.id.btn_done)//                Toast.makeText(context, "SeekBar Touch Stop ", Toast.LENGTH_SHORT).show()
        //            sortingCategories()


        //            sortingCategories()
        when {
            sortingValue.equals("LTH") -> {
                rb_low_to_high?.isChecked = true
                rb_high_to_low?.isChecked = false
                rb_negotiable?.isChecked = false
                rb_exchange?.isChecked = false
                rb_distance?.isChecked = false
            }
            sortingValue.equals("HTL") -> {
                rb_low_to_high?.isChecked = false
                rb_high_to_low?.isChecked = true
                rb_negotiable?.isChecked = false
                rb_exchange?.isChecked = false
                rb_distance?.isChecked = false
            }
            sortingValue.equals("Negotiable") -> {
                rb_low_to_high?.isChecked = false
                rb_high_to_low?.isChecked = false
                rb_negotiable?.isChecked = true
                rb_exchange?.isChecked = false
                rb_distance?.isChecked = false
            }
            sortingValue.equals("Exchange") -> {
                rb_low_to_high?.isChecked = false
                rb_high_to_low?.isChecked = false
                rb_negotiable?.isChecked = false
                rb_exchange?.isChecked = true
                rb_distance?.isChecked = false
            }
            sortingValue.equals("Distance") -> {
                rb_low_to_high?.isChecked = false
                rb_high_to_low?.isChecked = false
                rb_negotiable?.isChecked = false
                rb_exchange?.isChecked = false
                rb_distance?.isChecked = true
            }
        }

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
        Log.e("cat id", Helper.categoriesData.id)
        Log.e("sub cat id", subCategoryId)

        Log.e("lat", "" + Helper.latitude)
        Log.e("lng", "" + Helper.longitude)

        categoryTypePresenter?.getSortingCategories(
            Helper.categoriesData.id,
            subCategoryId,
            Helper.latitude,
            Helper.longitude,
            sortingValue,
            distance
        )

    }

    @SuppressLint("WrongConstant")
    private fun init_filter_bottomsheet() {

        val modalbottomsheet = layoutInflater.inflate(R.layout.filter_bottomsheet, null)

        filterDialog = BottomSheetDialog(activity!!)
        filterDialog?.setContentView(modalbottomsheet)
        filterDialog?.setCanceledOnTouchOutside(true)
        filterDialog?.setCancelable(true)

        tv_category_title =
            modalbottomsheet?.findViewById(com.uniimarket.app.R.id.tv_category_title)
        rv_category = modalbottomsheet?.findViewById(com.uniimarket.app.R.id.rv_category)
        btn_apply = modalbottomsheet?.findViewById(R.id.btn_apply)
        tv_no_filter = modalbottomsheet?.findViewById(R.id.tv_no_filter)
        tv_category_title?.text = "Filter"

        rv_category?.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)

        rv_category?.adapter =
            FilterAdapter(filterListData, context, this)

        Log.e("filter list", filterListData.size.toString());

//        val mng_layout: LinearLayoutManager =
//            LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
//        rv_category?.layoutManager = mng_layout
        filterDialog?.show()
//        filterPresenter = FilterPresenter(this, context)
//
//        Log.e("scid", subCategoryId)
//
//        filterPresenter?.getFilter(subCategoryId)

        btn_apply?.setOnClickListener {

            var filterIds: StringBuilder = StringBuilder()

            var filterIdList: ArrayList<String> = ArrayList()

            for (i in 0 until filterListData.size) {

                if (filterListData[i].filterChecked) {
                    Log.e("filt name", "" + filterListData[i].id)
                    filterIds.append(filterListData[i].id + ",")
//                    filterIdList.add(filterIdfilterIds)
                }
            }

            Log.e("fid", filterIds.toString())

            if (filterIds.isNotEmpty()) {

                filterDialog?.hide()
                filterId = filterIds.substring(0, filterIds.length - 1)
                Log.e("filter id ", filterId)
                Log.e("cat id ", Helper.categoriesData.id)
                categoryTypePresenter?.getFilterCategories(
                    Helper.categoriesData.id,
                    subCategoryId,
                    filterId
                )
            } else {
                Toast.makeText(context, "Please select at least one filter", Toast.LENGTH_LONG)
                    .show()
            }

        }
    }

    private fun getSubCategoriesList(id: String?) {
        Log.e("category Id", id)
        progressDialog?.setMessage("Fetching data ...")
        progressDialog?.setCancelable(false)
        progressDialog?.show()
        subCategoryPresenter?.getSubCategoryList(id)

    }

    private fun initialVariables(view: View?) {
        sharedPref = context?.getSharedPreferences("uniimarket", 0)
        rv_category_types = view?.findViewById(com.uniimarket.app.R.id.rv_category_types)
        tv_category_type = view?.findViewById(com.uniimarket.app.R.id.tv_category_type)
        tv_category_part = view?.findViewById(com.uniimarket.app.R.id.tv_category_part)
        rv_category_part = view?.findViewById(com.uniimarket.app.R.id.rv_category_part)
        imv_filter = view?.findViewById(com.uniimarket.app.R.id.imv_filter)
        imv_sorting = view?.findViewById(com.uniimarket.app.R.id.imv_sorting)
        imv_no_data_found = view?.findViewById(R.id.imv_no_data_found)
        tv_no_data_found = view?.findViewById(R.id.tv_no_data_found)
        subCategoryPresenter = SubCategoryPresenter(this, context)
        filterPresenter = FilterPresenter(this, context)
        categoryTypePresenter = CategoryTypePresenter(this, context)

        progressDialog = ProgressDialog(context)
    }

    override fun onClick(subCategoryName: String, subCategoryId: String) {

//        Toast.makeText(context, item, Toast.LENGTH_SHORT).show()
        tv_category_part?.text = subCategoryName
        this.subCategoryId = subCategoryId

        Log.e("cid " + Helper.categoriesData.id, " scid " + subCategoryId + "--")

        categoryTypePresenter?.getFilterCategories(
            Helper.categoriesData.id,
            subCategoryId,
            ""
        )


        Log.e("sub ca id", subCategoryId)
        filterPresenter?.getFilter(subCategoryId)
    }

    override fun subcategoriesResponseSuccess(
        status: String,
        message: String?,
        subCategoryDataList: java.util.ArrayList<SubCategoriesData.Datum>
    ) {

        rv_category_types?.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        rv_category_types?.adapter =
            SubCategoriesAdapter(subCategoryDataList, this, context)

        this.subCategoryId = subCategoryDataList[0].id
        tv_category_part?.text = subCategoryDataList[0].name
        Log.e("cid " + Helper.categoriesData.id, " scid " + subCategoryId)
        categoryTypePresenter?.getFilterCategories(
            Helper.categoriesData.id,
            subCategoryDataList[0].id,
            ""
        )

        Log.e("scid", subCategoryId)
        progressDialog?.dismiss()
        progressDialog?.setMessage("Fetching data ...")
        progressDialog?.setCancelable(false)
        progressDialog?.show()
        filterPresenter?.getFilter(subCategoryId)
    }

    override fun subcategoriesResponseFailure(status: String, message: String?) {
        try {
            progressDialog?.dismiss()
        }catch (e: Exception){
            e.printStackTrace()
        }
    }

    override fun productResponseSuccess(
        status: String,
        message: String?,
        productDataList: java.util.ArrayList<ProductsData.Datum>
    ) {
        try {
            progressDialog?.dismiss()
        }catch (e: Exception){
            e.printStackTrace()
        }
    }

    override fun productResponseFailure(status: String, message: String?) {
        try {
            progressDialog?.dismiss()
        }catch (e: Exception){
            e.printStackTrace()
        }
    }


    @SuppressLint("WrongConstant")
    override fun filterResponseSuccess(
        status: String,
        message: String?,
        filterList: java.util.ArrayList<FilterData.Datum>
    ) {

        try {
            progressDialog?.dismiss()
        }catch (e: Exception){
            e.printStackTrace()
        }
//        rv_category_part?.visibility = View.GONE
//        imv_no_data_found?.visibility = View.VISIBLE
        filterListData.clear()
        filterListData = filterList
        Log.e("filter list", filterListData.size.toString());

        rv_category?.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)

        rv_category?.adapter =
            FilterAdapter(filterList, context, this)

//        this.subCategoryId = subCategoryDataList[0].name
//        subCategoryDataList[0].name?.let {
//            Log.e("sub cat id", it + "")
//            getCategoriesType(it)

//        categoryTypePresenter?.getFilterCategories(
//            Helper.categoriesData.id,
//            subCategoryId,
//            distance
//        )
    }

    @SuppressLint("WrongConstant")
    override fun newPostFilterResponseSuccess(
        status: String,
        message: String?,
        filterDataList: ArrayList<NewPostFilterData.Datum?>
    ) {
//
////        rv_category_part?.visibility = View.GONE
////        imv_no_data_found?.visibility = View.VISIBLE
//
//        rv_category?.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
//        filterListData = filterList
//        rv_category?.adapter =
//            FilterAdapter(filterList, context, this)
//
////        this.subCategoryId = subCategoryDataList[0].name
////        subCategoryDataList[0].name?.let {
////            Log.e("sub cat id", it + "")
////            getCategoriesType(it)
//
////        categoryTypePresenter?.getFilterCategories(
////            Helper.categoriesData.id,
////            subCategoryId,
////            distance
////        )
    }

    override fun filterResponseFailure(status: String, message: String?) {
//        rv_category?.visibility = View.GONE
//        imv_no_data_found?.visibility = View.VISIBLE
//        tv_no_data_found?.visibility = View.VISIBLE
        try {
            progressDialog?.dismiss()
        }catch (e: Exception){
            e.printStackTrace()
        }
    }

    override fun categoryTypeResponseSuccess(
        status: String,
        message: String?,
        categoryTypeDataList: ArrayList<CategoriesTypeData.Datum>
    ) {
        rv_category_part?.visibility = View.VISIBLE
        imv_no_data_found?.visibility = View.GONE
        tv_no_data_found?.visibility = View.GONE

        rv_category_part?.layoutManager = GridLayoutManager(context, 2)
        rv_category_part?.adapter =
            CategoriesPartAdapter(categoryTypeDataList, context)

    }

    override fun categoryTypeResponseFailure(status: String, message: String?) {
        rv_category_part?.visibility = View.GONE
        imv_no_data_found?.visibility = View.VISIBLE
        tv_no_data_found?.visibility = View.VISIBLE
    }

    override fun filterTypeResponseSuccess(
        status: String,
        message: String?,
        categoryTypeDataList: ArrayList<CategoriesTypeData.Datum>
    ) {

        rv_category_part?.visibility = View.VISIBLE
        imv_no_data_found?.visibility = View.GONE
        tv_no_data_found?.visibility = View.GONE

        rv_category_part?.layoutManager = GridLayoutManager(context, 2)
        rv_category_part?.adapter =
            FilterProductsPartAdapter(categoryTypeDataList, context)
    }

    override fun filterTypeResponseFailure(status: String, message: String?) {
        rv_category_part?.visibility = View.GONE
        imv_no_data_found?.visibility = View.VISIBLE
        tv_no_data_found?.visibility = View.VISIBLE
    }

    override fun newPostFilterTypeResponseSuccess(
        s: String,
        message: String?,
        categoriesList: java.util.ArrayList<CategoriesTypeData.Datum>
    ) {


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

}
