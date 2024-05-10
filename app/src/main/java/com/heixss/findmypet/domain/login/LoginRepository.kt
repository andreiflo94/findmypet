package com.heixss.findmypet.domain.login

import com.heixss.findmypet.data.login.LoginResponse
import com.heixss.findmypet.data.Resource

interface LoginRepository {

    suspend fun login(username: String, password: String): Resource<LoginResponse>

    suspend fun register(username: String, password: String, phone: String, email: String): Resource<Unit>

    fun saveAccessToken(token: String)

    fun isLoggedIn(): Boolean
}