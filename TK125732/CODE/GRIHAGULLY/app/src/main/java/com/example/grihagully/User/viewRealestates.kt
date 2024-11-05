package com.example.grihagully.User

import android.annotation.SuppressLint
import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.grihagully.Admin.AdminUsers
import com.example.grihagully.R
import com.example.grihagully.databinding.ActivityViewRealestatesBinding
import com.example.grihagully.databinding.CarduseradminBinding
import com.example.grihagully.databinding.CarduserrealestateBinding
import com.example.grihagully.model.RetrofitClient
import com.example.grihagully.model.Userresponse
import com.ymts0579.model.model.User
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class viewRealestates : AppCompatActivity() {
    private val b by lazy {
        ActivityViewRealestatesBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(b.root)
        b.etemail1.addTextChangedListener {
            it?.toString()?.let {
               readstring(it)
            }
        }
    }

    private fun readstring(name: String) {
        if(name.isEmpty()){
            Toast.makeText(this, "Enter city to search", Toast.LENGTH_SHORT).show()
        }else{
            val p= ProgressDialog(this)
            p.show()
            CoroutineScope(Dispatchers.IO).launch {
                RetrofitClient.instance.viewrealstates(name,"viewrealstates")
                    .enqueue(object : Callback<Userresponse> {
                        @SuppressLint("SetTextI18n")
                        override fun onResponse(call: Call<Userresponse>, response: Response<Userresponse>) {

                            b.listuser.let {
                                response.body()?.user?.let {
                                        it1 ->
                                    it.adapter= userrealestateAdapter(this@viewRealestates, it1)
                                    it.layoutManager= LinearLayoutManager(this@viewRealestates)
                                    Toast.makeText(this@viewRealestates, "success", Toast.LENGTH_SHORT).show()
                                }
                            }
                            p.dismiss()
                        }

                        override fun onFailure(call: Call<Userresponse>, t: Throwable) {
                            Toast.makeText(this@viewRealestates, "${t.message}", Toast.LENGTH_SHORT).show()

                        }

                    })
            }
        }


    }

    class userrealestateAdapter(var context: Context, var listdata: ArrayList<User>):
        RecyclerView.Adapter<userrealestateAdapter.DataViewHolder>(){

        inner class DataViewHolder(val view: CarduserrealestateBinding) : RecyclerView.ViewHolder(view.root)

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DataViewHolder {
            return DataViewHolder(
                CarduserrealestateBinding.inflate(
                LayoutInflater.from(context),parent,
                false))
        }

        override fun onBindViewHolder(holder: DataViewHolder, @SuppressLint("RecyclerView") position:Int) {
            with(holder.view){

                listdata[position].apply {
                    tvfname.text=name
                    tvfemail.text=email
                    tvfnum.text=num
                    tvfcity.text=address

                    tvmoreinfo.setOnClickListener {
                        context.startActivity(Intent(context,Viewlands::class.java).apply {
                            putExtra("email",email)
                            putExtra("name",name)
                            putExtra("num",num)
                        })

                    }

                }

            }




        }


        override fun getItemCount() = listdata.size
    }
}