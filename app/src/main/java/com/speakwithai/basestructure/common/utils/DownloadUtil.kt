package com.speakwithai.basestructure.common.utils

import android.app.DownloadManager
import android.content.Context
import android.net.Uri
import android.os.Environment
import android.widget.Toast
import androidx.core.content.res.TypedArrayUtils.getString
import com.speakwithai.basestructure.R

object DownloadUtil {
    fun downloadImage(context: Context, url: String, fileName: String) {
        try {
            val downloadManager: DownloadManager = context.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
            val uri: Uri = Uri.parse(url)
            val request: DownloadManager.Request = DownloadManager.Request(uri)
            request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)

            request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, fileName)

            val downloadId = downloadManager.enqueue(request)

            Toast.makeText(context, R.string.download_started, Toast.LENGTH_LONG).show()
        } catch (e: Exception) {
            Toast.makeText(context, R.string.download_failed, Toast.LENGTH_SHORT).show()
        }
    }

    fun downloadMusic(context: Context, url: String, fileName: String) {
        try {
            val downloadManager: DownloadManager = context.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
            val uri: Uri = Uri.parse(url)
            val request: DownloadManager.Request = DownloadManager.Request(uri)

            request.setTitle(fileName)
            request.setDescription(context.getString(R.string.download_music))

            val downloadDirectory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MUSIC)
            request.setDestinationInExternalPublicDir(Environment.DIRECTORY_MUSIC, "$fileName.mp3")

            request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)

            request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI or DownloadManager.Request.NETWORK_MOBILE)

            val downloadId = downloadManager.enqueue(request)

            Toast.makeText(context, R.string.download_started, Toast.LENGTH_LONG).show()
        } catch (e: Exception) {
            Toast.makeText(context, R.string.download_failed, Toast.LENGTH_SHORT).show()
        }
    }
}
