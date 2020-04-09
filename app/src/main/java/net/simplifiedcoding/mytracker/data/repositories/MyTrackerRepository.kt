package net.simplifiedcoding.mytracker.data.repositories

import net.simplifiedcoding.mytracker.data.db.MyTrackerDatabase
import net.simplifiedcoding.mytracker.data.models.UserSheetData
import net.simplifiedcoding.mytracker.data.network.MyTrackerAPI
import net.simplifiedcoding.mytracker.data.network.SafeApiRequest

class MyTrackerRepository(
    private val api: MyTrackerAPI,
    private val db: MyTrackerDatabase
) : SafeApiRequest() {

    suspend fun signup(user: UserSheetData, accessToken: String) = apiRequest {
        api.signup(user, accessToken)
    }

    suspend fun getUsers(accessToken: String) = apiRequest { api.getUsers(accessToken) }

    fun getSurveys() = db.getSurveyDao().getSurveys()
    fun getCoordinates(surveyID: Int) = db.getCoordinateDao().getCoordinates(surveyID)
}