package com.nivekaa.icepurpykt.infra.holder

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.nivekaa.icepurpykt.R

class TransactionsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    var date_trans: TextView = itemView.findViewById(R.id.date_transaction)
    var numero_trans: TextView = itemView.findViewById(R.id.numero_transaction)
    var icon_carte: ImageView = itemView.findViewById(R.id.icon_carte)
    var print_btn: ImageView = itemView.findViewById(R.id.print_btn)
    var prix_trans: TextView = itemView.findViewById(R.id.prix_transaction)
    var status_trans: TextView = itemView.findViewById(R.id.status_transaction)
    var numero_carte: TextView = itemView.findViewById(R.id.numero_carte)
}