package com.example.starzplayassignment.utilities

import android.app.AlertDialog
import android.content.Context
import android.content.Context.CONNECTIVITY_SERVICE
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.ConnectivityManager
import android.os.Build
import android.widget.ImageView
import androidx.annotation.RequiresApi
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterInside
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.example.starzplayassignment.R

fun checkNetworkConnectivity(context: Context): Boolean {
    val cm = context.getSystemService(CONNECTIVITY_SERVICE) as ConnectivityManager
    val netInfo = cm.activeNetworkInfo
    return netInfo != null && netInfo.isConnectedOrConnecting && (netInfo.type == ConnectivityManager.TYPE_MOBILE || netInfo.type == ConnectivityManager.TYPE_WIFI)
}

var dialog: AlertDialog? = null

@RequiresApi(Build.VERSION_CODES.LOLLIPOP)
fun showProgressDialog(context: Context) {
    if (dialog == null) {
        val builder = AlertDialog.Builder(context)
        builder.setCancelable(false) // if you want user to wait for some process to finish,

        builder.setView(R.layout.progress_dialog)
        dialog = builder.create()
        dialog?.setCanceledOnTouchOutside(false)
        dialog?.setCancelable(false)
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
    }

    dialog?.show()
}

fun hideProgressDialog() {
    if (dialog != null) {
        dialog?.dismiss()
        dialog = null
    }
}

fun loadImage(context: Context, url: String, imageView: ImageView) {
    Glide.with(context)
        .load(url)
        .placeholder(R.drawable.no_image)
        .apply(
            RequestOptions().transform(CenterInside(), RoundedCorners(12))
        )
        .into(imageView)
}
