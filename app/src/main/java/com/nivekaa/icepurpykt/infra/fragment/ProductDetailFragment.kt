package com.nivekaa.icepurpykt.infra.fragment

import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.graphics.Color
import android.os.Bundle
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.GridLayoutManager
import com.nivekaa.icepurpykt.R
import com.nivekaa.icepurpykt.domain.listener.OnAddToCartListener
import com.nivekaa.icepurpykt.domain.model.ProductVM
import com.nivekaa.icepurpykt.infra.adapter.ProductAdapter
import com.nivekaa.icepurpykt.infra.holder.ProductDetailViewHolder
import com.nivekaa.icepurpykt.infra.storage.DBHelper
import com.nivekaa.icepurpykt.util.Util
import com.squareup.picasso.Picasso
import java.lang.String
import java.util.*

/**
 * A simple [Fragment] subclass.
 * Use the [ProductDetailFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ProductDetailFragment(activity: Context) : DialogFragment() {
    private var product: ProductVM? = null
    private val cartListener: OnAddToCartListener = activity as OnAddToCartListener
    private var _context: Context = activity
    private val dbHelper: DBHelper
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NO_TITLE, R.style.DialogTheme)
        if (arguments != null) {
            product = requireArguments().getSerializable(ARG_PRODUCT_ID) as ProductVM?
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val viewRoot: View = inflater.inflate(R.layout.fragment_product_detail, container, false)
        val holder = ProductDetailViewHolder(viewRoot)
        setViewHolder(holder)
        return viewRoot
    }

    override fun getTheme(): Int {
        return R.style.DialogTheme
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState)
        dialog.setOnKeyListener { _: DialogInterface?, keyCode: Int, _: KeyEvent? ->
            if (keyCode == KeyEvent.KEYCODE_BACK) {
                dismiss()
                return@setOnKeyListener true
            }
            false
        }
        return dialog
    }

    private fun setViewHolder(holder: ProductDetailViewHolder) {
        holder.name.text = product?.name
        holder.description.text = product?.description
        holder.price.text = String.valueOf(Util.getFloatValAvoidingNullable(product?.price))
        if (product?.discount != null && product!!.discount!! > 0) {
            holder.cardView.text = "-" + product!!.discount.toString() + "%"
        } else {
            holder.cardView.labelColor = Color.TRANSPARENT
        }
        if (product?.oldPrice != null && product!!.oldPrice!!.compareTo(product!!.price) > 0
        ) {
            holder.oldPrice.text = product!!.oldPrice!!.toPlainString()
            holder.oldPrice.visibility = View.VISIBLE
            holder.oldPrice.paint.isStrikeThruText = true
        } else {
            holder.oldPrice.visibility = View.GONE
        }
        Picasso.get() //.setDebugging(true)
            .load(product?.imageUrl)
            .error(R.drawable.shoe_nike_air_max_red_128dp)
            .into(holder.photo)
        holder.btnAddToCart.setOnClickListener { v -> cartListener.addProduct(product) }
        holder.closeDialog.setOnClickListener { v -> dismiss() }
        if (product?.categoryType != null) {
            val vms: MutableList<ProductVM> = ArrayList<ProductVM>()
            val fromDb: List<ProductVM> =
                dbHelper.searchByCategory(product!!.categoryType.name)
            for (prod in fromDb) if (prod.id != product!!.id) vms.add(prod)
            val productAdapter = ProductAdapter(_context, vms, cartListener)
            holder.suggestionRv.adapter = productAdapter
            holder.suggestionRv.layoutManager = GridLayoutManager(activity, 2)
        }
    }

    companion object {
        private const val ARG_PRODUCT_ID = "product_id"
        fun newInstance(activity: Context, product: ProductVM?): ProductDetailFragment {
            val fragment = ProductDetailFragment(activity)
            val args = Bundle()
            args.putSerializable(ARG_PRODUCT_ID, product)
            fragment.arguments = args
            return fragment
        }
    }

    init {
        _context = activity
        dbHelper = DBHelper.getInstance(activity)!!
    } /**/
}