package com.rifqi.testpaging3.data.injection

import android.content.Context
import com.rifqi.testpaging3.data.QuoteRepository
import com.rifqi.testpaging3.data.local.QuoteDatabase
import com.rifqi.testpaging3.network.ApiConfig

object DependencyInjection {
    fun parameterStory(context: Context, tokenUser: String): QuoteRepository {
        val db = QuoteDatabase.getDatabase(context)
        val service = ApiConfig.getService()
        return QuoteRepository(db, service, tokenUser)
    }
}