package com.nivekaa.icepurpykt.infra.storage

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import com.nivekaa.icepurpykt.domain.model.CategoryType
import com.nivekaa.icepurpykt.domain.model.OrderItemVM
import com.nivekaa.icepurpykt.domain.model.ProductVM
import com.nivekaa.icepurpykt.infra.storage.Constants.Lorem
import com.nivekaa.icepurpykt.infra.storage.Constants.SQL_CREATE_ORDER_ENTRY
import com.nivekaa.icepurpykt.infra.storage.Constants.SQL_CREATE_PRODUCT_ENTRY
import com.nivekaa.icepurpykt.infra.storage.Constants.SQL_DELETE_ENTRIES
import com.nivekaa.icepurpykt.infra.storage.entry.OrderEntity
import com.nivekaa.icepurpykt.infra.storage.entry.ProductEntity
import com.nivekaa.icepurpykt.util.Util
import java.lang.String.format
import java.math.BigDecimal
import java.util.*

class DBHelper(context: Context?) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {
    private var currentDB: SQLiteDatabase? = null
    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(SQL_CREATE_PRODUCT_ENTRY)
        db.execSQL(SQL_CREATE_ORDER_ENTRY)
        currentDB = db
        initData()
        currentDB = null
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL(SQL_DELETE_ENTRIES)
        onCreate(db)
    }

    override fun onDowngrade(db: SQLiteDatabase, oldv: Int, newVersion: Int) {
        onUpgrade(db, oldv, newVersion)
    }

    override fun getWritableDatabase(): SQLiteDatabase {
        return currentDB ?: super.getWritableDatabase()
    }

    override fun getReadableDatabase(): SQLiteDatabase {
        return currentDB ?: super.getWritableDatabase()
    }

    fun addProduct(product: ProductVM?) {
        val db = this.writableDatabase
        db.beginTransaction()
        try {
            val values = ContentValues()
            values.put(ProductEntity.COLUMN_NAME_IMAGE, product!!.imageUrl)
            values.put(ProductEntity.COLUMN_NAME_NAME, product.name)
            values.put(ProductEntity.COLUMN_NAME_DISCOUNT, product.discount)
            values.put(
                ProductEntity.COLUMN_NAME_OLD_PRICE, Util.getFloatValAvoidingNullable(
                    product.oldPrice
                )
            )
            values.put(ProductEntity.COLUMN_NAME_PRICE, product.price.toFloat())
            values.put(ProductEntity.COLUMN_NAME_RATE, product.rate)
            values.put(
                ProductEntity.COLUMN_NAME_CATEGORY,
                product.categoryType.name
            )
            values.put(ProductEntity.COLUMN_NAME_DESCRIPTION, product.description)
            values.put(ProductEntity.COLUMN_NAME_UUID, UUID.randomUUID().toString())
            db.insertOrThrow(ProductEntity.TABLE_NAME, null, values)
            db.setTransactionSuccessful()
        } catch (e: Exception) {
            Log.d(ContentValues.TAG, "Error while trying to add product to database")
            e.printStackTrace()
        } finally {
            db.endTransaction()
        }
    }

    fun addOrder(order: OrderItemVM) {
        val db = this.writableDatabase
        db.beginTransaction()
        try {
            val entry = getProductEntryByUUID(order.product.id)
            val values = ContentValues()
            values.put(OrderEntity.COLUMN_NAME_PRODUCT_UUID, entry!!.id)
            values.put(OrderEntity.COLUMN_NAME_QTE, order.quantity)
            db.insertOrThrow(OrderEntity.TABLE_NAME, null, values)
            db.setTransactionSuccessful()
        } catch (e: Exception) {
            Log.d(ContentValues.TAG, "Error while trying to add OrderItem to database")
            e.stackTrace
        } finally {
            db.endTransaction()
        }
    }

    fun updateOrder(order: OrderItemVM): Int {
        val db = this.writableDatabase
        val values = ContentValues()
        values.put(OrderEntity.COLUMN_NAME_QTE, order.quantity)
        return db.update(
            OrderEntity.TABLE_NAME,
            values,
            OrderEntity.COLUMN_NAME_ID + " = ?",
            arrayOf(order.id.toString())
        )
    }

    //System.out.println(products.size());
    //System.out.println(products);
    val allProducts: List<ProductVM>
        get() {
            val products: MutableList<ProductVM> = ArrayList()
            val PRODUCTS_SELECT_QUERY: String = format(
                "SELECT * FROM %s",
                ProductEntity.TABLE_NAME
            )
            queryToSelectProducts(products, PRODUCTS_SELECT_QUERY)
            //System.out.println(products.size());
            //System.out.println(products);
            return products
        }

    fun searchByCategory(category: String?): List<ProductVM> {
        val products: MutableList<ProductVM> = ArrayList()
        val PRODUCTS_SELECT_QUERY: String = format(
            "SELECT * FROM %s WHERE %s='%s'",
            ProductEntity.TABLE_NAME, ProductEntity.COLUMN_NAME_CATEGORY, category
        )
        queryToSelectProducts(products, PRODUCTS_SELECT_QUERY)
        return products
    }

    private fun queryToSelectProducts(
        products: MutableList<ProductVM>,
        PRODUCTS_SELECT_QUERY: String
    ) {
        val db = this.readableDatabase
        val cursor = db.rawQuery(PRODUCTS_SELECT_QUERY, null)
        try {
            if (cursor!!.moveToFirst()) {
                do {
                    val newEntry = ProductEntity.Entry()
                    newEntry.id = cursor.getInt(cursor.getColumnIndex(ProductEntity.COLUMN_NAME_ID))
                    newEntry.uuid =
                        cursor.getString(cursor.getColumnIndex(ProductEntity.COLUMN_NAME_UUID))
                    newEntry.name =
                        cursor.getString(cursor.getColumnIndex(ProductEntity.COLUMN_NAME_NAME))
                    newEntry.image =
                        cursor.getString(cursor.getColumnIndex(ProductEntity.COLUMN_NAME_IMAGE))
                    newEntry.price =
                        cursor.getFloat(cursor.getColumnIndex(ProductEntity.COLUMN_NAME_PRICE))
                    newEntry.discount =
                        cursor.getFloat(cursor.getColumnIndex(ProductEntity.COLUMN_NAME_DISCOUNT))
                    newEntry.oldPprice =
                        cursor.getFloat(cursor.getColumnIndex(ProductEntity.COLUMN_NAME_OLD_PRICE))
                    newEntry.rate =
                        cursor.getFloat(cursor.getColumnIndex(ProductEntity.COLUMN_NAME_RATE))
                    newEntry.category =
                        cursor.getString(cursor.getColumnIndex(ProductEntity.COLUMN_NAME_CATEGORY))
                    newEntry.description =
                        cursor.getString(cursor.getColumnIndexOrThrow(ProductEntity.COLUMN_NAME_DESCRIPTION))
                    products.add(ProductEntity.entryToVm(newEntry))
                } while (cursor.moveToNext())
            }
        } catch (e: Exception) {
            Log.d(ContentValues.TAG, "Error while trying to get produts from database")
            e.printStackTrace()
        } finally {
            if (cursor != null && !cursor.isClosed) {
                cursor.close()
            }
        }
    }

    fun deleteOrder(id: Int): Boolean {
        val db = this.writableDatabase
        return db.delete(OrderEntity.TABLE_NAME, OrderEntity.COLUMN_NAME_ID + "=" + id, null) > 0
    }

    fun deleteAllOrders(): Boolean {
        val db = this.writableDatabase
        return db.delete(OrderEntity.TABLE_NAME, null, null) > 0
    }

    val allOrders: List<OrderItemVM>
        get() {
            val orderItems: MutableList<OrderItemVM> = ArrayList()
            val PRODUCTS_SELECT_QUERY: String = format(
                "SELECT * FROM %s INNER JOIN %s ON %s.%s = %s.%s",
                OrderEntity.TABLE_NAME,
                ProductEntity.TABLE_NAME,
                OrderEntity.TABLE_NAME, OrderEntity.COLUMN_NAME_PRODUCT_UUID,
                ProductEntity.TABLE_NAME, ProductEntity.COLUMN_NAME_ID
            )
            val db = this.readableDatabase
            val cursor = db.rawQuery(PRODUCTS_SELECT_QUERY, null)
            try {
                if (cursor!!.moveToFirst()) {
                    do {
                        val newEntry = ProductEntity.Entry()
                        newEntry.id =
                            cursor.getInt(cursor.getColumnIndex(ProductEntity.COLUMN_NAME_ID))
                        newEntry.uuid =
                            cursor.getString(cursor.getColumnIndex(ProductEntity.COLUMN_NAME_UUID))
                        newEntry.name =
                            cursor.getString(cursor.getColumnIndex(ProductEntity.COLUMN_NAME_NAME))
                        newEntry.image =
                            cursor.getString(cursor.getColumnIndex(ProductEntity.COLUMN_NAME_IMAGE))
                        newEntry.price =
                            cursor.getFloat(cursor.getColumnIndex(ProductEntity.COLUMN_NAME_PRICE))
                        newEntry.oldPprice =
                            cursor.getFloat(cursor.getColumnIndex(ProductEntity.COLUMN_NAME_OLD_PRICE))
                        newEntry.discount =
                            cursor.getFloat(cursor.getColumnIndex(ProductEntity.COLUMN_NAME_DISCOUNT))
                        newEntry.rate =
                            cursor.getFloat(cursor.getColumnIndex(ProductEntity.COLUMN_NAME_RATE))
                        newEntry.description =
                            cursor.getString(cursor.getColumnIndex(ProductEntity.COLUMN_NAME_DESCRIPTION))
                        newEntry.category =
                            cursor.getString(cursor.getColumnIndex(ProductEntity.COLUMN_NAME_CATEGORY))
                        val id = cursor.getInt(cursor.getColumnIndex(OrderEntity.COLUMN_NAME_ID))
                        val qte = cursor.getInt(cursor.getColumnIndex(OrderEntity.COLUMN_NAME_QTE))
                        val order = OrderItemVM(ProductEntity.entryToVm(newEntry), qte)
                        order.id = id
                        orderItems.add(order)
                    } while (cursor.moveToNext())
                }
            } catch (e: Exception) {
                Log.d(ContentValues.TAG, "Error while trying to get orderItem from database")
                e.stackTrace
            } finally {
                if (cursor != null && !cursor.isClosed) {
                    cursor.close()
                }
            }
            return orderItems
        }

    fun getProductEntryByUUID(uuid: String): ProductEntity.Entry? {
        val PRODUCTS_SELECT_QUERY: String = format(
            "SELECT * FROM %s WHERE %s = ?",
            ProductEntity.TABLE_NAME, ProductEntity.COLUMN_NAME_UUID
        )
        val db = readableDatabase
        val cursor = db.rawQuery(PRODUCTS_SELECT_QUERY, arrayOf(uuid))
        try {
            if (cursor!!.moveToFirst()) {
                val newEntry = ProductEntity.Entry()
                newEntry.id = cursor.getInt(cursor.getColumnIndex(ProductEntity.COLUMN_NAME_ID))
                newEntry.uuid =
                    cursor.getString(cursor.getColumnIndex(ProductEntity.COLUMN_NAME_UUID))
                newEntry.name =
                    cursor.getString(cursor.getColumnIndex(ProductEntity.COLUMN_NAME_NAME))
                newEntry.image =
                    cursor.getString(cursor.getColumnIndex(ProductEntity.COLUMN_NAME_IMAGE))
                newEntry.price =
                    cursor.getFloat(cursor.getColumnIndex(ProductEntity.COLUMN_NAME_PRICE))
                newEntry.oldPprice =
                    cursor.getFloat(cursor.getColumnIndex(ProductEntity.COLUMN_NAME_OLD_PRICE))
                newEntry.discount =
                    cursor.getFloat(cursor.getColumnIndex(ProductEntity.COLUMN_NAME_DISCOUNT))
                newEntry.rate =
                    cursor.getFloat(cursor.getColumnIndex(ProductEntity.COLUMN_NAME_RATE))
                newEntry.description =
                    cursor.getString(cursor.getColumnIndex(ProductEntity.COLUMN_NAME_DESCRIPTION))
                newEntry.category =
                    cursor.getString(cursor.getColumnIndex(ProductEntity.COLUMN_NAME_CATEGORY))
                return newEntry
            }
        } finally {
            if (cursor != null && !cursor.isClosed) {
                cursor.close()
            }
        }
        return null
    }

    private fun initData() {
        if (allProducts.size == 0) {
            val vms = Arrays.asList(
                ProductVM(
                    id = UUID.randomUUID().toString(),
                    name = "Nike Air max 20 ABC",
                    price = BigDecimal("230.00"),
                    rate = 4.5f,
                    imageUrl = "https://disuku.xyz/soko/5/paypal-test-integration/2bce6bb5904049589cb9a899ca07e2bd.png",
                    oldPrice = BigDecimal("290.00"),
                    discount = 10f,
                    categoryType = CategoryType.SHOES,
                    description = Lorem.getWords(30, 123)
                ),
                ProductVM(
                    id = UUID.randomUUID().toString(),
                    name = "Nike Air max 20 Black DEF",
                    price = BigDecimal("210.00"),
                    rate = 3.1f,
                    imageUrl = "https://disuku.xyz/soko/5/paypal-test-integration/dd1e3f613ba74488874e45caf01a1c27.png",
                    oldPrice = BigDecimal("390.00"),
                    discount = 12f,
                    categoryType = CategoryType.SHOES,
                    description = Lorem.getWords(30, 123)
                ),
                ProductVM(
                    id = UUID.randomUUID().toString(),
                    name = "Men's Shoe - Nike Space Hippie 04",
                    price = BigDecimal("476.00"),
                    rate = 4.5f,
                    imageUrl = "https://disuku.xyz/soko/5/paypal-test-integration/572b6af84e83453bb5b75fe0f9703cfd.png",
                    oldPrice = null,
                    discount = null,
                    categoryType = CategoryType.SHOES,
                    description = Lorem.getWords(30, 123)
                ),
                ProductVM(
                    id = UUID.randomUUID().toString(),
                    name = "Men's Training Shoe - Nike Free Metcon 3",
                    price = BigDecimal("90.00"),
                    rate = 5f,  //"https://disuku.xyz/soko/5/paypal-test-integration/7ae8e29f7f7545d693beb700b7c42fe2.jpg",
                    imageUrl = "https://disuku.xyz/soko/5/paypal-test-integration/904c748b5df84e5eb4f3f495729e52d6.jpg",
                    oldPrice = null,
                    discount = null,
                    categoryType = CategoryType.SHOES,
                    description = Lorem.getWords(30, 123)
                ),
                ProductVM(
                    id = UUID.randomUUID().toString(),name =  "Nike Air max 20 LMN", price = BigDecimal("129.70"),rate =  4.5f,
                    imageUrl = "https://disuku.xyz/soko/5/paypal-test-integration/e609c3b8dea84d33af95c62d6d70dc12.png",
                    discount =  45f, categoryType = CategoryType.SHOES, description = Lorem.getWords(30, 123)
                ),
                ProductVM(
                    UUID.randomUUID().toString(),
                    "Nike Air max 20 Black OPQ",
                    BigDecimal("216.00"),
                    2.1f,
                    "https://disuku.xyz/soko/5/paypal-test-integration/dd1e3f613ba74488874e45caf01a1c27.png",
                    null,
                    null,
                    CategoryType.CLOTHES,
                    Lorem.getWords(30, 123)
                ),
                ProductVM(
                    UUID.randomUUID().toString(),
                    "xxxxx xxxx xxxxx xxxx",
                    BigDecimal("216.00"),
                    2.1f,
                    "https://disuku.xyz/soko/5/paypal-test-integration/38c3242b219f45cc9f4437289136d4e6.png",
                    null,
                    null,
                    CategoryType.CLOTHES,
                    Lorem.getWords(30, 123)
                ),
                ProductVM(
                    UUID.randomUUID().toString(), "Nike Air max 20 RST", BigDecimal("98.00"), 1.5f,
                    "https://disuku.xyz/soko/5/paypal-test-integration/2bce6bb5904049589cb9a899ca07e2bd.png",
                    null, null, CategoryType.SHOES, Lorem.getWords(23, 234)
                ),
                ProductVM(
                    UUID.randomUUID().toString(), "Nike Air max 20 RST", BigDecimal("98.00"), 1.5f,
                    "https://disuku.xyz/soko/5/paypal-test-integration/1b34ae0d5c9f4cc88acb3ca1cf69c1b3.png",
                    null, null, CategoryType.SHOES, Lorem.getWords(23, 234)
                ),
                ProductVM(
                    UUID.randomUUID().toString(), "Nike Air max 20 RST", BigDecimal("98.00"), 1.5f,
                    "https://disuku.xyz/soko/5/paypal-test-integration/82402f6151344cf893001c8e48b62df0.png",
                    null, null, CategoryType.SHOES, Lorem.getWords(23, 234)
                ),  // PANTS
                /*new ProductVM(UUID.randomUUID().toString(),"Pants blue fashion clothes woman",new BigDecimal("28.99"),3.8F,
                            "https://disuku.xyz/soko/5/paypal-test-integration/4fc962543e844a76b3073b67adbd6b30.jpg",
                            null, null, CategoryType.PANTS, Lorem.getWords(30, 123)),*/
                ProductVM(
                    UUID.randomUUID().toString(),
                    "Cloth jeans pants textile material fashion",
                    BigDecimal("24.00"),
                    5f,
                    "https://disuku.xyz/soko/5/paypal-test-integration/fc8ac9541f704840a1fb9dc486dbeea3.jpg",
                    null,
                    null,
                    CategoryType.PANTS,
                    Lorem.getWords(30, 123)
                ),
                ProductVM(
                    UUID.randomUUID().toString(),
                    "Women's Fleece Pants - Jordan Flight",
                    BigDecimal("18.50"),
                    4.5f,
                    "https://disuku.xyz/soko/5/paypal-test-integration/408a0e17125d4b50a56fffce56a7e6df.png",  //"https://disuku.xyz/soko/5/paypal-test-integration/b2090f7da2cc4dd1adf2057650740426.png",
                    BigDecimal.valueOf(35),
                    25f,
                    CategoryType.PANTS,
                    Lorem.getWords(30, 123)
                ),
                ProductVM(
                    UUID.randomUUID().toString(),
                    "Colorful cloth textil material object",
                    BigDecimal("18.50"),
                    4.5f,
                    "https://disuku.xyz/soko/5/paypal-test-integration/a8da8a71ac794e569775c57dfb1ec155.jpg",
                    BigDecimal.valueOf(35),
                    25f,
                    CategoryType.PANTS,
                    Lorem.getWords(30, 123)
                ),  //BAGS
                ProductVM(
                    UUID.randomUUID().toString(),
                    "Aya Laptop Bag by Mamtak Bags",
                    BigDecimal("120.50"),
                    3.5f,  //"https://disuku.xyz/soko/5/paypal-test-integration/b2540e3d36ec4208a0173f5a3f910198.jpg",
                    "https://disuku.xyz/soko/5/paypal-test-integration/46dac464b815438e900ca230fcfde7bb.png",
                    BigDecimal.valueOf(340.5),
                    35f,
                    CategoryType.BAGS,
                    Lorem.getWords(30, 123)
                ),
                ProductVM(
                    UUID.randomUUID().toString(),
                    "Carrying bags black model - Chromy XDMM",
                    BigDecimal("170.50"),
                    3.5f,
                    "https://disuku.xyz/soko/5/paypal-test-integration/99343959231f4fde8765926c8c420b11.png",
                    BigDecimal.valueOf(240.5),
                    35f,
                    CategoryType.BAGS,
                    Lorem.getWords(30, 123)
                ),  /*new ProductVM(UUID.randomUUID().toString(),"Ladies orange bags",new BigDecimal("70.50"),3.5F,
                            "https://www.publicdomainpictures.net/pictures/340000/velka/ladies-orange-bags.jpg",
                            BigDecimal.valueOf(140.5f), 5F, CategoryType.BAGS, Lorem.getWords(30, 123)),
                    new ProductVM(UUID.randomUUID().toString(),"Ladies orange bags",new BigDecimal("70.50"),3.5F,
                            "https://www.publicdomainpictures.net/pictures/340000/velka/ladies-orange-bags.jpg",
                            BigDecimal.valueOf(140.5f), 5F, CategoryType.BAGS, Lorem.getWords(30, 123)),*/
                // HATS
                ProductVM(
                    UUID.randomUUID().toString(),
                    "Monticristi Straw Hat Optimo",
                    BigDecimal("470.50"),
                    3.5f,
                    "https://disuku.xyz/soko/5/paypal-test-integration/572b6af84e83453bb5b75fe0f9703cfd.png",
                    BigDecimal.valueOf(700.0),
                    15f,
                    CategoryType.HATS,
                    Lorem.getWords(30, 123)
                ),
                ProductVM(
                    UUID.randomUUID().toString(),
                    "Cap, man, hat, mannequin",
                    BigDecimal("470.50"),
                    3.5f,  // "https://i0.hippopx.com/photos/809/506/484/cap-man-hat-mannequin-0103d48b8e86d660f622f493b5695b6e.jpg",
                    "https://disuku.xyz/soko/5/paypal-test-integration/135fe329f67a47d1a4bcd8b3dcc39fc0.png",
                    BigDecimal.valueOf(700.0),
                    15f,
                    CategoryType.HATS,
                    Lorem.getWords(30, 123)
                )
            )
            Collections.shuffle(vms)
            for (vm in vms) {
                addProduct(vm)
            }
        }
    }

    companion object {
        var dbHelperInstance: DBHelper? = null
        const val DATABASE_VERSION = 1
        const val DATABASE_NAME = "__nick_shopping_kt__.db"
        // @Synchronized
        fun getInstance(context: Context): DBHelper? {
            if (dbHelperInstance == null) {
                dbHelperInstance = DBHelper(context.applicationContext)
            }
            return dbHelperInstance
        }
    }
}