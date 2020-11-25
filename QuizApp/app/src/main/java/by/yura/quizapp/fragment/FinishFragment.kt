package by.yura.quizapp.fragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import by.yura.quizapp.listener.OnFragmentListener
import by.yura.quizapp.R
import by.yura.quizapp.database.DBHelper
import by.yura.quizapp.listener.FirebaseListener
import by.yura.quizapp.model.User
import kotlinx.android.synthetic.main.fragment_finish.*

class FinishFragment(private val rightAnswers: Int, private val wrongAnswers: Int, private val score: Int) : Fragment(), FirebaseListener {

    private lateinit var onFragmentListener: OnFragmentListener
    lateinit var  button : Button

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_finish, container, false)
        button = view.findViewById<Button>(R.id.button4)
        val textView = view.findViewById<TextView>(R.id.textView5)
        val textView2 = view.findViewById<TextView>(R.id.textView8)
        val textView3 = view.findViewById<TextView>(R.id.textView9)

        textView.text = rightAnswers.toString()
        textView2.text = wrongAnswers.toString()
        textView3.text = "СЧЁТ: $score"

        button.setOnClickListener {
            if (!(editText.text.toString() == "")) {
                val dbHelper = DBHelper.getInstance(inflater.context)
                val editText = view.findViewById<EditText>(R.id.editText)

                dbHelper.insertUser(User(editText.text.toString(), rightAnswers, wrongAnswers, score), this)

//                onFragmentListener.onClickBtn(button)
            } else
                Toast.makeText(view.context, "Enter your name!", Toast.LENGTH_LONG).show()
        }
        return view
    }

    fun setOnFragmentListener(onFragmentListener: OnFragmentListener) {
        this.onFragmentListener = onFragmentListener
    }

    override fun update(list: List<Any>?) {
        Log.i("TAG", "updatef: ")
        onFragmentListener.onClickBtn(button)
    }

}
