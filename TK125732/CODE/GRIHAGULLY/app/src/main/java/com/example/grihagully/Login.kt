package com.example.grihagully

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.grihagully.Admin.AdminDashboard
import com.example.grihagully.Realestate.RealestateDashboard
import com.example.grihagully.User.UserDashboard
import com.example.grihagully.databinding.ActivityLoginBinding
import com.example.grihagully.model.RetrofitClient
import com.ymts0579.model.model.DefaultResponse
import com.ymts0579.model.model.LoginResponse
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class Login : AppCompatActivity() {
    private val b by lazy {
        ActivityLoginBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(b.root)


        b.btnsignup.setOnClickListener {
            b.cardlogin.visibility= View.GONE
            b.cardregister.visibility=View.VISIBLE
        }



        b.btnsignin.setOnClickListener {
            b.cardlogin.visibility= View.VISIBLE
            b.cardregister.visibility=View.GONE
        }


       //Login screen
        b.btnlogin.setOnClickListener{
           val email= b.etemail1.text.toString().trim()
           val pass =b.etpassword.text.toString().trim()

            if(email.isEmpty()){b.etemail1.error="Enter your Email"}
            else if( pass.isEmpty()){b.etpassword.error="Enter your Password"}
            else if(email.contains("admin")&&pass.contains("admin")){
                getSharedPreferences("user", AppCompatActivity.MODE_PRIVATE).edit().putString("type","admin").apply()
                startActivity(Intent(this,AdminDashboard::class.java))
                finish()
            }else{
                loginfun(email,pass)
            }

        }
        ArrayAdapter(this@Login,android.R.layout.simple_list_item_checked, arrayOf("choose your choice","Real estate","User")).apply {
            b.spinnertype.adapter=this
        }

        //register screen
        b.btnsubmit.setOnClickListener {
            val name=b.etname.text.toString().trim()
            val num= b.etnum.text.toString().trim()
            val email= b.etemail.text.toString().trim()
            val addres= b.etaddress.text.toString().trim()
            val city= b.etcity.text.toString().trim()
            val pass= b.etpassword1.text.toString().trim()
            val type=b.spinnertype.selectedItem.toString()

           if(name.isEmpty()){b.etname.error="Enter your name"}
           else if(num.isEmpty()){b.etnum.error="Enter your number"}
           else if(email.isEmpty()){b.etemail.error="Enter your email "}
           else if(addres.isEmpty()){b.etaddress.error="Enter your Address"}
           else if(city.isEmpty()){b.etcity.error="Enter your city"}
           else if(pass.isEmpty()){b.etpassword1.error="Enter your password "}
            else if(type=="choose your choice"){
               Toast.makeText(this, "choose your choice properly", Toast.LENGTH_SHORT).show()
            } else{
                if(num.count()==10){

                     register(name,num,email, addres, city, pass, type)

                }else{
                    b.etnum.error="Enter your number properly"
                }
            }
        }
    }

    private fun loginfun(email: String, pass: String) {
        CoroutineScope(Dispatchers.IO).launch {
            RetrofitClient.instance.login(email,pass,"login")
                .enqueue(object: Callback<LoginResponse> {
                    override fun onResponse(
                        call: Call<LoginResponse>, response: Response<LoginResponse>
                    ) {
                        if(!response.body()?.error!!){
                            val type=response.body()?.user
                            if (type!=null) {
                               getSharedPreferences("user", AppCompatActivity.MODE_PRIVATE).edit().apply {
                                    putString("num",type.num)
                                    putString("pass",type.pass)
                                    putString("email",type.email)
                                    putString("name",type.name)
                                    putString("address",type.address)
                                    putString("type",type.type)
                                    putString("status",type.status)
                                    putString("status1",type.stat)
                                    putString("city",type.city)
                                    putInt("id",type.id)
                                    apply()
                                }

                                val kk=type.type
                                if(kk=="User"){
                                    startActivity(Intent(this@Login,UserDashboard::class.java))
                                    finish()
                                }else if(kk=="Real estate"){
                                    startActivity(Intent(this@Login,RealestateDashboard::class.java))
                                    finish()


                                }
                                Toast.makeText(this@Login, response.body()?.message, Toast.LENGTH_SHORT).show()
                            }
                        }else{
                            Toast.makeText(this@Login, response.body()?.message, Toast.LENGTH_SHORT).show()
                        }

                    }

                    override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                        Toast.makeText(this@Login, t.message, Toast.LENGTH_LONG).show()


                    }

                })
        }

    }

    private fun register(name: String, num: String, email: String, addres: String, city: String, pass: String, type: String) {

        if(type=="User"){
            CoroutineScope(Dispatchers.IO).launch {
                RetrofitClient.instance.register(name,num,email,addres,city,pass,type,"","","register")
                    .enqueue(object: Callback<DefaultResponse> {
                        override fun onFailure(call: Call<DefaultResponse>, t: Throwable) {
                            Toast.makeText(this@Login, ""+t.message, Toast.LENGTH_SHORT).show()
                        }
                        override fun onResponse(call: Call<DefaultResponse>, response: Response<DefaultResponse>) {
                            Toast.makeText(this@Login, "${response.body()!!.message}", Toast.LENGTH_SHORT).show()

                        }
                    })
            }
        }else{
            CoroutineScope(Dispatchers.IO).launch {
                RetrofitClient.instance.register(name,num,email,addres,city,pass,"Real estate","Available","Not verified","register")
                    .enqueue(object: Callback<DefaultResponse> {
                        override fun onFailure(call: Call<DefaultResponse>, t: Throwable) {
                            Toast.makeText(this@Login, ""+t.message, Toast.LENGTH_SHORT).show()
                        }
                        override fun onResponse(call: Call<DefaultResponse>, response: Response<DefaultResponse>) {
                            Toast.makeText(this@Login, "${response.body()!!.message}", Toast.LENGTH_SHORT).show()

                        }
                    })
            }
        }

    }
}