package com.example.retrofitexample.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import android.widget.TextView
import com.example.retrofitexample.R
import com.example.retrofitexample.RetrofitService
import com.example.retrofitexample.model.Post
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class PostDetailActivity : AppCompatActivity() {

    private lateinit var progressBar: ProgressBar
    private lateinit var tvTitle: TextView
    private lateinit var tvBody: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_post_detail)

        progressBar = findViewById(R.id.progressBar)
        tvTitle = findViewById(R.id.tvTitle)
        tvBody = findViewById(R.id.tvBody)

        val postId = intent.getIntExtra("post_id", 1)

        getPost(postId)
    }

    private fun getPost(id: Int) {
        RetrofitService.getPostApi().getPostById(id).enqueue(object : Callback<Post> {
            override fun onResponse(call: Call<Post>, response: Response<Post>) {
                progressBar.visibility = View.GONE
                val post = response.body()
                if (post != null) {
                    tvBody.text = post.body
                    tvTitle.text = post.title
                }
            }

            override fun onFailure(call: Call<Post>, t: Throwable) {
                progressBar.visibility = View.GONE
            }
        })
    }
}