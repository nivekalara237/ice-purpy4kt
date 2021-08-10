package com.nivekaa.icepurpykt.infra.fragment

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.fragment.app.Fragment
import butterknife.BindView
import butterknife.ButterKnife
import butterknife.OnClick
import com.developer.kalert.KAlertDialog
import com.gbksoft.countrycodepickerlib.CountryCodePicker
import com.mobsandgeeks.saripaar.ValidationError
import com.mobsandgeeks.saripaar.Validator
import com.mobsandgeeks.saripaar.annotation.Email
import com.mobsandgeeks.saripaar.annotation.NotEmpty
import com.nivekaa.icepurpykt.R
import com.nivekaa.icepurpykt.domain.listener.OnFragmentBackPressedListener
import com.nivekaa.icepurpykt.domain.listener.OnSubmitCashPaymentInfoListener
import com.nivekaa.icepurpykt.domain.model.AddressInfo
import com.nivekaa.icepurpykt.domain.model.CashOrCardPaymentInfo
import com.nivekaa.icepurpykt.infra.activity.CartActivity.Companion.TOTAL_PRICE
import com.nivekaa.icepurpykt.infra.activity.CheckoutActivity
import org.apache.commons.lang3.StringUtils
import java.lang.String.format
import java.util.*

/**
 * A simple [Fragment] subclass.
 * Use the [CashPaymentInfoFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
@SuppressLint("NonConstantResourceId")
class CashPaymentInfoFragment(ctxOrListener: Context) : Fragment(), OnFragmentBackPressedListener,
    Validator.ValidationListener {
    private val cashPaymentInfoListener: OnSubmitCashPaymentInfoListener = ctxOrListener as OnSubmitCashPaymentInfoListener
    private var price = 0f
    private val _context: Context = ctxOrListener
    private var validator: Validator? = null

    @NotEmpty
    @BindView(R.id.fullname)
    @JvmField var fullname: EditText? = null

    @NotEmpty
    @BindView(R.id.phone)
    @JvmField var phone: EditText? = null

    @Email
    @NotEmpty
    @BindView(R.id.email)
    @JvmField var email: EditText? = null

    @NotEmpty
    @BindView(R.id.address)
    @JvmField var address: EditText? = null

    @BindView(R.id.countryCodePickerView)
    @JvmField var countryCode: CountryCodePicker? = null

    @BindView(R.id.city)
    @JvmField var city: EditText? = null

    @BindView(R.id.state)
    @JvmField var state: EditText? = null

    @NotEmpty
    @BindView(R.id.zipcode)
    @JvmField var zipcode: EditText? = null

    @BindView(R.id.deserve)
    @JvmField var btnSubmit: Button? = null
    @SuppressLint("UseRequireInsteadOfGet")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (arguments != null) {
            price = arguments!!.getFloat(TOTAL_PRICE)
        }
        validator = Validator(this)
        validator!!.setValidationListener(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        val view: View = inflater.inflate(R.layout.fragment_cash_payment_info, container, false)
        ButterKnife.bind(this, view)
        countryCode?.registerCarrierNumberEditText(phone)
        btnSubmit?.text = format("Place my order - $%s", price)
        return view
    }

    override fun onBackPressed() {
        requireActivity().supportFragmentManager
            .beginTransaction()
            .remove(this).commit()
    }

    @OnClick(R.id.deserve)
    public fun submit(v: View?) {
        if (StringUtils.isEmpty(countryCode?.selectedCountryCode)) {
            (_context as CheckoutActivity).showErrorMessage("The country is required")
        } else validator!!.validate()
    }

    override fun onValidationSucceeded() {
        val info = CashOrCardPaymentInfo()
        info.addressInfo = (AddressInfo())
        info.email = (email?.text.toString())
        info.fullname = (fullname?.text.toString())
        info.phone = (phone?.text.toString())
        if (StringUtils.isNotEmpty(phone?.text)) info.phone = (phone?.text.toString())
        if (StringUtils.isNotEmpty(city?.text))
            info.addressInfo!!.city = (phone?.text.toString())
        if (StringUtils.isNotEmpty(state?.text))
            info.addressInfo!!.state = (state?.text.toString())
        info.addressInfo!!.zipcode = zipcode?.text.toString()
        info.addressInfo!!.country = (countryCode?.selectedCountryCode)
        info.addressInfo!!.address = (address?.text.toString())
        info.addressInfo!!.id = (UUID.randomUUID().toString())
        confirmation(info)
    }

    private fun confirmation(info: CashOrCardPaymentInfo) {
        val confirmDialog = KAlertDialog(_context, KAlertDialog.WARNING_TYPE)
        confirmDialog.setCancelable(false)
        confirmDialog.contentText = "Confirm the informations typed"
        confirmDialog.confirmText = "I Confirm"
        confirmDialog.cancelText = "No! Return"
        confirmDialog.confirmButtonColor(R.color.warning_stroke_color, _context)
        confirmDialog.cancelButtonColor(R.color.colorGrey, _context)
        confirmDialog.setConfirmClickListener { alert: KAlertDialog ->
            cashPaymentInfoListener.cashSubmit(info)
            alert.dismissWithAnimation()
        }
        confirmDialog.show()
    }

    override fun onValidationFailed(errors: List<ValidationError>) {
        for (error in errors) {
            val view = error.view
            val message = error.getCollatedErrorMessage(_context)
            if (view is EditText) {
                view.error = message
            } else {
                (_context as CheckoutActivity).showErrorMessage(message)
            }
        }
    }

    companion object {
        fun newInstance(ctxOrListener: Context, price: Float): CashPaymentInfoFragment {
            val fragment = CashPaymentInfoFragment(ctxOrListener)
            val args = Bundle()
            args.putFloat(TOTAL_PRICE, price)
            fragment.arguments = args
            return fragment
        }
    }

}