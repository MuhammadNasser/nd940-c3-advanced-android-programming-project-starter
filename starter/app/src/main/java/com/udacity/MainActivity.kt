package com.udacity

import android.app.DownloadManager
import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.Uri
import android.os.Bundle
import android.widget.RadioButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*
import org.apache.commons.io.FilenameUtils


class MainActivity : AppCompatActivity() {

    private var downloadID: Long = 0

    private lateinit var notificationManager: NotificationManager
    lateinit var downloadManager: DownloadManager
    private var status = ""
    private var fileUrl = ""
    private var fileName = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        notificationManager =
            getSystemService(NotificationManager::class.java) as NotificationManager

        NotificationHelper.createChannel(CHANNEL_ID, CHANNEL_NAME, notificationManager)

        registerReceiver(receiver, IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE))

        custom_button.setOnClickListener {
            download()
        }
    }

    private val receiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            val id = intent?.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1)

            if (id == downloadID) {
                val query = DownloadManager.Query()
                query.setFilterById(id)

                val cursor = downloadManager.query(query)

                if (cursor.moveToFirst()) {
                    fileName = FilenameUtils.getName(fileUrl)
                    val columnIndex = cursor.getColumnIndex(DownloadManager.COLUMN_STATUS)
                    when (cursor.getInt(columnIndex)) {
                        DownloadManager.STATUS_SUCCESSFUL -> {
                            status = "Success"
                        }
                        DownloadManager.STATUS_FAILED -> {
                            status = "Fail"
                        }
                    }
                }

                NotificationHelper.buildNotification(
                    this@MainActivity,
                    resources.getString(R.string.notification_description),
                    fileName,
                    status
                )
            }
        }
    }

    private fun download() {
        if (options_radio_group.checkedRadioButtonId == -1) {
            Toast.makeText(this, "Please select the file to download", Toast.LENGTH_SHORT).show()
            custom_button.changeButtonState(ButtonState.Completed)
        } else {
            fileUrl =
                findViewById<RadioButton>(options_radio_group.checkedRadioButtonId).text.toString()

            val request =
                DownloadManager.Request(Uri.parse(fileUrl))
                    .setTitle(getString(R.string.app_name))
                    .setDescription(getString(R.string.app_description))
                    .setRequiresCharging(false)
                    .setAllowedOverMetered(true)
                    .setAllowedOverRoaming(true)

            downloadManager = getSystemService(DOWNLOAD_SERVICE) as DownloadManager
            downloadID =
                downloadManager.enqueue(request)// enqueue puts the download request in the queue.
        }
    }

    companion object {
        const val CHANNEL_ID = "channelId"
        const val CHANNEL_NAME = "channelName"
    }

}
