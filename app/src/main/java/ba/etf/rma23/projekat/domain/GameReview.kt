package ba.etf.rma23.projekat.domain

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class GameReview(
    val rating: Int?,
    val review: String?,
    val igdb_id: Int,
    var online: Boolean,
    val student: String?,
    val timestamp: String?,
    @PrimaryKey(autoGenerate = true) val id: Int = 0
)
