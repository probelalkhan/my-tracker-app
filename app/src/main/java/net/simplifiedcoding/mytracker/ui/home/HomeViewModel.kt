package net.simplifiedcoding.mytracker.ui.home

import androidx.lifecycle.ViewModel
import net.simplifiedcoding.mytracker.data.repositories.MyTrackerRepository

class HomeViewModel(
    private val repository: MyTrackerRepository
) : ViewModel() {

    fun getSurveys() = repository.getSurveys()

    fun getCoordinates(surveyID: Int) = repository.getCoordinates(surveyID)
}