package com.example.schooassitant

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.hyphenate.chat.EMMessage
import com.hyphenate.easeui.EaseConstant
import kotlinx.android.synthetic.main.fragment_zuduidetail.*
import kotlinx.android.synthetic.main.fragment_zuduidetail.p_info
import java.time.LocalDateTime
import kotlin.concurrent.thread

class zuduiDetailActivity : AppCompatActivity()
{
    companion object{
        fun actionStart(context: Context)
        {
            val intent= Intent(context,zuduiDetailActivity::class.java)
            context.startActivity(intent)
        }
    }

    var headerUrl=HttpUtils.Url+"getPicture"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_zuduidetail)
        supportActionBar?.hide()

        val recruitId=intent.getStringExtra("recruit_id")
        p_info_username.text=intent.getStringExtra("user")
        p_info_time.text=intent.getStringExtra("time")
        p_info.text=intent.getStringExtra("information")
        val account=intent.getStringExtra("account")

        val image1= intent.getByteArrayExtra("image")?.let { Utils.getBitmap(it) }
        p_info_userimg.setImageBitmap(image1)

        image_back.setOnClickListener {
            finish()
        }
        btn_interest.setOnClickListener{
            val per=getSharedPreferences("Rem_file", Context.MODE_PRIVATE)
            val myaccount=per.getString("account","")


            val intent = Intent(this, ChatActivity::class.java)
            intent.putExtra(EaseConstant.EXTRA_USER_ID, account)
            intent.putExtra(EaseConstant.EXTRA_CHAT_TYPE, EMMessage.ChatType.Chat)
            startActivity(intent)
        }
        if (recruitId != null) {
            getPicture(recruitId)
        }
    }


    private fun getPicture(recruit_id:String){
        thread {
            try {
                val bytes = HttpUtils.GetBytesRequest(headerUrl, "D://pictureApp/recruit/",recruit_id)
                val image = bytes?.let { Utils.getBitmap(it)}
                if (image != null) {
                    InitPicture(image)
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    private fun InitPicture(bitmap: Bitmap){
        runOnUiThread{
            if(bitmap!=null){
               image.setImageBitmap(bitmap)
            }

        }
    }


}