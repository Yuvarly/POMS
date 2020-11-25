package by.yura.quizapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import by.yura.quizapp.database.DBHelper
import by.yura.quizapp.fragment.*
import by.yura.quizapp.listener.FirebaseListener
import by.yura.quizapp.listener.OnFragmentListener
import by.yura.quizapp.model.Question

class MainActivity : AppCompatActivity(),
    OnFragmentListener, FirebaseListener {

    private lateinit var fragmentTransaction: FragmentTransaction
    private lateinit var questionFragment: QuestionFragment
    private lateinit var categoryFragment: CategoryFragment
    private lateinit var startFragment: StartFragment
    private lateinit var fragmentManager: FragmentManager
    private lateinit var ratingFragment: RatingFragment
    private lateinit var finishFragment: FinishFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        ratingFragment = RatingFragment()

        fragmentManager = supportFragmentManager


        startFragment = StartFragment()



        fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.add(R.id.frame_layout, startFragment)
        fragmentTransaction.commit()
        startFragment.setOnFragmentListener(this)


    }

    override fun onClickBtn(view: View) {
        title = "QuizApp"

        if (view.id == R.id.button) {
//            categoryFragment = CategoryFragment()
//
//            val fragmentTransaction = fragmentManager.beginTransaction()
//            fragmentTransaction.replace(R.id.frame_layout, categoryFragment)
//            fragmentTransaction.addToBackStack(null)
//            fragmentTransaction.commit()
//
//            categoryFragment.setOnFragmentListener(this)

            onItemClick(2)

        }
        if (view.id == R.id.button2) {
            supportFragmentManager
                .beginTransaction()
                .replace(R.id.frame_layout, ratingFragment)
                .addToBackStack(null)
                .commit()
        }

        if (view.id == R.id.button4) {

            clearStack()

            supportFragmentManager
                .beginTransaction()
                .replace(R.id.frame_layout, ratingFragment)
                .addToBackStack(null)
                .commit()

        }
    }
    override fun onItemClick(position: Int) {


        Log.i("TAG", "onItemClick: " + position)

        if(position>=2) {
            DBHelper.getInstance(this).getQuestionsFFireBase(this)
        } else {
            DBHelper.getInstance(this).getQuestionsFireBase(this)

        }
//        val questionList = DBHelper.getInstance(this).getQuestions(position)

    }

    override fun onFinish(rightAnswers: Int, wrongAnswers: Int, score: Int) {
        clearStack()
        title = "QuizApp"

        finishFragment = FinishFragment(
            rightAnswers,
            wrongAnswers,
            score
        )
        finishFragment.setOnFragmentListener(this)
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.frame_layout, finishFragment)
            .addToBackStack(null)
            .commit()


    }


    private fun clearStack() {
        var count = fragmentManager.backStackEntryCount

        while (count > 0) {
            fragmentManager.popBackStack()
            count--
        }
    }

    override fun update(list: List<Any>?) {

        Log.i("TAG", "update: ")

        val button = findViewById<Button>(R.id.button8)
        val complexity: Array<String> = arrayOf("1", "0")

//        complexity = if (button.text == "Легко") arrayOf("1", "0")
//        else arrayOf("1", "2")

        questionFragment =
            QuestionFragment(list as List<Question>, complexity)
        questionFragment.setOnFragmentListener(this)

        supportFragmentManager
            .beginTransaction()
            .replace(R.id.frame_layout, questionFragment)
            .addToBackStack(null)
            .commit()
    }
}
