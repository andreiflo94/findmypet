package com.heixss.findmypet.presenter.register

import android.content.Context
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.heixss.findmypet.R
import com.heixss.findmypet.data.Resource
import com.heixss.findmypet.domain.login.LoginRepository
import com.heixss.findmypet.presenter.common.NavigationEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val loginRepository: LoginRepository,
    @ApplicationContext private val applicationContext: Context
) : ViewModel() {

    private val actionChannel = Channel<NavigationEvent>()

    var isLoading = mutableStateOf(false)
    var registerErrorMsg = mutableStateOf("")
    val actionChannelFlow = actionChannel.receiveAsFlow()

    fun register(username: String, password: String, phone: String, email: String) {
        viewModelScope.launch {
            isLoading.value = true
            registerErrorMsg.value = ""
            val response = loginRepository.register(username, password, phone, email)
            when (response.status) {
                Resource.Status.SUCCESS -> {
                    registerErrorMsg.value = ""
                    actionChannel.send(NavigationEvent.BottomNav)
                }

                Resource.Status.ERROR -> {
                    response.throwable?.message?.let { message ->
                        registerErrorMsg.value = message
                    } ?: run {
                        registerErrorMsg.value = applicationContext.getString(R.string.app_generic_error)
                    }
                }
            }
            isLoading.value = false
        }
    }
}