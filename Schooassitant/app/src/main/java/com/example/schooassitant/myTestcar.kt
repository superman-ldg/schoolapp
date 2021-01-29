package com.example.schooassitant

import android.content.Context
import android.graphics.Bitmap
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.android.synthetic.main.activity_my_testcar.*
import kotlinx.android.synthetic.main.zudui.recyclerView
import kotlin.concurrent.thread

class myTestcar : AppCompatActivity() {

    var myorderUrl= HttpUtils.Url+"UserGetMyOrder"
    lateinit var mybookList:ArrayList<Book>
    lateinit var imageList:ArrayList<Bitmap>
    lateinit var  myorderList:ArrayList<Order>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_testcar)
        supportActionBar?.hide()
        val prefs =getSharedPreferences("Rem_file", Context.MODE_PRIVATE)
        val account=prefs.getString("account","")
        myorderUrl=myorderUrl+"?account="+account?.trim()
        mybookList= ArrayList<Book>()
        imageList=ArrayList<Bitmap>()
        myorderList=ArrayList<Order>()
        //初始化和刷新功能
        initMyOrder()
        mycar_update.setOnRefreshListener {
            initMyOrder()
            mycar_update.isRefreshing = false
        }
        image_back.setOnClickListener {
            finish()
        }
    }

    private fun initMyOrder() {
        thread {
            try {
                val data = HttpUtils.GetRequest(myorderUrl)
                val gson = Gson()
                val typeOf = object : TypeToken<List<Order>>(){}.type
                var list = gson.fromJson<List<Order>>(data,typeOf)
                val perimageList=ArrayList<Bitmap>()
                val perbookList=ArrayList<Book>()
                for(item in list)
                {
                    val i=item.order_book_id.toString()
                    val bookstr=HttpUtils.GetRequest(HttpUtils.Url+"getOneBook?id="+i)
                    val book=gson.fromJson(bookstr,Book::class.java)
                        perbookList.add(book)
                    val bytes= HttpUtils.GetBytesRequest(HttpUtils.Url+"getPicture","D://pictureApp/book/",i)
                    val image= bytes?.let { Utils.getBitmap(it) }
                    if (image != null) {
                        perimageList.add(image)
                    }
                }
                getData(list,perimageList,perbookList)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    private fun getData(list: List<Order>,perimage:ArrayList<Bitmap>,bookList:ArrayList<Book>) {
        runOnUiThread{
            val perorderList=ArrayList<Order>()
            for(a in list){
                perorderList.add(a)
            }
            val layoutManager= LinearLayoutManager(this)
            recyclerView.layoutManager = layoutManager
            myorderList=perorderList
            mybookList=bookList
            imageList=perimage
            val adapter = myCarAdapter(myorderList,mybookList,imageList)
            recyclerView.adapter = adapter
        }
    }


}