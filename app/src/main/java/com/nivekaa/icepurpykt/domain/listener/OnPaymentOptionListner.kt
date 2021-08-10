package com.nivekaa.icepurpykt.domain.listener

import com.nivekaa.icepurpykt.domain.model.PaymentMode

interface OnPaymentOptionListner {
    fun paymentOptionSelected(paymentMode: PaymentMode?)
}