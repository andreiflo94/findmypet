package com.heixss.findmypet.data.common


import com.heixss.findmypet.data.login.LoginRequest
import com.heixss.findmypet.data.login.LoginResponse
import com.heixss.findmypet.data.searchpets.getpetsresponse.GetPetsResponse
import com.heixss.findmypet.data.profile.GetUserProfileResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Query

interface RemoteApi {

    @PUT("User/Update")
    suspend fun updateProfile(
        @Query("firstName") firstName: String,
        @Query("lastName") lastName: String,
        @Query("address") address: String,
        @Query("phone") phone: String,
        @Query("email") email: String
    ): Response<GetUserProfileResponse>

    @GET("User/Profile")
    suspend fun getProfile(): Response<GetUserProfileResponse>

    @GET("Pet/GetPets")
    suspend fun getPets(
        @Query("petName") query: String,
        @Query("perSize") pageSize: Int,
        @Query("page") pageNumber: Int
    ): Response<GetPetsResponse>

    @POST("User/Login")
    suspend fun login(
        @Body loginRequest: LoginRequest,
    ): Response<LoginResponse>

    @POST("User/AddUser")
    suspend fun register(
        @Query("username") username: String,
        @Query("password") password: String,
        @Query("email") email: String,
        @Query("phone") phone: String
    ): Response<Unit>

    companion object {
        const val BASE_URL = "https://solid-ocelot-apt.ngrok-free.app"
    }
}