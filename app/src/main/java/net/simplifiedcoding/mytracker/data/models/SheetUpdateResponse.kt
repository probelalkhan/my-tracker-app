package net.simplifiedcoding.mytracker.data.models

data class SheetUpdateResponse(
    val spreadsheetId: String?,
    val tableRange: String?,
    val updates: Updates?,
    val error: Boolean = false,
    val message: String
) {
    data class Updates(
        val spreadsheetId: String,
        val updatedCells: Int,
        val updatedColumns: Int,
        val updatedRange: String,
        val updatedRows: Int
    )
}