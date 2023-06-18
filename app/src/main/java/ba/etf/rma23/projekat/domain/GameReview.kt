package ba.etf.rma23.projekat.domain

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class GameReview(
    @PrimaryKey val igdb_id: Int,
    val rating: Int?,
    val review: String?,
    val timestamp: String?,
    var online: Boolean
)
