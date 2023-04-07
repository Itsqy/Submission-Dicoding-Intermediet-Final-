package com.rifqi.testpaging3

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import androidx.paging.AsyncPagingDataDiffer
import androidx.paging.PagingData
import androidx.recyclerview.widget.ListUpdateCallback
import com.rifqi.testpaging3.data.QuoteRepository
import com.rifqi.testpaging3.data.remote.ListStoryData
import com.rifqi.testpaging3.menu.StoryAdapter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class StoryViewModelTest {
    @get:Rule
    val instantExecutor = InstantTaskExecutorRule()

    @get:Rule
    val mainDispatcherRules = MainDispatchRules()


    @Mock
    private lateinit var quoteRepository: QuoteRepository


    @Test
    fun `when Get Quote Should Not Null and Return Data`() = runTest {

        val dummyStory = Dummy.generateDummyQuoteResponse()
        val data: PagingData<ListStoryData> = PageDataSource.snapshot(dummyStory)
        val expectedQuote = MutableLiveData<PagingData<ListStoryData>>()
        expectedQuote.value = data


        Mockito.`when`(quoteRepository.getQuote("tokenUser")).thenReturn(expectedQuote)
        val storyViewModel = StoryViewModel(quoteRepository)
        val actualStories: PagingData<ListStoryData> = storyViewModel.stories("tokenUser").getOrAwaitValue()



//        input paging data to adapter
        val differ = AsyncPagingDataDiffer(
            diffCallback = StoryAdapter.DIFF_CALLBACK,
            updateCallback = noopListUpdateCallback,
            workerDispatcher = Dispatchers.Main
        )
        differ.submitData(actualStories)

        advanceUntilIdle()

        assertNotNull(differ.snapshot())
        assertEquals(dummyStory.size, differ.snapshot().size)
        assertEquals(dummyStory[0].name, differ.snapshot()[0]?.name)
    }

}

val noopListUpdateCallback = object : ListUpdateCallback {
    override fun onInserted(position: Int, count: Int) {}
    override fun onRemoved(position: Int, count: Int) {}
    override fun onMoved(fromPosition: Int, toPosition: Int) {}
    override fun onChanged(position: Int, count: Int, payload: Any?) {}
}