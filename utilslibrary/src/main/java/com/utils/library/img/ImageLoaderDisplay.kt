package com.utils.library.img

import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.MultiTransformation
import com.bumptech.glide.load.Transformation
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.load.resource.gif.GifDrawable
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.utils.library.utils.isNotEmptyStr
import jp.wasabeef.glide.transformations.RoundedCornersTransformation
import java.io.File


/**
 *
 *CenterCrop
 *FitCenter
 *CircleCrop新增圆形 直接将图片切成圆的
 *
 * .transform(MultiTransformation(transformations))//加多个transformation需要使用MultiTransformation
 *
 *
 */
class ImageLoaderDisplay {
    companion object INSTANCE {
        /**
         *渐变效果
         */
        private val drawableTransitionOptions = DrawableTransitionOptions().crossFade(300)

        fun clearMemory(context: Context) {
            GlideApp.get(context).clearMemory()
        }

        fun clearDiskCache(context: Context) {
            GlideApp.get(context).clearDiskCache()
        }

        fun cancleRequest(context: Context) {
            Glide.with(context).pauseRequests()
        }

        fun resumeRequests(context: Context) {
            Glide.with(context).resumeRequests()
        }

        /**
         * 通用加载本地基础设置
         */
        fun getGlideLocal(context: Context, imgFile: File): GlideRequest<Drawable> {
            return GlideApp.with(context)
                    .load(imgFile)

                    .diskCacheStrategy(DiskCacheStrategy.NONE)//因为本身就是本地图片所以不需要磁盘缓存
//                    .skipMemoryCache(true)//跳过内存缓存使用磁盘缓存默认false
//                    .priority(Priority.HIGH)
        }

        fun getGlideLocal(context: Context, resId: Int): GlideRequest<Drawable> {
            return GlideApp.with(context)
                    .load(resId)

                    .diskCacheStrategy(DiskCacheStrategy.NONE)//因为本身就是本地图片所以不需要磁盘缓存
//                    .skipMemoryCache(true)//跳过内存缓存使用磁盘缓存默认false
//                    .priority(Priority.HIGH)
        }

        /**
         * 通用加载本地基础设置
         */
        fun getGlideLocalAsGif(context: Context, imgFile: File): GlideRequest<GifDrawable> {
            return GlideApp.with(context)
                    .asGif()
                    .load(imgFile)
                    .diskCacheStrategy(DiskCacheStrategy.NONE)//因为本身就是本地图片所以不需要磁盘缓存
//                    .skipMemoryCache(true)//跳过内存缓存使用磁盘缓存默认false
//                    .priority(Priority.HIGH)
        }

        /**
         * 通用加载网络基础设置
         */
        fun getGlideOnLine(context: Context, imgUrl: String): GlideRequest<Drawable> {
            return GlideApp.with(context)
                    .load(imgUrl)
                    .dontAnimate()
                    .diskCacheStrategy(DiskCacheStrategy.ALL)//让Glide既缓存全尺寸图片，下次在任何ImageView中加载图片的时候，全尺寸的图片将从缓存中取出，重新调整大小，然后缓存
//                    .skipMemoryCache(true)//跳过内存缓存使用磁盘缓存默认false
//                    .priority(Priority.HIGH)
        }

        /**
         * 通用加载网络基础设置
         */
        fun getGlideOnLineAsGif(context: Context, imgUrl: String): GlideRequest<GifDrawable> {
            return GlideApp.with(context)
                    .asGif()
                    .load(imgUrl)
//                    .dontAnimate()gif不能加这个方法否则会失败
                    .diskCacheStrategy(DiskCacheStrategy.ALL)//让Glide既缓存全尺寸图片，下次在任何ImageView中加载图片的时候，全尺寸的图片将从缓存中取出，重新调整大小，然后缓存
//                    .skipMemoryCache(true)//跳过内存缓存使用磁盘缓存默认false
//                    .priority(Priority.HIGH)
        }

        /**
         * 直接加载图片
         *
         */
        fun imageLoader(img: ImageView,
                        imgUrl: String = "",
                        file: File? = null,
                        Request: GlideRequest<Drawable>? = null,
                        defResourceId: Int = 0,
                        errorResourceId: Int = 0,
                        transformations: Transformation<Bitmap>? = null,
                        multiTransformation: MultiTransformation<Bitmap>? = null,
                        requestListener: RequestListener<Drawable>? = null,
                        width: Int = 0, height: Int = 0): Target<Drawable>? {

            var glideRequest = Request

            //为空也使用加载网络默认配置
            if (glideRequest == null && imgUrl.isNotEmptyStr()) {
                glideRequest = getGlideOnLine(img.context, imgUrl)

            } else if (glideRequest == null && file != null) {//加载本地图片配置
                glideRequest = getGlideLocal(img.context, file)

            } else if (glideRequest == null && defResourceId != 0) { //不加载网络图片，也不加载本地图片，默认加载占位图
                glideRequest = getGlideLocal(img.context, defResourceId)
            }
            if (glideRequest != null) {
                //愉快的加载吧
                return loaderImg(glideRequest, defResourceId, errorResourceId, transformations, multiTransformation,
                        requestListener, width, height).into(img)
            }
            return null
        }

        fun imageloderGif(img: ImageView,
                          imgUrl: String = "",
                          file: File? = null,
                          Request: GlideRequest<GifDrawable>? = null,
                          defResourceId: Int = 0,
                          errorResourceId: Int = 0,
                          transformations: Transformation<Bitmap>? = null,
                          multiTransformation: MultiTransformation<Bitmap>? = null,
                          requestListener: RequestListener<GifDrawable>? = null,
                          width: Int = 0, height: Int = 0): Target<GifDrawable>? {

            var glideRequest = Request

            //为空也使用加载网络默认配置
            if (glideRequest == null && imgUrl.isNotEmptyStr()) {
                glideRequest = getGlideOnLineAsGif(img.context, imgUrl)

            } else if (glideRequest == null && file != null) {//加载本地图片配置
                glideRequest = getGlideLocalAsGif(img.context, file)
            }
            if (glideRequest != null) {
                //愉快的加载吧
                return loaderImg(glideRequest, defResourceId, errorResourceId, transformations, multiTransformation,
                        requestListener, width, height).into(img)
            }
            return null
        }

        /**
         * glide配置添加
         */
        private fun <T> loaderImg(glideRequest: GlideRequest<T>,
                                  defResourceId: Int = 0,
                                  errorResourceId: Int = 0,
                                  transformations: Transformation<Bitmap>? = null,
                                  multiTransformation: MultiTransformation<Bitmap>? = null,
                                  requestListener: RequestListener<T>? = null,
                                  width: Int = 0, height: Int = 0): GlideRequest<T> {

            val IglideRequest = glideRequest

            //设置占位图
            if (defResourceId > 0) {
                IglideRequest.placeholder(defResourceId)
            }
            //设置错误图片
            if (errorResourceId > 0) {
                IglideRequest.error(errorResourceId)
            }

            //设置图片效果
            if (transformations != null) {
                IglideRequest.transform(transformations)
            } else if (multiTransformation != null) {
                IglideRequest.transform(multiTransformation)
            }

            //设置加载后宽高
            if (width > 0 && height > 0) {
                IglideRequest.override(width, height)
            }
            //设置下载监听
            IglideRequest.listener(requestListener)

            return IglideRequest
        }

        /**
         * 圆角图片Transformation
         */
        fun getRoundTransformation(): Transformation<Bitmap> {
            return RoundedCornersTransformation(30, 0, RoundedCornersTransformation.CornerType.ALL)
        }
    }

    enum class GlideLoader(val any: Int) {
        ON_LINE(0),
        LOCAL(1)
    }
}
