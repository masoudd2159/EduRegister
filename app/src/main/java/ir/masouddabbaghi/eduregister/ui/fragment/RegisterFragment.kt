package ir.masouddabbaghi.eduregister.ui.fragment

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.hilt.navigation.fragment.hiltNavGraphViewModels
import dagger.hilt.android.AndroidEntryPoint
import ir.masouddabbaghi.eduregister.R
import ir.masouddabbaghi.eduregister.databinding.FragmentRegisterBinding
import ir.masouddabbaghi.eduregister.ui.viewmodel.AuthenticationViewModel

@AndroidEntryPoint
class RegisterFragment : Fragment() {
    private lateinit var binding: FragmentRegisterBinding

    private var doubleBackToExitPressedOnce = false

    val viewModel: AuthenticationViewModel by hiltNavGraphViewModels(R.id.auth_nav)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentRegisterBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?,
    ) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {}
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val callback: OnBackPressedCallback =
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    if (doubleBackToExitPressedOnce) {
                        requireActivity().finish()
                        requireActivity().finishAffinity()
                    } else {
                        doubleBackToExitPressedOnce = true
                        Toast.makeText(requireContext(), resources.getString(R.string.exit_toast_message), Toast.LENGTH_SHORT).show()
                        Handler(Looper.getMainLooper()).postDelayed({ doubleBackToExitPressedOnce = false }, 2000)
                    }
                }
            }
        requireActivity().onBackPressedDispatcher.addCallback(this, callback)
    }
}
