package net.simplifiedcoding.mytracker.data.network

import net.simplifiedcoding.mytracker.data.models.SheetUpdateResponse
import net.simplifiedcoding.mytracker.data.models.UserSheetData
import net.simplifiedcoding.mytracker.data.models.UsersSheetResponse
import okhttp3.OkHttpClient
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST

interface MyTrackerAPI {

    @POST("$SHEET_NAME!$START_ROW:$END_COLUMN:append?valueInputOption=USER_ENTERED")
    suspend fun signup(
        @Body user: UserSheetData,
        @Header("Authorization") accessToken: String
    ): Response<SheetUpdateResponse>

    @GET("$SHEET_NAME!$START_ROW:$END_COLUMN")
    suspend fun getUsers(@Header("Authorization") accessToken: String): Response<UsersSheetResponse>

    companion object {

        private const val SHEET_ID = "1DPU9cj3VHWPQSAiSMIjfCvbUtAZtoiO3COpAmhrROH0"
        private const val SHEET_NAME = "Users"
        private const val START_ROW = "A2"
        private const val END_COLUMN = "C"

        operator fun invoke(
            networkConnectionInterceptor: NetworkConnectionInterceptor
        ): MyTrackerAPI {

            val okHttpClient = OkHttpClient.Builder()
                .addInterceptor(networkConnectionInterceptor)
                .build()

            return Retrofit.Builder()
                .client(okHttpClient)
                .baseUrl("https://sheets.googleapis.com/v4/spreadsheets/$SHEET_ID/values/")
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(MyTrackerAPI::class.java)
        }
    }
}
