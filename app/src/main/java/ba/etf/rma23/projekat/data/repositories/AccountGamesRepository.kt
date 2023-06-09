package ba.etf.rma23.projekat.data.repositories

import ba.etf.rma23.projekat.auxiliary.isAgeSafe
import ba.etf.rma23.projekat.data.repositories.result.GameResultAccount
import ba.etf.rma23.projekat.domain.Account
import ba.etf.rma23.projekat.domain.Game
import ba.unsa.etf.gamespirala.BuildConfig
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType
import okhttp3.RequestBody

object AccountGamesRepository {

    var account: Account = Account(BuildConfig.ACCOUNT_API_HASH, "zlendo1@etf.unsa.ba", 0)

    suspend fun setHash(accountHash: String): Boolean {
        return withContext(Dispatchers.IO) {
            if (accountHash.isEmpty()) {
                return@withContext true
            }

            account.acHash = accountHash

            return@withContext true
        }
    }

    suspend fun getHash(): String {
        return withContext(Dispatchers.IO) {
            return@withContext account.acHash
        }
    }

    suspend fun setAge(age: Int): Boolean {
        return withContext(Dispatchers.IO) {
            if (age < 3 || age > 100) {
                return@withContext false
            }

            account.age = age

            return@withContext true
        }
    }

    suspend fun getSavedGames(): List<Game> {
        return withContext(Dispatchers.IO) {
            val response = AccountApiConfig.retrofit.getGames()

            if (response.isSuccessful) {
                val result = response.body()

                if (!result.isNullOrEmpty()) {
                    return@withContext resultToGame(result)
                }
            }

            return@withContext emptyList()
        }
    }

    suspend fun getGamesContainingString(query: String): List<Game> {
        return withContext(Dispatchers.IO) {
            val response = AccountApiConfig.retrofit.getGamesByName(query)

            if (response.isSuccessful) {
                val body = response.body()!!

                return@withContext resultToGame(body)
            }

            return@withContext emptyList()
        }
    }

    suspend fun saveGame(game: Game): Game? {
        return withContext(Dispatchers.IO) {
            try {
                val body = RequestBody.create(
                    MediaType.get("application/json; chatset=utf-8"),
                    "{\"game\": {\"igdb_id\": \"${game.id}\", \"name\": \"${game.title}\"}}"
                )

                AccountApiConfig.retrofit.postGame(body)

                return@withContext game
            } catch (e: Exception) {
                return@withContext null
            }
        }
    }

    suspend fun removeGame(id: Int): Boolean {
        return withContext(Dispatchers.IO) {
            try {
                AccountApiConfig.retrofit.deleteGameById(id)

                return@withContext true
            } catch (e: Exception) {
                return@withContext false
            }
        }
    }

    suspend fun removeNonSafe(): Boolean {
        return withContext(Dispatchers.IO) {
            val games = getSavedGames()

            for (game in games) {
                if (!isAgeSafe(account, game)) {
                    if (!removeGame(game.id)) {
                        return@withContext false
                    }
                }
            }

            return@withContext true
        }
    }

    private suspend fun resultToGame(gameResults: List<GameResultAccount>): List<Game> {
        return withContext(Dispatchers.IO) {
            val games: ArrayList<Game> = arrayListOf()

            for (it in gameResults) {
                games.add(
                    GamesRepository.resultToGame(
                        listOf(GamesRepository.getGameById(it.igdb_id)!!)
                    ).first()
                )
            }

            return@withContext games
        }
    }

}