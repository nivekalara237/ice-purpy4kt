package com.nivekaa.icepurpykt.domain.listener

import com.nivekaa.icepurpykt.domain.model.CashOrCardPaymentInfo

interface OnSubmitCashPaymentInfoListener {
    fun cashSubmit(form: CashOrCardPaymentInfo?)
}