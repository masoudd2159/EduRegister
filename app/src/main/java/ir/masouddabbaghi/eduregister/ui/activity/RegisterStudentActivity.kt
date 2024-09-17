package ir.masouddabbaghi.eduregister.ui.activity

import android.view.View
import dagger.hilt.android.AndroidEntryPoint
import ir.masouddabbaghi.eduregister.databinding.ActivityRegisterStudentBinding
import ir.masouddabbaghi.eduregister.ui.base.BaseActivity

@AndroidEntryPoint
class RegisterStudentActivity : BaseActivity() {
    private lateinit var binding: ActivityRegisterStudentBinding

    override fun getLayoutResourceBinding(): View {
        binding = ActivityRegisterStudentBinding.inflate(layoutInflater)
        return binding.root
    }
}
