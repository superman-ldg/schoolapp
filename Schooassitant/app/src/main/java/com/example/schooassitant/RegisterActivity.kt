package com.example.schooassitant

import android.graphics.Bitmap
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.hyphenate.chat.EMClient
import com.hyphenate.exceptions.HyphenateException
import kotlinx.android.synthetic.main.activity_regist.*
import java.util.regex.Pattern
import kotlin.concurrent.thread


class RegisterActivity : AppCompatActivity() {

    val ResgisterUrl=HttpUtils.Url+"Register"
    val imageUrl=HttpUtils.Url+"savePicture"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_regist)
        supportActionBar?.hide()
        imageView.setImageResource(R.drawable.default_user_avatar)
        imageView53.setOnClickListener {
            finish()
        }
        btn_regist.setOnClickListener {
            signup()
            val account=reg_account.text.toString().trim()
            val password=reg_password.text.toString().trim()
            val xueyuan=reg_xueyuan.text.toString().trim()
            val nicheng=reg_nicheng.text.toString().trim()
            val youxiang=reg_youxiang.text.toString().trim()

            val regex_account = "^[0-9]+$"
            val regex_youxiang= "[1-9][0-9]+@qq.com"
            if (!Pattern.matches(regex_account,account)){
                Toast.makeText(this, "学号不规范！！", Toast.LENGTH_SHORT).show()
            }else if(!Pattern.matches(regex_youxiang,youxiang)){
                Toast.makeText(this, "邮箱不规范！！", Toast.LENGTH_SHORT).show()
            }else{
                val user="{user_account:'"+account+"',user_password:'"+password+"',user_college:'"+xueyuan+"',user_nicheng:'"+
                        nicheng+"',user_email:'"+youxiang+"'}"
                Register(user,account)
            }
        }
    }
    private fun signup() {
        Thread(Runnable {
            try {
                EMClient.getInstance().createAccount(reg_account.text.toString().trim(),
                        reg_password.text.toString().trim())
            } catch (e: HyphenateException) {
                e.printStackTrace()
                Log.i("账号", "注册失败" + e.errorCode + "," + e.message)
            }
        }).start()
    }

    private fun Register(user:String,account:String){
        thread{
            try {
                val result = HttpUtils.PostRequest(ResgisterUrl, "user", user)
                if(result=="true"){
                    imageView.setDrawingCacheEnabled(true);
                    val bitmap = Bitmap.createBitmap(imageView.getDrawingCache());
                    imageView.setDrawingCacheEnabled(false);
                    val bytes=Utils.btimapToBtyes(bitmap)
                    println("--------------------------------------------------"+bytes)
                    val t = HttpUtils.PostBytesRequest(imageUrl + "?url=D://pictureApp/header/&name=" +account, bytes)
                }
                getResult(result)
            }catch (e:Exception){
                e.printStackTrace()
                getResult("false")
            }
        }
    }

    private fun getResult(result:String){
        runOnUiThread {
            if (result.equals("true")) {
                Toast.makeText(this, "注册成功！", Toast.LENGTH_SHORT).show()
                finish()
            } else {
                Toast.makeText(this, "注册失败！", Toast.LENGTH_SHORT).show()
            }
        }
    }

}