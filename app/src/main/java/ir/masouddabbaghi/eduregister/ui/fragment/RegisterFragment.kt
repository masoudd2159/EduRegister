package ir.masouddabbaghi.eduregister.ui.fragment

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.hilt.navigation.fragment.hiltNavGraphViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import ir.masouddabbaghi.eduregister.R
import ir.masouddabbaghi.eduregister.databinding.FragmentRegisterBinding
import ir.masouddabbaghi.eduregister.network.NetworkResult
import ir.masouddabbaghi.eduregister.ui.viewmodel.AuthenticationViewModel
import kotlinx.coroutines.launch

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

        binding.apply {
            login.setOnClickListener {
                findNavController().navigate(R.id.loginFragment)
            }

            layoutRegister.setOnClickListener {
                val email = inputEmail.text.toString()
                val password = inputPassword.text.toString()
                val rePassword = inputRepeatPassword.text.toString()
                val name = inputName.text.toString()
                val family = inputFamily.text.toString()
                when {
                    Patterns.EMAIL_ADDRESS
                        .matcher(email)
                        .matches()
                        .not() -> inputEmail.error = resources.getString(R.string.email_error)

                    password.isEmpty() -> inputPassword.error = resources.getString(R.string.password_error)
                    (password == rePassword).not() -> inputRepeatPassword.error = resources.getString(R.string.password_error)
                    name.isEmpty() -> inputName.error = resources.getString(R.string.name_error)
                    family.isEmpty() -> inputFamily.error = resources.getString(R.string.family_error)
                    else -> viewModel.fetchRegister(email = email, password = password, firstName = name, lastName = family)
                }
            }

            viewLifecycleOwner.lifecycleScope.launch {
                viewModel.registerResponse.collect { registerResult ->
                    when (registerResult) {
                        is NetworkResult.Loading -> {
                            Log.i(javaClass.simpleName, "NetworkResult Loading Status -> ${registerResult.isLoading}")
                            if (registerResult.isLoading) {
                                textRegister.visibility = View.GONE
                                animationView.visibility = View.VISIBLE
                            } else {
                                textRegister.visibility = View.VISIBLE
                                animationView.visibility = View.GONE
                            }
                        }

                        is NetworkResult.Failure -> {
                            Log.e(javaClass.simpleName, "NetworkResult Failure Message -> ${registerResult.errorMessage}")
                        }

                        is NetworkResult.Success -> {
                            Log.i(javaClass.simpleName, "Register User Successfully, Server Message -> ${registerResult.data}")
                            Toast.makeText(requireContext(), resources.getString(R.string.register_successfully), Toast.LENGTH_LONG).show()
                            findNavController().navigate(R.id.loginFragment)
                        }
                    }
                }
            }
        }
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
