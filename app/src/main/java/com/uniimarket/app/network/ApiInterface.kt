package com.uniimarket.app.network

import com.uniimarket.app.ChangePassword.model.ChangePasswordData
import com.uniimarket.app.Dashboard.model.ComingSoonData
import com.uniimarket.app.Forgotpassword.model.ForgotEmailData
import com.uniimarket.app.Forgotpassword.model.ForgotPasswordData
import com.uniimarket.app.ScreenData
import com.uniimarket.app.SellerProfile.model.NotificationRequestList
import com.uniimarket.app.SellerProfile.model.SellerNotificationStatusData
import com.uniimarket.app.SellerProfile.model.SellerProfileData
import com.uniimarket.app.SellerProfile.model.SellerUpdateNotificationsData
import com.uniimarket.app.Signin.model.SignInData
import com.uniimarket.app.Signup.model.SignupData
import com.uniimarket.app.categories.model.AddFavouritesData
import com.uniimarket.app.categories.model.CategoriesTypeData
import com.uniimarket.app.categories.model.SubCategoriesData
import com.uniimarket.app.chat.model.ChatContactsData
import com.uniimarket.app.chat.model.ChatSendData
import com.uniimarket.app.chat.model.ChatViewData
import com.uniimarket.app.chat.model.UserInfoData
import com.uniimarket.app.home.model.CategoriesData
import com.uniimarket.app.home.model.NewPostFilterData
import com.uniimarket.app.home.model.ProductsData
import com.uniimarket.app.home.model.TokenUpdateData
import com.uniimarket.app.more.About.model.AboutData
import com.uniimarket.app.more.contact.model.ContactUsData
import com.uniimarket.app.more.favourites.model.CheckFavourites
import com.uniimarket.app.more.favourites.model.MyFavouritesData
import com.uniimarket.app.more.posts.model.AllUsersFriendData
import com.uniimarket.app.more.posts.model.ProductDeleteData
import com.uniimarket.app.more.posts.model.PurchaseData
import com.uniimarket.app.more.profile.model.FullProfileData
import com.uniimarket.app.more.profile.model.ProfilePicData
import com.uniimarket.app.more.settings.model.SettingsNotificationStatus
import com.uniimarket.app.more.settings.model.SubscribeCategoryData
import com.uniimarket.app.notification.modal.NotificationData
import com.uniimarket.app.notification.modal.UpdateNotificationStatusData
import com.uniimarket.app.sell.model.FilterData
import com.uniimarket.app.sell.model.ProductSellUpdate
import com.uniimarket.app.sell.model.SellData
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.*


interface ApiInterface {

    @FormUrlEncoded
    @POST("index.php/Register/register")
    fun getSignUpResponse(
        @Field("firstname") firstname: String,
        @Field("lastname") lastname: String,
        @Field("email") email: String,
        @Field("password") password: String,
        @Field("device_type") device_type: String,
        @Field("device_token") device_token: String
    ): Call<SignupData>

    @FormUrlEncoded
    @POST("index.php/Register/login")
    fun getSignInResponse(
        @Field("email") email: String,
        @Field("password") password: String,
        @Field("device_type") device_type: String,
        @Field("device_token") device_token: String
    ): Call<SignInData>

    @FormUrlEncoded
    @POST("index.php/Register/Forget_password")
    fun getForgotMailResponse(@Field("email") mail: String): Call<ForgotEmailData>

    @GET("index.php/Sale/get_categories")
    fun getCategories(): Call<CategoriesData>

    @POST("index.php/Sale/get_products")
    fun getSlideProducts(): Call<ProductsData>

    @FormUrlEncoded
    @POST("index.php/Sale/banners_products")
    fun getSlideBanners(@Field("uid") uid: String): Call<ProductsData>


    @FormUrlEncoded
    @POST("index.php/Sale/get_sub_categories")
    fun getSubCategories(@Field("cid") cid: String): Call<SubCategoriesData>

