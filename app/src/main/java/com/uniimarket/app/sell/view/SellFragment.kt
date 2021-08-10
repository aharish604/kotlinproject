package com.uniimarket.app.sell.view

import android.annotation.SuppressLint
import android.app.Activity.RESULT_OK
import android.app.AlertDialog
import android.app.ProgressDialog
import android.content.Context
import android.content.Context.LOCATION_SERVICE
import android.content.DialogInterface
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Bitmap
import android.icu.text.SimpleDateFormat
import android.location.Geocoder
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.provider.Settings
import android.text.Editable
import android.text.InputType
import android.text.TextUtils
import android.util.Log
import android.view.*
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.android.gms.location.places.ui.PlacePicker
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.textfield.TextInputEditText
import com.uniimarket.app.Dashboard.view.DashboardActivity
import com.uniimarket.app.Helper
import com.uniimarket.app.MapsActivity
import com.uniimarket.app.R
import com.uniimarket.app.categories.model.SubCategoriesData
import com.uniimarket.app.categories.presenter.SubCategoryPresenter
import com.uniimarket.app.home.model.CategoriesData
import com.uniimarket.app.home.model.ProductsData
import com.uniimarket.app.home.presenter.CategoriesPresenter
import com.uniimarket.app.home.view.HomeFragment
import com.uniimarket.app.sell.model.FilterData
import com.uniimarket.app.sell.presenter.FilterPresenter
import com.uniimarket.app.sell.presenter.SellPresenter
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.IOException
import java.util.*


