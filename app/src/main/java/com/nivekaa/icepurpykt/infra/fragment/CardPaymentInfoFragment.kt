package com.nivekaa.icepurpykt.infra.fragment

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import butterknife.BindView
import butterknife.ButterKnife
import com.craftman.cardform.CardForm
import com.craftman.cardform.OnPayBtnClickListner
import com.nivekaa.icepurpykt.R
import com.nivekaa.icepurpykt.domain.listener.OnFragmentBackPressedListener
import com.nivekaa.icepurpykt.domain.listener.OnSubmitCardPaymentInfoListener
import com.nivekaa.icepurpykt.domain.model.CashOrCardPaymentInfo
import com.nivekaa.icepurpykt.infra.activity.CartActivity.Companion.TOTAL_PRICE
import java.lang.String.format

/**
 * A simple [Fragment] subclass.
 * Use the [CardPaymentInfoFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
@SuppressLint("NonConstantResourceId")
class CardPaymentInfoFragment(ctxOrListener: Context) : Fragment(), OnFragmentBackPressedListener {
    private val submitBtnClickerListener: OnSubmitCardPaymentInfoListener = ctxOrListener as OnSubmitCardPaymentInfoListener
    private var price = 0f
    private val _context: Context = ctxOrListener

    @BindView(R.id.card_form)
    @JvmField var cardForm: CardForm? = null

    @BindView(R.id.payment_amount)
    @JvmField var paymentAmount: TextView? = null

    @JvmField
    @BindView(R.id.btn_pay)
    var paymentBtn: Button? = null
    @SuppressLint("UseRequireInsteadOfGet")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (arguments != null) {
            price = arguments!!.getFloat(TOTAL_PRICE)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view: View = inflater.inflate(R.layout.fragment_card_payment_info, container, false)
        ButterKnife.bind(this, view)
        cardForm?.setAmount(price.toString())
        paymentAmount?.text = format("$%s", price)
        cardForm?.setPayBtnClickListner(OnPayBtnClickListner {
            val info = CashOrCardPaymentInfo()
            info.cardInfo = it
            submitBtnClickerListener.cardSubmit(info)
        })
        paymentBtn!!.text = "Continue"
        return view
    }

    override fun onBackPressed() {
        requireActivity().supportFragmentManager
            .popBackStack()
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @return A new instance of fragment CardPaymentInfoFragment.
         */
        // TODO: Rename and change types and number of parameters
        fun newInstance(ctx: Context, price: Float): CardPaymentInfoFragment {
            val fragment = CardPaymentInfoFragment(ctx)
            val args = Bundle()
            args.putFloat(TOTAL_PRICE, price)
            fragment.arguments = args
            return fragment
        }
    }

}