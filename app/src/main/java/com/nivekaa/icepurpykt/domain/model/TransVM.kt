package com.nivekaa.icepurpykt.domain.model

import android.os.Parcel
import android.os.Parcelable
import java.io.Serializable

class TransVM : Serializable, Parcelable {
    var prix = 0.0
    var date: String? = null
    var id: Long = 0
    var isStatus = false
    var numero: String? = null
    var numeroAffichable: String? = null
    var message: String? = null
    var typeCarte: String? = null
    var carte: String? = null

    constructor() {}
    constructor(
        prix: Double,
        date: String?,
        status: Boolean,
        numero: String?,
        numeroAffichable: String?,
        message: String?,
        carte: String?
    ) {
        this.prix = prix
        this.date = date
        isStatus = status
        this.numero = numero
        this.numeroAffichable = numeroAffichable
        this.carte = carte
        this.message = message
    }

    constructor(
        prix: Double,
        date: String?,
        status: Boolean,
        numero: String?,
        numeroAffichable: String?,
        message: String?
    ) {
        this.prix = prix
        this.date = date
        isStatus = status
        this.numero = numero
        this.numeroAffichable = numeroAffichable
        this.message = message
    }

    protected constructor(`in`: Parcel) {
        prix = `in`.readFloat().toDouble()
        date = `in`.readString()
        isStatus = `in`.readByte().toInt() != 0
        numero = `in`.readString()
        numeroAffichable = `in`.readString()
        message = `in`.readString()
        typeCarte = `in`.readString()
        id = `in`.readLong()
    }

    override fun describeContents(): Int {
        return 0
    }

    override fun writeToParcel(parcel: Parcel, i: Int) {
        parcel.writeDouble(prix)
        parcel.writeString(date)
        parcel.writeByte((if (isStatus) 1 else 0).toByte())
        parcel.writeString(numero)
        parcel.writeString(numeroAffichable)
        parcel.writeString(message)
        parcel.writeString(typeCarte)
        parcel.writeLong(id)
    }

    companion object CREATOR : Parcelable.Creator<TransVM> {
        override fun createFromParcel(parcel: Parcel): TransVM {
            return TransVM(parcel)
        }

        override fun newArray(size: Int): Array<TransVM?> {
            return arrayOfNulls(size)
        }
    }
}