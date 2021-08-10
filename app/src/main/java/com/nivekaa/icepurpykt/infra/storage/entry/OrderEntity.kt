package com.nivekaa.icepurpykt.infra.storage.entry

import com.nivekaa.icepurpykt.domain.model.OrderItemVM

object OrderEntity {
    const val TABLE_NAME = "order_entry"
    const val COLUMN_NAME_PRODUCT_UUID = "product_fk_id"
    const val COLUMN_NAME_ID = "order_id"
    const val COLUMN_NAME_QTE = "quantity"
    fun vmToEntry(vm: OrderItemVM?): Entry? {
        //return new Entry(vm.getProduct().getId(), vm.getQuantity());
        return null
    }

    class Entry(var productUuid: Int, var qte: Int) {
        var id: Int? = null
    }
}