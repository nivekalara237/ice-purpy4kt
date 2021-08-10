package com.nivekaa.icepurpykt.domain.listener

import com.nivekaa.icepurpykt.domain.model.CashOrCardPaymentInfo

interface OnSubmitCardPaymentInfoListener {
    fun cardSubmit(form: CashOrCardPaymentInfo?)
}