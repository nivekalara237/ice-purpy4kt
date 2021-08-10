package com.nivekaa.icepurpykt.infra.holder

import android.view.View
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.nivekaa.icepurpykt.R
import com.nivekaa.icepurpykt.infra.view.LabelCardView

class ProductViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    var price: TextView = itemView.findViewById(R.id.price)
    var cardView: LabelCardView = itemView.findViewById(R.id.card)
    var oldPrice: TextView = itemView.findViewById(R.id.old_price)
    var name: TextView = itemView.findViewById(R.id.title)
    var rate: RatingBar = itemView.findViewById(R.id.rate)
    var photo: ImageView = itemView.findViewById(R.id.photo)
    var btnAddToCart: CardView = itemView.findViewById(R.id.btn_add_to_cart)

}