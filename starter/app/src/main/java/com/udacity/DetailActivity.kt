package com.udacity

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_detail.*
import kotlinx.android.synthetic.main.content_detail.*

class DetailActivity : AppCompatActivity() {

    companion object {
        const val FILE_NAME = "fileName"
        const val STATUS = "status"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)
        setSupportActionBar(toolbar)

        val fileName = intent.getStringExtra(FILE_NAME)
        fileNameText.text = fileName

        val status = intent.getStringExtra(STATUS)
        statusText.text = status

        if (status == "Success") {
            statusText.setTextColor(resources.getColor(R.color.colorPrimary, null))
        } else {
            statusText.setTextColor(resources.getColor(R.color.red, null))
        }

        goBack.setOnClickListener {
            motionLayout.transitionToEnd {
                startActivity(
                    Intent(
                        this,
                        MainActivity::class.java
                    )
                )
                finish()
            }
        }

    }
}
