package net.simplifiedcoding.mytracker.data.prefs

import android.content.SharedPreferences
import android.util.Log
import androidx.preference.PreferenceManager
import net.simplifiedcoding.mytracker.MyTrackerApplication

class PreferenceProvider() {

    companion object {
        private const val KEY_ACCESS_TOKEN = "key_access_token"
        private const val KEY_EMAIL = "key_email"
    }


    private val preference: SharedPreferences
        get() = PreferenceManager.getDefaultSharedPreferences(MyTrackerApplication.appContext)

    fun saveAccessToken(accessToken: String) {
        Log.e("AccessX", accessToken)
        preference.edit().putString(
            KEY_ACCESS_TOKEN,
            accessToken
        ).apply()
    }

    fun getAccessToken(): String {
        val token = preference.getString(KEY_ACCESS_TOKEN, null)
        return "Bearer $token"
    }


    fun saveUser(email: String) = preference.edit().putString(KEY_EMAIL, email).apply()

    fun getUser() = preference.getString(KEY_EMAIL, null)

}