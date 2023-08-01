package com.speakwithai.basestructure.common.globalExt

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.content.Intent
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.widget.Toast
import androidx.annotation.DrawableRes
import androidx.appcompat.content.res.AppCompatResources

fun Context.getActivity(): Activity? {
    return when (this) {
        is Activity -> this
        is ContextWrapper -> this.baseContext.getActivity()
        else -> null
    }
}

fun Context.getDrawableCompat(@DrawableRes id: Int): Drawable? =
    AppCompatResources.getDrawable(this, id)

fun Context.showToast(message : String) = Toast.makeText(this,message,Toast.LENGTH_LONG).show()

fun <T> Bundle.extGetParcelable(key : String,classes : Class<T>) = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        this.getParcelable(key,classes)
    } else {
        this.getParcelable(key)
    }


fun Context.openAppSettings(){
    val intent = Intent()
    intent.action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
    val uri = Uri.fromParts("package", this.packageName, null)
    intent.data = uri
    startActivity(intent)

}
