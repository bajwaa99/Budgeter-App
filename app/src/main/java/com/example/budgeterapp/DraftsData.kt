package com.example.budgeterapp
import java.io.Serializable

class DraftData(_id:String?,cat:String?,tit:String?,amo:Int?,desc:String?,date_n:String?,sta:String?,type_n:String?,_key:String?):Serializable
{
    var category:String? = null
    var title:String? = null
    var amoount: Int? = null
    var description:String? =null
    var date:String?=null
    var status:String?=null
    var type:String?=null
    var key:String?=null
    var id:String?=null
    init {
        this.category=cat
        this.title=tit
        this.amoount=amo
        this.description=desc
        this.date=date_n
        this.status=sta
        this.type=type_n
        this.key=_key
        this.id=_id
    }
    constructor():this("","","",0,"","","","","")
    {

    }
}