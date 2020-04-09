package net.simplifiedcoding.mytracker.service

import android.app.Notification
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.IBinder
import android.util.Log
import androidx.core.app.NotificationCompat
import io.nlopez.smartlocation.SmartLocation
import net.simplifiedcoding.mytracker.MyTrackerApplication
import net.simplifiedcoding.mytracker.R
import net.simplifiedcoding.mytracker.data.db.Coordinate
import net.simplifiedcoding.mytracker.data.db.MyTrackerDatabase
import net.simplifiedcoding.mytracker.ui.MainActivity
import net.simplifiedcoding.mytracker.ui.utils.io
import org.kodein.di.KodeinAware
import org.kodein.di.android.kodein
import org.kodein.di.generic.instance

class MyTrackerService : Service(), KodeinAware {

    override val kodein by kodein()
    private val db: MyTrackerDatabase by instance()

    companion object {
        const val FOREGROUND_SERVICE_NOTIFICATION_CODE = 100

        private const val KEY_SURVEY_ID = "key_survey_id"

        fun start(context: Context, id: Long) {
            Intent(context, MyTrackerService::class.java).also {
                it.putExtra(KEY_SURVEY_ID, id)
                context.startService(it)
            }
        }

        fun stop(context: Context) {
            context.stopService(Intent(context, MyTrackerService::class.java))
        }
    }


    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        intent?.let { _intent ->
            startForeground(FOREGROUND_SERVICE_NOTIFICATION_CODE, initNotification())
            val surveyID = _intent.getLongExtra(KEY_SURVEY_ID, -1).toInt()
            if (surveyID != -1) {
                SmartLocation.with(MyTrackerApplication.appContext).location()
                    .start {
                        val coordinate = Coordinate(
                            surveyID,
                            it.latitude,
                            it.longitude
                        )
                        io { db.getCoordinateDao().insert(coordinate) }
                    }
            }
            Log.e("SurveyX", surveyID.toString())
        }
        return START_STICKY
    }

    private fun initNotification(): Notification {
        val notifyIntent = Intent(this, MainActivity::class.java)
        val notifyPendingIntent =
            PendingIntent.getActivity(this, 0, notifyIntent, PendingIntent.FLAG_UPDATE_CURRENT)
        NotificationCompat.Builder(this, MyTrackerApplication.MY_TRACKER_CHANNEL_ID).apply {
            setContentTitle(getString(R.string.tracking_going_on))
            setContentText(getString(R.string.notification_text))
            setSmallIcon(R.mipmap.ic_launcher)
            setContentIntent(notifyPendingIntent)
            return build()
        }
    }


    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onDestroy() {
        super.onDestroy()
        SmartLocation.with(MyTrackerApplication.appContext).location().stop()
    }
}