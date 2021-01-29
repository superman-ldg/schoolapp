package com.example.schooassitant

import PostAdapter
import android.graphics.Bitmap
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.android.synthetic.main.fragment_mypost.*
import kotlin.concurrent.thread


//我的发布界面
class MypostActivity : AppCompatActivity() {

    var BookUrl=HttpUtils.Url+"getBook"
    private val itemList=ArrayList<Book>()
    private val imageList=ArrayList<Bitmap>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_mypost)
       // initTeams2()
       /* val layoutManager= LinearLayoutManager(this)
        recyclerView.layoutManager=layoutManager
        val adapter = PostAdapter(itemList)
        recyclerView.adapter=adapter
        supportActionBar?.hide()*/

    }

    private fun initTeams(){
        thread {
            try{
            val data = HttpUtils.GetRequest(BookUrl)
            val gson = Gson()
            val typeOf = object : TypeToken<List<Book>>() {}.type
            var list = gson.fromJson<List<Book>>(data, typeOf)
                for(i in 1..list.size)
                {
                    val bytes= HttpUtils.GetBytesRequest("http://10.242.213.81:8080/mySchoolApp/getPicture","D://pictureApp/",i.toString())
                    val image= bytes?.let { Utils.getBitmap(it) }
                    if (image != null) {
                        imageList.add(image)
                    }
                }
            getData(list,imageList)
            }catch(e: Exception){
                e.printStackTrace()
            }
        }
    }

    private fun getData(list: List<Book>,im:List<Bitmap>) {
        runOnUiThread {
            for (a in list) {
                itemList.add(a)
            }
            val layoutManager = LinearLayoutManager(this)
            recyclerView.layoutManager = layoutManager
            val adapter = BookAdapter(itemList,im)
            recyclerView.adapter = adapter
        }
    }




/*
    private fun initTeams2(){

        repeat(1)
        {


            itemList.add(Book("超级吴晓机，324最帅的帅哥", R.drawable.img1,"¥99"))
            itemList.add(Book("我是梁登光，324最nb最聪明的人物", R.drawable.img2,"¥76"))
            itemList.add(Book("张玉先成神之路，zyxyyds！", R.drawable.img3,"¥76"))
            itemList.add(Book("python！", R.drawable.img4,"¥76"))
            itemList.add(Book("曼秀雷敦洗面奶，没用几天！", R.drawable.img6,"¥76"))

        }
    }*/

}