package net.simplifiedcoding.mytracker.data.db

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface SurveyDao {

    @Insert
    suspend fun insert(survey: Survey): Long

    @Query("SELECT * FROM surveys")
    fun getSurveys(): LiveData<List<Survey>>

    @Update
    suspend fun update(survey: Survey): Int

}