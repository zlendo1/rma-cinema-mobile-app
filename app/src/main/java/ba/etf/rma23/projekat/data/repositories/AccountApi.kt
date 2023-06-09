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
        @Path("aid") aid: String = AccountGamesRepository.account.acHash,
        @Query("name") name: String
    ): Response<List<GameResultAccount>>

    @GET("account/{aid}/games")
    suspend fun getGameById(
        @Path("aid") aid: String = AccountGamesRepository.account.acHash,
        @Query("igdb_id") id: Int
    ): Response<List<GameResultAccount>>

    @DELETE("account/{aid}/game/{gid}")
    suspend fun deleteGameById(
        @Path("aid") aid: String = AccountGamesRepository.account.acHash,
        @Path("gid") id: Int
    )

    @POST("account/{aid}/game")
    suspend fun postGame(
        @Path("aid") aid: String = AccountGamesRepository.account.acHash,
        @Body body: RequestBody
    )

}