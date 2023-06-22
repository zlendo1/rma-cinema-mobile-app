package ba.etf.rma23.projekat.domain

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class GameReview(
    val rating: Int?,
    val review: String?,
    @PrimaryKey val igdb_id: Int,
    var online: Boolean,
    val student: String?,
    val timestamp: String?
)
