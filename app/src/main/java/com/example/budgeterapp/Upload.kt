package com.example.budgeterapp

class Upload(phone: String, mImageUrl: String) {

    var phone:String? = null
    var mImageUrl:String? = null

    init {
        this.phone= phone
        this.mImageUrl=mImageUrl
    }
    constructor():this("","")
    {

    }
}