    @Multipart
    @POST("index.php/Sale/sale_product")
    fun updateSell(
        @Part("name") sellerName: RequestBody,
        @Part("uniemail") uniiMail: RequestBody,
        @Part("date") date: RequestBody,
        @Part("latitude") lat: RequestBody,
        @Part("longitude") longitude: RequestBody,
        @Part("product_name") productName: RequestBody,
        @Part("price") price: RequestBody,
        @Part("description") description: RequestBody,
        @Part("cid") catId: RequestBody,
        @Part("scid") subCategoryId: RequestBody,
        @Part("product_type") productType: RequestBody,
        @Part("setprice") setPrice: RequestBody,
        @Part("uid") uid: RequestBody,
        @Part("location") location: RequestBody,
        @Part product_pics: MultipartBody.Part?
    ): Call<SellData>

    @FormUrlEncoded
    @POST("index.php/Register/profile_update")
    fun editProfile(
        @Field("firstname") firstName: String,
        @Field("lastname") lastName: String,
        @Field("age") age: String,
        @Field("university") university: String,
        @Field("course") course: String,
        @Field("phone") phone: String,
        @Field("adress") address: String,
        @Field("id") id: String,
        @Field("university_year") university_year: String,
        @Field("unii_email") unii_email: String,
        @Field("bio") bio: String
    ): Call<FullProfileData>

    @FormUrlEncoded
    @POST("index.php/Sale/get_filetr_list")
    fun getFilters(@Field("scid") scid: String): Call<FilterData>


    @FormUrlEncoded
    @POST("index.php/Notifications/notfication_list")
    fun getNotificationList(
        @Field("user_id") user_id: String
    ): Call<NotificationData>

    @FormUrlEncoded
    @POST("index.php/Settings/my_favourites_list")
    fun getFavouritesList(
        @Field("uid") uid: String
    ): Call<MyFavouritesData>


    @FormUrlEncoded
    @POST("index.php/Search/sorting_products")
    fun getSortingCategories(
        @Field("keyvalue") sortingValue: String?,
        @Field("cid") categoryId: String?,
        @Field("scid") subCategoryId: String?,
        @Field("lat") latitude: Double,
        @Field("log") longitude: Double,
        @Field("distance") distance: String?,
        @Field("uid") uid: String?
    ): Call<CategoriesTypeData>


    @FormUrlEncoded
    @POST("index.php/Settings/contact_us")
    fun getContactUs(
        @Field("name") name: String?,
        @Field("email") email: String?,
        @Field("univercity") univercity: String?,
        @Field("cource") cource: String,
        @Field("query") query: String,
        @Field("uid") uId: String
    ): Call<ContactUsData>

    @FormUrlEncoded
    @POST("index.php/Settings/my_favourites_add")
    fun addFavourites(@Field("pid") pId: String?, @Field("uid") uId: String): Call<AddFavouritesData>

    @FormUrlEncoded
    @POST("index.php/Register/profile_info")
    fun getSellerProfile(@Field("uid") uId: String?): Call<SellerProfileData>

    @FormUrlEncoded
    @POST("index.php/Sale/get_category_products_list")
    fun getFilterProducts(
        @Field("cid") cid: String,
        @Field("sid") sid: String,
        @Field("product_type") product_type: String,
        @Field("uid") uid: String
    ): Call<CategoriesTypeData>

    @Multipart
    @POST("index.php/Register/profile_photo_update")
    fun updateProfilePic(@Part("id") id: RequestBody, @Part product_pics: MultipartBody.Part?): Call<ProfilePicData>

    @FormUrlEncoded
    @POST("index.php/Sale/sale_products")
    fun getPurchaseData(@Field("uid") uid: String, @Field("status") status: String): Call<PurchaseData>

    @FormUrlEncoded
    @POST("index.php/Sale/sold_products")
    fun getPostedData(@Field("uid") uid: String, @Field("status") status: String): Call<PurchaseData>

//    @FormUrlEncoded
//    @POST("index.php/Sale/sale_products")
//    fun getSoldData(@Field("uid") uid: String, @Field("status") status: String): Call<PurchaseData>

