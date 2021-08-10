package com.nivekaa.icepurpykt.infra.storage

import android.content.Context
import android.content.SharedPreferences
import com.nivekaa.icepurpykt.domain.spi.SessionStoragePort

class SessionStorage(contxt: Context) : SessionStoragePort {
    private val prefs: SharedPreferences = contxt.getSharedPreferences("ice-purpy-kt__prefs", Context.MODE_PRIVATE)
    private val editor: SharedPreferences.Editor = prefs.edit()
    override fun saveDataInt(attr: String?, data: Int) {
        editor.putInt(attr, data)
        editor.apply()
    }

    override fun saveDataString(attr: String?, data: String?) {
        editor.putString(attr, data)
        editor.apply()
    }

    override fun saveDataBoolean(attr: String?, data: Boolean) {
        editor.putBoolean(attr, data)
        editor.apply()
    }

    override fun saveDataFloat(attr: String?, data: Float) {
        editor.putFloat(attr, data)
        editor.apply()
    }

    override fun saveDataLong(attr: String?, data: Long) {
        editor.putLong(attr, data)
        editor.apply()
    }

    override fun editValueInteger(KEY: String?, newValue: Int) {
        editor.putInt(KEY, newValue).commit()
    }

    override fun editValueString(KEY: String?, newValue: String?) {
        editor.putString(KEY, newValue).commit()
    }

    override fun editValueFloat(KEY: String?, newValue: Float) {
        editor.putFloat(KEY, newValue).commit()
    }

    override fun editValueBoolean(KEY: String?, newValue: Boolean) {
        editor.putBoolean(KEY, newValue).commit()
    }

    override fun retrieveDataString(key: String?): String {
        return prefs.getString(key, "").toString()
    }

    override fun remove(key: String?) {
        editor.remove(key)
        editor.apply()
    }

    override fun retrieveDataInteger(key: String?): Int {
        return prefs.getInt(key, 0)
    }

    override fun retrieveDataBoolean(key: String?): Boolean {
        return prefs.getBoolean(key, false)
    }

    override fun retrieveDataFloat(key: String?): Float {
        return prefs.getFloat(key, 0f)
    }

    override fun retrieveDataLong(key: String?): Float {
        return prefs.getFloat(key, 0F)
    }

    companion object {
        private var storageInstance: SessionStorage? = null
        // @Synchronized

        fun getInstance(context: Context): SessionStorage? {
            if (storageInstance == null) storageInstance =
                SessionStorage(context.applicationContext)
            return storageInstance
        }
    }

}