package by.yura.quizapp.listener

import android.view.View

interface OnFragmentListener {
    fun onClickBtn(view: View)
    fun onItemClick(position: Int)
    fun onFinish(rightAnswers: Int, wrongAnswers: Int, score: Int)
}