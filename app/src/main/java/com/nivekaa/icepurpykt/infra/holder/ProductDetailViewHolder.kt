package com.nivekaa.icepurpykt.infra.holder

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.nivekaa.icepurpykt.R
import com.nivekaa.icepurpykt.infra.view.LabelCardView

class ProductDetailViewHolder(itemView: View) {
    var price: TextView
    var cardView: LabelCardView
    var oldPrice: TextView
    var name: TextView
    var description: TextView
    var photo: ImageView
    var btnAddToCart: CardView
    var suggestionRv: RecyclerView
    var closeDialog: ImageView

    init {
        cardView = itemView.findViewById(R.id.layout1)
        price = itemView.findViewById<TextView>(R.id.price)
        oldPrice = itemView.findViewById<TextView>(R.id.old_price)
        name = itemView.findViewById<TextView>(R.id.title)
        description = itemView.findViewById<TextView>(R.id.description)
        photo = itemView.findViewById(R.id.photo)
        btnAddToCart = itemView.findViewById(R.id.btn_add_to_cart)
        suggestionRv = itemView.findViewById(R.id.suggestionRV)
        closeDialog = itemView.findViewById(R.id.close_dialog)
    }
}