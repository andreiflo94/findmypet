package com.heixss.findmypet.domain.searchpets

import androidx.paging.Pager

interface PetRepository {
    fun getPetPager(query: String): Pager<Int, Pet>
}