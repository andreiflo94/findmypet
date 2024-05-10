package com.heixss.findmypet.data.searchpets.getpetsresponse

data class GetPetsResponse(
    val error: String?,
    val errorCode: Int?,
    val petResponse: List<PetDto>?,
    val response: String?,
    val stackTrace: String?
)