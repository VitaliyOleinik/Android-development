package com.example.retrofitexample.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.retrofitexample.R
import com.example.retrofitexample.RetrofitService
import com.example.retrofitexample.model.Post
import com.example.retrofitexample.model.PostDAO
import com.example.retrofitexample.model.PostDatabase
import com.example.retrofitexample.view_model.PostListViewModel
import com.example.retrofitexample.view_model.ViewModelProviderFactory
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

class MainActivity : AppCompatActivity(), PostAdapter.RecyclerViewItemClick {

    lateinit var recyclerView: RecyclerView
    lateinit var swipeRefreshLayout: SwipeRefreshLayout

    private var postAdapter: PostAdapter? = null

    private lateinit var postListViewModel: PostListViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val viewModelProviderFactory = ViewModelProviderFactory(this)
        postListViewModel = ViewModelProvider(this, viewModelProviderFactory)
            .get(PostListViewModel::class.java)

        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)

        swipeRefreshLayout = findViewById(R.id.swipeRefreshLayout)
        swipeRefreshLayout.setOnRefreshListener {
            postAdapter?.clearAll()
            postListViewModel.getPosts()
        }

        postAdapter = PostAdapter(itemClickListener = this)
        recyclerView.adapter = postAdapter

        postListViewModel.getPosts()

        swipeRefreshLayout.isRefreshing = true
        postListViewModel.liveData.observe(this, Observer { result ->
            postAdapter?.list = result
            postAdapter?.notifyDataSetChanged()
            swipeRefreshLayout.isRefreshing = false
        })
    }

    override fun itemClick(position: Int, item: Post) {
        //Toast.makeText(this, item.title, Toast.LENGTH_SHORT).show()

        val intent = Intent(this, PostDetailActivity::class.java)
        intent.putExtra("post_id", item.id)
        startActivity(intent)
    }
}