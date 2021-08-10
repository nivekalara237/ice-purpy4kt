package com.nivekaa.icepurpykt.domain.model

class OrderItemVM(var product: ProductVM, quantity: Int) {
    var quantity = 1
    var id: Int? = null
    override fun toString(): String {
        return "OrderItemVM{" +
                "product=" + product +
                ", quantity=" + quantity +
                ", id=" + id +
                '}'
    }

    init {
        this.quantity = quantity
    }
}