package by.yura.quizapp.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewpager.widget.ViewPager
import by.yura.quizapp.listener.OnFragmentListener
import by.yura.quizapp.R
import by.yura.quizapp.TaskPager
import by.yura.quizapp.adapter.ViewPagerAdapter
import by.yura.quizapp.model.Question

class QuestionFragment(private var questionList: List<Question>, private var complexity: Array<String>) : Fragment(),
    ViewPagerAdapter.OnViewPagerListener {
    lateinit var viewPager: ViewPager
    var currentPage: Int = 0
    var rightAnswers : Int = 0
    var wrongAnswers : Int = 0
    var score : Int = 0

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_question, container, false)
        viewPager = view.findViewById(R.id.view_pager)





        var adapter = ViewPagerAdapter(questionList, inflater.context, complexity)
        activity?.setTitle("$currentPage/${questionList.size}")


        viewPager.adapter = adapter
//        viewPager.setOnTouchListener { v, event -> true }
        viewPager.offscreenPageLimit = 15
        adapter.setOnViewPagerListener(this)
        currentPage = viewPager.currentItem
        return view
    }

    override fun onClick(boolean: Boolean) {


        if (boolean) {
            rightAnswers++
//            viewPager.currentItem = currentPage + 1
            currentPage++

        }
        else{
            wrongAnswers++
//            viewPager.currentItem = currentPage + 1
            currentPage++
        }
            TaskPager(viewPager, currentPage, boolean).execute()



        if(currentPage == questionList.size) {
            score = 10 * rightAnswers
            monFragmentListener.onFinish(rightAnswers, wrongAnswers, score)
        }

        activity?.setTitle("$currentPage/${questionList.size}")
    }

    lateinit var monFragmentListener: OnFragmentListener

    fun setOnFragmentListener(monFragmentListener: OnFragmentListener) {
        this.monFragmentListener = monFragmentListener
    }
}
