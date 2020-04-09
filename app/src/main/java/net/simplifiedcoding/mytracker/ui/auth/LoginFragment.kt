package net.simplifiedcoding.mytracker.ui.auth

import android.os.Bundle
import kotlinx.coroutines.launch
import net.simplifiedcoding.mytracker.R
import net.simplifiedcoding.mytracker.databinding.FragmentLoginBinding
import net.simplifiedcoding.mytracker.ui.utils.hide
import net.simplifiedcoding.mytracker.ui.utils.popBackStackAndNavigate
import net.simplifiedcoding.mytracker.ui.utils.show
import net.simplifiedcoding.mytracker.ui.utils.snackbar


class LoginFragment : BaseAuthFragment<FragmentLoginBinding, AuthViewModel>() {

    override fun getFragmentView() = R.layout.fragment_login

    override fun getViewModel() = AuthViewModel::class.java

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        binding.textViewSignUp.setOnClickListener {
            popBackStackAndNavigate(it, R.id.registrationFragment)
        }

        binding.buttonSignIn.setOnClickListener {
            signIn()
        }
    }

    private fun signIn() {
        val email = binding.editTextEmail.text.toString().trim()
        val password = binding.editTextPassword.text.toString().trim()

        launch {
            binding.progressBar.show()
            if (viewModel.login(email, password, prefs.getAccessToken())) {
                prefs.saveUser(email)
                popBackStackAndNavigate(requireView(), R.id.homeFragment)
            } else {
                requireView().snackbar(getString(R.string.invalid_credentials))
            }
            binding.progressBar.hide()
        }
    }

}
