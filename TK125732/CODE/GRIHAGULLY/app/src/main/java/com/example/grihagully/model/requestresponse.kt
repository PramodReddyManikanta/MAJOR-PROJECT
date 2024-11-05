package com.example.grihagully.model

import com.google.gson.annotations.SerializedName

class requestresponse(val error: Boolean, val message:String, var user:ArrayList<requestsf>) {
}

data class requestsf(
    @SerializedName("requesId"    ) var requesId    : String? = null,
    @SerializedName("date"        ) var date        : String? = null,
    @SerializedName("descri"      ) var descri      : String? = null,
    @SerializedName("remail"      ) var remail      : String? = null,
    @SerializedName("rname"       ) var rname       : String? = null,
    @SerializedName("rnum"        ) var rnum        : String? = null,
    @SerializedName("uemail"      ) var uemail      : String? = null,
    @SerializedName("unum"        ) var unum        : String? = null,
    @SerializedName("uname"       ) var uname       : String? = null,
    @SerializedName("time"        ) var time        : String? = null,
    @SerializedName("lid"         ) var lid         : String? = null,
    @SerializedName("statusPoint"      ) var statusPoint      : String? = null,
    @SerializedName("feedback"    ) var feedback    : String? = null,
    @SerializedName("rating"      ) var rating      : String? = null,
    @SerializedName("sname"       ) var sname       : String? = null,
    @SerializedName("description" ) var description : String? = null,
    @SerializedName("landmark"    ) var landmark    : String? = null,
    @SerializedName("address"     ) var address     : String? = null,
    @SerializedName("oname"       ) var oname       : String? = null,
    @SerializedName("onum"        ) var onum        : String? = null,
    @SerializedName("cost"        ) var cost        : String? = null,
    @SerializedName("path"        ) var path        : String? = null,


    )