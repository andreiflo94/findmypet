package com.heixss.findmypet.domain.profile

import com.heixss.findmypet.domain.searchpets.Pet

data class User(
    val photo: String?,
    val firstName: String?,
    val lastName: String?,
    val phone: String?,
    val email: String?,
    val address: String?,
    val pets: List<Pet>?
)