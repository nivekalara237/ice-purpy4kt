package com.nivekaa.icepurpykt.domain.listener

import com.nivekaa.icepurpykt.domain.model.LabelVM

interface OnLabelSelectedListener {
    fun LabelSelected(label: LabelVM?)
}