package com.nivekaa.icepurpykt.infra.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.github.thunder413.datetimeutils.DateTimeStyle
import com.github.thunder413.datetimeutils.DateTimeUtils
import com.nivekaa.icepurpykt.R
import com.nivekaa.icepurpykt.domain.model.TransVM
import com.nivekaa.icepurpykt.infra.holder.TransactionsViewHolder

class TransactionAdapter(list: List<TransVM>, var context: Context) :
    RecyclerView.Adapter<TransactionsViewHolder?>() {
    var transactions: List<TransVM>
    var mListener: OnPrintButtonClickListener? = null
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TransactionsViewHolder {
        val view: View =
            LayoutInflater.from(context).inflate(R.layout.transaction_item, parent, false)
        return TransactionsViewHolder(view)
    }

    @SuppressLint("DefaultLocale")
    override fun onBindViewHolder(holder: TransactionsViewHolder, position: Int) {
        val trans: TransVM = transactions[position]
        holder.numero_trans.text = trans.numero
        holder.prix_trans.text = java.lang.String.format("%,.2f %s", trans.prix, "USD")
        val date: String
        date = if (DateTimeUtils.isToday(trans.date)) {
            DateTimeUtils.formatTime(trans.date, true)
        } else {
            DateTimeUtils.formatWithStyle(trans.date, DateTimeStyle.MEDIUM)
        }
        holder.date_trans.text = date
        if (trans.isStatus) {
            holder.status_trans.setBackgroundResource(R.drawable.bg_transaction_item_successeded)
            holder.status_trans.setTextColor(Color.parseColor("#484BAC"))
            holder.status_trans.text = "succeeded"
            holder.prix_trans.setTextColor(Color.parseColor("#14de14"))
        } else {
            holder.print_btn.visibility = View.GONE
            holder.status_trans.setBackgroundResource(R.drawable.bg_transaction_item_failed)
            holder.status_trans.setTextColor(Color.parseColor("#AAAAAA"))
            holder.status_trans.text = "Failed"
            holder.prix_trans.setTextColor(Color.parseColor("#AAAAAA"))
        }
        holder.numero_carte.text = trans.numeroAffichable
        holder.print_btn.visibility = View.GONE
        holder.print_btn.setOnClickListener {
            if (mListener != null) mListener!!.printTransaction(
                trans
            )
        }
    }

    override fun getItemCount(): Int {
        return transactions.size
    }

    interface OnPrintButtonClickListener {
        fun printTransaction(trans: TransVM?)
    }

    init {
        transactions = list
        if (context is OnPrintButtonClickListener) mListener = context as OnPrintButtonClickListener
    }
}