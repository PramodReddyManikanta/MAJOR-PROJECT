package com.example.grihagully.model



import com.example.grihagully.Realestate.Viewdetails
import com.ymts0579.model.model.DefaultResponse
import com.ymts0579.model.model.LoginResponse
import com.ymts0579.model.model.User
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST

interface Api {
    @FormUrlEncoded
    @POST("users.php")

    fun register(
        @Field("name") name:String,
        @Field("num") num:String,
        @Field("email") email:String,
        @Field("address") address:String,
        @Field("city") city:String,
        @Field("pass") pass:String,
        @Field("type") type:String,
        @Field("status") status:String,
        @Field("stat") stat:String,
        @Field("condition") condition:String,
    ): Call<DefaultResponse>


    @FormUrlEncoded
    @POST("users.php")
    fun login(@Field("email") email:String, @Field("pass") pass:String,
              @Field("condition") condition:String): Call<LoginResponse>


    @FormUrlEncoded
    @POST("users.php")
    fun updateusers(
        @Field("name") name:String,
        @Field("num") num:String,
        @Field("address") address:String,
        @Field("city") city:String,
        @Field("pass") pass:String,
        @Field("status") status:String,
        @Field("id")id:Int, @Field("condition")condition:String): Call<DefaultResponse>




    @GET("adminuser.php")
    fun adminuser():Call<Userresponse>


    @GET("adminrealestate.php")
    fun adminrealestate():Call<Userresponse>

    @FormUrlEncoded
    @POST("users.php")
    fun updatestat(
        @Field("stat") stat:String,
        @Field("id")id:Int,
        @Field("condition") condition:String,
    ): Call<DefaultResponse>


    @FormUrlEncoded
    @POST("users.php")
    fun Deleteperson(
        @Field("id")id:Int,
        @Field("condition") condition:String,
    ): Call<DefaultResponse>

    @FormUrlEncoded
    @POST("Land.php")
    fun adddeatils(
       @Field("sname") sname:String,
       @Field("description") description:String,
       @Field("landmark") landmark:String,
       @Field("address") address:String,
       @Field("oname") oname:String,
       @Field("onum") onum:String,
       @Field("cost") cost:String,
       @Field("status") status:String,
       @Field("path") path:String,
       @Field("email") email:String,
       @Field("condition") condition:String
    ): Call<DefaultResponse>

    @FormUrlEncoded
    @POST("Land.php")
    fun viewlands(
        @Field("email") email:String,
        @Field("condition") condition:String
    ):Call<landresponse>


    @FormUrlEncoded
    @POST("Land.php")
    fun updatestatusdetails(
        @Field("status") status:String,
        @Field("id")id:Int,
        @Field("condition") condition:String,
    ):Call<DefaultResponse>

    @FormUrlEncoded
    @POST("users.php")
    fun viewrealstates(
        @Field("city") city:String,
        @Field("condition") condition:String,
    ):Call<Userresponse>

    @FormUrlEncoded
    @POST("request.php")
    fun addrequest(
      @Field("date")  date:String,
      @Field("descri")  descri:String,
      @Field("remail")  remail:String,
      @Field("rname")  rname:String,
      @Field("rnum")  rnum:String,
      @Field("uemail")  uemail:String,
      @Field("unum")  unum:String,
      @Field("uname")  uname:String,
      @Field("time")  time:String,
      @Field("lid") lid: String,
      @Field("status") status:String,
      @Field("feedback")feedback:String,
      @Field("rating")rating:String,
      @Field("condition") condition:String,
    ):Call<DefaultResponse>



    @FormUrlEncoded
    @POST("request.php")
    fun history(
        @Field("uemail")  uemail:String,
        @Field("condition") condition:String,
    ):Call<requestresponse>




    @FormUrlEncoded
    @POST("request.php")
    fun realestatehistory(
        @Field("remail")  remail:String,
        @Field("condition") condition:String,
    ):Call<requestresponse>


    @FormUrlEncoded
    @POST("request.php")
    fun updatestatusreal(
        @Field("status") status:String,
        @Field("id")id:Int,
        @Field("condition") condition:String,
    ):Call<DefaultResponse>


    @FormUrlEncoded
    @POST( "request.php")
    suspend fun updaterating(
        @Field("rating")rating:String,
        @Field("feedback")feedback:String,
        @Field("id")id: Int,
        @Field("condition") condition: String
    ): Response<DefaultResponse>
}