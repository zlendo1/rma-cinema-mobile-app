package ba.etf.rma23.projekat.data.repositories

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import ba.etf.rma23.projekat.domain.GameReview

@Dao
interface GameReviewDao {

    @Query("SELECT * FROM gamereview")
    fun getAll(): List<GameReview>

    @Query("SELECT * FROM gamereview WHERE online = false")
    fun getOffline(): List<GameReview>

    @Update
    fun update(gameReview: GameReview)

    @Insert
    fun insertAll(vararg gameReview: GameReview)

}