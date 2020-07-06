package com.example.budgeterapp

class IncomeData(phone: String, month: String, amount: Int) {

    var phone:String? = null
    var month:String? = null
    var amount:Int = 0

    init {
        this.phone= phone
        this.month=month
        this.amount=amount
    }
    constructor():this("","",0)
    {

    }
}