package ba.unsa.etf.gamespirala.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RatingBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import ba.unsa.etf.gamespirala.R
import ba.unsa.etf.gamespirala.domain.UserImpression
import ba.unsa.etf.gamespirala.domain.UserRating
import ba.unsa.etf.gamespirala.domain.UserReview

class ImpressionListAdapter(
    private var impressions: List<UserImpression>
) : RecyclerView.Adapter<ImpressionListAdapter.ImpressionViewHolder>() {

    private val REVIEW_VIEW_TYPE: Int = 1
    private val RATING_VIEW_TYPE: Int = 2

    abstract inner class ImpressionViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val username: TextView = itemView.findViewById(R.id.username_textview)
    }

    inner class ReviewViewHolder(itemView: View) : ImpressionViewHolder(itemView) {
        val review: TextView = itemView.findViewById(R.id.review_textview)
    }

    inner class RatingViewHolder(itemView: View) : ImpressionViewHolder(itemView) {
        val rating: RatingBar = itemView.findViewById(R.id.rating_bar)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImpressionViewHolder {
        return when (viewType) {
            REVIEW_VIEW_TYPE -> {
                val reviewView = LayoutInflater
                    .from(parent.context)
                    .inflate(R.layout.item_impression_review, parent, false)

                ReviewViewHolder(reviewView)
            }
            RATING_VIEW_TYPE -> {
                val ratingView = LayoutInflater
                    .from(parent.context)
                    .inflate(R.layout.item_impression_rating, parent, false)

                RatingViewHolder(ratingView)
            }
            else -> throw IllegalArgumentException("Invalid view type")
        }
    }

    override fun getItemCount(): Int {
        return impressions.size
    }

    override fun onBindViewHolder(holder: ImpressionViewHolder, position: Int) {
        val impression: UserImpression = impressions[position]

        holder.username.text = impression.username

        when (holder) {
            is ReviewViewHolder -> holder.review.text = (impression as UserReview).review
            is RatingViewHolder -> holder.rating.rating = (impression as UserRating).rating.toFloat()
            else -> throw IllegalArgumentException("Invalid impression type")
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when (impressions[position]) {
            is UserReview -> REVIEW_VIEW_TYPE
            is UserRating -> RATING_VIEW_TYPE
            else -> throw IllegalArgumentException("Invalid impression type")
        }
    }

    fun updateImpressions(impressions: List<UserImpression>) {
        this.impressions = impressions.sortedBy { impression -> impression.timestamp }

        notifyDataSetChanged()
    }

}