package com.nivekaa.icepurpykt.domain.model

import java.io.Serializable
import java.math.BigDecimal

class ProductVM constructor(
    var categoryType: CategoryType,
    var id: String,
    var name: String,
    var price: BigDecimal,
    var rate: Float,
    var imageUrl: String,
    var oldPrice: BigDecimal? = null,
    var discount: Float? = null,
    var bgColor: String? = null,
    var description: String? = null,
    var inStock: Int? = null,
) : Serializable {
    constructor(
        id: String,
        name: String,
        price: BigDecimal,
        rate: Float,
        imageUrl: String,
        oldPrice: BigDecimal?,
        discount: Float?,
        categoryType: CategoryType,
        description: String?,
    ) : this(categoryType, id, name, price, rate, imageUrl,oldPrice, discount, null, description, null)


    /*constructor(
        id: String,
        name: String,
        price: BigDecimal,
        rate: Float,
        imageUrl: String
    ) : this() {
        this.id = id
        this.name = name
        this.price = price
        this.rate = rate
        this.imageUrl = imageUrl
    }*/

    /*constructor(
        id: String,
        name: String,
        price: BigDecimal,
        rate: Float,
        imageUrl: String,
        oldPrice: BigDecimal?,
        discount: Float?
    ) : this(id, name, price, rate, imageUrl) {
        if (oldPrice != null) {
            this.oldPrice = oldPrice
        }
        if (discount != null) {
            this.discount = discount
        }
    }*/

    /*constructor(
        id: String,
        name: String,
        price: BigDecimal,
        rate: Float,
        imageUrl: String,
        oldPrice: BigDecimal?,
        discount: Float?,
        ctype: CategoryType
    ) : this(id, name, price, rate, imageUrl, oldPrice, discount) {
        categoryType = ctype
    }*/

    /*constructor(
        id: String,
        name: String,
        price: BigDecimal,
        rate: Float,
        imageUrl: String,
        oldPrice: BigDecimal?,
        discount: Float?,
        ctype: CategoryType,
        description: String?
    ) : this(id, name, price, rate, imageUrl, oldPrice, discount, ctype) {
        this.description = description
    }*/

    override fun toString(): String {
        return "ProductVM{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", price=" + price +
                ", inStock=" + inStock +
                ", rate=" + rate +
                ", oldPrice=" + oldPrice +
                ", discount=" + discount +
                ", imageUrl='" + imageUrl + '\'' +
                ", bgColor='" + bgColor + '\'' +
                ", catrgoryType='" + categoryType + '\'' +
                ", description='" + description + '\'' +
                '}'
    }
}