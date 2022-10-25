package com.example.learnkotlin.util

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Rect
import android.net.Uri
import android.os.SystemClock
import android.util.Log
import android.view.View
import android.webkit.MimeTypeMap
import android.widget.CompoundButton
import android.widget.ImageView
import androidx.annotation.StringRes
import androidx.core.content.ContextCompat
import androidx.core.text.HtmlCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.example.learnkotlin.GlideApp
import com.example.learnkotlin.R
import com.example.learnkotlin.util.MESSAGE.STATUS_ERROR
import com.example.learnkotlin.util.MESSAGE.STATUS_SUCCESS
import com.google.android.material.appbar.CollapsingToolbarLayout
import com.google.android.material.snackbar.Snackbar
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStream
import java.io.OutputStream
import android.graphics.BitmapFactory

import android.graphics.Bitmap
import java.net.HttpURLConnection
import java.net.MalformedURLException
import java.net.URL
import cn.pedant.SweetAlert.SweetAlertDialog
import cn.pedant.SweetAlert.SweetAlertDialog.OnSweetClickListener
import com.example.learnkotlin.presentation.home.ui.kuis.fragment.KuisFragment


fun View.setOnClickListenerWithDebounce(debounceTime: Long = 600L, action: () -> Unit) {
    this.setOnClickListener(object : View.OnClickListener {
        private var lastClickTime: Long = 0

        override fun onClick(v: View) {
            if (SystemClock.elapsedRealtime() - lastClickTime < debounceTime) return
            else action()

            lastClickTime = SystemClock.elapsedRealtime()
        }
    })
}

fun snackbar(view: View, message: String, type: String, duration: Int = Snackbar.LENGTH_SHORT) {
    when(type) {
        STATUS_SUCCESS -> {
            Snackbar.make(view, message, duration).apply {
                this.view.setBackgroundColor(ContextCompat.getColor(this.context, R.color.green_color))
            }.show()
        }
        STATUS_ERROR -> {
            Snackbar.make(view, message, duration).apply {
                this.view.setBackgroundColor(ContextCompat.getColor(this.context, R.color.red_color))
            }.show()
        }
    }
}

fun CompoundButton.convertHtml(@SuppressLint("SupportAnnotationUsage") @StringRes text: Int) {
    this.text = HtmlCompat.fromHtml(this.context.getString(text), HtmlCompat.FROM_HTML_MODE_LEGACY)
}

fun ImageView.loadImage(imageUrl: String, cacheStrategy: DiskCacheStrategy = DiskCacheStrategy.NONE) {
    GlideApp.with(this.context)
        .load(imageUrl)
        .override(480, 320)
        .timeout(20000)
        .transition(DrawableTransitionOptions.withCrossFade())
        .diskCacheStrategy(cacheStrategy)
        .error(ContextCompat.getDrawable(this.context, R.drawable.ic_broken_image_black))
        .placeholder(ContextCompat.getDrawable(this.context, R.drawable.ic_image_black))
        .into(this)
}

fun customSuccessDialog(
    context: Context,
) {
    SweetAlertDialog(context, SweetAlertDialog.SUCCESS_TYPE)
        .setTitleText("\uD83C\uDF89 Selamat, jawaban anda Benar!!!")
        .setContentText("Jawaban yang anda masukan benar!!!")
        .setConfirmText("Oke!")
        .setConfirmClickListener { sDialog ->
            sDialog.dismissWithAnimation()
            context.startActivity(Intent(context, KuisFragment::class.java))
        }
        .show()
}

fun customFailureDialog(
    context: Context,
) {
    SweetAlertDialog(context, SweetAlertDialog.ERROR_TYPE)
        .setTitleText("Jawaban Anda salah!!!")
        .setContentText("Jawaban yang anda masukan salah!")
        .setConfirmText("Oke!")
        .setConfirmClickListener { sDialog ->
            sDialog.dismissWithAnimation()
            context.startActivity(Intent(context, KuisFragment::class.java))
        }
        .show()
}

class MarginItemDecorationVertical(private val spaceHeight: Int): RecyclerView.ItemDecoration() {
    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        with(outRect) {
            if (parent.getChildAdapterPosition(view) == 0) {
                top = spaceHeight
            }
            bottom = spaceHeight
        }
    }
}

private fun getFileExtension(context: Context, uri: Uri): String? {
    val fileType: String? = context.contentResolver.getType(uri)
    return MimeTypeMap.getSingleton().getExtensionFromMimeType(fileType)
}

@Throws(IOException::class)
private fun copy(source: InputStream, target: OutputStream) {
    val buf = ByteArray(8192)
    var length: Int
    while (source.read(buf).also { length = it } > 0) {
        target.write(buf, 0, length)
    }
}

fun Context.getFileFromContentUri(contentUri: Uri): File {
    val fileExtension = getFileExtension(this, contentUri)
    val fileName = "temp_file" + if (fileExtension != null) ".$fileExtension" else ""

    val tempFile = File(this.cacheDir, fileName)
    tempFile.createNewFile()

    var oStream: FileOutputStream? = null
    var inputStream: InputStream? = null

    try {
        oStream = FileOutputStream(tempFile)
        inputStream = this.contentResolver.openInputStream(contentUri)

        inputStream?.let { copy(inputStream, oStream) }
        oStream.flush()
    } catch (e: Exception) {
        e.printStackTrace()
    } finally {

        inputStream?.close()
        oStream?.close()
    }

    return tempFile
}

fun at(at: String) = "Bearer $at"

fun Activity.simpleName() = this::class.java.simpleName

fun Fragment.simpleName() = this::class.java.simpleName

fun P_E_M(tag: String, message: String) {
    Log.e(tag, message)
}

fun View.showView() {
    this.visibility = View.VISIBLE
}

fun View.removeView() {
    this.visibility = View.GONE
}