package com.example.schooassitant

import android.app.AlertDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_my_testzudui_xianqing.*
import kotlin.concurrent.thread

class myTestzuduiXianqing : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?)
    {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_testzudui_xianqing)
        supportActionBar?.hide()

        image_back1.setOnClickListener {
            finish()
        }

        val id=intent.getStringExtra("id")
        my_title.text="标题:"+intent.getStringExtra("title")
        recruit_time.text="发布时间:"+intent.getStringExtra("time")
        my_info.text="组队信息:\n"+intent.getStringExtra("information")
        val image1= intent.getByteArrayExtra("image")?.let { Utils.getBitmap(it) }
        image.setImageBitmap(image1)
            btn_del.setOnClickListener {

                AlertDialog.Builder(it.context).apply {
                    setTitle("")
                    setMessage("是否删除这条招募信息")
                    setCancelable(false)
                    setPositiveButton("是"){dialog,which->"是"
                        Toast.makeText(it.context, "删除成功!", Toast.LENGTH_SHORT).show()
                        thread {
                            try{
                                var delUrl = HttpUtils.Url + "UserDelRecruit?id=" +id
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


    private fun back(result:String){
        if(result.equals("true")){
            val intent= Intent(this,myTestmyzudui::class.java)
            startActivity(intent)
        }
    }

}