    @FormUrlEncoded
    @POST("index.php/Sale/sold_products")
    fun getSoldData(@Field("uid") uid: String, @Field("status") status: String): Call<PurchaseData>

    @FormUrlEncoded
    @POST("index.php/Sale/product_delete")
    fun getDeleteProduct(@Field("id") id: String?, @Field("uid") uid: String): Call<ProductDeleteData>

    @FormUrlEncoded
    @POST("index.php/Sale/product_status_update")
    fun productStatusUpdate(
        @Field("id") id: String?,
        @Field("uid") uid: String,
        @Field("product_status") sale: String,
        @Field("purchase_from") friendId: String
    ): Call<ProductDeleteData>

    @FormUrlEncoded
    @POST("index.php/Sale/get_latest_products")
    fun getBannerProducts(
        @Field("cid") cid: String?,
        @Field("scid") scid: String?,
        @Field("uid") uid: String?
    ): Call<CategoriesTypeData>

    @FormUrlEncoded
    @POST("index.php/Register/change_password")
    fun changePassword(
        @Field("email") email: String?,
        @Field("curentpassword") pswd: String,
        @Field("password") newPswd: String,
        @Field("cpassword") cNewPaswd: String
    ): Call<ChangePasswordData>

    @FormUrlEncoded
    @POST("index.php/Register/check_otp")
    fun getForgotPswdResponse(
        @Field("email") email: String,
        @Field("password") pswd: String,
        @Field("cpassword") cnfPswd: String,
        @Field("vcode") vCode: String
    ): Call<ForgotPasswordData>

    @FormUrlEncoded
    @POST("index.php/Search/search_product")
    fun searchProduct(
        @Field("keyvalue") productSearch: String,
        @Field("uid") uid: String
    ): Call<CategoriesTypeData>

    @FormUrlEncoded
    @POST("index.php/Notifications/nottfication_status")
    fun getNotificationStatus(
        @Field("uid") id: String,
        @Field("friend_id") uid: String,
        @Field("type") type: String
    ): Call<SellerNotificationStatusData>

    @FormUrlEncoded
    @POST("index.php/Notifications/notfications_off_or_on")
    fun getUpdateNotificationStatus(
        @Field("uid") uid: String,
        @Field("friend_id") friend_id: String,
        @Field("cid") cid: String?,
        @Field("scid") scid: String?,
        @Field("type") type: String
    ): Call<SellerUpdateNotificationsData>

    @FormUrlEncoded
    @POST("index.php/Notifications/notfications_off_or_on")
    fun getUpdateNotificationStatusUser(
        @Field("uid") uid: String,
        @Field("friend_id") friend_id: String,
        @Field("cid") cid: String?,
        @Field("scid") scid: String?,
        @Field("type") type: String
    ): Call<SellerUpdateNotificationsData>

    @FormUrlEncoded
    @POST("index.php/Notifications/notfication_request_sent")
    fun sendRequestList(
        @Field("uid") uid: String,
        @Field("friend_id") friend_id: String
    ): Call<NotificationRequestList>

    @FormUrlEncoded
    @POST("index.php/Notifications/notfication_request_status_update")
    fun updateNotificationStatus(
        @Field("uid") uid: String,
        @Field("friend_id") friend_id: String?,
        @Field("status") status: String
    ): Call<UpdateNotificationStatusData>

    @FormUrlEncoded
    @POST("index.php/Chat/get_friend_list")
    fun getChatContactsList(@Field("uid") uid: String): Call<ChatContactsData>

    @FormUrlEncoded
    @POST("index.php/Chat/chat_history")
    fun getChatHistoryList(
        @Field("from_id") id: String,
        @Field("to_id") friendId: String?
    ): Call<ChatViewData>

    @FormUrlEncoded
    @POST("index.php/Chat/send")
    fun sendText(
        @Field("from_id") id: String,
        @Field("to_id") friendId: String?,
        @Field("message") text: String
    ): Call<ChatSendData>

