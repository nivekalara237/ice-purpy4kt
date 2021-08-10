package com.nivekaa.icepurpykt.domain.spi

import android.content.Context
import com.nivekaa.icepurpykt.domain.model.CategoryType
import com.nivekaa.icepurpykt.domain.model.LabelVM
import com.nivekaa.icepurpykt.domain.model.OrderItemVM
import com.nivekaa.icepurpykt.domain.model.ProductVM

interface DBStoragePort {
    fun init(context: Context?)
    val pants: List<ProductVM?>?
    val shoes: List<ProductVM?>?
    val tshirts: List<ProductVM?>?
    val hats: List<ProductVM?>?
    val bags: List<ProductVM?>?
    val orders: List<OrderItemVM?>?
    fun addOrder(order: OrderItemVM?)
    fun addProduct(productVM: ProductVM?)
    val labels: List<LabelVM?>?
    fun searchAny(category: CategoryType?): List<ProductVM?>?
}