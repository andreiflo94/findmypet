package com.heixss.findmypet

import androidx.paging.PagingConfig
import androidx.paging.PagingSource
import androidx.paging.testing.TestPager
import com.google.common.truth.Truth.assertThat
import com.heixss.findmypet.data.searchpets.mappers.toPet
import com.heixss.findmypet.data.common.FakeRemoteApi
import com.heixss.findmypet.data.searchpets.SearchPetsPagingSource
import com.heixss.findmypet.domain.searchpets.Pet
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class SearchPetsPagingSourceTest {

    private val fakeApi = FakeRemoteApi()
    private lateinit var pager: TestPager<Int, Pet>
    private lateinit var pagingSource: SearchPetsPagingSource

    @Before
    fun load(){
        pagingSource = SearchPetsPagingSource(
            fakeApi,
            ""
        )

        pager = TestPager(PagingConfig(pageSize = 20), pagingSource)

    }

    @Test
    fun loadReturnsPageWhenOnSuccessfulLoadOfItemKeyedData() = runTest {
        val result = pager.refresh() as PagingSource.LoadResult.Page

        val petList = fakeApi.mockedGetPetsResponse.petResponse?.map { it.toPet() }

        assertThat(result.data)
            .containsExactlyElementsIn(petList)
            .inOrder()
    }

    @Test
    fun test_consecutive_loads() = runTest {

        val page = with(pager) {
            refresh()
            append()
            append()
        } as PagingSource.LoadResult.Page

        val petList = fakeApi.mockedGetPetsResponse.petResponse?.map { it.toPet() }

        assertThat(page.data)
            .containsExactlyElementsIn(petList)
            .inOrder()
    }

    @Test
    fun refresh_returnError() {

        // Configure your fake to return errors
        fakeApi.setReturnsError()
        runTest {
            val result = pager.refresh()
            assertTrue(result is PagingSource.LoadResult.Error)

            val page = pager.getLastLoadedPage()
            assertThat(page).isNull()
        }
    }
}