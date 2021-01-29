package com.example.schooassitant

import android.app.AlertDialog
import android.graphics.Bitmap
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import kotlin.concurrent.thread

class XiajiaAdapter(val bookList:List<Book>,val imageList:List<Bitmap>):
    RecyclerView.Adapter<XiajiaAdapter.ViewHolder>() {
    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val itemImage: ImageView = view.findViewById(R.id.my_img)
        val itemName: TextView = view.findViewById(R.id.my_item_name)
        val itemPrice: TextView = view.findViewById(R.id.my_price)
        val btn_del:Button=view.findViewById(R.id.btn_shanchu)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.mypost_item, parent, false)
        val holder = ViewHolder(view)
        return holder
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = bookList[position]
        val image = imageList[position]
        holder.itemImage.setImageBitmap(image)
        holder.itemName.text = item.book_name
        holder.itemPrice.text = item.book_price.toString()
        holder.btn_del.setOnClickListener {
            AlertDialog.Builder(it.context).apply { 
                setTitle("")
                setMessage("是否删除这条发布")
                setCancelable(false)
                setPositiveButton("是"){dialog,which->"是"
                    Toast.makeText(it.context, "删除成功!", Toast.LENGTH_SHORT).show()
                    thread {
                        try{
                        var delUrl = HttpUtils.Url + "UserDelBook?id=" + item.book_id.toString()
                        HttpUtils.GetRequest(delUrl)
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

    override fun getItemCount(): Int=bookList.size
}