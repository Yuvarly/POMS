package by.yura.quizapp.adapter

import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.recyclerview.widget.RecyclerView
import by.yura.quizapp.listener.OnAdapterClickItemListener
import by.yura.quizapp.R
import by.yura.quizapp.model.Answer
import java.util.*
import kotlin.collections.ArrayList


class AnswerListAdapter(private val answerList: List<Answer>) : RecyclerView.Adapter<AnswerListAdapter.ViewHolderItem>() {
    lateinit var mbutton: Button
    val buttons = ArrayList<Button>()
    init {
        Collections.shuffle(answerList)
    }
    inner class ViewHolderItem(itemView : View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {
        var button = itemView.findViewById<Button>(R.id.button3)
        init {
            button.setOnClickListener(this)
        }
        override fun onClick(v: View?) {
            if(answerList[adapterPosition].right) {
                button.setBackgroundColor(Color.GREEN)
            } else {
                button.setBackgroundColor(Color.RED)
                mbutton.setBackgroundColor(Color.GREEN)
            }

            for(button in buttons) {
                button.isEnabled = false
            }
            mbutton.isEnabled = false

            onAdapterClickItemListener.onClickItemBoolean(answerList[adapterPosition].right)
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderItem {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.answer_item, parent, false)
        return ViewHolderItem(itemView)
    }

    override fun getItemCount(): Int {
        return answerList.size
    }

    override fun onBindViewHolder(holder: ViewHolderItem, position: Int) {
        holder.button.text = answerList[position].content
        if (answerList[position].right) {
            mbutton = holder.button
        }
        else {
            buttons.add(holder.button)
        }
    }

    private lateinit var onAdapterClickItemListener : OnAdapterClickItemListener

    fun setMyOnClickItemListener(onAdapterClickItemListener : OnAdapterClickItemListener) {
        this.onAdapterClickItemListener = onAdapterClickItemListener
    }
}