package com.uniimarket.app

import android.content.Context
import android.widget.ImageView
import com.squareup.picasso.Picasso
import ss.com.bannerslider.ImageLoadingService

//import com.squareup.picasso.Picasso
//import ss.com.bannerslider.ImageLoadingService

class PicassoImageLoadingService(context: Context?) : ImageLoadingService {
//    (context: Context)
//    :
//    ImageLoadingService {
//
    var context : Context

    init {
        this.context = context!!
    }
    override fun loadImage(url: String?, imageView: ImageView?) {

        val picasso = Picasso.get()
        picasso.load(url)
            .into(imageView)
//        Picasso.with(context).load(url).into(imageView)
    }

    override fun loadImage(resource: Int, imageView: ImageView?) {

//        Picasso.with(context).load(resource).into(imageView)
        val picasso = Picasso.get()
        picasso.load(resource)
            .into(imageView)
    }

    override fun loadImage(url: String?, placeHolder: Int, errorDrawable: Int, imageView: ImageView?) {
        val picasso = Picasso.get()
        picasso.load(url)
            .into(imageView)
//        Picasso.with(context).load(url).placeholder(placeHolder).error(errorDrawable).into(imageView);
    }

}
