package net.simplifiedcoding.mytracker.data.models

data class UsersSheetResponse(
    val majorDimension: String,
    val range: String,
    val values: List<List<String>>?
)