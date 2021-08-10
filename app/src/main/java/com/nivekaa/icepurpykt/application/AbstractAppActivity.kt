package com.nivekaa.icepurpykt.application

import android.annotation.SuppressLint
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.FrameLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import butterknife.ButterKnife
import com.developer.kalert.KAlertDialog
import com.developer.kalert.KAlertDialog.KAlertClickListener
import com.nivekaa.icepurpykt.R
import com.nivekaa.icepurpykt.infra.activity.CartActivity
import com.nivekaa.icepurpykt.infra.activity.MainActivity
import com.nivekaa.icepurpykt.infra.storage.DBHelper
import com.nivekaa.icepurpykt.infra.storage.SessionStorage
import com.zplesac.connectionbuddy.ConnectionBuddy
import com.zplesac.connectionbuddy.interfaces.ConnectivityChangeListener
import com.zplesac.connectionbuddy.models.ConnectivityEvent
import com.zplesac.connectionbuddy.models.ConnectivityState

abstract class AbstractAppActivity: AppCompatActivity(), ConnectivityChangeListener {
    private var dialog: KAlertDialog? = null
    var sessionStorage: SessionStorage? = null
    var dbHelper: DBHelper? = null
    var alertCount = 0
    private var yellowCircleCart: FrameLayout? = null
    private var countCartTextView: TextView? = null
    var errorTextView: TextView? = null

    protected abstract fun hasErrorView(): Boolean
    protected abstract fun hasToolBar(): Boolean
    protected open fun displayBackPressedIcon(): Boolean {
        return false
    }

    /*open fun initView() {
        if (hasErrorView()) {
            errorTextView = findViewById(R.id.error)
        }
    }*/

    override fun onConnectionChange(event: ConnectivityEvent?) {
        if (!(event != null && event.state != null && event.state.value == ConnectivityState.CONNECTED)) {
            //no_connection.setVisibility(View.VISIBLE);
            dialog = KAlertDialog(this, KAlertDialog.CUSTOM_IMAGE_TYPE)
            dialog!!.titleText = "Run your data bundle or active your wifi if is not."
            dialog!!.setIcon(R.drawable.ic_baseline_signal_wifi_off_24)
            dialog!!.setCustomImage(R.drawable.ic_baseline_signal_wifi_off_24, this)
            dialog!!.setCancelable(false)
            dialog!!.confirmText = "Reload app"
            dialog!!.confirmButtonColor(R.color.colorPrimary, this)
            dialog!!.setConfirmClickListener { kAlertDialog: KAlertDialog ->
                kAlertDialog.dismiss()
                startActivity(Intent(this@AbstractAppActivity, MainActivity::class.java))
                finish()
            }
            dialog!!.show()
        } else {
            if (dialog != null) {
                dialog!!.dismissWithAnimation()
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ConnectionBuddy.getInstance().clearNetworkCache(this, savedInstanceState)
        sessionStorage = SessionStorage(this)
        dbHelper = DBHelper.getInstance(this)
        alertCount = dbHelper?.allOrders?.size ?: 0
        if (this.hasErrorView()) {
            errorTextView = findViewById(R.id.error)
        }
    }

    override fun setContentView(layoutResID: Int) {
        super.setContentView(layoutResID)
        ButterKnife.bind(this)
        val actionBar = supportActionBar
        if (actionBar != null) {
            if (!hasToolBar()) {
                actionBar.hide()
            } else {
                actionBar.show()
                actionBar.setHomeButtonEnabled(false)
                actionBar.setDisplayHomeAsUpEnabled(displayBackPressedIcon())
                actionBar.setDisplayShowHomeEnabled(displayBackPressedIcon())
            }
        }
    }

    override fun onStart() {
        super.onStart()
        ConnectionBuddy.getInstance().registerForConnectivityEvents(this, this)
    }

    override fun onDestroy() {
        super.onDestroy()
    }

    override fun onStop() {
        super.onStop()
        ConnectionBuddy.getInstance().unregisterFromConnectivityEvents(this)
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    @SuppressLint("NonConstantResourceId")
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_cart -> {
                startActivity(Intent(applicationContext, CartActivity::class.java))
                true
            }
            R.id.home -> {
                onBackPressed()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.manu, menu)
        return true
    }

    override fun onPrepareOptionsMenu(menu: Menu): Boolean {
        val alertMenuItem = menu.findItem(R.id.action_cart)
        val rootView = alertMenuItem.actionView as FrameLayout
        yellowCircleCart = rootView.findViewById(R.id.view_cart_circle)
        countCartTextView = rootView.findViewById(R.id.view_cart_badge_count)
        rootView.setOnClickListener {
            onOptionsItemSelected(
                alertMenuItem
            )
        }
        updateAlertIcon()
        return super.onPrepareOptionsMenu(menu)
    }


    protected open fun updateAlertIcon() {
        if (0 < alertCount && alertCount < 10) {
            countCartTextView!!.text = alertCount.toString()
        } else {
            countCartTextView!!.text =
                "" // if alert count extends into two digits, just show the red circle
        }
        yellowCircleCart!!.visibility = if (alertCount > 0) View.VISIBLE else View.GONE
    }


    protected open fun gotoMain() {
        val i = Intent(this, MainActivity::class.java)
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_CLEAR_TASK)
        startActivity(i)
        finish()
    }

    protected open fun addErrorMessage(newErr: String?) {
        val dialog = KAlertDialog(this, KAlertDialog.ERROR_TYPE)
        dialog.setCancelable(true)
        dialog.contentText = newErr
        dialog.confirmText = "Close"
        dialog.show()
    }

    protected open fun addErrorMessage(newErr: String?, event: DialogInterface.OnCancelListener?) {
        val dialog = KAlertDialog(this, KAlertDialog.ERROR_TYPE)
        dialog.setCancelable(true)
        dialog.contentText = newErr
        dialog.setOnCancelListener(event)
        dialog.confirmText = "Close"
        dialog.show()
    }

    protected open fun displayWarnDialog(warn: String?) {
        val dialog = KAlertDialog(this, KAlertDialog.WARNING_TYPE)
        dialog.setCancelable(true)
        dialog.confirmButtonColor(R.color.kalertWarnColor, this)
        dialog.contentText = warn
        dialog.confirmText = "Close"
        dialog.show()
    }

    protected open fun displayInfoDialog(info: String?) {
        val dialog = KAlertDialog(this, KAlertDialog.WARNING_TYPE)
        dialog.setCancelable(true)
        dialog.contentText = info
        dialog.confirmButtonColor(R.color.kalertInfoColor, this)
        dialog.confirmText = "Close"
        dialog.show()
    }

    protected open fun addSuccessMessage(msg: String?) {
        val dialogSuccess = KAlertDialog(this, KAlertDialog.SUCCESS_TYPE)
        dialogSuccess.setCancelable(true)
        dialogSuccess.contentText = msg
        dialogSuccess.confirmText = "Close"
        dialogSuccess.confirmButtonColor(R.color.colorSuccess, this)
        dialogSuccess.show()
    }

    protected open fun addSuccessMessage(msg: String?, event: KAlertClickListener?) {
        val dialogSuccess = KAlertDialog(this, KAlertDialog.SUCCESS_TYPE)
        dialogSuccess.setCancelable(true)
        dialogSuccess.contentText = msg
        dialogSuccess.confirmText = "Close"
        dialogSuccess.confirmButtonColor(R.color.colorSuccess, this)
        dialogSuccess.setConfirmClickListener(event)
        dialogSuccess.show()
    }
}