class SellFragment : Fragment(), CategoriesPresenter.CategoriesListener,
    SellCategoryAdapter.CategoryClickListener,
    SubCategoryPresenter.SubCategoriesListener, SellSubCategoryAdapter.SubCategoryClickListener,
    SellPresenter.SellListener, FilterPresenter.FilterListener,
    SellFilterAdapter.FilterClickListener {


    val PLACE_PICKER_REQUEST: Int = 123
    var week_str: String? = ""
    var et_seller_name: TextInputEditText? = null
    var et_ti_unii_mail: TextInputEditText? = null
    var et_ti_date: TextInputEditText? = null
    var et_ti_location: TextInputEditText? = null
    var et_ti_product_name: TextInputEditText? = null
    var et_ti_price: TextInputEditText? = null
    var et_ti_discription: TextInputEditText? = null
    var rl_category: RelativeLayout? = null
    var rl_sub_category: RelativeLayout? = null
    var rl_product_type: RelativeLayout? = null
    var imv_category: ImageView? = null
    var tv_category: TextView? = null
    var tv_sub_category: TextView? = null
    var imv_sub_category: ImageView? = null
    var tv_gender: TextView? = null
    var tv_product_type: TextView? = null
    var imv_gender: ImageView? = null
    var rl_set_price: RelativeLayout? = null
    var tv_set_price: TextView? = null
    var imv_price: ImageView? = null
    var categoryDialog: BottomSheetDialog? = null
    var subCategoryDialog: BottomSheetDialog? = null
    var genderDialog: BottomSheetDialog? = null
    var priceDialog: BottomSheetDialog? = null
    var calendarDialog: BottomSheetDialog? = null

    var rv_category: RecyclerView? = null
    var tv_category_title: TextView? = null
    var btn_gallery: Button? = null
    var rb_female: RadioButton? = null
    var rb_male: RadioButton? = null

    var rl_pup_set_price: RelativeLayout? = null
    var rl_pup_negotiable: RelativeLayout? = null
    var rl_pup_exchange: RelativeLayout? = null

    var rb_set_price: RadioButton? = null
    var rb_negotiable: RadioButton? = null
    var rb_exchange: RadioButton? = null
    var rl_take_picture: RelativeLayout? = null

    var categoriesPresenter: CategoriesPresenter? = null
    var subCategoryPresenter: SubCategoryPresenter? = null
    var filterPresenter: FilterPresenter? = null

    var categoryId: String? = ""
    var subCategoryId: String? = ""
    var productType: String? = ""
    var setPrice: String? = ""
    var cv_date: CalendarView? = null
    var btn_save: Button? = null
    var btn_upload_item: Button? = null
    var file: File? = null
    var filePathUri: Uri? = null
    var filePathString: String? = ""
    private val GALLERY = 1
    private val CAMERA = 2
    var catTouch: Int = 0
    var subcatTouch: Int = 0
    var productTouch: Int = 0
    var setPriceTouch: Int = 0
    private var locationManager: LocationManager? = null

    var sellPresenter: SellPresenter? = null
    var progressDialog: ProgressDialog? = null

    @SuppressLint("ClickableViewAccessibility")
    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater?.inflate(com.uniimarket.app.R.layout.sell_fragment, container, false)

        initialVariables(view)
        (context as DashboardActivity).ll_toolbar?.visibility = View.VISIBLE

//        view.setOnKeyListener( View.OnKeyListener { v, keyCode, event ->
//
//            if( keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_UP) {
//                Log.i(tag, "onKey Back listener is working!!!")
//                fragmentManager?.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE)
////                return true
//            }
////            return false;
//
//        })

        view.setOnKeyListener(object : View.OnKeyListener {
            override fun onKey(v: View, keyCode: Int, event: KeyEvent): Boolean {
                Log.i(tag, "keyCode: " + keyCode)
                if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() === KeyEvent.ACTION_UP) {
                    Log.e(tag, "onKey Back listener is working!!!")
//                    fragmentManager?.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE)
                    return true
                }
                return false
            }
        })

        (context as DashboardActivity).imv_search?.visibility = View.VISIBLE
        (context as DashboardActivity).imv_user_search?.visibility = View.GONE

        (context as DashboardActivity).ll_back?.visibility = View.VISIBLE
        (context as DashboardActivity).ll_back?.setOnClickListener {
            Helper.edit = ""
            //            (context as DashboardActivity).ll_back?.visibility = View.GONE
            (context as DashboardActivity).replaceFragment(
                com.uniimarket.app.R.id.frame_layout,
                HomeFragment(), true, false
            )
        }

        var sharedPref: SharedPreferences? = context?.getSharedPreferences("uniimarket", 0)
        (context as DashboardActivity).imv_profile?.let {
            Glide.with(this)
                .load(sharedPref?.getString("profilepic", null))
                .placeholder(R.drawable.profile).into(it)
        }

        var id: String = sharedPref?.getString("id", null)!!
        var firstname: String = sharedPref?.getString("firstname", null)!!
        var lastname: String = sharedPref?.getString("lastname", null)!!

        var email: String = sharedPref?.getString("email", null)!!

        et_ti_unii_mail?.setText(email)

        et_seller_name?.setText(firstname + " $lastname")

        et_seller_name?.isEnabled = false
        et_ti_unii_mail?.isEnabled = false


        et_ti_discription?.setOnTouchListener(object : View.OnTouchListener {
            override fun onTouch(view: View, event: MotionEvent): Boolean {
                // TODO Auto-generated method stub
                if (view.getId() === R.id.et_ti_discription) {
                    view.getParent().requestDisallowInterceptTouchEvent(true)
                    when (event.getAction() and MotionEvent.ACTION_MASK) {
                        MotionEvent.ACTION_UP -> view.getParent().requestDisallowInterceptTouchEvent(
                            false
                        )
                    }
                }
                return false
            }
        })

        rl_category?.setOnClickListener {
            init_category_bottomsheet()
        }

        imv_category?.setOnClickListener {
            init_category_bottomsheet()
        }

        rl_sub_category?.setOnClickListener {
            if (categoryId == "") {
                val builder = AlertDialog.Builder(context)
                builder.setTitle("Sub Category")
                builder.setMessage("Please select category")
                    .setPositiveButton("Ok") { dialog, id ->

                    }
                //Creating dialog box
                val alert = builder.create()
                alert.show()
            } else {

                init_subCategory_bottomsheet()
            }
        }

        imv_sub_category?.setOnClickListener {

            if (categoryId == "") {
                val builder = AlertDialog.Builder(context)
                builder.setTitle("Sub Category")
                builder.setMessage("Please select category")
                    .setPositiveButton("Ok") { dialog, id ->

                    }
                //Creating dialog box
                val alert = builder.create()
                alert.show()
            } else {

                init_subCategory_bottomsheet()
            }
        }

        rl_product_type?.setOnClickListener {
            if (subCategoryId == "") {
                val builder = AlertDialog.Builder(context)
                builder.setTitle("Products")
                builder.setMessage("Please select sub-category")
                    .setPositiveButton("Ok") { dialog, id ->

                    }
                //Creating dialog box
                val alert = builder.create()
                alert.show()
            } else {
                init_gender()
            }
        }

        imv_gender?.setOnClickListener {

            if (subCategoryId == "") {
                val builder = AlertDialog.Builder(context)
                builder.setTitle("Products")
                builder.setMessage("Please select sub-category")
                    .setPositiveButton("Ok") { dialog, id ->

                    }
                //Creating dialog box
                val alert = builder.create()
                alert.show()
            } else {
                init_gender()
            }
        }

        rl_set_price?.setOnClickListener {
            init_price()
        }
        imv_price?.setOnClickListener {
            init_price()
        }

        when {
            Build.VERSION.SDK_INT >= 21 -> et_ti_date!!.showSoftInputOnFocus = false
            Build.VERSION.SDK_INT >= 11 -> {
                et_ti_date!!.setRawInputType(InputType.TYPE_CLASS_TEXT)
                et_ti_date!!.setTextIsSelectable(true)
            }
            else -> {
                et_ti_date!!.setRawInputType(InputType.TYPE_NULL)
                et_ti_date!!.isFocusable = false
            }
        }

        et_ti_date?.setOnClickListener {
            //            et_ti_date?.setfocusable(false)
            init_date_bottomsheet()
        }

        et_ti_date?.setOnTouchListener(object : View.OnTouchListener {
            override fun onTouch(v: View, event: MotionEvent): Boolean {
                if (MotionEvent.ACTION_UP === event.getAction()) {
                    et_ti_date!!.isFocusable = false
                    init_date_bottomsheet()
                }
                return true // return is important...
            }
        })

        et_ti_location?.setOnTouchListener(object : View.OnTouchListener {
            override fun onTouch(v: View, event: MotionEvent): Boolean {
                if (MotionEvent.ACTION_UP === event.getAction()) {
                    et_ti_location!!.isFocusable = false
                    if (isLocationEnabled(context as DashboardActivity)) {
                        setCurrentLocation()
                    } else {
                        locationEnabled()
                    }
//                    setCurrentLocation()
                }
                return true // return is important...
            }
        })

        et_ti_location?.setOnClickListener {
            if (isLocationEnabled(context as DashboardActivity)) {
                setCurrentLocation()
            } else {
                locationEnabled()
            }
        }

        btn_gallery?.setOnClickListener {
            choosePhotoFromGallary()
        }
        rl_take_picture?.setOnClickListener {
            //            picImage()
            takePhotoFromCamera()
        }

        btn_upload_item?.setOnClickListener {
            if (Helper.edit == "Edit") {

                updateProduct()
            } else {

                updateSell()
            }
        }


        if (Helper.edit == "Edit") {

//            et_seller_name?.text = Helper?.purchaseProductData?.name.toString()

            et_seller_name?.setText(Helper.purchaseProductData.name.toString())
            et_ti_date?.setText(Helper.purchaseProductData.date.toString())
            tv_category?.text = Helper.purchaseProductData.categoriesName.toString()
            tv_sub_category?.text = Helper.purchaseProductData.subCategoriesName.toString()
            tv_product_type?.text = Helper.purchaseProductData.itemName.toString()
            tv_set_price?.text = Helper.purchaseProductData.setprice.toString()
            btn_upload_item?.text = "Update Product"

            Helper.pointLatitude = Helper.purchaseProductData.latitude?.toDouble()!!
            Helper.pointLongitude = Helper.purchaseProductData.longitude?.toDouble()!!
            Log.e("lat ${Helper.pointLatitude}", "long ${Helper.pointLongitude}")
            val geocoder = Geocoder(context, Locale.getDefault())
            try {
                var addresses = Helper.purchaseProductData.latitude?.toDouble()?.let {
                    Helper.purchaseProductData.longitude?.toDouble()?.let { it1 ->
                        geocoder.getFromLocation(
                            it,
                            it1, 1
                        )
                    }
                }

                var add: String? = null
                try {

                    val obj = addresses?.get(0)
                    add = obj?.getAddressLine(0)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
                Log.v("IGA", "Address " + add)
                et_ti_location?.setText(add)


            } catch (e: Exception) {
                // TODO Auto-generated catch block
                e.printStackTrace()
            }


            et_ti_product_name?.setText(Helper.purchaseProductData.productName.toString())
            et_ti_price?.setText(Helper.purchaseProductData.price.toString())
            et_ti_discription?.setText(Helper.purchaseProductData.description.toString())

            categoryId = Helper.purchaseProductData.cid.toString()
            subCategoryId = Helper.purchaseProductData.scid.toString()
            productType = Helper.purchaseProductData.productType.toString()
            setPrice = Helper.purchaseProductData.setprice.toString()
//            filePathString = Helper.purchaseProductData.productPics.toString()
            Helper.pointLatitude = Helper.purchaseProductData.latitude?.toDouble()!!
            Helper.pointLongitude = Helper.purchaseProductData.longitude?.toDouble()!!

            Log.e("cid", categoryId)
            Log.e("scid", subCategoryId)
            Log.e("pro type", productType)
            Log.e("set price", setPrice)

        }

        return view
    }


    private fun updateProduct() {

        Log.e("cid", categoryId)
        Log.e("scid", subCategoryId)
        Log.e("pro type", productType)
        Log.e("set price", setPrice)

        when {
            et_seller_name?.text.toString().trim() == "" -> {
//                et_seller_name?.error = "Enter the Seller Name"
                Toast.makeText(context, "Enter the Seller Name", Toast.LENGTH_LONG).show()
            }
            et_ti_unii_mail?.text.toString().trim() == "" -> {
//                et_unii_email?.error = "Enter the Uni Email"
                Toast.makeText(context, "Enter the Uni Email", Toast.LENGTH_LONG).show()
            }
            et_ti_date?.text.toString().trim() == "" -> {
//                et_ti_date?.error = "Please select date"
                Toast.makeText(context, "Please select date", Toast.LENGTH_LONG).show()
            }
            et_ti_location?.text.toString().trim() == "" -> {
//                et_ti_location?.error = "Please pic location"
                Toast.makeText(context, "Please pic location", Toast.LENGTH_LONG).show()
            }
            et_ti_product_name?.text.toString().trim() == "" -> {
//                et_ti_product_name?.error = "Enter the product name"
                Toast.makeText(context, "Enter the product name", Toast.LENGTH_LONG).show()
            }
            et_ti_price?.text.toString().trim() == "" -> {
//                et_ti_price?.error = "Enter the price"
                Toast.makeText(context, "Enter the price", Toast.LENGTH_LONG).show()
            }

            et_ti_price?.text.toString().trim().length > 15 -> {
//                et_ti_price?.error = "Price digit should be 15 or less"
                Toast.makeText(context, "Price digit should be 15 or less", Toast.LENGTH_LONG)
                    .show()
            }
            et_ti_price?.text.toString().trim().equals("0") -> {
//                et_ti_price?.error = "Enter the price"
                Toast.makeText(context, "Price cannot be zero", Toast.LENGTH_LONG).show()
            }
            et_ti_discription?.text.toString().trim() == "" -> {
//                et_ti_discription?.error = "Enter the description"
                Toast.makeText(context, "Enter the description", Toast.LENGTH_LONG).show()
            }

            categoryId == "" -> Toast.makeText(
                context,
                "Please Select Category",
                Toast.LENGTH_LONG
            ).show()
            categoryId == "0" -> Toast.makeText(
                context,
                "Please Select Category",
                Toast.LENGTH_LONG
            ).show()
            subCategoryId == "" -> Toast.makeText(
                context,
                "Please Select Sub Category",
                Toast.LENGTH_LONG
            ).show()
            subCategoryId == "0" -> Toast.makeText(
                context,
                "Please Select Sub Category",
                Toast.LENGTH_LONG
            ).show()
            productType == "" -> Toast.makeText(
                context,
                "Please Select Product Type",
                Toast.LENGTH_LONG
            ).show()
            productType == "0" -> Toast.makeText(
                context,
                "Please Select Product Type",
                Toast.LENGTH_LONG
            ).show()
            setPrice == "" -> Toast.makeText(
                context,
                "Please Select Set Price",
                Toast.LENGTH_LONG
            ).show()
//            filePathString == "" -> Toast.makeText(context, "Please Select File", Toast.LENGTH_LONG).show()


//            et_seller_name?.text.toString().trim() == ""
//            -> et_seller_name?.error = "Enter the First Name to Register"
//            et_seller_name?.text.toString().trim() == "" -> et_seller_name?.error = "Enter the First Name to Register"
//            et_seller_name?.text.toString().trim() == "" -> et_seller_name?.error = "Enter the First Name to Register"
//            et_seller_name?.text.toString().trim() == "" -> et_seller_name?.error = "Enter the First Name to Register"
//            et_seller_name?.text.toString().trim() == "" -> et_seller_name?.error = "Enter the First Name to Register"

            else -> {

                if (!checkEmail(et_ti_unii_mail?.text.toString())) {
                    Toast.makeText(
                        context,
                        "Please enter correct email to Product upload",
                        Toast.LENGTH_LONG
                    ).show()
                } else {
                    progressDialog?.setMessage("Updating data...")
                    progressDialog?.setCancelable(false)
                    progressDialog?.show()

                    Log.e("lat ${Helper.pointLatitude}", "long ${Helper.pointLongitude}")

                    if (filePathString == "") {
                        sellPresenter?.updateProductWithoutImage(
                            "Active",
                            et_ti_product_name?.text!!.trim().toString(),
                            productType,
                            categoryId,
                            subCategoryId,
                            et_ti_discription?.text!!.trim().toString(),
                            et_ti_price?.text!!.trim().toString(),
                            setPrice,
                            Helper.purchaseProductData.id,
                            et_ti_date?.text!!.trim().toString(),
                            Helper.pointLatitude,
                            Helper.pointLongitude,
                            et_ti_location?.text!!.trim().toString()
                        )
                    } else {

                        Log.e("product type", productType)
                        sellPresenter?.updateProductWithImage(
                            "Active",
                            et_ti_product_name?.text!!.trim().toString(),
                            productType,
                            categoryId,
                            subCategoryId,
                            et_ti_discription?.text!!.trim().toString(),
                            et_ti_price?.text!!.trim().toString(),
                            setPrice,
                            Helper.purchaseProductData.id,
                            et_ti_date?.text!!.trim().toString(),
                            Helper.pointLatitude,
                            Helper.pointLongitude,
                            file,
                            filePathUri,
                            et_ti_location?.text!!.trim().toString()
                        )
                    }
                }
            }
        }

    }

    fun isLocationEnabled(context: Context): Boolean {
        var locationMode = 0
        var locationProviders: String
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            try {
                locationMode = Settings.Secure.getInt(
                    context.getContentResolver(),
                    Settings.Secure.LOCATION_MODE
                )
            } catch (e: Settings.SettingNotFoundException) {
                e.printStackTrace()
                return false
            }
            return locationMode != Settings.Secure.LOCATION_MODE_OFF
        } else {
            locationProviders =
                Settings.Secure.getString(
                    context.getContentResolver(),
                    Settings.Secure.LOCATION_PROVIDERS_ALLOWED
                )
            return !TextUtils.isEmpty(locationProviders)
        }
    }

    private fun locationEnabled() {
        val lm = context?.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        var gps_enabled = false
        var network_enabled = false
        try {
            gps_enabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        try {
            network_enabled = lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        if (!gps_enabled && !network_enabled) {
            AlertDialog.Builder(context)
                .setMessage("GPS Enable")
                .setPositiveButton("Settings", object : DialogInterface.OnClickListener {
                    override fun onClick(paramDialogInterface: DialogInterface, paramInt: Int) {
                        startActivity(Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS))
                    }
                })
                .setNegativeButton("Cancel", null)
                .show()
        } else {
            setCurrentLocation()
        }
    }

    @SuppressLint("ServiceCast")
    private fun hideKeyboard(v: View?) {

        val `in` = context?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        `in`.hideSoftInputFromWindow(view?.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS)
    }

    private fun updateSell() {

        when {
            et_seller_name?.text.toString().trim() == "" -> {
//                et_seller_name?.error = "Enter the Seller Name"
                Toast.makeText(context, "Enter the Seller Name", Toast.LENGTH_LONG).show()
            }
            et_ti_unii_mail?.text.toString().trim() == "" -> {
//                et_unii_email?.error = "Enter the Uni Email"
                Toast.makeText(context, "Enter the Uni Email", Toast.LENGTH_LONG).show()
            }
            et_ti_date?.text.toString().trim() == "" -> {
//                et_ti_date?.error = "Please select date"
                Toast.makeText(context, "Please select date", Toast.LENGTH_LONG).show()
            }
            et_ti_location?.text.toString().trim() == "" -> {
//                et_ti_location?.error = "Please pic location"
                Toast.makeText(context, "Please pic location", Toast.LENGTH_LONG).show()
            }
            et_ti_product_name?.text.toString().trim() == "" -> {
//                et_ti_product_name?.error = "Enter the product name"
                Toast.makeText(context, "Enter the product name", Toast.LENGTH_LONG).show()
            }
            et_ti_price?.text.toString().trim() == "" -> {
//                et_ti_price?.error = "Enter the price"
                Toast.makeText(context, "Enter the price", Toast.LENGTH_LONG).show()
            }


            et_ti_price?.text.toString().trim().length > 15 -> {
//                et_ti_price?.error = "Price digit should be 15 or less"
                Toast.makeText(context, "Price digit should be 15 or less", Toast.LENGTH_LONG)
                    .show()
            }

            et_ti_discription?.text.toString().trim() == "" -> {
//                et_ti_discription?.error = "Enter the description"
                Toast.makeText(context, "Enter the description", Toast.LENGTH_LONG).show()
            }

            categoryId == "" -> Toast.makeText(
                context,
                "Please Select Category",
                Toast.LENGTH_LONG
            ).show()
            subCategoryId == "" -> Toast.makeText(
                context,
                "Please Select Sub Category",
                Toast.LENGTH_LONG
            ).show()
            productType == "" -> Toast.makeText(
                context,
                "Please Select Product Type",
                Toast.LENGTH_LONG
            ).show()
            setPrice == "" -> Toast.makeText(
                context,
                "Please Select Set Price",
                Toast.LENGTH_LONG
            ).show()

            setPrice == "Price" ->Toast.makeText(
                context,
                "Please Select Set Price",
                Toast.LENGTH_LONG
            ).show()
                filePathString == ""
            -> Toast.makeText(
                context,
                "Please Select File",
                Toast.LENGTH_LONG
            ).show()


//            et_seller_name?.text.toString().trim() == ""
//            -> et_seller_name?.error = "Enter the First Name to Register"
//            et_seller_name?.text.toString().trim() == "" -> et_seller_name?.error = "Enter the First Name to Register"
//            et_seller_name?.text.toString().trim() == "" -> et_seller_name?.error = "Enter the First Name to Register"
//            et_seller_name?.text.toString().trim() == "" -> et_seller_name?.error = "Enter the First Name to Register"
//            et_seller_name?.text.toString().trim() == "" -> et_seller_name?.error = "Enter the First Name to Register"

            else -> {

                if (!checkEmail(et_ti_unii_mail?.text.toString())) {
                    Toast.makeText(
                        context,
                        "Please enter correct email to Register",
                        Toast.LENGTH_LONG
                    ).show()
                } else {
                    progressDialog?.setMessage("Submitting data...")
                    progressDialog?.setCancelable(false)
                    progressDialog?.show()
                    Log.e("product type", productType)
                    sellPresenter?.updateSellItem(
                        et_seller_name?.text!!.trim().toString(),
                        et_ti_unii_mail?.text!!.trim().toString(),
                        et_ti_date?.text!!.trim().toString(),
                        et_ti_location?.text!!.trim().toString(),
                        et_ti_product_name?.text!!.trim().toString(),
                        et_ti_price?.text!!.trim().toString(),
                        et_ti_discription?.text!!.trim().toString(),
                        categoryId,
                        subCategoryId,
                        productType,
                        setPrice,
                        file,
                        filePathUri,
                        Helper.pointLatitude,
                        Helper.pointLongitude
                    )
                }
            }
        }
    }

    private fun checkEmail(email: String): Boolean {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }


    private fun picImage() {

        val pictureDialog = AlertDialog.Builder(context)
        pictureDialog.setTitle("Select Action")
        val pictureDialogItems = arrayOf("Select photo from gallery", "Capture photo from camera")
        pictureDialog.setItems(
            pictureDialogItems
        ) { dialog, which ->
            when (which) {
                0 -> choosePhotoFromGallary()
                1 -> takePhotoFromCamera()
            }
        }
        pictureDialog.show()
    }


    fun choosePhotoFromGallary() {
        val galleryIntent = Intent(
            Intent.ACTION_PICK,
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        )

        startActivityForResult(galleryIntent, GALLERY)
    }

    private fun takePhotoFromCamera() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        startActivityForResult(intent, CAMERA)
    }

    private fun init_price() {


        try {
            categoryDialog?.dismiss()
        } catch (e: Exception) {
            e.printStackTrace()
        }

        try {
            subCategoryDialog?.dismiss()
        } catch (e: Exception) {
            e.printStackTrace()
        }

        try {
            genderDialog?.dismiss()
        } catch (e: Exception) {
            e.printStackTrace()
        }

        try {
            priceDialog?.dismiss()
        } catch (e: Exception) {
            e.printStackTrace()
        }

        try {
            calendarDialog?.dismiss()
        } catch (e: Exception) {
            e.printStackTrace()
        }

//        setPriceTouch++

//        if (setPriceTouch == 1) {

        val modalbottomsheet =
            layoutInflater.inflate(com.uniimarket.app.R.layout.price_bottomsheet, null)

        priceDialog = BottomSheetDialog(context!!)
        priceDialog?.setContentView(modalbottomsheet)
        priceDialog?.setCanceledOnTouchOutside(false)
        priceDialog?.setCancelable(true)


        tv_gender = modalbottomsheet.findViewById(com.uniimarket.app.R.id.tv_gender)

        rb_exchange = modalbottomsheet.findViewById(com.uniimarket.app.R.id.rb_exchange)
        rb_negotiable = modalbottomsheet.findViewById(com.uniimarket.app.R.id.rb_negotiable)
        rb_set_price = modalbottomsheet.findViewById(com.uniimarket.app.R.id.rb_set_price)

        rl_pup_set_price =
            modalbottomsheet.findViewById(com.uniimarket.app.R.id.rl_pup_set_price)
        rl_pup_negotiable =
            modalbottomsheet.findViewById(com.uniimarket.app.R.id.rl_pup_negotiable)
        rl_pup_exchange = modalbottomsheet.findViewById(com.uniimarket.app.R.id.rl_pup_exchange)

        priceDialog?.show()

        tv_set_price?.text = "Price"
        setPrice = "Price"

        rl_pup_exchange?.setOnClickListener {
            setPrice = "Exchange"
            tv_set_price?.text = "Exchange"
            setPriceTouch = 0
            priceDialog!!.hide()
        }

        rl_pup_negotiable?.setOnClickListener {
            setPrice = "Negotiable"
            tv_set_price?.text = "Negotiable"
            setPriceTouch = 0
            priceDialog!!.hide()
        }

        rl_pup_set_price?.setOnClickListener {
            setPrice = "Set Price"
            tv_set_price?.text = "Set Price"
            setPriceTouch = 0
            priceDialog!!.hide()
        }

        rb_exchange?.setOnClickListener {
            setPrice = "Exchange"
            tv_set_price?.text = "Exchange"
            setPriceTouch = 0
            priceDialog!!.hide()
        }

        rb_negotiable?.setOnClickListener {
            setPrice = "Negotiable"
            tv_set_price?.text = "Negotiable"
            setPriceTouch = 0
            priceDialog!!.hide()
        }
        rb_set_price?.setOnClickListener {
            setPrice = "Set Price"
            tv_set_price?.text = "Set Price"
            setPriceTouch = 0
            priceDialog!!.hide()
        }
//        }
    }

    @SuppressLint("MissingPermission")
    private fun setCurrentLocation() {

//        locationManager = context!!.getSystemService(Context.LOCATION_SERVICE) as LocationManager
//
//        if (!locationManager!!.isProviderEnabled(LocationManager.GPS_PROVIDER) && !locationManager!!.isProviderEnabled(
//                LocationManager.NETWORK_PROVIDER
//            )
//        ) {
////            enableLoc()
//        } else {
//            var builder: PlacePicker.IntentBuilder = PlacePicker.IntentBuilder()
//            try {
//                // Initialize Places.
////                this!!.context?.let { Places.initialize(it, "AIzaSyDrR9RNXQ_KRD_e6icQNclEq_RwgkU7LM0") };
//
//
//                startActivityForResult(builder.build(context as Activity?), PLACE_PICKER_REQUEST)
//            } catch (e: GooglePlayServicesRepairableException) {
//                e.printStackTrace();
//            } catch (e: GooglePlayServicesNotAvailableException) {
//                e.printStackTrace()
//            }
//        }

//        (context as DashboardActivity).replaceFragment(
//            com.uniimarket.app.R.id.frame_layout,
//            MapsFragment(), true, false
//        )

        startActivity(Intent(context, MapsActivity::class.java))

//
//        locationManager?.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0L, 0f, locationListener)
    }

    @SuppressLint("SetTextI18n")
    override fun onResume() {
        super.onResume()

        var location: String = "" + Helper.pointLatitude + ":" + Helper.pointLongitude
        if (Helper.pointLongitude != 0.0 || Helper.pointLatitude != 0.0) {
            et_ti_location?.setText(location)

            getAddress(Helper.pointLatitude, Helper.pointLongitude)
        }

    }

    private fun getAddress(lat: Double, lng: Double) {

        val geocoder = Geocoder(context, Locale.getDefault())
        try {
            val addresses = geocoder.getFromLocation(lat, lng, 1)
            val obj = addresses[0]
            var add = obj.getAddressLine(0)
//            add = add + "\n" + obj.getCountryName()
//            add = add + "\n" + obj.getCountryCode()
//            add = add + "\n" + obj.getAdminArea()
//            add = add + "\n" + obj.getPostalCode()
//            add = add + "\n" + obj.getSubAdminArea()
//            add = add + "\n" + obj.getLocality()
//            add = add + "\n" + obj.getSubThoroughfare()
            Log.v("IGA", "Address " + add)

            et_ti_location?.setText(add)
            // Toast.makeText(this, "Address=>" + add,
            // Toast.LENGTH_SHORT).show();
            // TennisAppActivity.showDialog(add);
        } catch (e: Exception) {
            // TODO Auto-generated catch block
            e.printStackTrace()
            Toast.makeText(context, e.message, Toast.LENGTH_SHORT).show()
        }

    }


    private val locationListener: LocationListener = object : LocationListener {
        override fun onLocationChanged(location: Location) {
            Log.e("lat lon", "" + location.longitude + ":" + location.latitude)
            et_ti_location?.setText("" + location.longitude + ":" + location.latitude)
        }

        override fun onStatusChanged(provider: String, status: Int, extras: Bundle) {}
        override fun onProviderEnabled(provider: String) {}
        override fun onProviderDisabled(provider: String) {}
    }


    @SuppressLint("WrongConstant")
    private fun init_gender() {
//        catTouch = 0
//        subcatTouch = 0
//        productTouch++
//
//        if (productTouch == 1) {

        try {
            categoryDialog?.dismiss()
        } catch (e: Exception) {
            e.printStackTrace()
        }

        try {
            subCategoryDialog?.dismiss()
        } catch (e: Exception) {
            e.printStackTrace()
        }

        try {
            genderDialog?.dismiss()
        } catch (e: Exception) {
            e.printStackTrace()
        }

        try {
            priceDialog?.dismiss()
        } catch (e: Exception) {
            e.printStackTrace()
        }

        try {
            calendarDialog?.dismiss()
        } catch (e: Exception) {
            e.printStackTrace()
        }


        val modalbottomsheet = layoutInflater.inflate(R.layout.category_bottomsheet, null)

        genderDialog = BottomSheetDialog(context!!)
        genderDialog?.setContentView(modalbottomsheet)
        genderDialog?.setCanceledOnTouchOutside(false)
        genderDialog?.setCancelable(true)

//        rb_male = modalbottomsheet.findViewById(com.uniimarket.app.R.id.rb_male)
//        rb_female = modalbottomsheet.findViewById(com.uniimarket.app.R.id.rb_female)
        tv_category_title = modalbottomsheet.findViewById(com.uniimarket.app.R.id.tv_category_title)
        tv_category_title?.text = "Filters"

        rv_category = modalbottomsheet.findViewById(com.uniimarket.app.R.id.rv_category)

//        genderDialog?.show()


//        rb_female?.setOnClickListener {
//            gender = "Female"
//            tv_gender?.text = "Female"
//            genderDialog!!.hide()
//        }
//
//        rb_male?.setOnClickListener {
//            gender = "Male"
//            tv_gender?.text = "Male"
//            genderDialog!!.hide()
//        }

        genderDialog?.show()
        filterPresenter = FilterPresenter(this, context)
        Log.e("sub cat id", subCategoryId)
        filterPresenter!!.getFilter(subCategoryId)
//        }
//        subCategoryPresenter?.getSubCategoryList(categoryId)

    }

    @SuppressLint("SetTextI18n")
    @RequiresApi(Build.VERSION_CODES.N)
    private fun init_date_bottomsheet() {


        try {
            categoryDialog?.dismiss()
        } catch (e: Exception) {
            e.printStackTrace()
        }

        try {
            subCategoryDialog?.dismiss()
        } catch (e: Exception) {
            e.printStackTrace()
        }

        try {
            genderDialog?.dismiss()
        } catch (e: Exception) {
            e.printStackTrace()
        }

        try {
            priceDialog?.dismiss()
        } catch (e: Exception) {
            e.printStackTrace()
        }

        try {
            calendarDialog?.dismiss()
        } catch (e: Exception) {
            e.printStackTrace()
        }


        val modalbottomsheet =
            layoutInflater.inflate(com.uniimarket.app.R.layout.calendar_bottomsheet, null)

        calendarDialog = BottomSheetDialog(context!!)
        calendarDialog?.setContentView(modalbottomsheet)
        calendarDialog?.setCanceledOnTouchOutside(true)
        calendarDialog?.setCancelable(true)


//        cv_date?.setOnDateChangeListener(CalendarView.OnDateChangeListener { view, year, month, dayOfMonth ->
//            // display the selected date by using a toast
//            Toast.makeText(context, "" + dayOfMonth + "/" + month + "/" + year, Toast.LENGTH_LONG).show()
//        })

        cv_date = modalbottomsheet.findViewById(com.uniimarket.app.R.id.cv_date)
        btn_save = modalbottomsheet.findViewById(com.uniimarket.app.R.id.btn_save)
//        cv_date?.setSelectedDateVerticalBar(com.uniimarket.app.R.color.colorGreyTransparent) // set the color for the vertical bar
        cv_date?.maxDate = System.currentTimeMillis()
        val c = Calendar.getInstance().time
        println("Current time => $c")

        val df = SimpleDateFormat("dd-MM-yyyy")
        val formattedDate = df.format(c)
        week_str = formattedDate
        btn_save?.setOnClickListener {
            //            cv_date?.setOnDateChangeListener(CalendarView.OnDateChangeListener { view, year, month, dayOfMonth ->
//                // display the selected date by using a toast
//                Log.e("date", "" + dayOfMonth + "/" + month + "/" + year)
//                Toast.makeText(context, "" + dayOfMonth + "/" + month + "/" + year, Toast.LENGTH_LONG).show()
//            })


            calendarDialog!!.hide()
            et_ti_date?.text = Editable.Factory.getInstance().newEditable(week_str)
//            et_ti_date?.text = week_str
            et_ti_date?.clearFocus()
            Log.e("date", week_str + "")
        }

        cv_date?.setOnDateChangeListener(CalendarView.OnDateChangeListener { calendarView, pyear, pmonth, pday ->
            week_str = ""
//            isFirst = false
            val msg = "Selected date Day: " + pday + " Month : " + (pmonth + 1) + " Year " + pyear
            var time_str = pyear.toString() + "-" + (pmonth + 1) + "-" + pday

            if (pday <= 9 && pmonth + 1 <= 9) {
                time_str =
                    StringBuilder().append("0")
                        .append(pday).append("-").append("0").append(pmonth + 1)
                        .append("-").append(pyear)
                        .toString()
            } else if (pday >= 9 && pmonth + 1 <= 9) {
                time_str =
                    StringBuilder().append(pday).append("-").append("0").append(pmonth + 1)
                        .append("-").append(pyear)
                        .toString()
            } else if (pday <= 9 && pmonth + 1 >= 9) {
                time_str =
                    StringBuilder().append(pday).append("-").append(pmonth + 1).append("-")
                        .append("0").append(pyear)
                        .toString()
            } else {
                time_str =
                    StringBuilder().append(pday).append("-").append(pmonth + 1).append("-")
                        .append(pyear).toString()

            }

            // Toast.makeText(getActivity(), time_str, Toast.LENGTH_SHORT).show();
            week_str = time_str
//            MyAppointmentsListgetting()
        })

        calendarDialog?.show()
    }

    @SuppressLint("WrongConstant")
    private fun init_category_bottomsheet() {
//        productTouch = 0
//        subcatTouch = 0
//        catTouch++
//        if (catTouch == 1) {

        try {
            categoryDialog?.dismiss()
        } catch (e: Exception) {
            e.printStackTrace()
        }

        try {
            subCategoryDialog?.dismiss()
        } catch (e: Exception) {
            e.printStackTrace()
        }

        try {
            genderDialog?.dismiss()
        } catch (e: Exception) {
            e.printStackTrace()
        }

        try {
            priceDialog?.dismiss()
        } catch (e: Exception) {
            e.printStackTrace()
        }

        try {
            calendarDialog?.dismiss()
        } catch (e: Exception) {
            e.printStackTrace()
        }

        val modalbottomsheet =
            layoutInflater.inflate(com.uniimarket.app.R.layout.category_bottomsheet, null)

        categoryDialog = BottomSheetDialog(context!!)
        categoryDialog?.setContentView(modalbottomsheet)
        categoryDialog?.setCanceledOnTouchOutside(false)
        categoryDialog?.setCancelable(true)

        Log.e("dia show", categoryDialog?.isShowing.toString())

        if (categoryDialog?.isShowing!!) {

        } else {
            rv_category = modalbottomsheet.findViewById(com.uniimarket.app.R.id.rv_category)

            val mng_layout: LinearLayoutManager =
                LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            rv_category?.layoutManager = mng_layout

            categoriesPresenter = CategoriesPresenter(this, context)
            categoriesPresenter?.getCategories()
        }
//        }

    }


    @SuppressLint("WrongConstant")
    private fun init_subCategory_bottomsheet() {

        try {
            categoryDialog?.dismiss()
        } catch (e: Exception) {
            e.printStackTrace()
        }

        try {
            subCategoryDialog?.dismiss()
        } catch (e: Exception) {
            e.printStackTrace()
        }

        try {
            genderDialog?.dismiss()
        } catch (e: Exception) {
            e.printStackTrace()
        }

        try {
            priceDialog?.dismiss()
        } catch (e: Exception) {
            e.printStackTrace()
        }

        try {
            calendarDialog?.dismiss()
        } catch (e: Exception) {
            e.printStackTrace()
        }

//        Log.e("sc t", "$subcatTouch")
//        productTouch = 0
//        subcatTouch++
//        catTouch = 0
//
//        if (subcatTouch == 1) {

        val modalbottomsheet =
            layoutInflater.inflate(com.uniimarket.app.R.layout.category_bottomsheet, null)

        subCategoryDialog = BottomSheetDialog(context!!)
        subCategoryDialog?.setContentView(modalbottomsheet)
        subCategoryDialog?.setCanceledOnTouchOutside(false)
        subCategoryDialog?.setCancelable(true)

        tv_category_title = modalbottomsheet.findViewById(com.uniimarket.app.R.id.tv_category_title)
        tv_category_title?.text = "Sub Categories"

        rv_category = modalbottomsheet.findViewById(com.uniimarket.app.R.id.rv_category)

//        val mng_layout: LinearLayoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
//        rv_category?.layoutManager = mng_layout

        subCategoryDialog?.show()
        subCategoryPresenter = SubCategoryPresenter(this, context)
        Log.e("cat id", categoryId)
        subCategoryPresenter?.getSubCategoryList(categoryId)
//        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

        if (requestCode == GALLERY) {
            if (data != null) {
                val contentURI = data!!.data
                try {
                    val bitmap =
                        MediaStore.Images.Media.getBitmap(context?.contentResolver, contentURI)
//                    val path = saveImage(bitmap)
//                    Toast.makeText(this@MainActivity, "Image Saved!", Toast.LENGTH_SHORT).show()
//                    imageview!!.setImageBitmap(bitmap)

                    var filePath: String? = null

                    Log.e("gal ima", bitmap.toString())
                    filePathUri = context?.let { getImagePath(it, bitmap) }!!
                    var pathUtil: PathUtil = PathUtil()
                    filePathString = pathUtil?.getPath(context!!, filePathUri!!)
                    Log.e("gal ima path", "" + filePath)

                    file = File(filePathString)
                    Log.e("file", "" + file)
                } catch (e: IOException) {
                    e.printStackTrace()
                    Toast.makeText(context, "Failed!", Toast.LENGTH_SHORT).show()
                }

            }

        } else if (requestCode == CAMERA) {
            try {
                val bitmap = data!!.extras!!.get("data") as Bitmap
                Log.e("cam ima", bitmap.toString())

                var filePath: String? = null
                filePathUri = context?.let { getImagePath(it, bitmap) }!!
                var pathUtil: PathUtil = PathUtil()
                filePathString = pathUtil?.getPath(context!!, filePathUri!!)
                Log.e("cam ima path", filePathString)
                file = File(filePathString)
                Log.e("file", "" + file)
//            imageview!!.setImageBitmap(thumbnail)
//            saveImage(thumbnail)
//            Toast.makeText(this@MainActivity, "Image Saved!", Toast.LENGTH_SHORT).show()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        if (requestCode == PLACE_PICKER_REQUEST) {
            if (resultCode == RESULT_OK) {
                try {
                    var place: com.google.android.gms.location.places.Place? =
                        PlacePicker.getPlace(getActivity(), data)
//                    serviceplacename = String.format("%s", place.getName());
//                    servicelatitude = String.valueOf(place.getLatLng().latitude);
//                    servicelongitude = String.valueOf(place.getLatLng().longitude);
//                    serviceaddress = String.format("%s", place.getAddress());

                    Log.e(
                        "lat lng",
                        "" + place?.getLatLng()?.latitude + " : " + place?.getLatLng()?.longitude
                    )

                } catch (ee: Exception) {
                    println(ee.message);
                }

            }
        } else {
        }

    }

    private fun initialVariables(view: View?) {

        et_seller_name = view?.findViewById(com.uniimarket.app.R.id.et_seller_name)
        et_ti_unii_mail = view?.findViewById(com.uniimarket.app.R.id.et_ti_unii_mail)
        et_ti_date = view?.findViewById(com.uniimarket.app.R.id.et_ti_date)
        et_ti_location = view?.findViewById(com.uniimarket.app.R.id.et_ti_location)
        et_ti_product_name = view?.findViewById(com.uniimarket.app.R.id.et_ti_product_name)
        et_ti_price = view?.findViewById(com.uniimarket.app.R.id.et_ti_price)
        et_ti_discription = view?.findViewById(com.uniimarket.app.R.id.et_ti_discription)
        et_ti_discription = view?.findViewById(com.uniimarket.app.R.id.et_ti_discription)
        rl_category = view?.findViewById(com.uniimarket.app.R.id.rl_category)
        rl_sub_category = view?.findViewById(com.uniimarket.app.R.id.rl_sub_category)
        rl_product_type = view?.findViewById(com.uniimarket.app.R.id.rl_product_type)
        imv_category = view?.findViewById(com.uniimarket.app.R.id.imv_category)
        tv_category = view?.findViewById(com.uniimarket.app.R.id.tv_category)
        tv_sub_category = view?.findViewById(com.uniimarket.app.R.id.tv_sub_category)
        imv_sub_category = view?.findViewById(com.uniimarket.app.R.id.imv_sub_category)
        tv_product_type = view?.findViewById(com.uniimarket.app.R.id.tv_product_type)
        imv_gender = view?.findViewById(com.uniimarket.app.R.id.imv_gender)
        tv_set_price = view?.findViewById(com.uniimarket.app.R.id.tv_set_price)
        imv_price = view?.findViewById(com.uniimarket.app.R.id.imv_price)
        btn_upload_item = view?.findViewById(R.id.btn_upload_item)
        rl_take_picture = view?.findViewById(R.id.rl_take_picture)
        btn_gallery = view?.findViewById(R.id.btn_gallery)

        rl_set_price = view?.findViewById(R.id.rl_set_price)

        // Create persistent LocationManager reference
        locationManager = context?.getSystemService(LOCATION_SERVICE) as LocationManager?;
        sellPresenter = SellPresenter(this, context)

        progressDialog = ProgressDialog(context)
    }

    fun getImagePath(inContext: Context, inImage: Bitmap): Uri? {

        var bytes: ByteArrayOutputStream = ByteArrayOutputStream()
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes)
        var path: String =
            MediaStore.Images.Media.insertImage(inContext.contentResolver, inImage, "Title", null)
//        val values = ContentValues()
//        values.put(MediaStore.Images.Media.TITLE, "Title")
//        values.put(MediaStore.Images.Media.DESCRIPTION, "From Camera")
//        val path = context?.contentResolver?.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)
        Log.e("path", "" + path)

        return Uri.parse(path)
    }

    @SuppressLint("WrongConstant")
    override fun categoriesResponseSuccess(
        status: String,
        message: String?,
        categoryDataList: ArrayList<CategoriesData.Datum>
    ) {

        subCategoryId = ""
        tv_sub_category?.text = "Select Sub Category"

        productType = ""
        tv_product_type?.text = "Select Product Type"

        categoryDialog?.show()
        Log.e("cat size", categoryDataList.size.toString())
        val mng_layout = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        rv_category?.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        rv_category?.adapter =
            SellCategoryAdapter(categoryDataList, context, this, this)

    }

    override fun categoriesResponseFailure(status: String, message: String?) {
        categoryDialog?.hide()
        catTouch = 0
    }

    override fun tokenSuccess(s: String, message: String?) {


    }

