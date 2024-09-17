package ir.masouddabbaghi.eduregister.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import ir.masouddabbaghi.eduregister.data.model.StudentList
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

        fun fetchStudentList() {
            _studentListResponse.value = NetworkResult.Loading(isLoading = true)
            viewModelScope.launch { studentRepository.studentList().collect { result -> _studentListResponse.value = result } }
        }
    }
