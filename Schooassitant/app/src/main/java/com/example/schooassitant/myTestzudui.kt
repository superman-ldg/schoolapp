package com.example.schooassitant

import android.content.Intent
import android.graphics.Bitmap
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.android.synthetic.main.activity_my_testzudui.*
import kotlinx.android.synthetic.main.bottom_navigation.*
import kotlinx.android.synthetic.main.zudui.recyclerView
import kotlinx.android.synthetic.main.zudui.zudui_update
import kotlin.concurrent.thread

class myTestzudui : AppCompatActivity() {

    var recruitUrl= HttpUtils.Url+"getRecruit"

    lateinit var recruitList:ArrayList<Recruit>
    lateinit var imageList:ArrayList<Bitmap>


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        setContentView(R.layout.activity_my_testzudui)
        recruitList=ArrayList<Recruit>()
        imageList=ArrayList<Bitmap>()
        initRecruit()

        zudui_update.setOnRefreshListener {
            initRecruit()
            zudui_update.isRefreshing = false
        }
        btn_main.setOnClickListener{
            val intent= Intent(this,myTest::class.java)
            startActivity(intent)
        }
        btn_person.setOnClickListener{
            val intent= Intent(this,myTestwode::class.java)
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

    }
    private fun initRecruit() {
        thread {
            try {
                val data = HttpUtils.GetRequest(recruitUrl)
                val gson = Gson()
                val typeOf = object : TypeToken<List<Recruit>>() {}.type
                var list = gson.fromJson<List<Recruit>>(data, typeOf)
                val perimageList=ArrayList<Bitmap>()
                for(item in list)
                {
                    val i=item.recruit_user_account.toString()
                    val bytes= HttpUtils.GetBytesRequest(HttpUtils.Url+"getPicture","D://pictureApp/header/",i)
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

    private fun getData(list: List<Recruit>, perimage:ArrayList<Bitmap>) {
       runOnUiThread{
            val perrecruitList=ArrayList<Recruit>()
            for (a in list) {
                perrecruitList.add(a)
            }
           val layoutManager= LinearLayoutManager(this)
           recyclerView.layoutManager = layoutManager
           recruitList.clear()
           imageList.clear()
           recruitList=perrecruitList
           imageList=perimage
           val adapter = RecruitAdapter(recruitList,imageList)
           recyclerView.adapter = adapter
        }
    }
}