package com.heixss.findmypet.data.searchpets.mappers

import com.heixss.findmypet.data.searchpets.getpetsresponse.PetDto
import com.heixss.findmypet.domain.searchpets.Pet

fun PetDto.toPet(): Pet {
    return Pet(
        description = petDetail?.description ?: "",
        petName = petDetail?.petName ?: "",
        petType = petDetail?.petType ?: 0,
        username = ownerDetail?.username ?: "",
        petPhoto = petDetail?.petPhoto ?: "",
        petBreed = petDetail?.petBreed ?: "",
        petSex = petDetail?.petSex ?: ""
    )
}


