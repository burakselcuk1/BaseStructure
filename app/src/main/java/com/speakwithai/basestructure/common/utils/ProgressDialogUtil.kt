package com.speakwithai.basestructure.common.utils


import android.app.Dialog
import android.content.Context
import android.graphics.Color
import androidx.core.graphics.drawable.toDrawable
import com.speakwithai.basestructure.R


object ProgressDialogUtil {

    lateinit var progressDialog: Dialog
    fun showLoadingProgress(context: Context): Dialog {

        progressDialog = Dialog(context)
        progressDialog.let {
            //it.show()
            it.window!!.setBackgroundDrawable(Color.TRANSPARENT.toDrawable())
            it.setContentView(R.layout.progress_dialog_view)
            it.setCancelable(true)
            it.setCanceledOnTouchOutside(true)

            return it
        }
    }

    fun start() {
        progressDialog.show()
    }

    fun stop() {
        progressDialog.cancel()
        progressDialog.dismiss()
    }

}
