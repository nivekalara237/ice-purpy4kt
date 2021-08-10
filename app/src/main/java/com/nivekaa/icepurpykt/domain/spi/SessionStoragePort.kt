package com.nivekaa.icepurpykt.domain.spi

interface SessionStoragePort {
    fun saveDataInt(attr: String?, data: Int)
    fun saveDataString(attr: String?, data: String?)
    fun saveDataBoolean(attr: String?, data: Boolean)
    fun saveDataFloat(attr: String?, data: Float)
    fun saveDataLong(attr: String?, data: Long)
    fun editValueInteger(KEY: String?, newValue: Int)
    fun editValueString(KEY: String?, newValue: String?)
    fun editValueFloat(KEY: String?, newValue: Float)
    fun editValueBoolean(KEY: String?, newValue: Boolean)
    fun retrieveDataString(key: String?): String?
    fun remove(key: String?)
    fun retrieveDataInteger(key: String?): Int
    fun retrieveDataBoolean(key: String?): Boolean
    fun retrieveDataFloat(key: String?): Float
    fun retrieveDataLong(key: String?): Float
}