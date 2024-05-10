package com.heixss.findmypet.domain.profile

import com.heixss.findmypet.data.Resource

interface UserRepository {

    suspend fun getUserProfile(): Resource<User>

    suspend fun updateUserProfile(
        firstName: String, lastName: String, address: String,
        phone: String,
        email: String
    ): Resource<User>

    suspend fun logOut()
}