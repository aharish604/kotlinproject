package com.uniimarket.app

import com.uniimarket.app.SellerProfile.model.SellerProfileData
import com.uniimarket.app.categories.model.CategoriesTypeData
import com.uniimarket.app.categories.model.Datum
import com.uniimarket.app.home.model.CategoriesData
import com.uniimarket.app.home.model.ProductsData
import com.uniimarket.app.more.favourites.model.MyFavouritesData
import com.uniimarket.app.more.posts.model.PurchaseData

class Helper {

    companion object {
        var chatStop: Boolean = false
        var productSeller: String = ""
        lateinit var sellerProductView: SellerProfileData.Datum
        var homeScreen: String = ""
        var friendId: String = ""
        var searchProduct: String = ""
        var edit: String = ""
        lateinit var purchaseProductData: PurchaseData.Datum
        var longitude: Double = 0.0
        var latitude: Double = 0.0
        var pointLongitude: Double = 0.0
        var pointLatitude: Double = 0.0
        lateinit var categoriesData: CategoriesData.Datum
        lateinit var productData: CategoriesTypeData.Datum
        lateinit var productFavouriteData: MyFavouritesData.Datum
        //        lateinit var filterProductData: CategoriesTypeData.Datum
        lateinit var sliderProductData: ProductsData.Datum
        var cid = "0"
        var scid = "0"
        //        lateinit var bannerData: CategoriesTypeData.Datum
        var searchProductData: ArrayList<CategoriesTypeData.Datum> = ArrayList()
        var post = ""
        var fbToken = ""

        lateinit var productDatum: Datum

    }

}