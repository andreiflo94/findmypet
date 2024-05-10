package com.heixss.findmypet.data.login

data class LoginResponse(
    val error: String?,
    val errorCode: Int?,
    val token: String
)