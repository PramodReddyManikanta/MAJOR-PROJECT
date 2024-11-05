package com.example.grihagully.model

import com.ymts0579.model.model.User

class landresponse(val error: Boolean, val message:String, var user:ArrayList<land>) {
}

data class land(
    var id :Int,
   var sname:String,
   var description:String,
   var landmark:String,
   var address:String,
   var oname:String,
   var onum:String,
   var cost:String,
   var status:String,
   var path:String,
   var email:String,
)