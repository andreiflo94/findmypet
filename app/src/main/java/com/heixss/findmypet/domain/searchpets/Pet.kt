package com.heixss.findmypet.domain.searchpets

data class Pet(
    val description: String,
    val petName: String,
    val petType: Int,
    val username: String,
    val petPhoto: String,
    val petSex: String,
    val petBreed: String
)