package ba.etf.rma23.projekat.data.repositories

import ba.etf.rma23.projekat.data.repositories.result.GameResultAccount
import okhttp3.ResponseBody
import retrofit2.Call
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

    @DELETE("account/{aid}/games/{gid}")
    suspend fun deleteGameById(
        @Path("gid") id: Int,
        @Path("aid") aid: String = AccountGamesRepository.account.acHash
    ): Call<ResponseBody>

    @POST("account/{aid}/games")
    suspend fun postGame(
        @Body game: GameResultAccount,
        @Path("aid") aid: String = AccountGamesRepository.account.acHash
    ): Call<ResponseBody>

}