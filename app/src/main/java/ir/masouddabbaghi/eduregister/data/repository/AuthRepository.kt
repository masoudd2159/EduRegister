package ir.masouddabbaghi.eduregister.data.repository

import ir.masouddabbaghi.eduregister.data.model.Login
import ir.masouddabbaghi.eduregister.data.model.requestBody.LoginBody
import ir.masouddabbaghi.eduregister.data.model.requestBody.RegisterBody
import ir.masouddabbaghi.eduregister.network.ApiService
import ir.masouddabbaghi.eduregister.network.NetworkResult
import ir.masouddabbaghi.eduregister.network.NetworkUtils
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthRepository
    @Inject
    constructor(
        private val apiService: ApiService,
        private val networkUtils: NetworkUtils,
    ) {
        fun login(loginBody: LoginBody): Flow<NetworkResult<Login>> =
            networkUtils.performNetworkRequest {
                apiService.login(loginBody = loginBody)
            }

        fun register(registerBody: RegisterBody): Flow<NetworkResult<String>> =
            networkUtils.performNetworkRequest {
                apiService.register(registerBody = registerBody)
            }
    }
