package com.heixss.findmypet.data.searchpets.mappers

import com.heixss.findmypet.data.profile.UserProfileDto
import com.heixss.findmypet.domain.profile.User

fun UserProfileDto.toUser(): User {
    return User(
        photo = photo,
        firstName = firstName,
        lastName = lastName,
        phone = phone,
        email = email,
        address = address,
        pets = pets?.map { it.toPet() } ?: emptyList()
    )
}