    @FormUrlEncoded
    @POST("index.php/Settings/check_favourites")
    fun checkFavourites(
        @Field("pid") pid: String?,
        @Field("uid") id: String
    ): Call<CheckFavourites>

    @FormUrlEncoded
    @POST("index.php/Sale/product_update")
    fun updateProductSell(
        @Field("product_status") productStatus: String,
        @Field("product_name") productName: String,
        @Field("product_type") productType: String?,
        @Field("cid") categoryId: String?,
        @Field("scid") subCategoryId: String?,
        @Field("description") description: String,
        @Field("price") price: String,
        @Field("setprice") setprice: String?,
        @Field("id") productId: String?,
        @Field("date") date: String,
        @Field("latitude") pointLatitude: Double,
        @Field("longitude") pointLongitude: Double,
        @Field("uid") uid: String,
        @Field("location") location: String
    ): Call<ProductSellUpdate>

    @Multipart
    @POST("index.php/Sale/product_update")
    fun updateProductSellWithImage(
        @Part("product_status") productStatus: RequestBody?,
        @Part("product_name") productName: RequestBody?,
        @Part("product_type") productType: RequestBody?,
        @Part("cid") categoryId: RequestBody?,
        @Part("scid") subCategoryId: RequestBody?,
        @Part("description") description: RequestBody?,
        @Part("price") price: RequestBody?,
        @Part("setprice") setPrice: RequestBody?,
        @Part("id") productId: RequestBody?,
        @Part("date") date: RequestBody?,
        @Part("latitude") pointLatitude: RequestBody?,
        @Part("longitude") pointLongitude: RequestBody?,
        @Part("uid") uid: RequestBody?,
        @Part product_pics: MultipartBody.Part?,
        @Part("location") location: RequestBody?
    ): Call<ProductSellUpdate>


    @FormUrlEncoded
    @POST("index.php/Notifications/get_user_notfications")
    fun getSettingsNotificationStatus(
        @Field("user_id") uid: String
    ): Call<SettingsNotificationStatus>

    @FormUrlEncoded
    @POST("index.php/Notifications/notfications_on")
    fun updateSettingsNotificationStatus(
        @Field("user_id") uid: String,
        @Field("all_notifications") showNotification: String,
        @Field("app_notifications") appNotification: String
    ): Call<SettingsNotificationStatus>

    @FormUrlEncoded
    @POST("index.php/Register/user_info")
    fun getUserInfo(
        @Field("uid") uid: String?
    ): Call<UserInfoData>

    @FormUrlEncoded
    @POST("index.php/Search/search_users")
    fun getAllContactsList(@Field("keyvalue") keyvalue: String, @Field("uid") id: String): Call<AllUsersFriendData>


//    fun getContactUs(userName: String, uniiMail: String, university: String, course: String, query: String): Any

    @GET("index.php/Settings/screens_pages")
    fun getIntro(): Call<ScreenData>

    @GET("index.php/Settings/comming_soon")
    fun getComingSoon(): Call<ComingSoonData>

    @GET("index.php/Settings/about_us")
    fun getAbout(): Call<AboutData>


    @POST("index.php/Sale/getNewPostFilters")
    fun getNewPostFilters(): Call<NewPostFilterData>

    @GET("index.php/Sale/getFilerProducts")
    fun getNewPostFilterProducts(@Query("id") toString: String): Call<CategoriesTypeData>

    @FormUrlEncoded
    @POST("index.php/Settings/getUsersubcribeStatus")
    fun getSubscribedNotifications(@Field("uid") uid: String): Call<SubscribeCategoryData>

    @FormUrlEncoded
    @POST("index.php/Register/TokenUpdate")
    fun updateFBToken(
        @Field("uid") uid: String?,
        @Field("device_type") device_type: String,
        @Field("device_token") device_token: String,
        @Field("device_id") device_id: String,
        @Field("email") email: String?
    ): Call<TokenUpdateData>

}