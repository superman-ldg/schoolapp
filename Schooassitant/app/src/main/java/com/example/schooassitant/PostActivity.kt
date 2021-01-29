package com.example.schooassitant

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.get
import kotlinx.android.synthetic.main.activity_post.*
import java.time.LocalDateTime
import java.util.regex.Pattern
import kotlin.concurrent.thread


class PostActivity : AppCompatActivity() {
    val imageUrl=HttpUtils.Url+"savePicture"
    val delUrl=HttpUtils.Url+"UserDelBook"
    val SellUrl = HttpUtils.Url + "UserSellBook"
    val takePhoto=1
    lateinit var  imageBitmap: Bitmap
    var mytype=""

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_post)
        supportActionBar?.hide()

        val  mItems=resources.getStringArray(R.array.spinnername)
        // Create an ArrayAdapter using a simple spinner layout and languages array
        val aa = ArrayAdapter(this, android.R.layout.simple_spinner_item, mItems)
        // Set layout to use when the list of choices appear
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        //Set Adapter to Spinner
        spinner!!.setAdapter(aa)

        val type= getResources().getStringArray(R.array.spinnername);

        spinner.setOnItemSelectedListener(object: AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(parent: AdapterView<*>?) {
                TODO("Not yet implemented")
            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                mytype=type[position]
            }
        })

        imgback.setOnClickListener {
            finish()
        }
        my_image.setOnClickListener{
            val intent= Intent(Intent.ACTION_OPEN_DOCUMENT)
            intent.addCategory(Intent.CATEGORY_OPENABLE)
            intent.type="image/*"
            startActivityForResult(intent,takePhoto)
        }

        Issue_btn.setOnClickListener {

            if(mytype==""||mytype=="请选择物品类型")
              remind(1)
            else if(!this::imageBitmap.isInitialized)
                remind(2)
            else
            thread {
                val prefs =getSharedPreferences("Rem_file", Context.MODE_PRIVATE)
                val name = my_title.text.toString()
                val type1 =mytype
                var type=""
                when(type1){
                    "书本"->{type="book"}
                    "服装"->{type="digital"}
                    "数码"->{type="dress"}
                    "手机"->{type="phone"}
                    "其它"->{type="other"}
                }
                val price = my_price.text.toString()
                val information = my_content.text.toString()
                val date = LocalDateTime.now().toString().substring(0, 10)
                val user_account =prefs?.getString("account", "")
                if (name.equals("") || type.equals("") || price.equals("") || information.equals("")) {
                 remind(3)
                } else {
                    val regex = "^[0-9]+$";
                    if (Pattern.matches(regex, price)) {
                        val data =
                                "{book_id:0,book_user_account:'" + user_account + "',book_name:'" + name + "',book_type:'" + type + "',book_" +
                                        "information:'" + information + "',book_price:'" + price + "',book_date:'" + date + "',book_state:0}"
                        val result = HttpUtils.PostRequest(SellUrl, "book", data)
                        if (!result.equals("0")) {
                            val bytes = Utils.btimapToBtyes(imageBitmap)
                            val t = HttpUtils.PostBytesRequest(imageUrl + "?url=D://pictureApp/book/&name=" + result, bytes)
                            if (t.equals("true"))
                                getResult(t)
                            else {
                                HttpUtils.GetRequest(delUrl + "?id=" + result)
                                getResult("false")
                            }
                        } else {
                            getResult("false")
                        }
                    } else {
                        Toast.makeText(this, "价钱不规范！！", Toast.LENGTH_SHORT).show()
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
                    data.data?.let {uri ->
                        val bitmap=getBitmapFromUri(uri)
                        if(bitmap.byteCount>40000000){
                            Toast.makeText(this, "不能上传大于4M的图片！！", Toast.LENGTH_SHORT).show()
                        }else {

                            val prebitmap = Utils.pressImage(bitmap)
                            if (prebitmap != null) {
                                imageBitmap = prebitmap
                            }
                            my_image.setImageBitmap(prebitmap)
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
                Toast.makeText(this, "请选择物品类型！", Toast.LENGTH_SHORT).show()
            if(a==2)
            Toast.makeText(this, "请选择图片！", Toast.LENGTH_SHORT).show()
            if(a==3)
                Toast.makeText(this, "请输入完整的信息！", Toast.LENGTH_SHORT).show()
        }
    }


}