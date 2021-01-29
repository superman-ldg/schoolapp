package com.example.schooassitant

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_sendzudui.*
import kotlinx.android.synthetic.main.activity_sendzudui.imgback
import java.time.LocalDateTime
import kotlin.concurrent.thread


class PostzuduiActivity : AppCompatActivity() {

    val takePhoto=1
    lateinit var  imageBitmap: Bitmap
    val imageUrl=HttpUtils.Url+"savePicture"
    val delUrl=HttpUtils.Url+"UserDelRecruit"
    val SellUrl = HttpUtils.Url + "UserReleaseRecruit"

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sendzudui)
        imgback.setOnClickListener{
            finish()
        }
        supportActionBar?.hide()

        zudui_image.setOnClickListener{
            val intent= Intent(Intent.ACTION_OPEN_DOCUMENT)
            intent.addCategory(Intent.CATEGORY_OPENABLE)
            intent.type="image/*"
            startActivityForResult(intent,takePhoto)
        }
        imgback.setOnClickListener {
            finish()
        }

        Issue_recruit_btn.setOnClickListener {
            if (!this::imageBitmap.isInitialized)
            { remind(1) }
            else {
                thread {
                    val prefs = getSharedPreferences("Rem_file", Context.MODE_PRIVATE)
                    val title = zudui_title.text.toString()
                    val information = zudui_information.text.toString()
                    val date = LocalDateTime.now().toString().substring(0, 10)
                    val user_account = prefs?.getString("account", "")
                    if (title.equals("") || information.equals("")) {
                        remind(2)
                    } else {
                        val recruit_user = prefs?.getString("user_name", "")
                        val data =
                            "{recruit_id:0,recruit_user:'" + recruit_user + "',recruit_user_account:'" + user_account + "',recruit_title:'" + title + "',recruit_" +
                                    "information:'" + information + "',recruit_date:'" + date + "'}"
                        val result = HttpUtils.PostRequest(SellUrl, "recruit", data)
                        if (!result.equals("0")) {
                            val bytes = Utils.btimapToBtyes(imageBitmap)
                            val t = HttpUtils.PostBytesRequest(
                                imageUrl + "?url=D://pictureApp/recruit/&name=" + result,
                                bytes
                            )
                            if (t.equals("true"))
                                getResult(t)
                            else {
                                HttpUtils.GetRequest(delUrl + "?id=" + result)
                                getResult("false")
                            }
                        } else {
                            getResult("false")
                        }
                    }
                }
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when(requestCode){
            takePhoto->{
                if(resultCode== Activity.RESULT_OK&&data!=null){
                    data.data?.let { uri ->
                        val bitmap = getBitmapFromUri(uri)
                        if (bitmap.byteCount > 40000000) {
                            Toast.makeText(this, "不能上传大于4M的图片！！", Toast.LENGTH_SHORT).show()
                        } else {
                            val prebitmap = Utils.pressImage(bitmap)
                            if (prebitmap != null) {
                                imageBitmap = prebitmap
                            }
                            zudui_image.setImageBitmap(prebitmap)
                        }
                    }
                }
            }
        }
    }


    private fun getBitmapFromUri(uri: Uri)=contentResolver?.openFileDescriptor(uri,"r").use{
        BitmapFactory.decodeFileDescriptor(it?.fileDescriptor)
    }

    private fun getResult(result: String) {
        runOnUiThread {
            if (result.equals("true")) {
                Toast.makeText(this, "发布成功！", Toast.LENGTH_SHORT).show()
                finish()
            } else {
                Toast.makeText(this, "发布失败！", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun remind(a:Int) {
        runOnUiThread {
            if(a==1)
                Toast.makeText(this, "请选择图片！", Toast.LENGTH_SHORT).show()
            if(a==2)
                Toast.makeText(this, "请输入完整的信息！", Toast.LENGTH_SHORT).show()
        }
    }


}
