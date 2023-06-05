package ba.etf.rma23.projekat.data.repositories

import ba.etf.rma23.projekat.data.repositories.result.*
import ba.unsa.etf.gamespirala.BuildConfig
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface IGDBApi {

    @Headers(
        "Client-ID: " + BuildConfig.IGDB_API_CLIENT_ID,
        "Authorization: " + BuildConfig.IGDB_API_AUTHORIZATION
    )
    @POST("games")
    suspend fun getGames(@Body body: RequestBody): Response<List<GameResult>>

    @Headers(
        "Client-ID: " + BuildConfig.IGDB_API_CLIENT_ID,
        "Authorization: " + BuildConfig.IGDB_API_AUTHORIZATION
    )
    @POST("platforms")
    suspend fun getPlatforms(@Body body: RequestBody): Response<List<PlatformResult>>

    @Headers(
        "Client-ID: " + BuildConfig.IGDB_API_CLIENT_ID,
        "Authorization: " + BuildConfig.IGDB_API_AUTHORIZATION
    )
    @POST("covers")
    suspend fun getCovers(@Body body: RequestBody): Response<List<CoverResult>>

    @Headers(
        "Client-ID: " + BuildConfig.IGDB_API_CLIENT_ID,
        "Authorization: " + BuildConfig.IGDB_API_AUTHORIZATION
    )
    @POST("age_ratings")
    suspend fun getAgeRatings(@Body body: RequestBody): Response<List<AgeRatingResult>>

    @Headers(
        "Client-ID: " + BuildConfig.IGDB_API_CLIENT_ID,
        "Authorization: " + BuildConfig.IGDB_API_AUTHORIZATION
    )
    @POST("involved_companies")
    suspend fun getInvolvedCompanies(@Body body: RequestBody): Response<List<InvolvedCompanyResult>>

    @Headers(
        "Client-ID: " + BuildConfig.IGDB_API_CLIENT_ID,
        "Authorization: " + BuildConfig.IGDB_API_AUTHORIZATION
    )
    @POST("companies")
    suspend fun getCompanies(@Body body: RequestBody): Response<List<CompanyResult>>

    @Headers(
        "Client-ID: " + BuildConfig.IGDB_API_CLIENT_ID,
        "Authorization: " + BuildConfig.IGDB_API_AUTHORIZATION
    )
    @POST("genres")
    suspend fun getGenres(@Body body: RequestBody): Response<List<GenreResult>>

}