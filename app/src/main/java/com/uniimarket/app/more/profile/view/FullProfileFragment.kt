package com.uniimarket.app.more.profile.view

import android.app.AlertDialog
import android.app.Dialog
import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment.DIRECTORY_PICTURES
import android.provider.MediaStore
import android.util.Log
import android.view.*
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.squareup.picasso.Picasso
import com.uniimarket.app.Dashboard.view.DashboardActivity
import com.uniimarket.app.R
import com.uniimarket.app.more.MoreFragment
import com.uniimarket.app.more.profile.model.FullProfileData
import com.uniimarket.app.more.profile.model.ProfilePicData
import com.uniimarket.app.more.profile.presenter.FullProfilePresenter
import com.uniimarket.app.more.profile.presenter.ProfilePicPresenter
import com.uniimarket.app.sell.view.PathUtil
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*


class FullProfileFragment : Fragment(), FullProfilePresenter.FullProfileListener,
    ProfilePicPresenter.FullProfilePicListener {


    val PLACE_PICKER_REQUEST: Int = 123
    var et_first_name: EditText? = null
    var et_last_name: EditText? = null
    var et_age: EditText? = null
    var et_university: AutoCompleteTextView? = null
    var et_university_year: EditText? = null
    var et_course: EditText? = null
    var et_unii_email: EditText? = null
    var et_phone: EditText? = null
    var et_address: EditText? = null
    var et_bio: EditText? = null

    var btn_save: Button? = null
    var btn_edit_profile: Button? = null
    var imv_edit_dot: ImageView? = null
    var tv_user_mail: TextView? = null
    var tv_user_name: TextView? = null
    var profile_image: ImageView? = null
    var imv_camera: ImageView? = null
    var fullProfilePresenter: FullProfilePresenter? = null
    var sharedPref: SharedPreferences? = null
    var ll_camera: LinearLayout? = null
    private val GALLERY = 1
    private val CAMERA = 2
    var file: File? = null
    var filePathUri: Uri? = null
    var filePathString: String? = ""
    var ProfilePicPresenter: ProfilePicPresenter? = null
    var progressDialog: ProgressDialog? = null
    var imageFilePath: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activity?.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE or WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // Inflate the layout for this fragment
        val view = inflater?.inflate(R.layout.full_profile_fragment, container, false)
        initialVariables(view)
        (context as DashboardActivity).ll_toolbar?.visibility = View.VISIBLE
        (context as DashboardActivity).ll_back?.visibility = View.VISIBLE

        (context as DashboardActivity).ll_back?.setOnClickListener {
            (context as DashboardActivity).replaceFragment(
                R.id.frame_layout,
                MoreFragment(), true, false
            )
        }


        if (sharedPref?.getString("profile","")=="edited"){
            imv_edit_dot!!.visibility = View.GONE
        } else{
            imv_edit_dot!!.visibility = View.VISIBLE
        }

        et_bio?.setOnTouchListener(object : View.OnTouchListener {
            override fun onTouch(view: View, event: MotionEvent): Boolean {
                // TODO Auto-generated method stub
                if (view.getId() === R.id.et_bio) {
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


        // Initialize a new array with elements
        val universityNames = arrayOf(
            "University of Brighton", "University of Sussex"
        )


        // Initialize a new array adapter object
        val adapter = ArrayAdapter<String>(
            context, // Context
            android.R.layout.simple_dropdown_item_1line, // Layout
            universityNames // Array
        )


        // Set the AutoCompleteTextView adapter
        et_university?.setAdapter(adapter)

        editProfile(false)

        imv_camera?.setOnClickListener {
            picImage()
        }

        ll_camera?.setOnClickListener {
            picImage()
        }

        btn_edit_profile?.setOnClickListener {
            editProfile(true)
            btn_edit_profile!!.visibility = View.GONE
            imv_edit_dot!!.visibility = View.GONE

        }

        btn_save?.setOnClickListener {

            when {
                et_first_name?.text.toString().trim() == "" -> {
                    et_first_name?.error = "Please Enter First Name"
                    Toast.makeText(context, "Please Enter First Name", Toast.LENGTH_LONG).show()
                }
                et_last_name?.text.toString().trim() == "" -> {
                    et_last_name?.error = "Please Enter Last Name"
                    Toast.makeText(context, "Please Enter Last Name", Toast.LENGTH_LONG).show()
                }
//                et_age?.text.toString().trim() == "" || et_age?.text.toString().trim() == "0" -> {
//                    et_age?.error = "Please Enter Age"
//                    Toast.makeText(context, "Please Enter Age", Toast.LENGTH_LONG).show()
//                }

                et_university?.text.toString().trim() == "" -> {
                    et_university?.error = "Please Enter University"
                    Toast.makeText(context, "Please Enter University", Toast.LENGTH_LONG).show()
                }

//                et_university?.text.toString().trim().length >= 20 -> {
//                    Toast.makeText(context, "University max length should be 20", Toast.LENGTH_LONG)
//                        .show()
//                }

//                et_university_year?.text.toString().trim() == "" -> {
//                    et_university_year?.error =
//                        "Please Enter University Year"
//                    Toast.makeText(context, "Please Enter University Year", Toast.LENGTH_LONG).show()
//                }
//
//                et_university_year?.text.toString().trim().length >= 10 -> {
//                    Toast.makeText(context, "University Year max length should be 10", Toast.LENGTH_LONG).show()
//                }

                et_course?.text.toString().trim() == "" -> {
                    et_course?.error = "Please Enter Course"
                    Toast.makeText(context, "Please Enter Course", Toast.LENGTH_LONG).show()
                }

                et_course?.text.toString().trim().length >= 20 -> {
                    Toast.makeText(context, "Course maximum length should be 20", Toast.LENGTH_LONG)
                        .show()
                }

//                et_unii_email?.text.toString().trim() == "" -> {
//                    et_unii_email?.error = "Please Enter Unii Email"
//                    Toast.makeText(context, "Please Enter Unii Email", Toast.LENGTH_LONG).show()
//                }
//                et_phone?.text.toString().trim() == "" -> {
//                    et_phone?.error = "Please Enter Phone Number"
//                    Toast.makeText(context, "Please Enter Phone Number", Toast.LENGTH_LONG).show()
//                }
//                et_phone?.text.toString().trim().length <= 10 || et_phone?.text.toString().trim().length >= 14 -> {
//                    et_phone?.error = "Please Enter 10 digit phone number"
//                    Toast.makeText(context, "Phone number length should be 11 to 13", Toast.LENGTH_LONG).show()
//                }
//                et_address?.text.toString().trim() == "" -> {
//                    et_address?.error = "Please Enter Address"
//                    Toast.makeText(context, "Please Enter Address", Toast.LENGTH_LONG).show()
//                }
//                et_university_year?.text.toString().trim() .equals("0")  -> {
//                    et_university_year?.error = "Year cannot be zero"
//                    Toast.makeText(context, "Year cannot be zero", Toast.LENGTH_LONG).show()
//                }

                else -> {
                    progressDialog?.setMessage("Updating profile...")
                    progressDialog?.setCancelable(false)
                    progressDialog?.show()
                    fullProfilePresenter?.editProfile(
                        et_first_name?.text.toString().trim(),
                        et_last_name?.text.toString().trim(),
                        et_age?.text.toString().trim(),
                        et_university?.text.toString().trim(),
                        et_university_year?.text.toString().trim(),
                        et_course?.text.toString().trim(),
                        et_unii_email?.text.toString().trim(),
                        et_phone?.text.toString().trim(),
                        et_address?.text.toString().trim(),
                        et_bio?.text.toString().trim()
                    )

//                    signinPresenter?.signIn(
//                        et_ti_mail?.text!!.trim().toString(),
//                        et_ti_pswd?.text!!.trim().toString()
//                    )
                }

            }
        }

        return view
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

    private fun editProfile(status: Boolean) {
        et_first_name?.isEnabled = status
        et_last_name?.isEnabled = status
        et_age?.isEnabled = status
        et_university?.isEnabled = status
        et_university_year?.isEnabled = status
        et_course?.isEnabled = status
        et_unii_email?.isEnabled = status
        et_phone?.isEnabled = status
        et_address?.isEnabled = status
        et_bio?.isEnabled = status

        if (status) {
            btn_save?.visibility = View.VISIBLE
        } else {
            btn_save?.visibility = View.GONE
        }

    }


    private fun initialVariables(view: View?) {

        et_first_name = view?.findViewById(R.id.et_first_name)
        et_last_name = view?.findViewById(R.id.et_last_name)
        et_age = view?.findViewById(R.id.et_age)
        et_university = view?.findViewById(R.id.et_university)
        et_university_year = view?.findViewById(R.id.et_university_year)
        et_course = view?.findViewById(R.id.et_course)
        et_unii_email = view?.findViewById(R.id.et_unii_email)
        et_phone = view?.findViewById(R.id.et_phone)
        et_address = view?.findViewById(R.id.et_address)
        et_bio = view?.findViewById(R.id.et_bio)
        btn_save = view?.findViewById(R.id.btn_save)
        btn_edit_profile = view?.findViewById(R.id.btn_edit_profile)
        imv_edit_dot = view?.findViewById(R.id.imv_edit_dot)
        tv_user_mail = view?.findViewById(R.id.tv_user_mail)
        tv_user_name = view?.findViewById(R.id.tv_user_name)
        profile_image = view?.findViewById(R.id.profile_image)
        imv_camera = view?.findViewById(R.id.imv_camera)
        ll_camera = view?.findViewById(R.id.ll_camera)

        fullProfilePresenter = FullProfilePresenter(context, this)
        ProfilePicPresenter = ProfilePicPresenter(context, this)
        sharedPref = context?.getSharedPreferences("uniimarket", 0)
        progressDialog = ProgressDialog(context)

        setDataFromSP()


    }

    private fun setDataFromSP() {
        et_first_name?.setText(sharedPref?.getString("firstname", null))
        et_last_name?.setText(sharedPref?.getString("lastname", null))

        if (sharedPref?.getString("age", null).equals("0")) {
            et_age?.setText("Update your age")
        } else {
            et_age?.setText(sharedPref?.getString("age", null))
        }
        et_university?.setText(sharedPref?.getString("university", null))
        et_university_year?.setText(sharedPref?.getString("university_year", null))
        et_course?.setText(sharedPref?.getString("course", null))
        et_unii_email?.setText(sharedPref?.getString("email", null))
        et_phone?.setText(sharedPref?.getString("phone", null))
        et_address?.setText(sharedPref?.getString("adress", null))
        et_bio?.setText(sharedPref?.getString("bio", null))

        tv_user_mail?.text = sharedPref?.getString("email", null)
        tv_user_name?.text =
            sharedPref?.getString("firstname", null) + " " + sharedPref?.getString("lastname", null)


//        profile_image?.let {
//            Glide
//                .with(context!!)
//                .load(sharedPref?.getString("profilepic", null))
//                .centerCrop()
//                .placeholder(R.drawable.profile)
//                .into(it)
//        }

        val picasso = Picasso.get()
        picasso.load(sharedPref?.getString("profilepic", null))
            .placeholder(R.drawable.profile).into(profile_image)


//        context?.let {
//            profile_image?.let { it1 ->
//                Glide.with(it).load(sharedPref?.getString("profilepic", null))
//                    .placeholder(R.drawable.profile).into(
//                    it1
//                )
//            }

//            Glide.with(context!!)
//                .asBitMap() //[for new glide versions]
//                .load(sharedPref?.getString("profilepic", null)) //.asBitmap()[for older glide versions]
////.placeholder(R.drawable.default_placeholder)
//                .override(1600, 1600) // Can be 2000, 2000
//                .into(object : BitmapImageViewTarget(profile_image) {
//                    fun onResourceReady(
//                        drawable: Bitmap?,
//                        anim: GlideAnimation?
//                    ) {
//                        super.onResourceReady(drawable!!, anim)
////                        progressBar.setVisibility(View.GONE)
//                    }
//                })
//        }

        (context as DashboardActivity).imv_profile?.let {
            Glide.with(this)
                .load(sharedPref?.getString("profilepic", null))
                .placeholder(R.drawable.profile).into(it)
        }


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
                    profile_image?.setImageBitmap(bitmap)
                    file = File(filePathString)
                    Log.e("file", "" + file)

                    profilePicUpdate(file!!)

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
                profile_image?.setImageBitmap(bitmap)
                var pathUtil: PathUtil = PathUtil()
                filePathString = pathUtil?.getPath(context!!, filePathUri!!)
                Log.e("cam ima path", filePathString)
                file = File(filePathString)




                Log.e("file", "" + file)
                var cameraFile: File = createImageFile()
                Log.e("file im path", imageFilePath)
                Log.e("cam file", cameraFile.toString() + "")
                profilePicUpdate(file!!)
//            imageview!!.setImageBitmap(thumbnail)
//            saveImage(thumbnail)
//            Toast.makeText(this@MainActivity, "Image Saved!", Toast.LENGTH_SHORT).show()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    private fun profilePicUpdate(file: File) {
        progressDialog?.setMessage("Updating data...")
        progressDialog?.setCancelable(false)
        progressDialog?.show()
        ProfilePicPresenter?.updateProfilePic(file)

    }

    fun getImagePath(inContext: Context, inImage: Bitmap): Uri? {

        var bytes: ByteArrayOutputStream = ByteArrayOutputStream()
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes)
        var sdf: SimpleDateFormat = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault())
        var currentDateandTime: String = sdf.format(Date())

        var path: String =
            MediaStore.Images.Media.insertImage(
                inContext.contentResolver,
                inImage,
                "unii_" + currentDateandTime,
                null
            )
//        val values = ContentValues()
//        values.put(MediaStore.Images.Media.TITLE, "Title")
//        values.put(MediaStore.Images.Media.DESCRIPTION, "From Camera")
//        val path = context?.contentResolver?.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)
        Log.e("path", "" + path)

        return Uri.parse(path)
    }


    override fun profileResponseSuccess(
        status: String,
        message: String,
        profileList: FullProfileData.Records
    ) {
        try {
            progressDialog?.dismiss()
        } catch (e: Exception) {
            e.printStackTrace()
        }
        editProfile(false)
        btn_edit_profile!!.visibility = View.VISIBLE
        imv_edit_dot!!.visibility = View.GONE



        with(sharedPref!!.edit()) {
            putString("id", profileList.id)
            putString("firstname", profileList.firstname)
            putString("lastname", profileList.lastname)
            putString("age", profileList.age)
            putString("email", profileList.email)
            putString("uniiemail", profileList.uniiEmail)
            putString("password", profileList.password)
            putString("profilepic", profileList.profilepic)
            putString("university", profileList.university)
            putString("university_year", profileList.universityYear)
            putString("course", profileList.course)
            putString("phone", profileList.phone)
            putString("adress", profileList.adress as String?)
            putString("uotp", profileList.uotp)
            putString("device_type", profileList.deviceType as String?)
            putString("device_token", profileList.deviceToken as String?)
            putString("device_id", profileList.deviceId as String?)
            putString("created_at", profileList.createdAt)
            putString("bio", profileList.bio)
            putString("profile", "edited")

            commit()


            val dialog = Dialog(context, android.R.style.Theme_DeviceDefault_Dialog_NoActionBar)
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
            dialog.setCancelable(false)

            dialog.setContentView(R.layout.notificaiton_popup)
            val body = dialog.findViewById(R.id.imv_cancel) as ImageView
            val tv_security_text = dialog.findViewById(R.id.tv_security_text) as TextView

            tv_security_text.text = "Profile updated successfully"

            body.setOnClickListener {
                dialog.dismiss()
            }

            dialog.show()
        }

        setDataFromSP()

    }

    override fun profileResponseFailure(status: String, message: String) {
        try {
            progressDialog?.dismiss()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun profilePicSuccess(
        status: String,
        message: String,
        profileData: ProfilePicData.Records?
    ) {
        try {
            progressDialog?.dismiss()
        } catch (e: Exception) {
            e.printStackTrace()
        }
        Toast.makeText(context, "Image uploaded successfully", Toast.LENGTH_LONG).show()
        with(sharedPref!!.edit()) {
            putString("id", profileData?.id)
            putString("firstname", profileData?.firstname)
            putString("lastname", profileData?.lastname)
            putString("age", profileData?.age)
            putString("email", profileData?.email)
            putString("uniiemail", profileData?.uniiEmail)
            putString("password", profileData?.password)
            putString("profilepic", profileData?.profilepic)
            putString("university", profileData?.university)
            putString("university_year", profileData?.universityYear)
            putString("course", profileData?.course)
            putString("phone", profileData?.phone)
            putString("adress", profileData?.adress as String?)
            putString("uotp", profileData?.uotp)
            putString("device_type", profileData?.deviceType as String?)
            putString("device_token", profileData?.deviceToken as String?)
            putString("device_id", profileData?.deviceId as String?)
            putString("created_at", profileData?.createdAt)

            commit()

        }

        setDataFromSP()

    }

    override fun profilePicFailure(status: String, message: String) {
        if (status == "false") {
            Toast.makeText(context, "Image upload failed, Please try again", Toast.LENGTH_LONG)
                .show()
        }
    }


    @Throws(IOException::class)
    private fun createImageFile(): File {
        val timeStamp = SimpleDateFormat(
            "yyyyMMdd_HHmmss"
        ).format(Date())
        val imageFileName = "IMG_" + timeStamp + "_"
        val storageDir = context?.getExternalFilesDir(DIRECTORY_PICTURES)
        val image = File.createTempFile(
            imageFileName, /* prefix */
            ".jpg" /* suffix */
//            storageDir /* directory */
        )
        imageFilePath = image.absolutePath
        return image
    }
}
