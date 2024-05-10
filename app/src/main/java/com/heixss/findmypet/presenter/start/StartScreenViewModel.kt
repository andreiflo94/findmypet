package com.heixss.findmypet.presenter.start

import androidx.lifecycle.ViewModel
import com.heixss.findmypet.domain.login.LoginRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class StartScreenViewModel @Inject constructor(
    loginRepository: LoginRepository,
) : ViewModel() {
    var isLoggedIn = loginRepository.isLoggedIn()
}