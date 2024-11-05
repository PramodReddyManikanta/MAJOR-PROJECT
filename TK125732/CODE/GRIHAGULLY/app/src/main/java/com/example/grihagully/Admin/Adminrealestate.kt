package com.example.grihagully.Admin

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
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.grihagully.R
import com.example.grihagully.databinding.ActivityAdminrealestateBinding
import com.example.grihagully.databinding.CarduseradminBinding
import com.example.grihagully.model.RetrofitClient
import com.example.grihagully.model.Userresponse
import com.ymts0579.model.model.DefaultResponse
import com.ymts0579.model.model.User
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class Adminrealestate : AppCompatActivity() {
    private val b by lazy {
        ActivityAdminrealestateBinding.inflate(layoutInflater)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(b.root)



        val p= ProgressDialog(this)
        p.show()
        CoroutineScope(Dispatchers.IO).launch {
            RetrofitClient.instance.adminrealestate()
                .enqueue(object : Callback<Userresponse> {
                    @SuppressLint("SetTextI18n")
                    override fun onResponse(call: Call<Userresponse>, response: Response<Userresponse>) {

                        b.listrealestate.let {
                            it.layoutManager=LinearLayoutManager(this@Adminrealestate)
                            it.adapter=realestateAdminAdapter(this@Adminrealestate,response.body()!!.user)
                        }

                        p.dismiss()
                    }

                    override fun onFailure(call: Call<Userresponse>, t: Throwable) {
                        Toast.makeText(this@Adminrealestate, "${t.message}", Toast.LENGTH_SHORT).show()

                    }

                })
        }

    }

    class realestateAdminAdapter(var context: Context, var listdata: ArrayList<User>):
        RecyclerView.Adapter<realestateAdminAdapter.DataViewHolder>(){

        inner class DataViewHolder(val view: CarduseradminBinding) : RecyclerView.ViewHolder(view.root)

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DataViewHolder {
            return DataViewHolder(
                CarduseradminBinding.inflate(
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
                    tvstatus.text=stat

                    if(stat=="Not verified"){
                        btndelete.visibility= View.VISIBLE
                        btnfeedback.visibility=View.GONE
                    }else{
                        btndelete.visibility= View.VISIBLE
                        btnfeedback.visibility=View.VISIBLE
                    }


                    btnfeedback.setOnClickListener {
                        context.startActivity(Intent(context,Adminrealfeedback::class.java).apply {
                            putExtra("email",email)
                        })
                    }

                    holder.itemView.setOnClickListener {
                        if (stat=="Not verified"){
                            val alertdialog= AlertDialog.Builder(context)
                            alertdialog.setTitle("Verify profile")
                            alertdialog.setIcon(R.drawable.logo)
                            alertdialog.setCancelable(false)
                            alertdialog.setMessage("Do you verified the Profile?")
                            alertdialog.setPositiveButton("Yes"){ alertdialog, which->

                                needtovrify("Verified",id)
                                alertdialog.dismiss()
                            }
                            alertdialog.setNegativeButton("No"){alertdialog,which->
                                needtovrify("Rejected",id)
                                alertdialog.dismiss()
                            }
                            alertdialog.show()
                        }else{
                            Toast.makeText(context, "Already $stat", Toast.LENGTH_SHORT).show()
                        }

                    }


                    btndelete.setOnClickListener {
                        val alertdialog= AlertDialog.Builder(context)
                        alertdialog.setTitle("Delete")
                        alertdialog.setIcon(R.drawable.logo)
                        alertdialog.setCancelable(false)
                        alertdialog.setMessage("Do you Deleted the Profile?")
                        alertdialog.setPositiveButton("Yes"){ alertdialog, which->

                            deleteperson(id)
                            alertdialog.dismiss()
                        }

                        alertdialog.show()
                    }

                }

            }




        }

        private fun deleteperson(id: Int) {
            CoroutineScope(Dispatchers.IO).launch {
                RetrofitClient.instance.Deleteperson(id,"deletetable")
                    .enqueue(object: Callback<DefaultResponse> {
                        override fun onFailure(call: Call<DefaultResponse>, t: Throwable) {
                            Toast.makeText(context, ""+t.message, Toast.LENGTH_SHORT).show()
                        }
                        override fun onResponse(call: Call<DefaultResponse>, response: Response<DefaultResponse>) {
                            Toast.makeText(context, "${response.body()!!.message }", Toast.LENGTH_SHORT).show()


                        }
                    })
            }
        }

        private fun needtovrify(status: String, id: Int) {
            CoroutineScope(Dispatchers.IO).launch {
                RetrofitClient.instance.updatestat(status,id,"updatestatus")
                    .enqueue(object: Callback<DefaultResponse> {
                        override fun onFailure(call: Call<DefaultResponse>, t: Throwable) {
                            Toast.makeText(context, ""+t.message, Toast.LENGTH_SHORT).show()
                        }
                        override fun onResponse(call: Call<DefaultResponse>, response: Response<DefaultResponse>) {
                            Toast.makeText(context, "${response.body()!!.message }, $status", Toast.LENGTH_SHORT).show()


                        }
                    })
            }

        }


        override fun getItemCount() = listdata.size
    }
}