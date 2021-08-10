package com.nivekaa.icepurpykt.infra.storage

import com.nivekaa.icepurpykt.infra.storage.entry.OrderEntity
import com.nivekaa.icepurpykt.infra.storage.entry.ProductEntity
import com.thedeanda.lorem.LoremIpsum

object Constants {
    const val SQL_CREATE_PRODUCT_ENTRY = "CREATE TABLE " + ProductEntity.TABLE_NAME + " (" +
            ProductEntity.COLUMN_NAME_ID + " INTEGER PRIMARY KEY," +
            ProductEntity.COLUMN_NAME_NAME + " TEXT," +
            ProductEntity.COLUMN_NAME_UUID + " TEXT," +
            ProductEntity.COLUMN_NAME_RATE + " FLOAT," +
            ProductEntity.COLUMN_NAME_PRICE + " FLOAT," +
            ProductEntity.COLUMN_NAME_DISCOUNT + " FLOAT," +
            ProductEntity.COLUMN_NAME_OLD_PRICE + " FLOAT," +
            ProductEntity.COLUMN_NAME_DESCRIPTION + " TEXT," +
            ProductEntity.COLUMN_NAME_CATEGORY + " TEXT," +
            ProductEntity.COLUMN_NAME_IMAGE + " TEXT)"
    const val SQL_CREATE_ORDER_ENTRY = "CREATE TABLE " + OrderEntity.TABLE_NAME + " (" +
            OrderEntity.COLUMN_NAME_ID + " INTEGER PRIMARY KEY," +
            OrderEntity.COLUMN_NAME_PRODUCT_UUID + " INTEGER," +
            OrderEntity.COLUMN_NAME_QTE + " INTEGER," +
            "FOREIGN KEY(" + OrderEntity.COLUMN_NAME_PRODUCT_UUID + ") REFERENCES " +
            ProductEntity.TABLE_NAME + "(" + ProductEntity.COLUMN_NAME_ID + "));"
    const val SQL_DELETE_ENTRIES = "DROP TABLE IF EXISTS " + ProductEntity.TABLE_NAME + ";" +
            "DROP TABLE IF EXISTS " + OrderEntity.TABLE_NAME + ";"
    val Lorem = LoremIpsum.getInstance()
}