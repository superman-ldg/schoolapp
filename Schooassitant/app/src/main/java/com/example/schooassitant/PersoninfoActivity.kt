package com.example.schooassitant

import android.graphics.Bitmap
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.android.synthetic.main.person_info.*
import kotlin.concurrent.thread

class PersoninfoActivity : AppCompatActivity() {
    var BookUrl=HttpUtils.Url+"UserGetMyBook"
    var headerUrl=HttpUtils.Url+"getPicture"
    private val itemList=ArrayList<Book>()
    lateinit var  bookList:ArrayList<Book>
    lateinit var imageList:ArrayList<Bitmap>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.person_info)
        supportActionBar?.hide()
        bookList = ArrayList<Book>()
        imageList=ArrayList<Bitmap>()
        val account=intent.getStringExtra("account")
        BookUrl=BookUrl+"?account="+account?.trim()
        if (account != null) {
            initBooks(account)
        }
        image_back.setOnClickListener {
            finish()
        }
    }

    private fun initBooks(account:String) {
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
                val userStr=HttpUtils.GetRequest(HttpUtils.Url + "getUser?account="+account)
                val user=gson.fromJson(userStr,User::class.java)

                val bytes = HttpUtils.GetBytesRequest(headerUrl, "D://pictureApp/header/",account)
                val headerimage = bytes?.let { Utils.getBitmap(it)}
                if (headerimage != null) {
                    getData(list, perimageList, headerimage,user)
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    private fun getData(list: List<Book>,perimage:ArrayList<Bitmap>,headerimage:Bitmap,user:User) {
        runOnUiThread{
            val perbookList=ArrayList<Book>()
            for(a in list){
                perbookList.add(a)
            }
            user_img.setImageBitmap(headerimage)
            user_center_name.text=user.user_nicheng
            address_sex.text=user.user_college
            val layoutManager= StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
            Mycenter_listview.layoutManager=layoutManager
            bookList=perbookList
            imageList=perimage
            val adapter =personitemAdapter(bookList,imageList)
            Mycenter_listview.adapter = adapter
        }
    }

}