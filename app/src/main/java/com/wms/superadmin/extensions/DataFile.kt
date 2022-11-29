package com.wms.superadmin.extensions

public fun getTimePeriodList():ArrayList<PeriodObject>{
    var reasonlist= arrayListOf<PeriodObject>()
    reasonlist.add(PeriodObject(0,"Today"))
    reasonlist.add(PeriodObject(1,"Weekly"))
    reasonlist.add(PeriodObject(2,"Monthly"))
    reasonlist.add(PeriodObject(3,"Yearly"))
    return  reasonlist
}
public data class PeriodObject(
    public var PeriodID	: Int? = 0,
    public var PeriodName:String=""
)