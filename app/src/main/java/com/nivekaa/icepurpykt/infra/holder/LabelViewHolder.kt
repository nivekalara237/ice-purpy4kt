package com.nivekaa.icepurpykt.infra.holder

import android.view.View
import android.widget.Button
import androidx.recyclerview.widget.RecyclerView
import com.nivekaa.icepurpykt.R

class LabelViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    var button: Button = itemView.findViewById(R.id.label)
}