package com.example.retrofitexample.view_model

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.retrofitexample.RetrofitService
import com.example.retrofitexample.model.Post
import com.example.retrofitexample.model.PostDAO
import com.example.retrofitexample.model.PostDatabase
import com.example.retrofitexample.view.MainActivity
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

class PostListViewModel(
    private val context: Context
    ) : ViewModel(), CoroutineScope {

    private val job = Job()

    private val postDAO: PostDAO

    private val liveData = MutableLiveData<List<Post>>()

    init {
        postDAO = PostDatabase.getDatabase(context).postDao()
    }

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + job

    override fun onCleared() {
        super.onCleared()
        job.cancel()
    }

    fun getPosts(){
        launch {
            val list = withContext(Dispatchers.IO){
                try {
                    val response = RetrofitService.getPostApi().getPostListCoroutine()
                    if (response.isSuccessful) {
                        val result = response.body()
                        if (!result.isNullOrEmpty()) {
                            postDAO.insertAll(result)
                        }
                        result
                    } else {
                        postDAO.getAll() ?: emptyList()
                    }
                } catch (e: Exception) {
                    postDAO.getAll() ?: emptyList<Post>()
                }
            }
            liveData.postValue(list)
        }
    }
}