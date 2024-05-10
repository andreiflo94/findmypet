package com.heixss.findmypet.data.login

import com.heixss.findmypet.data.common.SessionManager
import com.heixss.findmypet.data.common.RemoteApi
import com.heixss.findmypet.domain.login.LoginRepository
import com.heixss.findmypet.data.Resource
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Response
import javax.inject.Inject

class LoginRepositoryImpl @Inject constructor(
    private val remoteApi: RemoteApi,
    private val sessionManager: SessionManager
) : LoginRepository {
    override suspend fun login(username: String, password: String): Resource<LoginResponse> {
        return withContext(Dispatchers.IO) {
            val login = remoteApi.login(LoginRequest(username = username, password = password))
            if (login.isSuccessful) {
                login.body()?.token?.let { _ ->
                    return@withContext Resource.success(login.body())
                } ?: run {
                    return@withContext handleLoginError(login)
                }
            } else {
                return@withContext handleLoginError(login)
            }
        }
    }

    private fun CoroutineScope.handleLoginError(login: Response<LoginResponse>): Resource<LoginResponse> {
        return login.body()?.error?.let { errorMsg ->
            Resource.error(Throwable(errorMsg))
        } ?: run { Resource.error() }
    }

    override suspend fun register(username: String, password: String, phone: String, email: String): Resource<Unit> {
        return withContext(Dispatchers.IO) {
            val login = remoteApi.register(
                username = username, password = password,
                phone = phone, email = email
            )
            if (login.isSuccessful) {
                return@withContext Resource.success(Unit)
            } else {
                return@withContext login.errorBody()?.string()?.let { errorMsg ->
                    Resource.error(Throwable(errorMsg))
                } ?: run { Resource.error() }
            }
        }
    }

    override fun saveAccessToken(token: String) {
        sessionManager.saveAccessToken(token)
    }

    override fun isLoggedIn(): Boolean = sessionManager.getAccessToken() != null
}