package com.nivekaa.icepurpykt.domain.listener

import com.nivekaa.icepurpykt.domain.model.OrderItemVM

interface OnXxCreaseQteItemListener {
    fun increase(order: OrderItemVM?)
    fun decrease(order: OrderItemVM?)
    fun remove(order: OrderItemVM?)
    fun allItemsHaveRemoved()
}