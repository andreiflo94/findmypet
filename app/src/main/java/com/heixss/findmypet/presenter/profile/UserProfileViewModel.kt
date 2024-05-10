package com.heixss.findmypet.presenter.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.heixss.findmypet.data.Resource
import com.heixss.findmypet.domain.profile.User
import com.heixss.findmypet.domain.profile.UserRepository
import com.heixss.findmypet.presenter.common.NavigationEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class UserProfileViewModel @Inject constructor(
    private val userRepository: UserRepository
) : ViewModel() {


    private val logOutActionChannel = Channel<NavigationEvent>()
    val logOutChannelFlow = logOutActionChannel.receiveAsFlow()

    private val _uiState = MutableStateFlow(
        ProfileScreenState(
            isLoading = false,
            user = null,
            isEditing = false,
            errorMsg = null
        )
    )
    val state: StateFlow<ProfileScreenState> = _uiState


    init {
        getUserProfile()
    }

    fun logOut() {
        _uiState.value = _uiState.value.copy(isLoading = true)
        viewModelScope.launch {
            userRepository.logOut()
            logOutActionChannel.send(NavigationEvent.LogIn)
            _uiState.value = _uiState.value.copy(isLoading = false)
        }
    }

    fun cancelEdit() {
        _uiState.value = _uiState.value.copy(
            isLoading = false,
            isEditing = false,
            user = _uiState.value.user
        )
    }

    fun toggleEditMode(
        firstName: String,
        lastName: String,
        address: String,
        phone: String,
        email: String
    ) {
        _uiState.value = _uiState.value.copy(isLoading = true)
        viewModelScope.launch {
            if (_uiState.value.isEditing) {
                val resource = userRepository.updateUserProfile(
                    firstName = firstName,
                    lastName = lastName,
                    address = address,
                    phone = phone,
                    email = email
                )
                processUser(resource)
            } else {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    isEditing = !_uiState.value.isEditing
                )
            }
        }
    }

    private fun getUserProfile() {
        _uiState.value = _uiState.value.copy(isLoading = true)
        viewModelScope.launch {
            val resource = userRepository.getUserProfile()
            processUser(resource)
        }
    }

    private fun processUser(resource: Resource<User>) {
        when (resource.status) {
            Resource.Status.SUCCESS -> {
                resource.data?.let { user ->
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        isEditing = false,
                        user = user
                    )
                }
            }

            Resource.Status.ERROR -> {
                resource.throwable?.let { thr ->
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        isEditing = false,
                        user = null,
                        errorMsg = thr.message
                    )
                }
            }
        }
    }

}