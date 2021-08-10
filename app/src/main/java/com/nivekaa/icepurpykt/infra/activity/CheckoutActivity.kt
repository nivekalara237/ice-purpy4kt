package com.nivekaa.icepurpykt.infra.activity

import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.View
import android.widget.FrameLayout
import android.widget.RelativeLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import butterknife.BindView
import com.developer.kalert.KAlertDialog
import com.nivekaa.icepurpykt.R
import com.nivekaa.icepurpykt.application.AbstractAppActivity
import com.nivekaa.icepurpykt.domain.listener.*
import com.nivekaa.icepurpykt.domain.model.CashOrCardPaymentInfo
import com.nivekaa.icepurpykt.domain.model.PaymentMode
import com.nivekaa.icepurpykt.infra.activity.CartActivity.Companion.TOTAL_PRICE
import com.nivekaa.icepurpykt.infra.fragment.CardPaymentInfoFragment
import com.nivekaa.icepurpykt.infra.fragment.CashPaymentInfoFragment
import com.nivekaa.icepurpykt.infra.fragment.PaymentOptionsFragment

open class CheckoutActivity : AbstractAppActivity(), OnSubmitCardPaymentInfoListener,
    OnPaymentOptionListner, OnCloseCurrentFragmentListener, View.OnClickListener,
    OnSubmitCashPaymentInfoListener {
    private val TAG: String = this.javaClass.getSimpleName()
    private var price = 0f
    private var cashOrCardPaymentInfo: CashOrCardPaymentInfo = CashOrCardPaymentInfo()

    @BindView(R.id.go_shop)
    @JvmField var continueShop: RelativeLayout? = null

    @BindView(R.id.fragment_layout)
    @JvmField var fragmentLayout: FrameLayout? = null
    override fun hasErrorView(): Boolean {
        return true
    }

    override fun hasToolBar(): Boolean {
        return true
    }

    override fun displayBackPressedIcon(): Boolean {
        return true
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_payment_mode)
        continueShop?.setOnClickListener(this)
        val bundle: Bundle? = getIntent().getExtras()
        if (bundle != null) {
            price = bundle.getFloat(TOTAL_PRICE, 0f)
        }
        displayPaymentOptionsFragment()
    }

    override fun cardSubmit(form: CashOrCardPaymentInfo?) {
        cashOrCardPaymentInfo = form!!
        displayCashInfoFragment()
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.go_shop -> startActivity(Intent(this, MainActivity::class.java))
            else -> Log.d("DEFAULT", "DEFAULT")
        }
    }

    private fun displayCardInfoFragment() {
        val fm: FragmentManager = supportFragmentManager
        val fragmentTransaction = fm.beginTransaction()
        //fragmentTransaction.setCustomAnimations(R.anim.slide_up, R.anim.slide_down);
        fragmentTransaction.setCustomAnimations(
            R.anim.enter,
            R.anim.exit,
            R.anim.pop_enter,
            R.anim.pop_exit
        )
        fragmentTransaction.replace(
            R.id.fragment_layout,
            CardPaymentInfoFragment.newInstance(this, price)
        )
        fragmentTransaction.commit()
    }

    @JvmOverloads
    fun displayCashInfoFragment(_context: Context? = this) {
        val fm: FragmentManager = supportFragmentManager
        val fragmentTransaction = fm.beginTransaction()
        fragmentTransaction.setCustomAnimations(
            R.anim.enter,
            R.anim.exit,
            R.anim.pop_enter,
            R.anim.pop_exit
        )
        fragmentTransaction.replace(
            R.id.fragment_layout,
            CashPaymentInfoFragment.newInstance(_context!!, price)
        )
        fragmentTransaction.commit()
    }

    private fun displayPaymentOptionsFragment() {
        val fm: FragmentManager = supportFragmentManager
        val fragmentTransaction = fm.beginTransaction()
        fragmentTransaction.setCustomAnimations(R.anim.fade_in, R.anim.fade_out)
        fragmentTransaction.replace(R.id.fragment_layout, PaymentOptionsFragment.newInstance(this))
        fragmentTransaction.commit()
    }

    override fun cashSubmit(form: CashOrCardPaymentInfo?) {
        cashOrCardPaymentInfo.addressInfo = (form?.addressInfo)
        cashOrCardPaymentInfo.phone = (form?.phone)
        cashOrCardPaymentInfo.fullname = (form?.fullname)
        cashOrCardPaymentInfo.email = (form?.email)
        val contactServerDialog = KAlertDialog(this, KAlertDialog.PROGRESS_TYPE)
        contactServerDialog.setCancelable(true)
        contactServerDialog.contentText = "Loading..."
        contactServerDialog.setCanceledOnTouchOutside(false)
        contactServerDialog.progressHelper.barColor = resources.getColor(R.color.colorPrimary)
        contactServerDialog.setConfirmClickListener { obj: KAlertDialog -> obj.dismissWithAnimation() }
        contactServerDialog.setOnDismissListener { alert: DialogInterface ->
            this@CheckoutActivity.addSuccessMessage(
                """
    We prepare for your order,
    It will be delivered to you in a 3 or 4 hours.
    """.trimIndent()
            ) { cleanOrderInDb() }
            alert.dismiss()
        }
        contactServerDialog.show()
        Handler().postDelayed({ contactServerDialog.dismiss() }, 10000)
    }

    private fun cleanOrderInDb() {
        dbHelper?.deleteAllOrders()
        startActivity(Intent(this, MainActivity::class.java))
    }

    fun showErrorMessage(message: String?) {
        addErrorMessage(message)
    }

    override fun onBackPressed() {
        val currentFragment: Fragment? =
            supportFragmentManager.findFragmentById(R.id.fragment_layout)
        if (currentFragment is OnFragmentBackPressedListener) {
            (currentFragment as OnFragmentBackPressedListener).onBackPressed()
            displayPaymentOptionsFragment()
        } else {
            super.onBackPressed()
        }
    }

    override fun paymentOptionSelected(paymentMode: PaymentMode?) {
        if (paymentMode === PaymentMode.CARD) displayCardInfoFragment()
        if (paymentMode === PaymentMode.CASH) displayCashInfoFragment()
    }

    override fun onCloseCurrentFragment() {
        val currentFragment: Fragment? =
            supportFragmentManager.findFragmentById(R.id.fragment_layout)
        if (currentFragment != null) {
            supportFragmentManager
                .beginTransaction()
                .remove(currentFragment).commit()
        }
    }
}