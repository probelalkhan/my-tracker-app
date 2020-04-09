package net.simplifiedcoding.mytracker.data.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface CoordinateDao {

    @Insert
    suspend fun insert(coordinate: Coordinate): Long

    @Query("SELECT * FROM coordinates WHERE survey_id = :survey_id")
    fun getCoordinates(survey_id: Int): LiveData<List<Coordinate>>
}