package com.nivekaa.icepurpykt.infra.activity

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import android.os.Bundle
import android.util.Log
import android.util.Log.println
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.nivekaa.icepurpykt.application.AbstractAppActivity
import com.nivekaa.icepurpykt.R
import com.nivekaa.icepurpykt.domain.listener.OnFragmentBackPressedListener
import com.nivekaa.icepurpykt.domain.listener.OnLabelSelectedListener
import com.nivekaa.icepurpykt.domain.listener.OnProductSelectedListener
import com.nivekaa.icepurpykt.domain.model.CategoryType
import com.nivekaa.icepurpykt.domain.spi.DBStoragePort
import com.nivekaa.icepurpykt.infra.adapter.LabelAdapter
import com.nivekaa.icepurpykt.infra.adapter.ProductAdapter
import com.nivekaa.icepurpykt.domain.listener.OnAddToCartListener
import com.nivekaa.icepurpykt.domain.model.LabelVM
import com.nivekaa.icepurpykt.domain.model.OrderItemVM
import com.nivekaa.icepurpykt.domain.model.ProductVM
import com.nivekaa.icepurpykt.infra.fragment.ProductDetailFragment
import com.nivekaa.icepurpykt.infra.storage.DBStorageAdapter
import com.nivekaa.icepurpykt.infra.view.EmptiableRecyclerView


class MainActivity : AbstractAppActivity(), OnAddToCartListener, OnLabelSelectedListener,
    OnProductSelectedListener {
    // private val TAG: String = this.javaClass.getSimpleName()
    private var labelRecyclerView: RecyclerView? = null
    private var productRecyclerView: EmptiableRecyclerView? = null
    private var productAdapter: ProductAdapter? = null
    private var storagePort: DBStoragePort? = null
    override fun hasErrorView(): Boolean {
        return true
    }

    override fun hasToolBar(): Boolean {
        return true
    }

    override fun displayBackPressedIcon(): Boolean {
        return false
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        storagePort = DBStorageAdapter(this)
        setContentView(R.layout.activity_main)
        this.initView()
        mountLabelComponent()
        mountProductComponent()
    }

    private fun mountProductComponent() {
        val vms: List<ProductVM> = storagePort?.searchAny(CategoryType.ALL) as List<ProductVM>
        productAdapter = ProductAdapter(this, vms, this)
        productRecyclerView?.adapter = productAdapter
        productRecyclerView?.layoutManager = GridLayoutManager(this, 2)
        productAdapter!!.updateItems(vms)
    }

     fun initView() {
        labelRecyclerView = findViewById(R.id.labelRv)
        productRecyclerView = findViewById(R.id.productRv)
        productRecyclerView!!.setEmptyView(findViewById(R.id.empty_view))
    }

    private fun mountLabelComponent() {
        val vms: List<LabelVM?>? = storagePort?.labels
        val labelAdapter = LabelAdapter(this, vms as List<LabelVM>)
        labelRecyclerView?.adapter = labelAdapter
        labelRecyclerView?.layoutManager = LinearLayoutManager(
            this,
            LinearLayoutManager.HORIZONTAL,
            false
        )
    }

    override fun addProduct(product: ProductVM?) {
        //Toast.makeText(this, product.getName() + " has been added to cart", Toast.LENGTH_SHORT).show();
        val currentOrders: List<OrderItemVM> = storagePort?.orders as List<OrderItemVM>
        var found = false
        for (ord in currentOrders) {
            if (ord.product.id == product?.id) {
                found = true
                break
            }
        }
        if (!found) {
            val orderItem = OrderItemVM(product!!, 1)
            storagePort!!.addOrder(orderItem)
            alertCount++
            updateAlertIcon()
        } else {
            displayWarnDialog(product?.name.toString() + " has been already added in the cart.")
        }
    }

    override fun productSelected(product: ProductVM?) {
        val fragment: ProductDetailFragment = ProductDetailFragment.newInstance(this, product)
        fragment.isCancelable = false
        val fm: FragmentManager = supportFragmentManager
        val fragmentTransaction = fm.beginTransaction()
        fragmentTransaction.setCustomAnimations(R.anim.modal_in, R.anim.modal_out)
        fragment.show(fm, "product_detail_frag")
    }

    override fun onBackPressed() {
        val currentFragment: Fragment? = getSupportFragmentManager()
            .findFragmentByTag("product_detail_frag")
        if (currentFragment is ProductDetailFragment) {
            (currentFragment as OnFragmentBackPressedListener).onBackPressed()
        } else {
            super.onBackPressed()
        }
    }

    override fun LabelSelected(label: LabelVM?) {
        if (label?.category === CategoryType.ALL) {
            productAdapter?.updateItems(storagePort?.searchAny(CategoryType.ALL) as List<ProductVM>)
        } else {
            productAdapter?.updateItems(storagePort?.searchAny(label?.category) as List<ProductVM>)
        }
    }
}