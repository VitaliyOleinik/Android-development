package com.example.retrofitexample

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.FieldPosition

class MainActivity : AppCompatActivity(), PostAdapter.RecyclerViewItemClick {

    lateinit var recyclerView: RecyclerView
    lateinit var swipeRefreshLayout: SwipeRefreshLayout

    private var postAdapter: PostAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)

        swipeRefreshLayout = findViewById(R.id.swipeRefreshLayout)
        swipeRefreshLayout.setOnRefreshListener {
            postAdapter?.clearAll()
            getPosts()
        }

        postAdapter = PostAdapter(itemClickListener = this)
        recyclerView.adapter = postAdapter

        getPosts()
    }

    override fun itemClick(position: Int, item: Post) {
        val intent = Intent(this, PostDetailActivity::class.java)
        intent.putExtra("post_id", item.postId)
        startActivity(intent)
    }

    private fun getPosts() {
        swipeRefreshLayout.isRefreshing = true
        RetrofitService.getPostApi().getPostList().enqueue(object : Callback<List<Post>> {
            override fun onFailure(call: Call<List<Post>>, t: Throwable) {
                swipeRefreshLayout.isRefreshing = false
            }

            override fun onResponse(call: Call<List<Post>>, response: Response<List<Post>>) {
                Log.d("My_post_list", response.body().toString())
                if (response.isSuccessful) {
                    val list = response.body()
                    postAdapter?.list = list
                    postAdapter?.notifyDataSetChanged()
                }
                swipeRefreshLayout.isRefreshing = false
            }
        })
    }
}