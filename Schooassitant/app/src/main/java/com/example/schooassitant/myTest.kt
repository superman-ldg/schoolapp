package com.example.schooassitant

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.android.synthetic.main.activity_my_test.*
import kotlin.concurrent.thread

class myTest : AppCompatActivity() {
    var BookUrl=HttpUtils.Url+"getBook"
    lateinit var  bookList:ArrayList<Book>
    lateinit var imageList:ArrayList<Bitmap>
    lateinit var context:Context;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_test)
        supportActionBar?.hide()
        bookList = ArrayList<Book>()
        imageList=ArrayList<Bitmap>()
        context=this
        //初始化和刷新功能
        initBooks()
        search_view.setOnQueryTextListener(object :
            androidx.appcompat.widget.SearchView.OnQueryTextListener {

            override fun onQueryTextChange(newText: String): Boolean {
                return false
            }

            override fun onQueryTextSubmit(name: String): Boolean {
                val intent= Intent(context,SearchByNameActivity::class.java)
                intent.putExtra("name",name.trim())
                startActivity(intent)
                return false
            }
        })


        down_pull_update.setOnRefreshListener {
            initBooks()
            down_pull_update.isRefreshing = false
        }

        btn_zudui.setOnClickListener{
            val intent= Intent(this,myTestzudui::class.java)
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
        //书籍
        shuji.setOnClickListener {
            val intent= Intent(this,SearchByTypeActivity::class.java)
            intent.putExtra("type","book")
            startActivity(intent)
        }
        //数码
        shuma.setOnClickListener {
            val intent= Intent(this,SearchByTypeActivity::class.java)
            intent.putExtra("type","digital")
            startActivity(intent)
        }
        //服饰
        fushi.setOnClickListener {
            val intent= Intent(this,SearchByTypeActivity::class.java)
            intent.putExtra("type","dress")
            startActivity(intent)
        }
        //手机
        shouji.setOnClickListener {
            val intent= Intent(this,SearchByTypeActivity::class.java)
            intent.putExtra("type","phone")
            startActivity(intent)
        }
        //其它
        qita.setOnClickListener {
            val intent= Intent(this,SearchByTypeActivity::class.java)
            intent.putExtra("type","other")
            startActivity(intent)
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
                        val image = bytes?.let { Utils.getBitmap(it) }
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
            val layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
            layoutManager.gapStrategy = StaggeredGridLayoutManager.GAP_HANDLING_NONE
            recyclerView.layoutManager = layoutManager
           bookList.clear()
           imageList.clear()
           bookList=perbookList
           imageList=perimage
            val adapter = BookAdapter(bookList,imageList)
            recyclerView.adapter = adapter
        }
    }

}
