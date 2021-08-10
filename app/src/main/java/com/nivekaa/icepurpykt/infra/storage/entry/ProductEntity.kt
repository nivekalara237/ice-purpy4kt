package com.nivekaa.icepurpykt.infra.storage.entry

import com.nivekaa.icepurpykt.domain.model.CategoryType
import com.nivekaa.icepurpykt.domain.model.ProductVM
import org.apache.commons.lang3.StringUtils
import java.math.BigDecimal

object ProductEntity {
    const val TABLE_NAME = "product_entry"
    const val COLUMN_NAME_NAME = "name"
    const val COLUMN_NAME_UUID = "_uuid"
    const val COLUMN_NAME_ID = "product_id"
    const val COLUMN_NAME_PRICE = "price"
    const val COLUMN_NAME_DISCOUNT = "_discount"
    const val COLUMN_NAME_OLD_PRICE = "old_price"
    const val COLUMN_NAME_RATE = "rate"
    const val COLUMN_NAME_IMAGE = "image"
    const val COLUMN_NAME_CATEGORY = "categorie"
    const val COLUMN_NAME_DESCRIPTION = "description"
    fun vmToEntry(vm: ProductVM): Entry {
        return Entry(vm.id, vm.price.toFloat(), vm.imageUrl, vm.name, vm.rate)
    }

    fun entryToVm(entry: Entry): ProductVM {
        return ProductVM(
            entry.uuid!!,
            entry.name!!,
            BigDecimal.valueOf(entry.price!!.toDouble()),
            entry.rate!!,
            entry.image!!,
            BigDecimal.valueOf(entry.oldPprice!!.toDouble()),
            entry.discount!!,
            if (entry.category != null) CategoryType.valueOf(StringUtils.toRootUpperCase(entry.category!!)) else CategoryType.UNCATEGORIZED,
            entry.description
        )
    }

    class Entry {
        var uuid: String? = null
        var price: Float? = null
        var oldPprice: Float? = null
        var image: String? = null
        var name: String? = null
        var id: Int? = null
        var rate: Float? = null
        var discount: Float? = null
        var category: String? = null
        var description: String? = null

        constructor() {}
        constructor(uuid: String?, price: Float?, image: String?, name: String?, rate: Float?) {
            this.uuid = uuid
            this.price = price
            this.image = image
            this.name = name
            this.rate = rate
        }

        constructor(
            uuid: String?,
            price: Float?,
            image: String?,
            name: String?,
            rate: Float?,
            oldPrice: Float?,
            discount: Float?
        ) : this(uuid, price, image, name, rate) {
            this.discount = discount
            oldPprice = oldPrice
        }

        constructor(
            uuid: String?,
            price: Float?,
            image: String?,
            name: String?,
            rate: Float?,
            oldPrice: Float?,
            discount: Float?,
            category: String?,
            desc: String?
        ) : this(uuid, price, image, name, rate, oldPrice, discount) {
            this.category = category
            description = desc
        }
    }
}