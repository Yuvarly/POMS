package by.yura.quizapp

import android.os.AsyncTask
import android.util.Log
import androidx.viewpager.widget.ViewPager

class TaskPager(var viewPager: ViewPager, var currentPage : Int, var boolean: Boolean) : AsyncTask<Void, Void, Void>() {
    override fun doInBackground(vararg params: Void?): Void? {
        Thread.sleep(500)
        return null
    }

    override fun onPostExecute(result: Void?) {
        super.onPostExecute(result)
        if (boolean) {
            viewPager.currentItem = currentPage

        }
        else{
            viewPager.currentItem = currentPage
        }
    }
}