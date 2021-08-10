package com.nivekaa.icepurpykt.infra.adapter

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.nivekaa.icepurpykt.R
import com.nivekaa.icepurpykt.domain.listener.OnLabelSelectedListener
import com.nivekaa.icepurpykt.domain.model.LabelVM
import com.nivekaa.icepurpykt.infra.holder.LabelViewHolder

class LabelAdapter(ctx: Context, vms: List<LabelVM>) : RecyclerView.Adapter<LabelViewHolder?>() {
    private val labels: List<LabelVM> = vms
    private val context: Context = ctx
    private val labelSelectedListener: OnLabelSelectedListener = ctx as OnLabelSelectedListener
    override fun onCreateViewHolder(parent: ViewGroup, i: Int): LabelViewHolder {
        val v: View =
            LayoutInflater.from(parent.context).inflate(R.layout.label_item, parent, false)
        return LabelViewHolder(v)
    }

    override fun onBindViewHolder(labelViewHolder: LabelViewHolder, i: Int) {
        val vm: LabelVM = labels[i]
        labelViewHolder.button.text = vm.title
        if (vm.color != null) labelViewHolder.button.setBackgroundColor(Color.parseColor(vm.color))
        labelViewHolder.button.setOnClickListener { labelSelectedListener.LabelSelected(vm) }
    }

    override fun getItemCount(): Int {
        return labels.size
    }

}