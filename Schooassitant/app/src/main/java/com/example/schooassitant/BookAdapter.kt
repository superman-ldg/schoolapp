package com.example.schooassitant

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream

class BookAdapter(val bookList:List<Book>,val image:List<Bitmap>):
    RecyclerView.Adapter<BookAdapter.ViewHolder>() {

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val bookImage: ImageView = view.findViewById(R.id.mybook_image)
        val bookName: TextView = view.findViewById(R.id.mybook_name)
        val bookPrice: TextView = view.findViewById(R.id.mybook_price)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.book_item_, parent, false)
        val holder = ViewHolder(view)
        return holder
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val book = bookList[position]
        val image1 = image[position]
        holder.bookImage.setImageBitmap(image1)
        holder.bookName.text = book.book_name
        holder.bookPrice.text = book.book_price.toString()

        holder.itemView.setOnClickListener {
            val mintent = Intent(it.context, ProductInfoActivity::class.java)
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

    override fun getItemCount(): Int = bookList.size

}