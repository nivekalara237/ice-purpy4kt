package com.nivekaa.icepurpykt.domain.model

class LabelVM {
    var title: String
    var color: String? = null
    var category: CategoryType? = null

    constructor(title: String) {
        this.title = title
    }

    constructor(title: String, category: CategoryType?) : this(title) {
        this.category = category
    }

    constructor(title: String, color: String?) {
        this.title = title
        this.color = color
    }

    override fun toString(): String {
        return "LabelVM{" +
                "title='" + title + '\'' +
                ", color='" + color + '\'' +
                ", category=" + category +
                '}'
    }
}