package com.example.schooassitant;

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_enquiry.*



public class EnquiryActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_enquiry)
        imgback.setOnClickListener {
            finish()
        }
        supportActionBar?.hide()
    }
}
