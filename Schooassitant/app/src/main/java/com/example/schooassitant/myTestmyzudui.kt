package com.example.schooassitant

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.android.synthetic.main.activity_my_testmyzudui.*
import kotlinx.android.synthetic.main.zudui.recyclerView
import kotlin.concurrent.thread

class myTestmyzudui : AppCompatActivity() {

    var myrecruitUrl= HttpUtils.Url+"UserGetMyRecruit"
    lateinit var myrecruitList:ArrayList<Recruit>
    lateinit var imageList:ArrayList<Bitmap>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_testmyzudui)
        supportActionBar?.hide()
        image_back.setOnClickListener {
            finish()
        }
        val prefs =getSharedPreferences("Rem_file", Context.MODE_PRIVATE)
        val account=prefs.getString("account","")
        myrecruitUrl=myrecruitUrl+"?account="+account?.trim()
        myrecruitList= ArrayList<Recruit>()
        imageList=ArrayList<Bitmap>()
        //初始化和刷新功能
        initMyZudui()
        myzudui_update.setOnRefreshListener {
            initMyZudui()
            myzudui_update.isRefreshing = false
            val intent= Intent(this,myTestwode::class.java)
            startActivity(intent)
        }
    }

    private fun initMyZudui() {
        thread {
            try {
                val data = HttpUtils.GetRequest(myrecruitUrl)
                val gson = Gson()
                val typeOf = object : TypeToken<List<Recruit>>(){}.type
                var list = gson.fromJson<List<Recruit>>(data, typeOf)
                val perimageList=ArrayList<Bitmap>()
                for(item in list)
                {
                    val i=item.recruit_id.toString()
                    val bytes= HttpUtils.GetBytesRequest(HttpUtils.Url+"getPicture","D://pictureApp/recruit/",i)
                    val image= bytes?.let { Utils.getBitmap(it) }
                    if (image != null) {
                        perimageList.add(image)
                    }
                }
                getData(list,perimageList)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    private fun getData(list: List<Recruit>,perimage:ArrayList<Bitmap>) {
        runOnUiThread{
            val perrecruitList=ArrayList<Recruit>()
            for(a in list){
                perrecruitList.add(a)
            }
            val layoutManager= LinearLayoutManager(this)
            recyclerView.layoutManager = layoutManager
            myrecruitList=perrecruitList
            imageList=perimage
            val adapter = MyRecruitAdapter(myrecruitList,imageList)
            recyclerView.adapter = adapter
        }
    }

}