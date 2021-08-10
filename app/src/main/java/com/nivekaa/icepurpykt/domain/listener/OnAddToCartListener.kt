package com.nivekaa.icepurpykt.domain.listener

import com.nivekaa.icepurpykt.domain.model.ProductVM

interface OnAddToCartListener {
    fun addProduct(product: ProductVM?)
}