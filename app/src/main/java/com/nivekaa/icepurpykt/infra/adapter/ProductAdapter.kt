package com.nivekaa.icepurpykt.infra.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.nivekaa.icepurpykt.R
import com.nivekaa.icepurpykt.domain.listener.OnAddToCartListener
import com.nivekaa.icepurpykt.domain.listener.OnProductSelectedListener
import com.nivekaa.icepurpykt.domain.model.ProductVM
import com.nivekaa.icepurpykt.infra.holder.ProductViewHolder
import com.nivekaa.icepurpykt.util.Util
import com.squareup.picasso.Picasso
import java.lang.String

class ProductAdapter(ctx: Context, vms: List<ProductVM>, listener: OnAddToCartListener) :
    RecyclerView.Adapter<ProductViewHolder?>() {
    private var products: List<ProductVM>
    private val context: Context
    private val cartListener: OnAddToCartListener
    private val selectedListener: OnProductSelectedListener
    override fun onCreateViewHolder(parent: ViewGroup, i: Int): ProductViewHolder {
        val v: View =
            LayoutInflater.from(parent.context).inflate(R.layout.product_item, parent, false)
        return ProductViewHolder(v)
    }

    @SuppressLint("DefaultLocale", "SetTextI18n")
    override fun onBindViewHolder(holder: ProductViewHolder, i: Int) {
        val product: ProductVM = products[i]
        holder.rate.rating = product.rate
        holder.name.text = product.name
        holder.price.text = String.valueOf(Util.getFloatValAvoidingNullable(product.price))
        if (product.discount != null && product.discount!! > 0) {
            holder.cardView.text = "-" + product.discount.toString() + "%"
        } else {
            holder.cardView.setLabelColor(Color.TRANSPARENT)
        }
        if (product.oldPrice != null && product.oldPrice!!
                .compareTo(product.price) > 0
        ) {
            holder.oldPrice.text = product.oldPrice!!.toPlainString()
            holder.oldPrice.visibility = View.VISIBLE
            holder.oldPrice.paint.isStrikeThruText = true
        } else {
            holder.oldPrice.visibility = View.GONE
        }
        Picasso.get()
            .load(product.imageUrl)
            .placeholder(R.drawable.ic_launcher_foreground)
            .error(R.drawable.shoe_nike_air_max_red_128dp)
            .into(holder.photo)
        holder.btnAddToCart.setOnClickListener { cartListener.addProduct(product) }
        holder.cardView.setOnClickListener {
            if (it.id != R.id.btn_add_to_cart) {
                selectedListener.productSelected(product)
            }
        }
    }

    override fun getItemCount(): Int {
        return products.size
    }

    fun updateItems(vms: List<ProductVM>) {
        products = vms
        notifyDataSetChanged()
    }

    init {
        products = vms
        context = ctx
        cartListener = listener
        selectedListener = ctx as OnProductSelectedListener
    }
}