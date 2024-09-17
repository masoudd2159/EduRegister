package ir.masouddabbaghi.eduregister.data.repository

import ir.masouddabbaghi.eduregister.data.model.StudentList
import ir.masouddabbaghi.eduregister.network.ApiService
import ir.masouddabbaghi.eduregister.network.NetworkResult
import ir.masouddabbaghi.eduregister.network.NetworkUtils
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class StudentRepository
    @Inject
    constructor(
        private val apiService: ApiService,
        private val networkUtils: NetworkUtils,
    ) {
        fun studentList(): Flow<NetworkResult<StudentList>> =
            networkUtils.performNetworkRequest {
                apiService.studentList()
            }
    }
