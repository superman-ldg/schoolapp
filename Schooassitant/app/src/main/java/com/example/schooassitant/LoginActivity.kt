package com.example.schooassitant

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.Gson
import com.hyphenate.EMCallBack
import com.hyphenate.chat.EMClient
import kotlinx.android.synthetic.main.activity_login.*
import kotlin.concurrent.thread

class LoginActivity: AppCompatActivity()
{
    val loginUrl=HttpUtils.Url+"Login"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        supportActionBar?.hide()

        btn_regist.setOnClickListener {
            val intent=Intent(this,RegisterActivity::class.java)
            startActivity(intent)
        }
        btn_login.setOnClickListener{
            val account=edit_username.text.toString().trim()
            val password=edit_pass.text.toString().trim()
            val prefs=getSharedPreferences("Rem_file", Context.MODE_PRIVATE)
            val editor=prefs.edit()
            editor.putString("account",account)
            editor.putString("password",password)
            editor.apply()
            Login(account,password)
            signin()
        }
    }

    private fun Login(account:String,password:String){
        thread {
            try {
                val result =
                    HttpUtils.GetRequest(loginUrl + "?account=" + account + "&password=" + password)
                if(result.equals("true")){
                    val gson= Gson()
                    val userStr=HttpUtils.GetRequest(HttpUtils.Url + "getUser?account="+account)
                    val user=gson.fromJson(userStr,User::class.java)
                    val prefs=getSharedPreferences("Rem_file", Context.MODE_PRIVATE)
                    val editor=prefs.edit()
                    editor.putString("user_name",user.user_nicheng)
                    editor.apply()
                }
                getResult(result)
            }catch(e:Exception)
            {
                e.printStackTrace()
            }
        }
    }
    private fun signin() {
        val mUsernameView = findViewById<EditText?>(R.id.edit_username)
        val mPasswordView = findViewById<EditText?>(R.id.edit_pass)
        if (mUsernameView != null) {
            if (mPasswordView != null) {
                EMClient.getInstance().login(mUsernameView.text.toString().trim { it <= ' ' },
                    mPasswordView.text.toString().trim { it <= ' ' }, object : EMCallBack {
                        override fun onSuccess() {
                            startActivity(Intent(this@LoginActivity, myTest::class.java))
                        }

                        override fun onError(i: Int, s: String) {
                            Log.e("ldg", "登录失败$i,$s")
                        }

                        override fun onProgress(i: Int, s: String) {}
                    })
            }
        }
    }


    private fun getResult(result:String) {
            runOnUiThread {
                if (result.equals("true")) {
                    Toast.makeText(this, "登录成功!", Toast.LENGTH_SHORT).show()
                    val intent = Intent(this,myTest::class.java)
                    startActivity(intent)
                } else {
                    Toast.makeText(this, "登录失败!", Toast.LENGTH_SHORT).show()
                }
        }
    }
}