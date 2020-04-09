package net.simplifiedcoding.mytracker.ui.auth

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.ViewModel
import net.simplifiedcoding.mytracker.R
import net.simplifiedcoding.mytracker.ui.BaseFragment
import net.simplifiedcoding.mytracker.ui.utils.popBackStackAndNavigate

abstract class BaseAuthFragment<T : ViewDataBinding, VM : ViewModel> : BaseFragment<T, VM>() {


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        prefs.getUser()?.let {
            popBackStackAndNavigate(requireView(), R.id.homeFragment)
        }
    }

    override fun onResume() {
        super.onResume()
        (requireActivity() as AppCompatActivity).supportActionBar?.hide()
    }
}