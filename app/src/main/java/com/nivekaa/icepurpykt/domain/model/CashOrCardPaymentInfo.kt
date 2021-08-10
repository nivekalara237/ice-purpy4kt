package com.nivekaa.icepurpykt.domain.model

import com.craftman.cardform.Card

class CashOrCardPaymentInfo {
    var fullname: String? = null
    var email: String? = null
    var phone: String? = null
    var addressInfo: AddressInfo? = null
    var cardInfo: Card? = null

    constructor() {}
    constructor(fullname: String?, email: String?, phone: String?) {
        this.fullname = fullname
        this.email = email
        this.phone = phone
    }

    constructor(
        fullname: String?,
        email: String?,
        phone: String?,
        addressInfo: AddressInfo?
    ) : this(fullname, email, phone) {
        this.addressInfo = addressInfo
    }

    override fun toString(): String {
        return "CashPaymentInfo{" +
                "fullname='" + fullname + '\'' +
                ", email='" + email + '\'' +
                ", phone='" + phone + '\'' +
                ", addressInfo=" + addressInfo +
                '}'
    }
}