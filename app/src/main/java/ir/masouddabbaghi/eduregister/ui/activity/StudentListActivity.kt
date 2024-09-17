package ir.masouddabbaghi.eduregister.ui.activity

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.viewModels
import androidx.core.view.isVisible
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import ir.masouddabbaghi.eduregister.databinding.ActivityStudentListBinding
import ir.masouddabbaghi.eduregister.network.NetworkResult
import ir.masouddabbaghi.eduregister.ui.adapter.StudentAdapter
import ir.masouddabbaghi.eduregister.ui.base.BaseActivity
import ir.masouddabbaghi.eduregister.ui.viewmodel.StudentViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class StudentListActivity : BaseActivity() {
    private lateinit var binding: ActivityStudentListBinding

    private val studentViewModel: StudentViewModel by viewModels()

    @Inject
    lateinit var studentAdapter: StudentAdapter

    override fun getLayoutResourceBinding(): View {
        binding = ActivityStudentListBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onStart() {
        super.onStart()
        studentViewModel.fetchStudentList()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.apply {
            iconBack.setOnClickListener { onBackPressedDispatcher.onBackPressed() }

            recyclerViewStudent.setHasFixedSize(true)
            recyclerViewStudent.layoutManager = LinearLayoutManager(this@StudentListActivity, LinearLayoutManager.VERTICAL, false)
            recyclerViewStudent.adapter = studentAdapter

            lifecycleScope.launch {
                lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                    studentViewModel.studentListResponse.collect { studentListResult ->
                        when (studentListResult) {
                            is NetworkResult.Loading -> {
                                Log.i(
                                    javaClass.simpleName,
                                    "Student List NetworkResult Loading Status -> ${studentListResult.isLoading}",
                                )
                                animationView.isVisible = studentListResult.isLoading
                            }

                            is NetworkResult.Failure ->
                                Log.e(
                                    javaClass.simpleName,
                                    "Student List NetworkResult Failure Message -> ${studentListResult.errorMessage}",
                                )

                            is NetworkResult.Success -> {
                                Log.i(javaClass.simpleName, "Login NetworkResult Success Data -> ${studentListResult.data}")
                                studentAdapter.updateItems(studentListResult.data)
                            }
                        }
                    }
                }
            }
        }
    }
}
