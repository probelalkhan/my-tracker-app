package net.simplifiedcoding.mytracker

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import com.google.api.services.sheets.v4.SheetsScopes
import com.google.auth.oauth2.GoogleCredentials
import net.simplifiedcoding.mytracker.data.db.MyTrackerDatabase
import net.simplifiedcoding.mytracker.data.network.MyTrackerAPI
import net.simplifiedcoding.mytracker.data.network.NetworkConnectionInterceptor
import net.simplifiedcoding.mytracker.data.prefs.PreferenceProvider
import net.simplifiedcoding.mytracker.data.repositories.MyTrackerRepository
import net.simplifiedcoding.mytracker.ui.MyTrackerViewModelFactory
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.androidXModule
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.provider
import org.kodein.di.generic.singleton
import java.util.*

class MyTrackerApplication : Application(), KodeinAware {


    companion object {
        lateinit var appContext: Context

        fun generateAccessToken(prefs: PreferenceProvider) {
            val credentials =
                GoogleCredentials.fromStream(appContext.resources.openRawResource(R.raw.credentials))
                    .createScoped(Collections.singletonList(SheetsScopes.SPREADSHEETS))
            credentials.refreshIfExpired()
            prefs.saveAccessToken(credentials.refreshAccessToken().tokenValue)
        }

        const val MY_TRACKER_CHANNEL_ID = "my_tracker"
        const val MY_TRACKER_CHANNEL_NAME = "My Tracker"
    }

    override val kodein = Kodein.lazy {
        import(androidXModule(this@MyTrackerApplication))

        bind() from singleton { NetworkConnectionInterceptor() }
        bind() from singleton { PreferenceProvider() }
        bind() from singleton { MyTrackerDatabase(instance()) }

        bind() from singleton { MyTrackerAPI(instance()) }
        bind() from singleton { MyTrackerRepository(instance(), instance()) }
        bind() from provider { MyTrackerViewModelFactory(instance()) }
    }

    override fun onCreate() {
        super.onCreate()
        appContext = applicationContext
        createNotificationChannel()
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                MY_TRACKER_CHANNEL_ID,
                MY_TRACKER_CHANNEL_NAME,
                NotificationManager.IMPORTANCE_DEFAULT
            )
            val notificationManager = getSystemService(NotificationManager::class.java)
            notificationManager?.createNotificationChannel(channel)
        }
    }
}
