package com.bolo.bolomap.utils

import android.content.Context
import android.graphics.*

import android.net.Uri
import android.provider.MediaStore
import android.util.Base64
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.DrawableRes
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.bolo.bolomap.R
import com.bumptech.glide.GenericTransitionOptions
import com.bumptech.glide.Glide
import com.bumptech.glide.annotation.GlideModule
import com.bumptech.glide.request.RequestOptions
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import java.io.ByteArrayOutputStream
import java.io.File
import java.lang.Exception
import kotlin.math.abs
import kotlin.random.Random


/**
 * Created by Yvan RAJAONARIVONY on 18/07/2018
 * Copyright (c) 2018 Nouvonivo
 */
@GlideModule
class ImageUtils {
    // Get an image from smartphone, and convert it in 64 bits, to send it to GraphQl in 64 format
    // and "suppose" to check if it's > 1mb

    companion object {


        fun getRoundedCornerBitmap(bitmap: Bitmap, roundPixelSize: Int): Bitmap {
            val output = Bitmap.createBitmap(bitmap.width, bitmap.height, Bitmap.Config.ARGB_8888)
            val canvas = Canvas(output)
            val paint = Paint()
            val rect = Rect(0, 0, bitmap.width, bitmap.height)
            val rectF = RectF(rect)
            val roundPx = roundPixelSize.toFloat()
            paint.isAntiAlias = true
            canvas.drawRoundRect(rectF, roundPx, roundPx, paint)
            paint.xfermode = PorterDuffXfermode(PorterDuff.Mode.SRC_IN) as Xfermode?
            canvas.drawBitmap(bitmap, rect, rect, paint)
            return output
        }

        private fun getRoundedCroppedBitmap(bitmap: Bitmap): Bitmap {
            val widthLight = bitmap.width
            val heightLight = bitmap.height

            val output = Bitmap.createBitmap(bitmap.width, bitmap.height, Bitmap.Config.ARGB_8888)

            val canvas = Canvas(output)
            val paintColor = Paint()
            paintColor.flags = Paint.ANTI_ALIAS_FLAG

            val rectF = RectF(Rect(0, 0, widthLight, heightLight))

            canvas.drawRoundRect(rectF, (widthLight / 2).toFloat(), (heightLight / 2).toFloat(), paintColor)

            val paintImage = Paint()
            paintImage.xfermode = PorterDuffXfermode(PorterDuff.Mode.SRC_ATOP)
            canvas.drawBitmap(bitmap, 0F, 0F, paintImage)

            return output
        }
        fun loadImageBase(image: String?, holderResId: Int, imageView: ImageView) {
            image?.let {
                if (!it.isEmpty()){
                    val bitmap = convert(it)
                    Glide.with(imageView).load(bitmap).apply(RequestOptions().placeholder(holderResId).error(holderResId)).into(imageView)
            }}
        }

        fun loadImage(imageRef: String?, holderResId: Int, imageView: ImageView) {
            Glide.with(imageView).load(imageRef).apply(RequestOptions().placeholder(holderResId)).into(imageView)
        }


        fun loadImage(uri: Uri?, holderResId: Int, imageView: ImageView) {
            Glide.with(imageView).load(uri).apply(RequestOptions().placeholder(holderResId)).into(imageView)
        }

        fun loadRoundImage(imageRef: String?, holderResId: Int, imageView: ImageView) {
            Glide.with(imageView).load(imageRef).apply(RequestOptions().circleCrop().placeholder(holderResId)).into(imageView)
        }

        fun loadImageResize(uri: String?, holderResId: Int, imageView: ImageView, context:Context) {
            Glide.with(context).load(uri).apply(RequestOptions().centerCrop().placeholder(holderResId)).into(imageView)
        }

        fun loadImageResizeLocal(uri: Uri, holderResId: Int, imageView: ImageView, context:Context) {
            try{
                val file = File(Uri.decode(uri.path))
                Glide.with(context).load(file).apply(RequestOptions().centerCrop().placeholder(holderResId)).into(imageView)
            }
            catch (e:Exception){
                Log.i("tryhard",e.toString())
            }
        }

        fun loadImageResource(fragment: Fragment, holderResId: Int, imageView: ImageView) {
            Glide.with(fragment).load(holderResId).apply(RequestOptions().centerCrop()).into(imageView)
        }

        fun loadDiapo(url: String?, imageView: ImageView, transitionId: Int) {
            Glide.with(imageView)
                    .load(url)
                    .transition(GenericTransitionOptions.with(transitionId))
                    .apply(RequestOptions()
                            .centerCrop())
                    .into(imageView)
        }


        @Throws(IllegalArgumentException::class)
        fun convert(base64Str: String?): Bitmap? {
            val decodedBytes = Base64.decode(
                    base64Str?.substring(base64Str.indexOf(",") + 1),
                    Base64.DEFAULT
            )
            return if (decodedBytes.isNotEmpty())
                BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.size)
            else
                null
        }

        fun convert(bitmap: Bitmap): String {
            val outputStream = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream)
            return Base64.encodeToString(outputStream.toByteArray(), Base64.DEFAULT)
        }


        fun getImageUri(inContext: Context, inImage:Bitmap): Uri? {
            val bytes = ByteArrayOutputStream()
            inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes)
            val path = MediaStore.Images.Media.insertImage(inContext.contentResolver, inImage, "pic" + Random.nextInt().toString(), null)
            return Uri.parse(path)
        }

        fun getRealPathFromURI(uri:Uri,context: Context): String {
            var path = ""
            if (context.contentResolver != null) {
                val cursor = context.contentResolver!!.query(uri, null, null, null, null)
                if (cursor != null) {
                    cursor.moveToFirst()
                    val idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA)
                    path = cursor.getString(idx)
                    cursor.close()
                }
            }

            return path
        }

        fun bitmapDescriptorFromVector(context: Context, @DrawableRes vectorDrawableResourceId: Int): BitmapDescriptor {
            val background = ContextCompat.getDrawable(context, R.drawable.ic_notifications_black_24dp)
            background!!.setBounds(0, 0, background.intrinsicWidth, background.intrinsicHeight)
            val vectorDrawable = ContextCompat.getDrawable(context, vectorDrawableResourceId)
            vectorDrawable!!.setBounds(
                40,
                20,
                vectorDrawable.intrinsicWidth + 40,
                vectorDrawable.intrinsicHeight + 20
            )
            val bitmap = Bitmap.createBitmap(
                background.intrinsicWidth,
                background.intrinsicHeight,
                Bitmap.Config.ARGB_8888
            )
            val canvas = Canvas(bitmap)
            background.draw(canvas)
            vectorDrawable.draw(canvas)
            return BitmapDescriptorFactory.fromBitmap(bitmap)
        }

    }



}