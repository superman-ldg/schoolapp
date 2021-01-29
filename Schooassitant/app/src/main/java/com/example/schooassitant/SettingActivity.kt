package com.example.schooassitant

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.schooassitant.R
import com.hyphenate.EMCallBack
import com.hyphenate.chat.EMClient
import com.superrtc.mediamanager.EMediaManager.getContext
import kotlinx.android.synthetic.main.activity_setting.*


class SettingActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_setting)
        supportActionBar?.hide()
     btn_logout.setOnClickListener {
            logout()
        }
        seting_back.setOnClickListener {
            finish()
        }
    }

    private fun logout() {
        EMClient.getInstance().logout(false, object : EMCallBack {
            override fun onSuccess() {
                startActivity(Intent(getContext(), LoginActivity::class.java))
                finish()
            }

            override fun onError(i: Int, s: String?) {}
            override fun onProgress(i: Int, s: String?) {}
        })
    }
}