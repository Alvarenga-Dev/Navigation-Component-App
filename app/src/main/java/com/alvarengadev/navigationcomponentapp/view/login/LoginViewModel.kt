package com.alvarengadev.navigationcomponentapp.view.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.alvarengadev.navigationcomponentapp.R

class LoginViewModel : ViewModel() {

    sealed class AuthenticationState {
        object NotAuthenticated : AuthenticationState()
        object Authenticated : AuthenticationState()
        class InvalidAuthentication(val fields: List<Pair<String, Int>>) : AuthenticationState()
    }

    var username: String = ""
    var token: String = ""

    private val _authenticationStateEvent = MutableLiveData<AuthenticationState>()
    val authenticationStateEvent: LiveData<AuthenticationState>
        get() = _authenticationStateEvent

    init {
        refuseAuthentication()
    }

    fun refuseAuthentication() {
        _authenticationStateEvent.value = AuthenticationState.NotAuthenticated
    }

    fun authenticateToken(token: String, username: String) {
        this.token = token
        this.username = username
        _authenticationStateEvent.value = AuthenticationState.Authenticated
    }

    fun authentication(username: String, password: String) {
        if (isValidForm(username, password)) {
            this.username = username
            _authenticationStateEvent.value = AuthenticationState.Authenticated
        }
    }

    private fun isValidForm(username: String, password: String): Boolean {
        val invalidFields = arrayListOf<Pair<String, Int>>()
        if (username.isEmpty()) {
            invalidFields.add(INPUT_USERNAME)
        }

        if (password.isEmpty()) {
            invalidFields.add(INPUT_PASSWORD)
        }

        if (invalidFields.isNotEmpty()) {
            _authenticationStateEvent.value =
                AuthenticationState.InvalidAuthentication(invalidFields)
            return false
        }
        return true
    }

    companion object {
        val INPUT_USERNAME = "INPUT_USERNAME" to R.string.login_input_layout_error_invalid_username
        val INPUT_PASSWORD = "INPUT_PASSWORD" to R.string.login_input_layout_error_invalid_password
    }
}
