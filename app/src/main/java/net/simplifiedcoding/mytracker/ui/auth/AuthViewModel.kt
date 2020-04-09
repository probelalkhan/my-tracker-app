package net.simplifiedcoding.mytracker.ui.auth

import android.util.Log
import androidx.lifecycle.ViewModel
import net.simplifiedcoding.mytracker.MyTrackerApplication
import net.simplifiedcoding.mytracker.R
import net.simplifiedcoding.mytracker.data.models.SheetUpdateResponse
import net.simplifiedcoding.mytracker.data.models.UserSheetData
import net.simplifiedcoding.mytracker.data.repositories.MyTrackerRepository

class AuthViewModel(
    private val repository: MyTrackerRepository
) : ViewModel() {

    suspend fun signup(user: UserSheetData, accessToken: String): SheetUpdateResponse {
        val users = repository.getUsers(accessToken)
        users.values?.forEach {
            if (it[1] == user.values[0][1]) {
                return SheetUpdateResponse(
                    null,
                    null,
                    null,
                    true,
                    MyTrackerApplication.appContext.getString(R.string.email_taken)
                )
            }
        }
        return repository.signup(user, accessToken)
    }

    suspend fun login(email: String, password: String, accessToken: String): Boolean {
        val users = repository.getUsers(accessToken)
        users.values?.forEach {
            if (it[1] == email && it[2] == password) {
                return true
            }
        }
        return false
    }

}