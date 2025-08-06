package com.bwalshe.describedsky.model

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.bwalshe.describedsky.DescribedSkyApplication
import com.bwalshe.describedsky.data.BlueSkyRepository
import work.socialhub.kbsky.ATProtocolException


class LoginViewModel(
    val blueSkyRepository: BlueSkyRepository
) : ViewModel() {

    var password by mutableStateOf("")
        private set
    var identity by mutableStateOf("")
        private set

    var loggedIn by mutableStateOf(false)
        private set

    var lastLoginFailed by mutableStateOf(false)
        private set

    fun updateIdentity(identity: String) {
        this.identity = identity
        this.lastLoginFailed = false
    }

    fun updatePassword(password: String) {
        this.password = password
        this.lastLoginFailed = false
    }



    fun createSession() {
        try {
            blueSkyRepository.createSession(identity, password)
            loggedIn = blueSkyRepository.sessionActive
            lastLoginFailed = !loggedIn
        } catch (e: ATProtocolException) {
            loggedIn = false
            lastLoginFailed = true
        }
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = (this[APPLICATION_KEY] as DescribedSkyApplication)
                val blueSkyRepository = application.container.blueSkyRepository
                LoginViewModel(blueSkyRepository = blueSkyRepository)
            }
        }
    }


}