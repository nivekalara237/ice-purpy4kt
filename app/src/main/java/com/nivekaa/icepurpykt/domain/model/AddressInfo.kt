package com.nivekaa.icepurpykt.domain.model

class AddressInfo {
    var id: String? = null
    var country: String? = null
    var address: String? = null
    var state: String? = null
    var city: String? = null
    var zipcode: String? = null

    constructor() {}
    constructor(
        country: String?,
        address: String?,
        state: String?,
        city: String?,
        zipcode: String?
    ) {
        this.country = country
        this.address = address
        this.state = state
        this.city = city
        this.zipcode = zipcode
    }

    constructor(
        id: String?,
        country: String?,
        address: String?,
        state: String?,
        city: String?,
        zipcode: String?
    ) : this(country, address, state, city, zipcode) {
        this.id = id
    }

    override fun toString(): String {
        return "AddressInfo{" +
                "id='" + id + '\'' +
                ", country='" + country + '\'' +
                ", address='" + address + '\'' +
                ", state='" + state + '\'' +
                ", city='" + city + '\'' +
                ", zipcode='" + zipcode + '\'' +
                '}'
    }
}