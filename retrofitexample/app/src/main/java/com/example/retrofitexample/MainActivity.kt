package com.example.retrofitexample

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import kotlinx.coroutines.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import java.text.FieldPosition
import kotlin.coroutines.CoroutineContext

class MainActivity : AppCompatActivity(), PostAdapter.RecyclerViewItemClick, CoroutineScope {

    lateinit var recyclerView: RecyclerView
    lateinit var swipeRefreshLayout: SwipeRefreshLayout

    private val job = Job()

    private var postDAO: PostDAO? = null

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + job

    private var postAdapter: PostAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        postDAO = PostDatabase.getDatabase(context = this).postDao()

        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)

        swipeRefreshLayout = findViewById(R.id.swipeRefreshLayout)
        swipeRefreshLayout.setOnRefreshListener {
            postAdapter?.clearAll()
            getPostCoroutine()
        }

        postAdapter = PostAdapter(itemClickListener = this)
        recyclerView.adapter = postAdapter

        getPostCoroutine()
    }

    override fun onDestroy() {
        super.onDestroy()
        job.cancel()
    }

    override fun itemClick(position: Int, item: Post) {
        //Toast.makeText(this, item.title, Toast.LENGTH_SHORT).show()

        val intent = Intent(this, PostDetailActivity::class.java)
        intent.putExtra("post_id", item.id)
        startActivity(intent)
    }

    private fun getPostCoroutine() {
        launch {
            swipeRefreshLayout.isRefreshing = true
            val list = withContext(Dispatchers.IO){
                try {
                    val response = RetrofitService.getPostApi().getPostListCoroutine()
                    if (response.isSuccessful) {
                        val result = response.body()
                        if (!result.isNullOrEmpty()) {
                            postDAO?.insertAll(result)
                        }
                        result
                    } else {
                        postDAO?.getAll() ?: emptyList()
                    }
                } catch (e: Exception) {
                    postDAO?.getAll() ?: emptyList<Post>()
                }

            }
            postAdapter?.list = list
            postAdapter?.notifyDataSetChanged()
            swipeRefreshLayout.isRefreshing = false
        }
    }


}