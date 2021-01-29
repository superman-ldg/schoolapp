package com.example.schooassitant

import android.content.Context
import android.graphics.Bitmap
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.android.synthetic.main.activity_my_testxiajia.*
import kotlin.concurrent.thread

class myTestxiajia : AppCompatActivity() {
    var BookUrl=HttpUtils.Url+"UserGetMyBook"
    lateinit var  bookList:ArrayList<Book>
    lateinit var imageList:ArrayList<Bitmap>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_testxiajia)
        supportActionBar?.hide()
        val prefs =getSharedPreferences("Rem_file", Context.MODE_PRIVATE)
        val account=prefs.getString("account","")
        BookUrl=BookUrl+"?account="+account?.trim()
        bookList = ArrayList<Book>()
        imageList=ArrayList<Bitmap>()
        //初始化和刷新功能
        initBooks()
        mypost_update.setOnRefreshListener {
            initBooks()
            mypost_update.isRefreshing = false
        }



    }

    private fun initBooks() {
        thread {
            try {
                val data = HttpUtils.GetRequest(BookUrl)
                val gson = Gson()
                val typeOf = object : TypeToken<List<Book>>(){}.type
                var list = gson.fromJson<List<Book>>(data, typeOf)
                val perimageList=ArrayList<Bitmap>()
                for(item in list)
                {
                    val i=item.book_id.toString()
                    val bytes= HttpUtils.GetBytesRequest(HttpUtils.Url+"getPicture","D://pictureApp/book/",i)
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

    private fun getData(list: List<Book>,perimage:ArrayList<Bitmap>) {
        runOnUiThread{
            val perbookList=ArrayList<Book>()
            for(a in list){
                perbookList.add(a)
            }
            val layoutManager= LinearLayoutManager(this)
            recyclerView.layoutManager = layoutManager
            bookList=perbookList
            imageList=perimage
            val adapter = XiajiaAdapter(bookList,imageList)
            recyclerView.adapter = adapter
        }
    }
}