//    override fun productResponseSuccess(
//        status: String,
//        message: String?,
//        productDataList: ArrayList<ProductsData.Datum?>
//    ) {
//        catTouch = 0
//        categoryDialog?.hide()
//
//    }

//    override fun productResponseSuccess(
//        status: String,
//        message: String?,
//        productDataList: ArrayList<ProductsData.Datum>
//    ) {
//        catTouch = 0
//        categoryDialog?.hide()
//    }

    override fun productResponseFailure(status: String, message: String?) {
        categoryDialog?.hide()
        catTouch = 0
    }

    @SuppressLint("WrongConstant")
    override fun subcategoriesResponseSuccess(
        status: String,
        message: String?,
        subCategoryDataList: ArrayList<SubCategoriesData.Datum>
    ) {
        val mng_layout: LinearLayoutManager =
            LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        rv_category?.layoutManager = mng_layout
        rv_category?.adapter =
            SellSubCategoryAdapter(subCategoryDataList, context, this, this)

    }

    override fun subcategoriesResponseFailure(status: String, message: String?) {
        subcatTouch = 0
        subCategoryDialog?.hide()
    }

    override fun productResponseSuccess(
        status: String,
        message: String?,
        productDataList: ArrayList<ProductsData.Datum>
    ) {
        catTouch = 0
        categoryDialog?.hide()
    }


    override fun onClickCategory(id: String?, name: String?) {
        catTouch = 0
        categoryDialog?.hide()
        categoryId = id
        tv_category?.text = name

    }

    override fun onClickSubCategory(id: String?, name: String?) {

        productType = ""
        tv_product_type?.text = "Select Product Type"

        subcatTouch = 0
        subCategoryDialog?.hide()
        subCategoryId = id
        tv_sub_category?.text = name

    }

    override fun sellResponse(status: String, message: String) {
        Log.e("sell res", message)
        Toast.makeText(context, message, Toast.LENGTH_LONG).show()
        progressDialog?.dismiss()
        if (status == "true") {

//            Helper.post = "posted"
//            (context as DashboardActivity).replaceFragment(
//                R.id.frame_layout,
//                PurchasedFragment(), true, false)
            (context as DashboardActivity).replaceFragment(
                com.uniimarket.app.R.id.frame_layout,
                HomeFragment(), true, false
            )
            Helper.edit = ""
//            Helper.pointLongitude = 0.0
//            Helper.pointLatitude = 0.0
        }

    }

    @SuppressLint("WrongConstant")
    override fun filterResponseSuccess(
        status: String,
        message: String?,
        filterDataList: ArrayList<FilterData.Datum>
    ) {
        val mng_layout: LinearLayoutManager =
            LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        rv_category?.layoutManager = mng_layout
//        rv_category?.adapter =
//            FilterAdapter(filterDataList, context,  this)


        rv_category?.adapter =
            SellFilterAdapter(filterDataList, context, this)


    }

    override fun filterResponseFailure(status: String, message: String?) {

    }

    override fun onClickFilter(id: String?, name: String?) {
        genderDialog?.hide()
        productTouch = 0
        productType = id
        tv_product_type?.text = name

        tv_product_type?.setText(name + "")


        Log.e("filter", name + " " + id)


    }
}
