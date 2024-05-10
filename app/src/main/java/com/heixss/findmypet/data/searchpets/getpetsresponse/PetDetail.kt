package com.heixss.findmypet.data.searchpets.getpetsresponse

data class PetDetail(
    val description: String?,
    val petName: String?,
    val petType: Int?,
    val petPhoto: String?,
    val petSex: String,
    val petBreed: String
)