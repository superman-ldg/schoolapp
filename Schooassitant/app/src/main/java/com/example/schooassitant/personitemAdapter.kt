package com.example.schooassitant

import android.graphics.Bitmap
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class personitemAdapter(private val itemList:List<Book>,val imageList:List<Bitmap>):
    RecyclerView.Adapter<personitemAdapter.ViewHolder>(){
    inner class ViewHolder(view: View) :RecyclerView.ViewHolder(view){
        val itemImage: ImageView =view.findViewById(R.id.mybook_image)
        val itemName:TextView=view.findViewById(R.id.mybook_name)
        val itemPrice:TextView=view.findViewById(R.id.mybook_price)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view=LayoutInflater.from(parent.context)
            .inflate(R.layout.person_info_item,parent,false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item=itemList[position]
        val image=imageList[position]
        holder.itemImage.setImageBitmap(image)
        holder.itemName.text=item.book_name
        holder.itemPrice.text=item.book_price.toString()
    }

    override fun getItemCount()=itemList.size
}