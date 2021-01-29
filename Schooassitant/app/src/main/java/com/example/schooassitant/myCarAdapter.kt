package com.example.schooassitant

import android.content.Intent
import android.graphics.Bitmap
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class  myCarAdapter(val orderList:List<Order>,val bookList:List<Book>,val image:List<Bitmap>):
        RecyclerView.Adapter<myCarAdapter.ViewHolder>()
{

    inner class ViewHolder(view: View): RecyclerView.ViewHolder(view)
    {
        val bookImage: ImageView =view.findViewById(R.id.book_image)
        val bookName: TextView =view.findViewById(R.id.book_name)
        val bookPrice: TextView =view.findViewById(R.id.book_price)
        val btn:Button=view.findViewById(R.id.btn_xiangqing)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int):ViewHolder {
        val view= LayoutInflater.from(parent.context)
                .inflate(R.layout.mycarlist,parent,false)
        val holder=ViewHolder(view)


        return holder
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val book = bookList[position]
        val image1 = image[position]
        val order=orderList[position]
        holder.bookImage.setImageBitmap(image1)
        holder.bookName.text = book.book_name
        holder.bookPrice.text = book.book_price.toString()
        holder.btn.setOnClickListener {
            val mintent = Intent(it.context,myTestcarxiangqing::class.java)
            mintent.putExtra("order_id",order.order_id.toString())
            mintent.putExtra("book_id", book.book_id.toString())
            mintent.putExtra("book_user_account", book.book_user_account)
            mintent.putExtra("book_name", book.book_name)
            mintent.putExtra("book_type", book.book_type)
            mintent.putExtra("book_information", book.book_information)
            mintent.putExtra("book_price", book.book_price.toString())
            mintent.putExtra("book_date", book.book_date)
            mintent.putExtra("book_state", book.book_state.toString())
            mintent.putExtra("image", Utils.btimapToBtyes(image1))
            it.context.startActivity(mintent)
        }
    }

    override fun getItemCount(): Int =bookList.size

}