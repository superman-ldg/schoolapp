package com.example.schooassitant

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.example.schooassitant.SettingActivity
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_my_testwode.*

import kotlinx.android.synthetic.main.activity_my_testwode.person_img
import kotlinx.android.synthetic.main.bottom_navigation.*
import kotlin.concurrent.thread

class myTestwode : AppCompatActivity() {

    var headerUrl=HttpUtils.Url+"getPicture"
    val My=HttpUtils.Url+"getUser"
    lateinit var account:String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_testwode)
        supportActionBar?.hide()

        val prefs =getSharedPreferences("Rem_file", Context.MODE_PRIVATE)
        account= prefs.getString("account","").toString()
        if (account != null) {
            btn_tologin.setVisibility(View.INVISIBLE)
            InitHeader(account)
        }

        //闲置
        btn_main.setOnClickListener{
            val intent= Intent(this,myTest::class.java)
            startActivity(intent)
        }
        //设置
        set_shezhi.setOnClickListener {
            val intent= Intent(this, SettingActivity::class.java)
            startActivity(intent)
        }

        //组队
        btn_zudui.setOnClickListener{
            val intent= Intent(this,myTestzudui::class.java)
            startActivity(intent)
        }

        btn_post.setOnClickListener {
            val writePostsPopWindow = WritePostsPopWindow(this)
            writePostsPopWindow.showMoreWindow(it)
        }
        btn_message.setOnClickListener{
            val intent = Intent(this, TalkListActivity::class.java)
            startActivity(intent)
        }

        //我的发布
        set_mypost.setOnClickListener {
            val intent=Intent(this,myTestmypost::class.java)
            startActivity(intent)
        }

        //我的组队
        set_myzudui.setOnClickListener {
            val intent=Intent(this,myTestmyzudui::class.java)
            startActivity(intent)
        }

        //购物车
        my_car.setOnClickListener {
            val intent=Intent(this,myTestcar::class.java)
            startActivity(intent)
        }
    }


    private fun InitHeader(header:String){
        thread {
            try {
                val gson= Gson()
                val userStr=HttpUtils.GetRequest(HttpUtils.Url + "getUser?account="+account)
                val user=gson.fromJson(userStr,User::class.java)

                val bytes = HttpUtils.GetBytesRequest(headerUrl, "D://pictureApp/header/",header)
                val image = bytes?.let { Utils.getBitmap(it) }
                if (image != null&&user!=null) {
                    getHeader(image,user)
                }else{
                    getniCheng(user)
                }
            } catch (e:Exception) {
                e.printStackTrace()
            }
        }
    }

    private fun getHeader(image:Bitmap,user:User){
        runOnUiThread{
            person_img.setImageBitmap(image)
            set_text_name.text=user.user_nicheng
        }
    }

    private fun getniCheng(user:User){
        runOnUiThread{
            set_text_name.text=user.user_nicheng
        }
    }



}