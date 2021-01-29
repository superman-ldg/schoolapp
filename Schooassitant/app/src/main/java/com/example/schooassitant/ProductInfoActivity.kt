package com.example.schooassitant

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.os.Build
import android.os.Bundle
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.Gson
import com.hyphenate.chat.EMMessage
import com.hyphenate.easeui.EaseConstant
import kotlinx.android.synthetic.main.fragment_product_info.*
import kotlinx.android.synthetic.main.fragment_product_info.imageback
import kotlinx.android.synthetic.main.fragment_product_info.p_info
import kotlinx.android.synthetic.main.fragment_product_info.person_info
import kotlinx.android.synthetic.main.fragment_product_info.product_image
import java.time.LocalDateTime
import kotlin.concurrent.thread

//展示产品
class ProductInfoActivity :AppCompatActivity()
{
    companion object{
        fun actionStart(context: Context)
        {
            val intent= Intent(context,ProductInfoActivity::class.java)
            context.startActivity(intent)
        }
    }

    var headerUrl=HttpUtils.Url+"getPicture"
    val OrderUrl=HttpUtils.Url+"UserOrderBook"

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_product_info)
        supportActionBar?.hide()
        imageback.setOnClickListener {
            finish()
        }


        p_info_price.text="￥ "+intent.getStringExtra("book_price")
        p_info.text=intent.getStringExtra("book_information")
        p_info_time.text=intent.getStringExtra("book_date")
        val image= intent.getByteArrayExtra("image")?.let { Utils.getBitmap(it) }
        product_image.setImageBitmap(image)

        val account=intent.getStringExtra("book_user_account")
        if (account != null) {
            getPicture(account)
        }

        imageback.setOnClickListener {
            finish()
        }
        buy_talk.setOnClickListener{
            val per=getSharedPreferences("Rem_file", Context.MODE_PRIVATE)
            val myaccount=per.getString("account","")
            val book_id=intent.getStringExtra("book_id")

            val date = LocalDateTime.now().toString().substring(0, 10)
            val order1="{order_id:0,order_user_account:'"+myaccount+"',order_book_id:'"+book_id+"',order_date:'"+date+"'}"
             Order(order1)

            val intent = Intent(this, ChatActivity::class.java)
            intent.putExtra(EaseConstant.EXTRA_USER_ID, account)
            intent.putExtra(EaseConstant.EXTRA_CHAT_TYPE, EMMessage.ChatType.Chat)
            startActivity(intent)
        }
        person_info.setOnClickListener{
            val account=intent.getStringExtra("book_user_account")
            val intent=Intent(this,PersoninfoActivity::class.java)
            intent.putExtra("account",account)
            startActivity(intent)
        }
        supportActionBar?.hide()
    }

    private fun getPicture(account:String){
        thread {
            try {
                val gson= Gson()
                val userStr=HttpUtils.GetRequest(HttpUtils.Url + "getUser?account="+account)
                val user=gson.fromJson(userStr,User::class.java)
                val bytes = HttpUtils.GetBytesRequest(headerUrl, "D://pictureApp/header/",account)
                val image = bytes?.let { Utils.getBitmap(it)}
                if (image != null) {
                    InitPicture(image,user)
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    private fun InitPicture(bitmap: Bitmap, user:User){
        runOnUiThread{
            if(bitmap!=null){
                p_info_userimg.setImageBitmap(bitmap)
            }
            if(user!=null){
                p_info_username.text=user.user_nicheng
            }
        }
    }

    private fun Order(order:String){
        thread{
            try{
                val result = HttpUtils.PostRequest(OrderUrl, "order", order)
                println(result)
            }catch(e:Exception){

            }
        }
    }

}