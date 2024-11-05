package com.example.grihagully.Realestate

import android.annotation.SuppressLint
import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Handler
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
import com.bumptech.glide.Glide
import com.example.grihagully.Admin.AdminUsers
import com.example.grihagully.R
import com.example.grihagully.databinding.ActivityViewdetailsBinding
import com.example.grihagully.databinding.CarddetaillandBinding
import com.example.grihagully.databinding.CarduseradminBinding
import com.example.grihagully.model.RetrofitClient
import com.example.grihagully.model.Userresponse
import com.example.grihagully.model.land
import com.example.grihagully.model.landresponse
import com.ymts0579.model.model.DefaultResponse
import com.ymts0579.model.model.User
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class Viewdetails : AppCompatActivity() {
    private  val b by lazy {
        ActivityViewdetailsBinding.inflate(layoutInflater)
    }
    var email=""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(b.root)

        b.btnadddetails.setOnClickListener { startActivity(Intent(this,Adddetails::class.java)) }
        getSharedPreferences("user", AppCompatActivity.MODE_PRIVATE).apply {
            email=getString("email","").toString()
        }

        val p= ProgressDialog(this)
        p.show()
        CoroutineScope(Dispatchers.IO).launch {
            RetrofitClient.instance.viewlands(email,"viewlands")
                .enqueue(object : Callback<landresponse> {
                    @SuppressLint("SetTextI18n")
                    override fun onResponse(call: Call<landresponse>, response: Response<landresponse>) {

                        b.listlands.let {
                            response.body()?.user?.let {
                                    it1 ->
                                it.adapter=deatilsAdapter(this@Viewdetails,it1)
                                it.layoutManager=LinearLayoutManager(this@Viewdetails)
                                Toast.makeText(this@Viewdetails, "success", Toast.LENGTH_SHORT).show()
                            }
                        }
                        p.dismiss()
                    }

                    override fun onFailure(call: Call<landresponse>, t: Throwable) {
                        Toast.makeText(this@Viewdetails, "${t.message}", Toast.LENGTH_SHORT).show()

                    }

                })
        }



    }


    class deatilsAdapter(var context: Context, var listdata: ArrayList<land>):
        RecyclerView.Adapter<deatilsAdapter.DataViewHolder>(){

        inner class DataViewHolder(val view:CarddetaillandBinding) : RecyclerView.ViewHolder(view.root)

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DataViewHolder {
            return DataViewHolder(
                CarddetaillandBinding.inflate(
                LayoutInflater.from(context),parent,
                false))
        }

        override fun onBindViewHolder(holder: DataViewHolder, @SuppressLint("RecyclerView") position:Int) {
            with(holder.view){

                listdata[position].apply {
                    Glide.with(context).load(Uri.parse(path.trim())).into(image)
                    tvname.text=sname
                    tvcost.text="Cost : $cost"
                    tvstatus.text=status
                    tvlandmark.text=landmark
                    tvaddress.text=address
                    tvdescr.text=description
                    tvinfo.text="Owner name : $oname \nOwner Number : $onum"
                    tvinfo.visibility= View.GONE
                    tvownerdetails.setOnClickListener {
                        tvinfo.visibility= View.VISIBLE
                        Handler().postDelayed({
                            tvinfo.visibility= View.GONE
                        },3500)
                    }


                    holder.itemView.setOnClickListener {
                        if (status==""){
                            val alertdialog= AlertDialog.Builder(context)
                            alertdialog.setTitle("Verify profile")
                            alertdialog.setIcon(R.drawable.logo)
                            alertdialog.setCancelable(false)
                            alertdialog.setMessage("The Site is sold out?")
                            alertdialog.setPositiveButton("Yes"){ alertdialog, which->

                                updatestatus("Sold",id)
                                alertdialog.dismiss()
                            }

                            alertdialog.show()
                        }else{
                            Toast.makeText(context, "Already $status", Toast.LENGTH_SHORT).show()
                        }
                    }

                }

            }




        }

        private fun updatestatus(status: String, id: Int) {
            CoroutineScope(Dispatchers.IO).launch {
                RetrofitClient.instance.updatestatusdetails(status,id,"updatestatusdetails")
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