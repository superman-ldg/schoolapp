import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.schooassitant.Book
import com.example.schooassitant.ProductInfoActivity
import com.example.schooassitant.R





//主页显示
class PostAdapter(val bookList:List<Book>):
        RecyclerView.Adapter<PostAdapter.ViewHolder>()
{


    inner class ViewHolder(view: View): RecyclerView.ViewHolder(view)
    {
        val itemImage: ImageView =view.findViewById(R.id.my_img)
        val itemName: TextView =view.findViewById(R.id.my_item_name)
        val itemPrice: TextView =view.findViewById(R.id.my_price)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view= LayoutInflater.from(parent.context)
                .inflate(R.layout.mypost_item,parent,false)
        val holder=ViewHolder(view)


        holder.itemView.setOnClickListener { ProductInfoActivity.actionStart(parent.context) }

        return holder
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item=bookList[position]
        //holder.itemImage.setImageResource(item.imageId)
        holder.itemName.text=item.book_name
        holder.itemPrice.text=item.book_price.toString()

    }


    override fun getItemCount(): Int =bookList.size
}