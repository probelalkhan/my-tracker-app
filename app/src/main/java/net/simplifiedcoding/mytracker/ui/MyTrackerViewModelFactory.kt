package net.simplifiedcoding.mytracker.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import net.simplifiedcoding.mytracker.data.repositories.MyTrackerRepository
import net.simplifiedcoding.mytracker.ui.auth.AuthViewModel
import net.simplifiedcoding.mytracker.ui.home.HomeViewModel
import java.lang.IllegalArgumentException

@Suppress("UNCHECKED_CAST")
class MyTrackerViewModelFactory(
    private val repository: MyTrackerRepository
) : ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(AuthViewModel::class.java) -> AuthViewModel(repository) as T
            modelClass.isAssignableFrom(HomeViewModel::class.java) -> HomeViewModel(repository) as T
            else -> throw IllegalArgumentException("ViewModel class not found...")
        }
    }
}