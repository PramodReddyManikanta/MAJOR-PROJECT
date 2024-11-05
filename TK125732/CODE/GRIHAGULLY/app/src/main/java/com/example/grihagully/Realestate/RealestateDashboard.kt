package com.example.grihagully.Realestate

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.telephony.gsm.SmsManager
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.grihagully.Profile
import com.example.grihagully.R
import com.example.grihagully.databinding.ActivityRealestateDashboardBinding
import com.example.grihagully.databinding.CardrealestatehistoryBinding
import com.example.grihagully.logout
import com.example.grihagully.model.RetrofitClient
import com.example.grihagully.model.requestresponse
import com.example.grihagully.model.requestsf
import com.ymts0579.model.model.DefaultResponse
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RealestateDashboard : AppCompatActivity() {
    private val b by lazy {
        ActivityRealestateDashboardBinding.inflate(layoutInflater)
    }
    @SuppressLint("SetTextI18n")
    var email=""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(b.root)

       getSharedPreferences("user", AppCompatActivity.MODE_PRIVATE).apply {
            b.textView.setText("Welcome "+getString("name","").toString())
            b.textView1.setText("Status "+getString("status","").toString())
           email=getString("email","").toString()
        }

        b.btnprofile.setOnClickListener {startActivity(Intent(this@RealestateDashboard, Profile::class.java))}
        b.btnaddland.setOnClickListener {startActivity(Intent(this@RealestateDashboard,Viewdetails::class.java)) }
        b.btnlogout.setOnClickListener { logout() }

        getData()

    }
    private fun getData(){
        val p= ProgressDialog(this)
        p.show()
        CoroutineScope(Dispatchers.IO).launch {
            RetrofitClient.instance.realestatehistory(email,"realestatehistory")
                .enqueue(object : Callback<requestresponse> {
                    @SuppressLint("SetTextI18n")
                    override fun onResponse(call: Call<requestresponse>, response: Response<requestresponse>) {

                        b.listrealhistory.let {
                            response.body()?.user?.let {
                                    it1 ->
                                it.adapter=realhistoryAdapter(this@RealestateDashboard, it1)
                                it.layoutManager= LinearLayoutManager(this@RealestateDashboard)
                                Toast.makeText(this@RealestateDashboard, "success", Toast.LENGTH_SHORT).show()
                            }
                        }
                        p.dismiss()
                    }

                    override fun onFailure(call: Call<requestresponse>, t: Throwable) {
                        Toast.makeText(this@RealestateDashboard, "${t.message}", Toast.LENGTH_SHORT).show()

                    }

                })
        }
    }

   inner class realhistoryAdapter(var context: Context, var listdata: ArrayList<requestsf>):
        RecyclerView.Adapter<realhistoryAdapter.DataViewHolder>(){

        inner class DataViewHolder(val view: CardrealestatehistoryBinding) : RecyclerView.ViewHolder(view.root)

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DataViewHolder {
            return DataViewHolder(
                CardrealestatehistoryBinding.inflate(
                    LayoutInflater.from(context),parent,
                    false))
        }

        @SuppressLint("SetTextI18n")
        override fun onBindViewHolder(holder: DataViewHolder, @SuppressLint("RecyclerView") position:Int) {
            with(holder.view){

                listdata[position].apply {
                    Glide.with(context).load(Uri.parse(path?.trim())).into(image)
                    tvname.text=sname
                    tvcost.text="Cost : $cost"
                    tvstatus.text=statusPoint
                    tvlandmark.text=landmark
                    tvaddress.text=address
                    tvdescr.text=description
                    tvdate.text=date
                    tvinfo.text="Owner name : $oname "
                    tvinfo1.text=" Name: $uname \n  Number: $unum"
                    tvinfo1.visibility= View.GONE
                    tvinfo.visibility= View.GONE
                    tvfeedback.text=feedback

                    var floaft=0.0f
                    rating!!.forEach {
                        if(it!=' '&&it.isDigit()||it=='.'){
                            floaft=it.toFloat()
                        }
                    }
                    ratingbar.isIndeterminate=true
                    ratingbar.rating=floaft
                    tvownerdetails.setOnClickListener {
                        tvinfo.visibility= View.VISIBLE
                        Handler().postDelayed({
                            tvinfo.visibility= View.GONE
                        },3500)
                    }

                    tvuser.setOnClickListener {
                        tvinfo1.visibility= View.VISIBLE
                        Handler().postDelayed({
                            tvinfo1.visibility= View.GONE
                        },3500)
                    }

                    if(feedback==""){
                        linearfeed.visibility=View.GONE
                    }else{
                        linearfeed.visibility=View.VISIBLE
                    }
                    
                    holder.itemView.setOnClickListener {
                        if(statusPoint=="Rejected"||statusPoint=="Complete"){
                            Toast.makeText(context, "Already ${statusPoint}", Toast.LENGTH_SHORT).show()
                        }else{
                            holder.itemView.setOnClickListener {

                                val alertdialog= AlertDialog.Builder(context)
                                alertdialog.setIcon(R.drawable.ic_launcher_foreground)
                                alertdialog.setTitle("Accept or Reject the Request")
                                alertdialog.setIcon(R.drawable.logo)
                                alertdialog.setMessage("Do you Want to Accept or Reject?")
                                alertdialog.setPositiveButton("Accept"){ alertdialog, which->
                                    if(statusPoint=="Accepted"){
                                        Toast.makeText(context, "Already ${statusPoint}", Toast.LENGTH_SHORT).show()
                                    }else{
                                        updatestatus("Accepted",unum,requesId)
                                        alertdialog.dismiss()
                                    }
                                }
                                alertdialog.setNegativeButton("Reject"){alertdialog,which->
                                    if(statusPoint=="Accepted"){
                                        Toast.makeText(context, "You can't Reject the request,you already ${statusPoint}", Toast.LENGTH_SHORT).show()
                                    }else{
                                        updatestatus("Reject",unum,requesId)
                                        alertdialog.dismiss()
                                    }

                                }
                                alertdialog.setNeutralButton("Complete"){ alertdialog,w->
                                    updatestatus("Completed",unum,requesId)
                                    alertdialog.dismiss()
                                }
                                alertdialog.show()

                            }

                        }
                    }

                }

            }

        }

        private fun updatestatus(status: String, unum: String?, id: String?) {
            CoroutineScope(Dispatchers.IO).launch {
                RetrofitClient.instance.updatestatusreal(status,id!!.toInt(),"updatestatusreal")
                    .enqueue(object: Callback<DefaultResponse> {
                        override fun onFailure(call: Call<DefaultResponse>, t: Throwable) {
                            Toast.makeText(context, "${t.message}", Toast.LENGTH_SHORT).show()
                        }
                        override fun onResponse(call: Call<DefaultResponse>, response: Response<DefaultResponse>) {
                            response.body()?.message?.let {
                                if(it=="updated successful!"){
                                    getData()
                                }
                                Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
                            }


                            if (TextUtils.isDigitsOnly(unum)) {
                                val smsManager: SmsManager = SmsManager.getDefault()
                                smsManager.sendTextMessage(unum, null, "your Request is $status", null, null)
                            }

                        }
                    })
            }

        }


        override fun getItemCount() = listdata.size
    }
}