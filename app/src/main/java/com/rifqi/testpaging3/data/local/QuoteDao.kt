package com.rifqi.testpaging3.data.local

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.rifqi.testpaging3.data.remote.ListStoryData

@Dao
interface QuoteDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
     suspend fun insertQuote(quote: List<ListStoryData>)

    @Query("SELECT * FROM listStoryData")
    fun getAllQuote(): PagingSource<Int, ListStoryData>


    @Query("DELETE FROM listStoryData")
    suspend fun deleteAll()
}