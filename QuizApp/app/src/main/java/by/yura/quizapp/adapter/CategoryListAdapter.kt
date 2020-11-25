package by.yura.quizapp.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import by.yura.quizapp.listener.OnAdapterClickItemListener
import by.yura.quizapp.R
import by.yura.quizapp.model.Category


class CategoryListAdapter(private var categoryList: List<Category>, private val context: Context) :
    RecyclerView.Adapter<CategoryListAdapter.ViewHolderItem>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderItem {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item, parent, false)
        return ViewHolderItem(view)
    }

    override fun getItemCount(): Int {
        return categoryList.size
    }

    override fun onBindViewHolder(holder: ViewHolderItem, position: Int) {
        holder.textView.text = categoryList[position].text
    }


     inner class ViewHolderItem(view: View) : RecyclerView.ViewHolder(view), View.OnClickListener {
        var textView: TextView = view.findViewById(R.id.textView2)

        init {
            view.setOnClickListener(this)
        }

        override fun onClick(v: View?) {

            onAdapterClickItemListener.onClickItem(categoryList[adapterPosition].id)

        }

    }
    private lateinit var onAdapterClickItemListener : OnAdapterClickItemListener

    fun setMyOnClickItemListener(onAdapterClickItemListener : OnAdapterClickItemListener) {
        this.onAdapterClickItemListener = onAdapterClickItemListener
    }
}