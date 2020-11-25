package by.yura.quizapp.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import by.yura.quizapp.R
import by.yura.quizapp.adapter.UserListAdapter
import by.yura.quizapp.database.DBHelper
import by.yura.quizapp.listener.FirebaseListener
import by.yura.quizapp.model.User

class RatingFragment : Fragment(), FirebaseListener {

    private lateinit var recyclerView: RecyclerView

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_rating, container, false)
        var userList = DBHelper.getInstance(inflater.context).getUsers(this)
        val button = view.findViewById<Button>(R.id.button6)
        recyclerView = view.findViewById<RecyclerView>(R.id.recycler_view2)

        recyclerView.layoutManager = LinearLayoutManager(inflater.context, LinearLayoutManager.VERTICAL, false)
//        recyclerView.adapter = UserListAdapter(userList)
        recyclerView.addItemDecoration(DividerItemDecoration(view.context, DividerItemDecoration.VERTICAL))

        button.setOnClickListener {
            DBHelper.getInstance(inflater.context).deleteAllUsers()
            userList = emptyList()
            recyclerView.adapter = UserListAdapter(userList)
        }
        return view
    }

    override fun update(list: List<Any>?) {

//        recyclerView.layoutManager = LinearLayoutManager(inflater.context, LinearLayoutManager.VERTICAL, false)
        recyclerView.adapter = UserListAdapter(list as List<User>)
//        recyclerView.addItemDecoration(DividerItemDecoration(view.context, DividerItemDecoration.VERTICAL))

    }
}
