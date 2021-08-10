package com.nivekaa.icepurpykt.infra.storage

import android.content.Context
import com.nivekaa.icepurpykt.domain.model.CategoryType
import com.nivekaa.icepurpykt.domain.model.LabelVM
import com.nivekaa.icepurpykt.domain.model.OrderItemVM
import com.nivekaa.icepurpykt.domain.model.ProductVM
import com.nivekaa.icepurpykt.domain.spi.DBStoragePort
import java.util.*

class DBStorageAdapter(context: Context?) : DBStoragePort {
    private val dbHelper: DBHelper? = DBHelper.getInstance(context!!)
    override fun init(context: Context?) {
        //this.dbHelper = DBHelper.getInstance(context);
    }

    override val pants: List<ProductVM?>
        get() = dbHelper?.searchByCategory(CategoryType.PANTS.name) ?: ArrayList()
    override val shoes: List<ProductVM?>
        get() = dbHelper?.searchByCategory(CategoryType.SHOES.name) ?: ArrayList()
    override val tshirts: List<ProductVM?>
        get() = dbHelper?.searchByCategory(CategoryType.TSHIRTS.name) ?: ArrayList()
    override val hats: List<ProductVM?>
        get() = dbHelper?.searchByCategory(CategoryType.HATS.name) ?: ArrayList()
    override val bags: List<ProductVM?>
        get() = dbHelper?.searchByCategory(CategoryType.BAGS.name) ?: ArrayList()
    override val orders: List<OrderItemVM?>
        get() = dbHelper?.allOrders ?: ArrayList()

    override fun addOrder(order: OrderItemVM?) {
        if (order != null) {
            dbHelper!!.addOrder(order)
        }
    }

    override fun addProduct(productVM: ProductVM?) {
        dbHelper!!.addProduct(productVM)
    }

    override val labels: List<LabelVM?>?
        get() = Arrays.asList(
            LabelVM("All", CategoryType.ALL),
            LabelVM("Shoes", CategoryType.SHOES),
            LabelVM("Pants", CategoryType.PANTS),
            LabelVM("T-shirts", CategoryType.TSHIRTS),
            LabelVM("Hats", CategoryType.HATS),
            LabelVM("Bags", CategoryType.BAGS),
            LabelVM("Clothes", CategoryType.CLOTHES),
            LabelVM("Furnitures", CategoryType.FURNITURES)
        )


    override fun searchAny(category: CategoryType?): List<ProductVM?>? {
        return if (dbHelper != null) {
            if (category === CategoryType.ALL) dbHelper.allProducts else dbHelper.searchByCategory(
                category?.name
            )
        } else ArrayList<ProductVM>()
    }

}