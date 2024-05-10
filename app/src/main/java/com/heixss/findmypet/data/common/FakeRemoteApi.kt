package com.heixss.findmypet.data.common

import com.heixss.findmypet.data.login.LoginRequest
import com.heixss.findmypet.data.login.LoginResponse
import com.heixss.findmypet.data.searchpets.getpetsresponse.GetPetsResponse
import com.heixss.findmypet.data.searchpets.getpetsresponse.PetDetail
import com.heixss.findmypet.data.searchpets.getpetsresponse.PetDto
import com.heixss.findmypet.data.profile.GetUserProfileResponse
import com.heixss.findmypet.data.profile.UserProfileDto
import kotlinx.coroutines.delay
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.ResponseBody
import retrofit2.Response
import javax.inject.Inject

class FakeRemoteApi @Inject constructor() : RemoteApi {

    private val mockedGetUserProfileResponse = GetUserProfileResponse(
        error = null,
        errorCode = null,
        userProfile = UserProfileDto(
            photo = "https://static.wikia.nocookie.net/5eb23293-19b2-4531-83fe-129a79440d43",
            firstName = "Andrei",
            lastName = "Florea",
            email = "andrei.ctin.florea@gmail.com",
            phone = "0723705870",
            address = "Bucuresti, Sector 1, Soseaua Chitilei 242e",
            pets = arrayListOf( PetDto(
                ownerDetail = null,
                PetDetail(
                    description = "",
                    petName = "Apollo",
                    petType = 1,
                    petPhoto = "https://upload.wikimedia.org/wikipedia/commons/d/d4/Cat_March_2010-1a.jpg",
                    petSex = "M",
                    petBreed = "Cane Corso"
                )
            ),
                PetDto(
                    ownerDetail = null,
                    PetDetail(
                        description = "",
                        petName = "Apollo",
                        petType = 1,
                        petPhoto = "https://upload.wikimedia.org/wikipedia/commons/d/d4/Cat_March_2010-1a.jpg",
                        petSex = "M",
                        petBreed = "Cane Corso"
                    )
                )
            )
        ),
        response = "",
        stackTrace = null
    )

    val mockedGetPetsResponse = GetPetsResponse(
        error = null,
        errorCode = null,
        petResponse = listOf(
            PetDto(
                ownerDetail = null,
                PetDetail(
                    description = "",
                    petName = "Apollo",
                    petType = 1,
                    petPhoto = "https://upload.wikimedia.org/wikipedia/commons/d/d4/Cat_March_2010-1a.jpg",
                    petSex = "M",
                    petBreed = "Cane Corso"
                )
            ),
            PetDto(
                ownerDetail = null,
                PetDetail(
                    description = "",
                    petName = "Apollo",
                    petType = 1,
                    petPhoto = "https://upload.wikimedia.org/wikipedia/commons/d/d4/Cat_March_2010-1a.jpg",
                    petSex = "M",
                    petBreed = "Cane Corso"
                )
            ),
            PetDto(
                ownerDetail = null,
                PetDetail(
                    description = "",
                    petName = "Apollo",
                    petType = 1,
                    petPhoto = "https://upload.wikimedia.org/wikipedia/commons/d/d4/Cat_March_2010-1a.jpg",
                    petSex = "M",
                    petBreed = "Cane Corso"
                )
            )
        ),
        response = "",
        stackTrace = null
    )


    private var shouldReturnError = false

    override suspend fun updateProfile(
        firstName: String,
        lastName: String,
        address: String,
        phone: String,
        email: String
    ): Response<GetUserProfileResponse> {
        delay(2000)
        if (shouldReturnError) {
            return Response.error(404, ResponseBody.create("application/json".toMediaType(), ""))
        }
        return Response.success(
            GetUserProfileResponse(
            error = null,
            errorCode = null,
            userProfile = UserProfileDto(
                photo = "https://static.wikia.nocookie.net/5eb23293-19b2-4531-83fe-129a79440d43",
                firstName = firstName,
                lastName = lastName,
                email = email,
                phone = phone,
                address = address,
                pets = arrayListOf( PetDto(
                    ownerDetail = null,
                    PetDetail(
                        description = "",
                        petName = "Apollo",
                        petType = 1,
                        petPhoto = "https://upload.wikimedia.org/wikipedia/commons/d/d4/Cat_March_2010-1a.jpg",
                        petSex = "M",
                        petBreed = "Cane Corso"
                    )
                ),
                    PetDto(
                        ownerDetail = null,
                        PetDetail(
                            description = "",
                            petName = "Apollo",
                            petType = 1,
                            petPhoto = "https://upload.wikimedia.org/wikipedia/commons/d/d4/Cat_March_2010-1a.jpg",
                            petSex = "M",
                            petBreed = "Cane Corso"
                        )
                    )
                )
            ),
            response = "",
            stackTrace = null
        )
        )
    }

    override suspend fun getProfile(): Response<GetUserProfileResponse> {
        delay(2000)
        if (shouldReturnError) {
            return Response.error(404, ResponseBody.create("application/json".toMediaType(), ""))
        }
        return Response.success(mockedGetUserProfileResponse)
    }

    override suspend fun getPets(query: String, pageSize: Int, pageNumber: Int): Response<GetPetsResponse> {
        delay(2000)
        if (shouldReturnError) {
            return Response.error(404, ResponseBody.create("application/json".toMediaType(), ""))
        }
        return Response.success(mockedGetPetsResponse)
    }

    override suspend fun login(loginRequest: LoginRequest): Response<LoginResponse> {
        delay(2000)
        if (shouldReturnError) {
            return Response.error(404, ResponseBody.create("application/json".toMediaType(), "User and password not ok"))
        }
        return Response.success(
            LoginResponse(
            error = null,
            errorCode = null,
            token = "eyJhbGciOiJodHRwOi8vd3d3LnczLm9yZy8yMDAxLzA0L3htbGRzaWctbW9yZSNobWFjLXNoYTI1NiIsInR5cCI6IkpXVCJ9.eyJodHRwOi8vc2NoZW1hcy54bWxzb2FwLm9yZy93cy8yMDA1LzA1L2lkZW50aXR5L2NsYWltcy9uYW1lIjoic3RyaW5nNCIsImh0dHA6Ly9zY2hlbWFzLm1pY3Jvc29mdC5jb20vd3MvMjAwOC8wNi9pZGVudGl0eS9jbGFpbXMvcm9sZSI6IlBldEFwcEFkbWluIiwiZXhwIjoxNzE0ODE3MjIxLCJpc3MiOiJQZXRBcHAiLCJhdWQiOiJQZXRBcHBBcGkifQ.RhkBUlsg-J7OmE0w7pxx31pfllOg-KpaxVvHfBJDNQ4"
        )
        )
    }

    override suspend fun register(username: String, password: String, email: String, phone: String): Response<Unit> {
        delay(2000)
        if (shouldReturnError) {
            return Response.error(404, ResponseBody.create("application/json".toMediaType(), ""))
        }
        return Response.success(Unit)
    }

    fun setReturnsError() {
        this.shouldReturnError = true
    }
}