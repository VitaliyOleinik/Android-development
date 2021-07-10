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

class MainActivity : AppCompatActivity() {

    lateinit var recyclerView: RecyclerView

    private var postAdapter: PostAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)

        postAdapter = PostAdapter(itemClickListener = this)
        recyclerView.adapter = postAdapter

        getPosts()
    }

    override fun itemClick(position: Int, item: Post){
        Toast.makeText(this, item.title, Toast.LENGTH_SHORT).show()
    }

    private fun getPosts() {
        RetrofitService.getPostApi().getPostList().enqueue(object : Callback<List<Post>> {
            override fun onFailure(call: Call<List<Post>>, t: Throwable) {
            }

            override fun onResponse(call: Call<List<Post>>, response: Response<List<Post>>) {
                Log.d("My_post_list", response.body().toString())

                    val list = response.body()
                    postAdapter?.list = list
                    postAdapter?.notifyDataSetChanged()

            }
        })
    }
}