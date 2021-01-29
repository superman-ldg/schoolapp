package com.example.schooassitant

import android.app.AlertDialog
import android.content.Intent
import android.graphics.Bitmap
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_my_testcarxiangqing.*
import kotlinx.android.synthetic.main.activity_my_testcarxiangqing.p_info
import kotlinx.android.synthetic.main.activity_my_testcarxiangqing.product_image
import kotlin.concurrent.thread

class myTestcarxiangqing : AppCompatActivity() {

    var headerUrl=HttpUtils.Url+"getPicture"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_testcarxiangqing)
        supportActionBar?.hide()
        imageback.setOnClickListener {
            finish()
        }
        time.text=intent.getStringExtra("time")
        val useraccount= intent.getStringExtra("book_user_account")?.trim()
        p_info.text=intent.getStringExtra("book_information")
        price.text="￥ "+intent.getStringExtra("book_price")
        val image= intent.getByteArrayExtra("image")?.let { Utils.getBitmap(it) }
        product_image.setImageBitmap(image)

        if (useraccount != null) {
            getPicture(useraccount)
        }

        btn_del.setOnClickListener {
            val id=intent.getStringExtra("order_id")
            println(id)
            AlertDialog.Builder(it.context).apply {
                setTitle("")
                setMessage("是否删除这条订单")
                setCancelable(false)
                setPositiveButton("是"){dialog,which->"是"
                    Toast.makeText(it.context, "删除成功!", Toast.LENGTH_SHORT).show()
                    thread {
                        try{
                            var delUrl = HttpUtils.Url + "UserDelOrder?id=" +id
                            val result = HttpUtils.GetRequest(delUrl)
                            back(result)
                        }catch (e:Exception){
                            e.printStackTrace()
                        }
                    }
                }
                setNegativeButton("否"){ dialog,which->"否"
                    Toast.makeText(it.context, "删除失败!", Toast.LENGTH_SHORT).show()
                }
                show()
            }
        }
    }


    private fun getPicture(account:String){
        thread {
            try {
                val gson=Gson()
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


    private fun back(result:String){
        if(result.equals("true")){
            val intent= Intent(this,myTestcar::class.java)
            startActivity(intent)
        }
    }

    private fun InitPicture(bitmap: Bitmap,user:User){
        runOnUiThread{
            if(bitmap!=null){
                user_image.setImageBitmap(bitmap)
            }
            if(user!=null){
                user_name.text=user.user_nicheng
            }
        }
    }

}