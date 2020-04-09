package net.simplifiedcoding.mytracker.data.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Surveys")
data class Survey(
    val name: String,
    val area: Long?
) {
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0
}