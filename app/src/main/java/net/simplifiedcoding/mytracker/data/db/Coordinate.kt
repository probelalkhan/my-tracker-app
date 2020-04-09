package net.simplifiedcoding.mytracker.data.db

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "Coordinates", foreignKeys = [ForeignKey(
        entity = Survey::class,
        parentColumns = arrayOf("id"),
        childColumns = arrayOf("survey_id"),
        onDelete = ForeignKey.CASCADE
    )]
)
data class Coordinate(
    val survey_id: Int,
    val lat: Double,
    val lng: Double
) {
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0
}