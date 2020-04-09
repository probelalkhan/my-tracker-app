package net.simplifiedcoding.mytracker.ui.auth

import android.os.Bundle
import kotlinx.coroutines.launch
import net.simplifiedcoding.mytracker.R
import net.simplifiedcoding.mytracker.data.models.UserSheetData
import net.simplifiedcoding.mytracker.databinding.FragmentRegistrationBinding
import net.simplifiedcoding.mytracker.ui.utils.*


class RegistrationFragment : BaseAuthFragment<FragmentRegistrationBinding, AuthViewModel>() {

    override fun getFragmentView(): Int = R.layout.fragment_registration

    override fun getViewModel() = AuthViewModel::class.java

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        binding.textViewSignIn.setOnClickListener {
            popBackStackAndNavigate(it, R.id.loginFragment)
        }

        binding.buttonSignUp.setOnClickListener {
            initSignup()
        }
    }

    private fun initSignup() {
        val name = binding.editTextName.text.toString().trim()
        val email = binding.editTextEmail.text.toString().trim()
        val password = binding.editTextPassword.text.toString().trim()

        if (name.isEmpty() || email.isEmpty() || password.isEmpty()) {
            requireView().snackbar(getString(R.string.all_fields_required))
        }

        launch {
            binding.progressBar.show()
            val userdata = listOf(name, email, password)
            val user = UserSheetData(listOf(userdata))
            val result =
                viewModel.signup(user, prefs.getAccessToken())

            val message = if (!result.error && result.updates!!.updatedRows > 0) {
                getString(R.string.user_registered)
            } else {
                result.message
            }
            requireView().snackbar(message)
            binding.progressBar.hide()
        }
    }
}
