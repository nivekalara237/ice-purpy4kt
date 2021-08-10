package com.nivekaa.icepurpykt.domain.model

class CategorieVM(var name: String?) {
    var id: Int? = null
    var description: String? = null
    var discount: Int? = null

    constructor(name: String?, _id: Int?) : this(name) {
        id = _id
    }
}