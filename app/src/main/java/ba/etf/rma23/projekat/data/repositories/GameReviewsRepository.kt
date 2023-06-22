package ba.etf.rma23.projekat.data.repositories

import android.content.Context
import ba.etf.rma23.projekat.domain.GameReview
import ba.etf.rma23.projekat.data.repositories.result.GameReviewResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

object GameReviewsRepository {

    suspend fun getOfflineReviews(context: Context): List<GameReview> = withContext(Dispatchers.IO) {
        val db = GameReviewDatabase.getInstance(context)

        return@withContext db.gameReviewDao().getOffline()
    }

    suspend fun sendOfflineReviews(context: Context): Int = withContext(Dispatchers.IO) {
        val db = GameReviewDatabase.getInstance(context)

        val offlineReviews = getOfflineReviews(context)
        var counter = 0

        offlineReviews.forEach { review ->
            try {
                AccountApiConfig.retrofit.postGameReview(
                    AccountGamesRepository.account.acHash,
                    review.igdb_id,
                    GameReviewResult(review.rating, review.review, review.timestamp, review.student)
                )

                review.online = true

                db.gameReviewDao().update(review)

                ++counter
            } catch (_: Exception) {}
        }

        return@withContext counter
    }

    suspend fun sendReview(context: Context, gameReview: GameReview): Boolean = withContext(Dispatchers.IO) {
        val db = GameReviewDatabase.getInstance(context)

        if (AccountGamesRepository.getGameById(gameReview.igdb_id) == null) {
            val savedGame = AccountGamesRepository.saveGame(
                GamesRepository.resultToGame(
                    listOf(GamesRepository.getGameById(gameReview.igdb_id)!!)
                ).first()
            ) ?: throw Exception("Game not properly saved in sendReview")

        }

        try {
            AccountApiConfig.retrofit.postGameReview(
                AccountGamesRepository.account.acHash,
                gameReview.igdb_id,
                GameReviewResult(gameReview.rating, gameReview.review, gameReview.timestamp, gameReview.student)
            )

            return@withContext true

        } catch (e: Exception) {
            gameReview.online = false

            db.gameReviewDao().insertAll(gameReview)
        }

        return@withContext false
    }

    suspend fun getReviewsForGame(igdb_id:Int): List<GameReview> = withContext(Dispatchers.IO) {
        val response = AccountApiConfig.retrofit.getGameReviews(igdb_id)

        if (response.isSuccessful) {
            val body = response.body()

            return@withContext resultToReview(igdb_id, body!!)
        }

        return@withContext emptyList()
    }

    private fun resultToReview(igdb_id: Int, gameReviewResults: List<GameReviewResult>): List<GameReview> {
        val gameReviews: ArrayList<GameReview> = arrayListOf()

        gameReviewResults.forEach {
            gameReviews.add(
                GameReview(it.rating, it.review, igdb_id, true, it.student, it.timestamp)
            )
        }

        return gameReviews
    }

}