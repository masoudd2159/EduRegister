package ir.masouddabbaghi.eduregister.ui.fragment

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
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
import ir.masouddabbaghi.eduregister.data.storage.SharedPreferencesHelper
import ir.masouddabbaghi.eduregister.data.storage.SharedPreferencesKeys.KEY_ACCESS_TOKEN
import ir.masouddabbaghi.eduregister.databinding.FragmentLoginBinding
import ir.masouddabbaghi.eduregister.network.NetworkResult
import ir.masouddabbaghi.eduregister.ui.activity.MainActivity
import ir.masouddabbaghi.eduregister.ui.viewmodel.AuthenticationViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class LoginFragment : Fragment() {
    private lateinit var binding: FragmentLoginBinding

    private var doubleBackToExitPressedOnce = false

    val viewModel: AuthenticationViewModel by hiltNavGraphViewModels(R.id.auth_nav)

    @Inject
    lateinit var sharedPreferencesHelper: SharedPreferencesHelper

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?,
    ) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {
            binding.appVersion.text =
                getString(
                    R.string.app_version,
                    requireContext().packageManager.getPackageInfo(requireContext().packageName, 0).versionName,
                )

            register.setOnClickListener {
                findNavController().navigate(R.id.registerFragment)
            }

            layoutLoginToApp.setOnClickListener {
                val username = inputUsername.text.toString()
                val password = inputPassword.text.toString()
                when {
                    username.isEmpty() -> inputUsername.error = resources.getString(R.string.username_error)
                    password.isEmpty() -> inputPassword.error = resources.getString(R.string.password_error)
                    else -> viewModel.fetchLogin(email = username, password = password)
                }
            }

            viewLifecycleOwner.lifecycleScope.launch {
                viewModel.loginResponse.collect { loginResult ->
                    when (loginResult) {
                        is NetworkResult.Loading -> {
                            Log.i(javaClass.simpleName, "NetworkResult Loading Status -> ${loginResult.isLoading}")
                            if (loginResult.isLoading) {
                                textLoginToApp.visibility = View.GONE
                                animationView.visibility = View.VISIBLE
                            } else {
                                textLoginToApp.visibility = View.VISIBLE
                                animationView.visibility = View.GONE
                            }
                        }

                        is NetworkResult.Failure -> {
                            Log.e(javaClass.simpleName, "NetworkResult Failure Message -> ${loginResult.errorMessage}")
                        }

                        is NetworkResult.Success -> {
                            Log.i(javaClass.simpleName, "Login User Successfully, Access Token -> ${loginResult.data.token}")
                            sharedPreferencesHelper.saveString(KEY_ACCESS_TOKEN, loginResult.data.token)
                            val intent = Intent(requireContext(), MainActivity::class.java)
                            startActivity(intent)
                            requireActivity().finish()
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
