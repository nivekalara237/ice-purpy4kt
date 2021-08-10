package com.nivekaa.icepurpykt.infra.activity

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import butterknife.BindView
import butterknife.OnClick
import butterknife.OnTouch
import com.nivekaa.icepurpykt.R
import com.nivekaa.icepurpykt.application.AbstractAppActivity
import com.nivekaa.icepurpykt.domain.listener.OnXxCreaseQteItemListener
import com.nivekaa.icepurpykt.domain.model.OrderItemVM
import com.nivekaa.icepurpykt.infra.adapter.OrderApdater
import com.nivekaa.icepurpykt.util.Util
import java.lang.String.format
import java.math.BigDecimal

@SuppressLint("NonConstantResourceId")
class CartActivity : AbstractAppActivity(), OnXxCreaseQteItemListener {
    private val TAG: String = javaClass.getSimpleName()
    private val deliveryPrice = BigDecimal.valueOf(Util.DELIVERY_PRICE)

    @JvmField
    @BindView(R.id.cartRv)
    public var cartRecyclerView: RecyclerView? = null

    @JvmField
    @BindView(R.id.total_old_price)
    public var totalOldPriceView: TextView? = null

    @JvmField
    @BindView(R.id.delivery_price)
    public var deliveryPriceView: TextView? = null

    @JvmField
    @BindView(R.id.total_price)
    public var totalPriceView: TextView? = null

    @JvmField
    @BindView(R.id.default_nodata)
    public var noDataView: RelativeLayout? = null

    @BindView(R.id.cart_items_block)
    @JvmField
    public var blockItems: RelativeLayout? = null
    private var apdater: OrderApdater? = null
    private var totalPrice = BigDecimal.ZERO
    private var totalOldPrice = BigDecimal.ZERO
    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cart)
        apdater = OrderApdater(this, dbHelper!!.allOrders as MutableList<OrderItemVM>, this)
        cartRecyclerView!!.adapter = apdater
        cartRecyclerView!!.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        deliveryPriceView!!.text = "$$deliveryPrice"
        totalOldPriceView!!.paint.isStrikeThruText = true
        updatePrize()
    }

    override fun hasErrorView(): Boolean {
        return true
    }

    override fun hasToolBar(): Boolean {
        return true
    }

    override fun displayBackPressedIcon(): Boolean {
        return true
    }

    @SuppressLint("DefaultLocale")
    private fun updatePrize() {
        var prizze = 0.0f
        var oldPrizze = 0.0f
        totalOldPrice = BigDecimal.ZERO
        totalPrice = BigDecimal.ZERO
        val orders = dbHelper!!.allOrders
        if (orders.isEmpty()) {
            noDataView!!.visibility = View.VISIBLE
            blockItems!!.visibility = View.GONE
        } else {
            for (ordered in orders) {
                prizze += ordered.quantity * ordered.product.price.toFloat()
                oldPrizze += ordered.quantity * ordered.product.oldPrice!!.toFloat()
            }
            totalPrice = totalPrice
                .add(BigDecimal.valueOf(prizze.toDouble()))
                .add(deliveryPrice)
                .setScale(2, BigDecimal.ROUND_HALF_EVEN)
            totalOldPrice = totalOldPrice.add(BigDecimal.valueOf(oldPrizze.toDouble()))
                .add(deliveryPrice)
                .setScale(2, BigDecimal.ROUND_HALF_EVEN)
            totalPriceView?.text = format("$%.2f", totalPrice.toFloat())
            totalOldPriceView?.text = format("$%.2f", totalOldPrice.toFloat())
        }
    }

    override fun increase(order: OrderItemVM?) {
        order!!.quantity = order.quantity + 1
        val i = dbHelper!!.updateOrder(order)
        updatePrize()
    }

    override fun decrease(order: OrderItemVM?) {
        order!!.quantity = order.quantity - 1
        val i = dbHelper!!.updateOrder(order)
        updatePrize()
    }

    override fun remove(order: OrderItemVM?) {
        val res = dbHelper!!.deleteOrder(order!!.id!!)
        if (res) {
            apdater!!.notifyDataSetChanged()
            updateAlertIcon()
            updatePrize()
        }
    }

    override fun allItemsHaveRemoved() {
        noDataView!!.visibility = View.VISIBLE
        blockItems!!.visibility = View.GONE
        //updateAlertIcon();
        updatePrize()
    }

    fun cashout(view: View?) {
        val i = Intent(this, CheckoutActivity::class.java)
        i.putExtra(TOTAL_PRICE, totalPrice.toFloat())
        startActivity(i)
    }

    @OnTouch(R.id.slide_down)
    public fun slideDown(v: View?, event: MotionEvent?) {
        startActivity(Intent(this, MainActivity::class.java))
    }

    @OnClick(R.id.start_shopping)
    public fun startShopping(view: View?) {
        startActivity(Intent(this, MainActivity::class.java))
    }

    companion object {
        const val TOTAL_PRICE = "total_price"
    }
}