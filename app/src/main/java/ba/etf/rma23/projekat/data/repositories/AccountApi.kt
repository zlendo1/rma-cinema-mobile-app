package ba.etf.rma23.projekat.data.repositories

import ba.etf.rma23.projekat.data.repositories.result.GameResultAccount
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.*

interface AccountApi {

    @GET("account/{aid}/games")
    suspend fun getGames(
        @Path("aid") aid: String = AccountGamesRepository.account.acHash
    ): Response<List<GameResultAccount>>

    @GET("account/{aid}/games")
    suspend fun getGamesByName(
        @Query("name") name: String,
        @Path("aid") aid: String = AccountGamesRepository.account.acHash
    ): Response<List<GameResultAccount>>

    @GET("account/{aid}/games")
    suspend fun getGameById(
        @Query("igdb_id") id: Int,
        @Path("aid") aid: String = AccountGamesRepository.account.acHash
    ): Response<List<GameResultAccount>>

    @DELETE("account/{aid}/game/{gid}")
    suspend fun deleteGameById(
        @Path("gid") id: Int,
        @Path("aid") aid: String = AccountGamesRepository.account.acHash
    )

    @POST("account/{aid}/game")
    suspend fun postGame(
        @Body body: RequestBody,
        @Path("aid") aid: String = AccountGamesRepository.account.acHash
    )

}