package ba.etf.rma23.projekat.data.repositories

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import ba.etf.rma23.projekat.domain.GameReview

@Dao
interface GameReviewDao {

    @Query("SELECT * FROM gamereview")
    suspend fun getAll(): List<GameReview>

    @Query("SELECT * FROM gamereview WHERE online = false")
    suspend fun getOffline(): List<GameReview>

    @Update
    suspend fun update(gameReview: GameReview)

    @Insert
    suspend fun insertAll(vararg gameReview: GameReview)

}