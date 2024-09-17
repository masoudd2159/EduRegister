package ir.masouddabbaghi.eduregister.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import ir.masouddabbaghi.eduregister.data.model.DepartmentList
import ir.masouddabbaghi.eduregister.data.model.StudentList
import ir.masouddabbaghi.eduregister.data.model.requestBody.StudentRegisterBody
import ir.masouddabbaghi.eduregister.data.repository.StudentRepository
import ir.masouddabbaghi.eduregister.network.NetworkResult
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class StudentViewModel
    @Inject
    constructor(
        private val studentRepository: StudentRepository,
    ) : ViewModel() {
        private val _studentListResponse = MutableStateFlow<NetworkResult<StudentList>>(NetworkResult.Loading(isLoading = false))
        val studentListResponse: StateFlow<NetworkResult<StudentList>> = _studentListResponse.asStateFlow()

        private val _registerStudentResponse = MutableStateFlow<NetworkResult<String>>(NetworkResult.Loading(isLoading = false))
        val registerStudentResponse: StateFlow<NetworkResult<String>> = _registerStudentResponse.asStateFlow()

        private val _departmentListResponse = MutableStateFlow<NetworkResult<DepartmentList>>(NetworkResult.Loading(isLoading = false))
        val departmentListResponse: StateFlow<NetworkResult<DepartmentList>> = _departmentListResponse.asStateFlow()

        fun fetchStudentList() {
            _studentListResponse.value = NetworkResult.Loading(isLoading = true)
            viewModelScope.launch { studentRepository.studentList().collect { result -> _studentListResponse.value = result } }
        }

        fun fetchDepartmentList() {
            _departmentListResponse.value = NetworkResult.Loading(isLoading = true)
            viewModelScope.launch { studentRepository.departmentList().collect { result -> _departmentListResponse.value = result } }
        }

        fun fetchRegisterStudent(
            firstName: String,
            middleName: String,
            lastName: String,
            gender: String,
            birthDate: String,
            major: String,
            departmentId: Int,
        ) {
            _registerStudentResponse.value = NetworkResult.Loading(isLoading = true)
            viewModelScope.launch {
                studentRepository
                    .registerStudent(
                        studentRegisterBody =
                            StudentRegisterBody(
                                firstName = firstName,
                                middleName = middleName,
                                lastName = lastName,
                                gender = gender,
                                birthDate = birthDate,
                                major = major,
                                departmentId = departmentId,
                            ),
                    ).collect { result ->
                        _registerStudentResponse.value = result
                    }
            }
        }
    }
