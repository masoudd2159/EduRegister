package ir.masouddabbaghi.eduregister.ui.activity

import android.view.View
import dagger.hilt.android.AndroidEntryPoint
import ir.masouddabbaghi.eduregister.databinding.ActivityAuthenticationBinding
import ir.masouddabbaghi.eduregister.ui.base.BaseActivity

@AndroidEntryPoint
class AuthenticationActivity : BaseActivity() {
    private lateinit var binding: ActivityAuthenticationBinding

    override fun getLayoutResourceBinding(): View {
        binding = ActivityAuthenticationBinding.inflate(layoutInflater)
        return binding.root
    }
}
