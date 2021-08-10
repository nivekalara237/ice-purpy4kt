package com.nivekaa.icepurpykt.infra.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.nivekaa.icepurpykt.R
import com.nivekaa.icepurpykt.domain.listener.OnXxCreaseQteItemListener
import com.nivekaa.icepurpykt.domain.model.OrderItemVM
import com.nivekaa.icepurpykt.infra.holder.OrderViewHolder
import com.squareup.picasso.Picasso
import java.math.BigDecimal

class OrderApdater(
    private val context: Context,
    vms: MutableList<OrderItemVM>,
    listener: OnXxCreaseQteItemListener
) : RecyclerView.Adapter<OrderViewHolder?>() {
    private val qteItemListener: OnXxCreaseQteItemListener = listener
    private val orderItems: MutableList<OrderItemVM> = vms
    override fun onCreateViewHolder(parent: ViewGroup, i: Int): OrderViewHolder {
        val v: View =
            LayoutInflater.from(parent.context).inflate(R.layout.order_item, parent, false)
        return OrderViewHolder(v)
    }

    override fun onBindViewHolder(holder: OrderViewHolder, i: Int) {
        val orderItem: OrderItemVM = orderItems[i]
        holder.qte.setText(java.lang.String.valueOf(orderItem.quantity))
        holder.name.text = orderItem.product.name
        holder.price.text = java.lang.String.valueOf(
            orderItem.product.price.setScale(2, BigDecimal.ROUND_HALF_EVEN)
        )
        if (orderItem.product.price != null && orderItem.product.oldPrice
                !!.compareTo(orderItem.product.price) > 0
        ) {
            holder.oldPrice.text = orderItem.product.oldPrice!!.setScale(2, BigDecimal.ROUND_HALF_EVEN)
                .toPlainString()
            holder.oldPrice.visibility = View.VISIBLE
            holder.oldPrice.paint.isStrikeThruText = true
        } else {
            holder.oldPrice.visibility = View.GONE
        }
        holder.remove.setOnClickListener { v ->
            qteItemListener.remove(orderItem)
            orderItems.remove(orderItem)
            if (orderItems.size == 0) qteItemListener.allItemsHaveRemoved()
        }
        holder.increateQte.setOnClickListener {
            val v: String = holder.qte.text.toString()
            qteItemListener.increase(orderItem)
            holder.qte.setText((Integer.parseInt(v) + 1).toString())
        }
        holder.decreateQte.setOnClickListener {
            val v: String = holder.qte.text.toString()
            if (v != null && Integer.parseInt(v) > 1) {
                qteItemListener.decrease(orderItem)
                holder.qte.setText((Integer.parseInt(v) - 1).toString())
            }
        }
        Picasso.get().load(orderItem.product.imageUrl)
            .error(R.drawable.shoe_nike_air_max_red_128dp)
            .into(holder.image)
    }

    override fun getItemCount(): Int {
        return orderItems.size
    }

}