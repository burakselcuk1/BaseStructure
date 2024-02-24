package com.speakwithai.basestructure.ui.mainActivity


import android.content.Intent
import android.net.Uri
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.Observer
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.bumptech.glide.Glide
import com.speakwithai.basestructure.base.BaseActivity
import com.speakwithai.basestructure.R
import com.speakwithai.basestructure.common.CheckDayWorker
import com.speakwithai.basestructure.common.enums.UserStatus
import com.speakwithai.basestructure.common.utils.MessageManager
import com.speakwithai.basestructure.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint
import java.util.concurrent.TimeUnit

@AndroidEntryPoint
class MainActivity : BaseActivity<ActivityMainBinding, MainViewModel>(
    layoutId = R.layout.activity_main, viewModelClass = MainViewModel::class.java
) {

    override fun onInitDataBinding() {
        // Eğer intent aracılığıyla fcmden veri gönderildiyse
        val message = intent.getStringExtra("message")
        val title = intent.getStringExtra("title")
        val url = intent.getStringExtra("url")
        val imageUrl = intent.getStringExtra("imageUrl")

        // Veri varsa ve bu verileri içeren bir dialog
        if (message != null && title != null && url != null) {
            if (imageUrl != null) {
                showNotificationDialog(title, message, url, imageUrl)
            }
        }

        /*val periodicWorkRequest = PeriodicWorkRequestBuilder<CheckDayWorker>(15, TimeUnit.MINUTES).build()
        WorkManager.getInstance(this).enqueue(periodicWorkRequest)*/

      //  MessageManager.initialize(this)

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)


    /*   viewModel.userStatus.observe(this, Observer { status ->
            when(status) {
                UserStatus.PREMIUM -> {
                    Toast.makeText(this,"Premium", Toast.LENGTH_SHORT).show()

                }
                UserStatus.NON_PREMIUM -> {
                    Toast.makeText(this,"Değil", Toast.LENGTH_SHORT).show()
                }
                else -> {
                    Toast.makeText(this,"Belirsiz", Toast.LENGTH_SHORT).show()

                }
            }
        })*/
    }

 /*   private fun showNotificationDialog(title: String, message: String, url: String) {
        val builder = AlertDialog.Builder(this)
        builder.setTitle(title)
        builder.setMessage(message)
        builder.setPositiveButton("Tamam") { dialog, _ ->
            // Tamam düğmesine basıldığında yapılacak işlemler (isteğe bağlı)
            dialog.dismiss() // Dialog'u kapat
        }
        builder.setNegativeButton("URL'yi Aç") { dialog, _ ->
            // URL'yi Aç düğmesine basıldığında yapılacak işlemler
            // Tarayıcıyı açmak için Intent oluştur
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
            startActivity(intent)
            dialog.dismiss() // Dialog'u kapat
        }
        val dialog = builder.create()
        dialog.show()
    }*/

    private fun showNotificationDialog(title: String, message: String, url: String, imageUrl: String) {
        val builder = AlertDialog.Builder(this)
        val inflater = layoutInflater
        val dialogView = inflater.inflate(R.layout.notification_custom_dialog, null)

        val titleTextView = dialogView.findViewById<TextView>(R.id.titleTextView)
        val messageTextView = dialogView.findViewById<TextView>(R.id.messageTextView)
        val openUrlButton = dialogView.findViewById<Button>(R.id.openUrlButton)
        val notificationImage = dialogView.findViewById<ImageView>(R.id.notification_image)
        val closeButton = dialogView.findViewById<Button>(R.id.closeButton)

        titleTextView.text = title
        messageTextView.text = message
        Glide.with(this)
            .load(imageUrl)
            .into(notificationImage)

        builder.setView(dialogView)

        val dialog = builder.create()

        openUrlButton.setOnClickListener {
            // URL'yi Aç düğmesine basıldığında yapılacak işlemler
            // Tarayıcıyı açmak için Intent oluştur
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
            startActivity(intent)
            dialog.dismiss() // Dialog'u kapat
        }

        closeButton.setOnClickListener {
            dialog.dismiss() // Dialog'u kapat
        }

        dialog.show()
    }

}
