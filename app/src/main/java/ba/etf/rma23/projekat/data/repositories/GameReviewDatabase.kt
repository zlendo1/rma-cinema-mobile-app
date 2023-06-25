package ba.etf.rma23.projekat.data.repositories

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import ba.etf.rma23.projekat.domain.GameReview

@Database(entities = [GameReview::class], version = 1, exportSchema = false)
abstract class GameReviewDatabase : RoomDatabase() {

    abstract fun gameReviewDao(): GameReviewDao

    companion object {
        private var INSTANCE: GameReviewDatabase? = null

        fun getInstance(context: Context): GameReviewDatabase {
            if (INSTANCE == null) {
                synchronized(GameReviewDatabase::class) {
                    INSTANCE = buildRoomDB(context)
                }
            }

            return INSTANCE!!
        }

        private fun buildRoomDB(context: Context) =
            Room.databaseBuilder(
                context.applicationContext,
                GameReviewDatabase::class.java,
                "gamereview-db"
            ).build()
    }

}