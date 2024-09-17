package ir.masouddabbaghi.eduregister.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import ir.masouddabbaghi.eduregister.data.model.Login
import ir.masouddabbaghi.eduregister.data.model.requestBody.LoginBody
import ir.masouddabbaghi.eduregister.data.model.requestBody.RegisterBody
import ir.masouddabbaghi.eduregister.data.repository.AuthRepository
import ir.masouddabbaghi.eduregister.data.storage.SharedPreferencesHelper
import ir.masouddabbaghi.eduregister.data.storage.SharedPreferencesKeys.KEY_EMAIL
import ir.masouddabbaghi.eduregister.data.storage.SharedPreferencesKeys.KEY_PASSWORD
import ir.masouddabbaghi.eduregister.network.NetworkResult
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthenticationViewModel
    @Inject
    constructor(
        private val authRepository: AuthRepository,
        private val sharedPreferencesHelper: SharedPreferencesHelper,
    ) : ViewModel() {
        private val _loginResponse = MutableStateFlow<NetworkResult<Login>>(NetworkResult.Loading(isLoading = false))
        val loginResponse: StateFlow<NetworkResult<Login>> = _loginResponse.asStateFlow()

        private val _registerResponse = MutableStateFlow<NetworkResult<String>>(NetworkResult.Loading(isLoading = false))
        val registerResponse: StateFlow<NetworkResult<String>> = _registerResponse.asStateFlow()

        fun fetchLogin(
            email: String = sharedPreferencesHelper.getString(key = KEY_EMAIL, defaultValue = ""),
            password: String = sharedPreferencesHelper.getString(key = KEY_PASSWORD, defaultValue = ""),
        ) {
            _loginResponse.value = NetworkResult.Loading(isLoading = true)
            viewModelScope.launch {
                authRepository
                    .login(
                        loginBody =
                            LoginBody(
                                email = email,
                                password = password,
                            ),
                    ).collect { result ->
                        _loginResponse.value = result
                        sharedPreferencesHelper.saveString(KEY_EMAIL, email)
                        sharedPreferencesHelper.saveString(KEY_PASSWORD, password)
                    }
            }
        }

        fun fetchRegister(
            email: String,
            password: String,
            firstName: String,
            lastName: String,
        ) {
            _registerResponse.value = NetworkResult.Loading(isLoading = true)
            viewModelScope.launch {
                authRepository
                    .register(
                        registerBody =
                            RegisterBody(
                                email = email,
                                password = password,
                                firstName = firstName,
                                lastName = lastName,
                            ),
                    ).collect { result ->
                        _registerResponse.value = result
                    }
            }
        }
    }
