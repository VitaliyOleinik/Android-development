package com.example.retrofitexample.model

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.retrofitexample.model.Post

@Dao
interface PostDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(list: List<Post>)
    @Query("SELECT * FROM post_table")
    fun getAll() : List<Post>
}