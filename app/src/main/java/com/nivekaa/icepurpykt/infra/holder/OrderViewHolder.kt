package com.nivekaa.icepurpykt.infra.holder

import android.view.View
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.nivekaa.icepurpykt.R

class OrderViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    var name: TextView = itemView.findViewById<TextView>(R.id.title)
    var price: TextView = itemView.findViewById<TextView>(R.id.price)
    var oldPrice: TextView = itemView.findViewById<TextView>(R.id.old_price)
    var image: ImageView = itemView.findViewById(R.id.photo)
    var increateQte: ImageButton = itemView.findViewById<ImageButton>(R.id.increase_qte)
    var decreateQte: ImageButton = itemView.findViewById<ImageButton>(R.id.decrease_qte)
    var remove: ImageButton = itemView.findViewById<ImageButton>(R.id.remove)
    var qte: EditText = itemView.findViewById<EditText>(R.id.qte)

}