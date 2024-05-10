package com.heixss.findmypet.data.searchpets


import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.heixss.findmypet.data.searchpets.mappers.toPet
import com.heixss.findmypet.data.common.RemoteApi
import com.heixss.findmypet.domain.searchpets.Pet

class SearchPetsPagingSource(
    private val remoteApi: RemoteApi,
    private val query: String
) : PagingSource<Int, Pet>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Pet> {
        val currentPage = params.key ?: 1
        return try {
            val response = remoteApi.getPets(query = query, pageSize = 20, pageNumber = currentPage)
            if (response.isSuccessful) {
                val endOfPaginationReached = response.body()?.petResponse?.isEmpty() == true
                val petsEntities = arrayListOf<Pet>()
                response.body()?.petResponse?.forEach { petDto ->
                    petsEntities.add(petDto.toPet())
                }
                if (response.body()?.petResponse?.isNotEmpty() == true) {
                    LoadResult.Page(
                        data = petsEntities,
                        prevKey = if (currentPage == 1) null else currentPage - 1,
                        nextKey = if (endOfPaginationReached) null else currentPage + 1
                    )
                } else {
                    LoadResult.Page(
                        data = emptyList(),
                        prevKey = null,
                        nextKey = null
                    )
                }
            } else {
                response.errorBody()?.let { LoadResult.Error(Throwable(it.string())) } ?: run { LoadResult.Error(Exception()) }
            }
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, Pet>): Int? {
        return state.anchorPosition
    }

}