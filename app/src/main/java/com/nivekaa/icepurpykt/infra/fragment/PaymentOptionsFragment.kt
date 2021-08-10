package com.nivekaa.icepurpykt.infra.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import androidx.fragment.app.Fragment
import com.nivekaa.icepurpykt.R
import com.nivekaa.icepurpykt.domain.listener.OnPaymentOptionListner
import com.nivekaa.icepurpykt.domain.model.PaymentMode

/**
 * A simple [Fragment] subclass.
 * Use the [PaymentOptionsFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class PaymentOptionsFragment(listner: OnPaymentOptionListner) : Fragment(), View.OnClickListener {
    private val paymentOptionListner: OnPaymentOptionListner = listner

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view: View = inflater.inflate(R.layout.fragment_payment_options, container, false)
        val cashMode: RelativeLayout = view.findViewById(R.id.cash_mode)
        val cardMode: RelativeLayout = view.findViewById(R.id.card_mode)
        cardMode.setOnClickListener(this)
        cashMode.setOnClickListener(this)
        return view
    }

    override fun onClick(v: View) {
        if (v.id == R.id.cash_mode) {
            paymentOptionListner.paymentOptionSelected(PaymentMode.CASH)
        } else if (v.id == R.id.card_mode) {
            paymentOptionListner.paymentOptionSelected(PaymentMode.CARD)
        }
    }

    companion object {
        fun newInstance(listner: OnPaymentOptionListner): PaymentOptionsFragment {
            val fragment = PaymentOptionsFragment(listner)
            val args = Bundle()
            fragment.arguments = args
            return fragment
        }
    }
}