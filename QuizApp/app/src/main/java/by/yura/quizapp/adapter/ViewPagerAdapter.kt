package by.yura.quizapp.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager.widget.PagerAdapter
import by.yura.quizapp.listener.OnAdapterClickItemListener
import by.yura.quizapp.R
import by.yura.quizapp.database.DBHelper
import by.yura.quizapp.model.Answer
import by.yura.quizapp.model.Question


class ViewPagerAdapter(private var questionList: List<Question>, private var mContext: Context, private var complexity: Array<String>) :
    PagerAdapter(), OnAdapterClickItemListener {
    private lateinit var answerList: List<Answer>
    private var position: Int = 0
    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view == `object`
    }

    override fun getCount(): Int {
        return questionList.size
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val view = LayoutInflater.from(mContext).inflate(R.layout.page_item, container, false)
        val textView = view.findViewById<TextView>(R.id.textView3)
        Log.d("MYLOG", questionList[position].content)
//            textView.text = questionList[position].content
        textView.setText(questionList[position].content)
        this.position = position
        val recyclerView = view.findViewById<RecyclerView>(R.id.recycler_view3)
        recyclerView.layoutManager = GridLayoutManager(mContext, 2)


        val imageView = view.findViewById<ImageView>(R.id.imageView4)
        val bitmap = DBHelper.getInstance(mContext).getImage(questionList[position].id)
        imageView.setImageBitmap(bitmap)

//        answerList =
//            DBHelper.getInstance(mContext).getAnswers(questionList[position].id, complexity)

        answerList = DBHelper.getInstance(mContext).getAnswerF(questionList[position].id)

        var adapter = AnswerListAdapter(answerList)
        recyclerView.adapter = adapter
        adapter.setMyOnClickItemListener(this)
        container.addView(view)
        return view
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as LinearLayout)
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return (position + 1).toString()
    }

    override fun onClickItem(position: Int) {
    }

    override fun onClickItemBoolean(boolean: Boolean) {
        Log.d("MYLOG", boolean.toString())
        monViewPagerListener.onClick(boolean)
    }

    interface OnViewPagerListener {
        fun onClick(boolean: Boolean)
    }

    private lateinit var monViewPagerListener: OnViewPagerListener

    fun setOnViewPagerListener(onViewPagerListener: OnViewPagerListener) {
        this.monViewPagerListener = onViewPagerListener
    }

}