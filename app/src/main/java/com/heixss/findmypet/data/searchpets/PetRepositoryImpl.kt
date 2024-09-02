package com.heixss.findmypet.data.searchpets

import androidx.paging.Pager
import androidx.paging.PagingConfig
import com.heixss.findmypet.data.common.FakeRemoteApi
import com.heixss.findmypet.data.common.RemoteApi
import com.heixss.findmypet.domain.searchpets.Pet
import com.heixss.findmypet.domain.searchpets.PetRepository
import javax.inject.Inject

class PetRepositoryImpl @Inject constructor(private val remoteApi: FakeRemoteApi) : PetRepository {

    override fun getPetPager(query: String): Pager<Int, Pet> {
        return Pager(
            config = PagingConfig(pageSize = 20),
            pagingSourceFactory = {
                SearchPetsPagingSource(remoteApi = remoteApi, query = query)
            }
        )
    }
}