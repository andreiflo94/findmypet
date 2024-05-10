package com.heixss.findmypet.data.profile

import com.heixss.findmypet.data.Resource
import com.heixss.findmypet.data.common.RemoteApi
import com.heixss.findmypet.data.common.SessionManager
import com.heixss.findmypet.data.searchpets.mappers.toUser
import com.heixss.findmypet.domain.profile.User
import com.heixss.findmypet.domain.profile.UserRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private val remoteApi: RemoteApi,
    private val sessionManager: SessionManager
) : UserRepository {

    override suspend fun getUserProfile(): Resource<User> {
        return withContext(Dispatchers.IO) {
            val getProfileResponse = remoteApi.getProfile()
            if (getProfileResponse.isSuccessful) {
                getProfileResponse.body()?.userProfile?.let { userProfile ->
                    return@withContext Resource.success(userProfile.toUser())
                } ?: Resource.error(RuntimeException("Request was successfully, but userProfile was null"))
            } else {
                return@withContext getProfileResponse.errorBody()?.string()?.let { errorMsg ->
                    Resource.error(Throwable(errorMsg))
                } ?: run { Resource.error() }
            }
        }
    }

    override suspend fun updateUserProfile(
        firstName: String, lastName: String, address: String,
        phone: String,
        email: String
    ): Resource<User> {
        return withContext(Dispatchers.IO) {
            val getProfileResponse = remoteApi.updateProfile(
                firstName = firstName,
                lastName = lastName,
                address = address,
                phone = phone,
                email = email
            )
            if (getProfileResponse.isSuccessful) {
                getProfileResponse.body()?.userProfile?.let { userProfile ->
                    return@withContext Resource.success(userProfile.toUser())
                } ?: Resource.error(RuntimeException("Request was successfully, but userProfile was null"))
            } else {
                return@withContext getProfileResponse.errorBody()?.string()?.let { errorMsg ->
                    Resource.error(Throwable(errorMsg))
                } ?: run { Resource.error() }
            }
        }
    }

    override suspend fun logOut() {
        sessionManager.clearAccessToken()
    }
}