package com.nivekaa.icepurpykt.domain.listener

import com.nivekaa.icepurpykt.domain.model.ProductVM

interface OnProductSelectedListener {
    fun productSelected(product: ProductVM?)
}