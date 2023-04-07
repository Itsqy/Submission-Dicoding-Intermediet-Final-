package com.rifqi.testpaging3

import com.rifqi.testpaging3.data.remote.ListStoryData

object Dummy {
    fun generateDummyQuoteResponse(): List<ListStoryData> {
        val items: MutableList<ListStoryData> = arrayListOf()
        for (i in 0..100) {
            val quote = ListStoryData(
                i.toString(),
                "author + $i",
                "quote $i",
                "photo + $i ",
                "date + $i"
            )
            items.add(quote)
        }
        return items
    }
}