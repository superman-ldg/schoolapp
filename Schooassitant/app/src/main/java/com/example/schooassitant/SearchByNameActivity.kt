package com.example.schooassitant

import android.graphics.Bitmap
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.android.synthetic.main.activity_search.*
import kotlinx.android.synthetic.main.activity_search.image_back
import kotlinx.android.synthetic.main.activity_search.search_list
import kotlinx.android.synthetic.main.activity_search_by_name.*
import kotlin.concurrent.thread

class SearchByNameActivity : AppCompatActivity() {
    val SearchUrlname=HttpUtils.Url+"UserQueryBookByName"
    lateinit var  bookList:ArrayList<Book>
    lateinit var imageList:ArrayList<Bitmap>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search_by_name)
        supportActionBar?.hide()
        bookList = ArrayList<Book>()
        imageList=ArrayList<Bitmap>()
        image_back.setOnClickListener {
            finish()
        }
        val name=intent.getStringExtra("name")
        if(name!=null||name!=""){
            if (name != null) {
                SearchByName(name)
            }
        }
    }


    private  fun SearchByName(name:String){
        thread {
            try {
                val data = HttpUtils.GetRequest(SearchUrlname+"?name="+name)
                if(data!="没有搜索到！！") {
                    val gson = Gson()
                    val typeOf = object : TypeToken<List<Book>>() {}.type
                    var list = gson.fromJson<List<Book>>(data, typeOf)
                    val perimageList = ArrayList<Bitmap>()
                    for (item in list) {
                        val i = item.book_id.toString()
                        val bytes = HttpUtils.GetBytesRequest(
                            HttpUtils.Url + "getPicture",
                            "D://pictureApp/book/",
                            i
                        )
                        val image = bytes?.let { Utils.getBitmap(it) }
                        if (image != null) {
                            perimageList.add(image)
                        }
                    }
                    getData(list, perimageList)
                }else{
                    nothing()
                }
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
            val layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
            layoutManager.gapStrategy = StaggeredGridLayoutManager.GAP_HANDLING_NONE
            search_list.layoutManager = layoutManager
            bookList=perbookList
            imageList=perimage
            val adapter = BookAdapter(bookList,imageList)
            search_list.adapter = adapter
        }
    }

    fun nothing(){
        runOnUiThread{
            Toast.makeText(this,"没有搜索到！！！！", Toast.LENGTH_LONG).show()
        }
    }



}