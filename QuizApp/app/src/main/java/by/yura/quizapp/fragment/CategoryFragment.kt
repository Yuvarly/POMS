package by.yura.quizapp.fragment

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import by.yura.quizapp.listener.OnAdapterClickItemListener
import by.yura.quizapp.listener.OnFragmentListener
import by.yura.quizapp.R
import by.yura.quizapp.adapter.CategoryListAdapter
import by.yura.quizapp.database.DBHelper
import by.yura.quizapp.listener.FirebaseListener
import by.yura.quizapp.model.Category

class CategoryFragment : Fragment(),
    OnAdapterClickItemListener, FirebaseListener {

    lateinit var recyclerView: RecyclerView
    lateinit var mContext: Context
    lateinit var monFragmentListener: OnFragmentListener
    lateinit var mView: View

    lateinit var adapter: CategoryListAdapter

    fun setOnFragmentListener(monFragmentListener: OnFragmentListener) {
        this.monFragmentListener = monFragmentListener
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        mContext = inflater.context


//        val categoryList = DBHelper.getInstance(inflater.context).getCategories(object : FirebaseListener{
//            override fun update(list: List<Category>) {
//
////                adapter = CategoryListAdapter(categoryList, inflater.context)
//
//
//
//            }
//
//        })

        DBHelper.getInstance(mContext).getCategories(this)


        val view = inflater.inflate(R.layout.fragment_category, container, false)
        val button = view.findViewById<Button>(R.id.button8)

        recyclerView = view.findViewById(R.id.recycler_view)
        recyclerView.layoutManager = GridLayoutManager(inflater.context, 2)
        recyclerView.addItemDecoration(DividerItemDecoration(view.context, DividerItemDecoration.VERTICAL))
        recyclerView.addItemDecoration(DividerItemDecoration(view.context, DividerItemDecoration.HORIZONTAL))


//        adapter = CategoryListAdapter(categoryList, inflater.context)

//        var adapter = CategoryListAdapter(categoryList, inflater.context)
//        recyclerView.adapter = adapter

//        adapter.setMyOnClickItemListener(this)

        mView = view

        button.text = "Легко"

        button.setOnClickListener {
            if (button.text == "Легко") button.text = "Сложно"
            else button.text = "Легко"
        }




        return view
    }


    override fun onClickItem(position: Int) {
        monFragmentListener.onItemClick(position)

    }

    override fun onClickItemBoolean(boolean: Boolean) {

    }

    override fun update(list: List<Any>?) {
        var adapter = CategoryListAdapter(list as List<Category>, mContext)
        recyclerView.adapter = adapter
        adapter.setMyOnClickItemListener(this)
    }

}
