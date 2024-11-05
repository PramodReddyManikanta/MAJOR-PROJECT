package com.example.grihagully

import android.R
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.grihagully.Realestate.RealestateDashboard
import com.example.grihagully.databinding.ActivityProfileBinding
import com.example.grihagully.model.RetrofitClient
import com.ymts0579.model.model.DefaultResponse
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class Profile : AppCompatActivity() {
    private val b by lazy{
        ActivityProfileBinding.inflate(layoutInflater)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(b.root)


        var type=""
        var status=""
        var stat=""
        var id=0
       getSharedPreferences("user", AppCompatActivity.MODE_PRIVATE).apply {
            id= getInt("id",0)
            b.etemail.setText( getString("email","").toString())
            b.etphone.setText(getString("num","").toString())
            b.etname.setText(getString("name","").toString())
            b.etaddress.setText(getString("address","").toString())
            b.etpassword.setText(getString("pass","").toString())
            b.etcity.setText(getString("city","").toString())

            type=getString("type","").toString()
            status=getString("status","").toString()
            stat=getString("status1","").toString()
        }

        b.textView1.setText(status)

        if(type=="User"){
            b.spinstatus.visibility= View.GONE
            b.textView1.visibility=View.GONE
        }else{
            b.spinstatus.visibility= View.VISIBLE
            b.textView1.visibility=View.VISIBLE
        }

        ArrayAdapter(this@Profile,
            R.layout.simple_list_item_checked, arrayOf("choose your choice","Available","Not Available")).apply {
            b.spinstatus.adapter=this
        }

        b.btnproile.setOnClickListener {
            val name=b.etname.text.toString().trim()
            val num=b.etphone.text.toString().trim()
            val email=b.etemail.text.toString().trim()
            val address=b.etaddress.text.toString().trim()
            val city=b.etcity.text.toString().trim()
            val pass=b.etpassword.text.toString().trim()

            if(name.isEmpty()){
                Toast.makeText(this@Profile, "Enter Your Name", Toast.LENGTH_SHORT).show()
            }else if(num.isEmpty()){
                Toast.makeText(this@Profile, "Enter Your Number", Toast.LENGTH_SHORT).show()
            }else if(address.isEmpty()){
                Toast.makeText(this@Profile, "Enter Your Address", Toast.LENGTH_SHORT).show()
            }else if(city.isEmpty()){
                Toast.makeText(this@Profile, "Enter Your City", Toast.LENGTH_SHORT).show()
            }else if(pass.isEmpty()){
                Toast.makeText(this@Profile, "Enter Your Password", Toast.LENGTH_SHORT).show()
            }else{
                if(email.contains("@gmail.com")){
                    if(num.count()==10){
                        if(type=="User"){
                            CoroutineScope(Dispatchers.IO).launch {
                                RetrofitClient.instance.updateusers(name,num,address,city,pass,"",id,"update")
                                    .enqueue(object: Callback<DefaultResponse> {
                                        override fun onResponse(
                                            call: Call<DefaultResponse>, response: Response<DefaultResponse>
                                        ) {
                                            getSharedPreferences("user",
                                                AppCompatActivity.MODE_PRIVATE
                                            ).edit().apply {
                                                putString("num",num)
                                                putString("pass",pass)
                                                putString("email",email)
                                                putString("name",name)
                                                putString("address",address)
                                                putString("type",type)
                                                putString("status",status)
                                                putString("status1",stat)
                                                putString("city",city)
                                                putInt("id",id)
                                                apply()
                                            }

                                            Toast.makeText(this@Profile, response.body()?.message, Toast.LENGTH_SHORT).show()

                                        }

                                        override fun onFailure(call: Call<DefaultResponse>, t: Throwable) {
                                            Toast.makeText(this@Profile, t.message, Toast.LENGTH_LONG).show()


                                        }

                                    })
                            }
                        }else{
                            if(b.spinstatus.selectedItem.toString()!="choose your choice"){
                                CoroutineScope(Dispatchers.IO).launch {
                                    RetrofitClient.instance.updateusers(name,num,address,city,pass,b.spinstatus.selectedItem.toString(),id,"update")
                                        .enqueue(object: Callback<DefaultResponse> {
                                            override fun onResponse(
                                                call: Call<DefaultResponse>, response: Response<DefaultResponse>
                                            ) {
                                                getSharedPreferences("user",
                                                    AppCompatActivity.MODE_PRIVATE
                                                ).edit().apply {
                                                    putString("num",num)
                                                    putString("pass",pass)
                                                    putString("email",email)
                                                    putString("name",name)
                                                    putString("address",address)
                                                    putString("type",type)
                                                    putString("status",b.spinstatus.selectedItem.toString())
                                                    putString("status1",stat)
                                                    putString("city",city)
                                                    putInt("id",id)
                                                    apply()
                                                }
                                                finish()
                                                startActivity(Intent(this@Profile,RealestateDashboard::class.java))


                                                Toast.makeText(this@Profile, response.body()?.message, Toast.LENGTH_SHORT).show()

                                            }

                                            override fun onFailure(call: Call<DefaultResponse>, t: Throwable) {
                                                Toast.makeText(this@Profile, t.message, Toast.LENGTH_LONG).show()


                                            }

                                        })
                                }
                            }else{
                                Toast.makeText(this@Profile, "choose your choice properly", Toast.LENGTH_SHORT).show()
                            }

                        }


                    }else{
                        Toast.makeText(this@Profile, "Enter Your Number Properly", Toast.LENGTH_SHORT).show()
                    }

                }else{
                    Toast.makeText(this@Profile, "Enter Your Email Properly", Toast.LENGTH_SHORT).show()
                }
            }
        }

    }
}