package com.heixss.findmypet.presenter.login

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
class LoginViewModel @Inject constructor(
    private val loginRepository: LoginRepository,
    @ApplicationContext private val applicationContext: Context
) : ViewModel() {

    private val actionChannel = Channel<NavigationEvent>()

    var isLoading = mutableStateOf(false)
    var loginErrorMsg = mutableStateOf("")
    val actionChannelFlow = actionChannel.receiveAsFlow()

    fun login(username: String, password: String) {
        viewModelScope.launch {
            isLoading.value = true
            loginErrorMsg.value = ""
            val response = loginRepository.login(username, password)
            when (response.status) {
                Resource.Status.SUCCESS -> {
                    loginErrorMsg.value = ""
                    response.data?.token?.let { token ->
                        loginRepository.saveAccessToken(token)
                    }
                    actionChannel.send(NavigationEvent.BottomNav)
                }

                Resource.Status.ERROR -> {
                    response.throwable?.message?.let { message ->
                        loginErrorMsg.value = message
                    } ?: run {
                        loginErrorMsg.value = applicationContext.getString(R.string.app_generic_error)
                    }
                }
            }
            isLoading.value = false
        }
    }
}