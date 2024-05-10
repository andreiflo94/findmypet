package com.heixss.findmypet.data.profile

import com.heixss.findmypet.data.searchpets.getpetsresponse.PetDto

class UserProfileDto(
    val photo: String?,
    val firstName: String?,
    val lastName: String?,
    val phone: String?,
    val email: String?,
    val address: String?,
    val pets: List<PetDto>?
)