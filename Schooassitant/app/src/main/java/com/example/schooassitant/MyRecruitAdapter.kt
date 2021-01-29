package com.example.schooassitant

import android.content.Intent
import android.graphics.Bitmap
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class MyRecruitAdapter(val recruitList:List<Recruit>,val image:List<Bitmap>):
    RecyclerView.Adapter<MyRecruitAdapter.ViewHolder>()
{
    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tilte: TextView = view.findViewById(R.id.zuduititle)
        val user_img: ImageView = view.findViewById(R.id.p_info_userimg)
        val user_name: TextView = view.findViewById(R.id.p_info_username)
        val time:TextView=view.findViewById(R.id.p_info_time)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.zudui_item, parent, false)
        val holder = ViewHolder(view)
        return holder
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val recruit = recruitList[position]
        val image = image[position]
        holder.user_img.setImageBitmap(image)
        holder.tilte.text = recruit.recruit_title
        holder.time.text=recruit.recruit_date
        holder.user_name.text = recruit.recruit_user
        holder.itemView.setOnClickListener {
            val mintent = Intent(it.context, myTestzuduiXianqing::class.java)
            mintent.putExtra("title", recruit.recruit_title)
            mintent.putExtra("time", recruit.recruit_date)
            mintent.putExtra("information", recruit.recruit_information)
            mintent.putExtra("id",recruit.recruit_id.toString())
            mintent.putExtra("image", Utils.btimapToBtyes(image))
            it.context.startActivity(mintent)
        }
    }

    override fun getItemCount(): Int = recruitList.size

}