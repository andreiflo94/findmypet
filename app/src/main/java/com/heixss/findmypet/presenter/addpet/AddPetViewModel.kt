package com.heixss.findmypet.presenter.addpet

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AddPetViewModel @Inject constructor(): ViewModel() {

    private val _addPetScreentate = mutableStateOf(AddPetScreenState())
    val addPetScreenState: State<AddPetScreenState> = _addPetScreentate

    fun updateSelectedPetType(newType: String) {
        _addPetScreentate.value = _addPetScreentate.value.copy(selectedPetType = newType)
    }

    fun updateSelectedPetSex(newSex: String) {
        _addPetScreentate.value = _addPetScreentate.value.copy(selectedPetSex = newSex)
    }

    fun updatePetName(newPetName: String) {
        _addPetScreentate.value = _addPetScreentate.value.copy(name = newPetName)
    }

    fun updateBreed(newBreed: String) {
        _addPetScreentate.value = _addPetScreentate.value.copy(breed = newBreed)
    }
}