package com.example.grihagully.Admin

import android.annotation.SuppressLint
import android.app.ProgressDialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.grihagully.R
import com.example.grihagully.databinding.ActivityAdminUsersBinding
import com.example.grihagully.databinding.CarduseradminBinding
import com.example.grihagully.model.RetrofitClient
import com.example.grihagully.model.Userresponse
import com.ymts0579.model.model.User
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AdminUsers : AppCompatActivity() {
    private val b by lazy {
        ActivityAdminUsersBinding.inflate(layoutInflater)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(b.root)


        val p= ProgressDialog(this)
        p.show()
        CoroutineScope(Dispatchers.IO).launch {
            RetrofitClient.instance.adminuser()
                .enqueue(object : Callback<Userresponse> {
                    @SuppressLint("SetTextI18n")
                    override fun onResponse(call: Call<Userresponse>, response: Response<Userresponse>) {

                        b.listuser.let {
                            response.body()?.user?.let {
                                    it1 ->
                                it.adapter=   userAdminAdapter(this@AdminUsers, it1)
                                it.layoutManager= LinearLayoutManager(this@AdminUsers)
                                Toast.makeText(this@AdminUsers, "success", Toast.LENGTH_SHORT).show()
                            }
                        }
                        p.dismiss()
                    }

                    override fun onFailure(call: Call<Userresponse>, t: Throwable) {
                        Toast.makeText(this@AdminUsers, "${t.message}", Toast.LENGTH_SHORT).show()

                    }

                })
        }

    }

    class userAdminAdapter(var context: Context, var listdata: ArrayList<User>):
        RecyclerView.Adapter<userAdminAdapter.DataViewHolder>(){

        inner class DataViewHolder(val view: CarduseradminBinding) : RecyclerView.ViewHolder(view.root)

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DataViewHolder {
            return DataViewHolder(CarduseradminBinding.inflate(
                LayoutInflater.from(context),parent,
                false))
        }

        override fun onBindViewHolder(holder: DataViewHolder, @SuppressLint("RecyclerView") position:Int) {
            with(holder.view){

                listdata[position].apply {
                    tvfname.text=name
                    tvfemail.text=email
                    tvfnum.text=num
                    tvfcity.text=city
                    if(type=="User"){
                        tvstatus.visibility= View.GONE
                        btndelete.visibility= View.GONE
                        btnfeedback.visibility=View.GONE
                    }

                }

            }




        }


        override fun getItemCount() = listdata.size
    }
}