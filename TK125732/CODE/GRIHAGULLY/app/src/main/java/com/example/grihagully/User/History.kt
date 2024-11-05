package com.example.grihagully.User

import android.annotation.SuppressLint
import android.app.ProgressDialog
import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.RatingBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.grihagully.R
import com.example.grihagully.databinding.ActivityHistoryBinding
import com.example.grihagully.databinding.CardhistoryBinding
import com.example.grihagully.model.RetrofitClient
import com.example.grihagully.model.requestresponse
import com.example.grihagully.model.requestsf
import com.google.android.material.bottomsheet.BottomSheetDialog
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class History : AppCompatActivity() {
    private val b by lazy {
        ActivityHistoryBinding.inflate(layoutInflater)
    }
    var email=""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(b.root)

        getSharedPreferences("user", AppCompatActivity.MODE_PRIVATE).apply {
            email=getString("email","").toString()
        }

        val p= ProgressDialog(this)
        p.show()
        CoroutineScope(Dispatchers.IO).launch {
            RetrofitClient.instance.history(email,"getData")
                .enqueue(object : Callback<requestresponse> {
                    @SuppressLint("SetTextI18n")
                    override fun onResponse(call: Call<requestresponse>, response: Response<requestresponse>) {

                        b.listhistory.let {
                            response.body()?.user?.let {
                                    it1 ->
                                it.adapter=historyAdapter(this@History,it1)
                                it.layoutManager= LinearLayoutManager(this@History)
                                Toast.makeText(this@History, "success", Toast.LENGTH_SHORT).show()
                            }
                        }
                        p.dismiss()
                    }

                    override fun onFailure(call: Call<requestresponse>, t: Throwable) {
                        Toast.makeText(this@History, "${t.message}", Toast.LENGTH_SHORT).show()

                    }

                })
        }

    }

    class historyAdapter(var context: Context, var listdata: ArrayList<requestsf>):
        RecyclerView.Adapter<historyAdapter.DataViewHolder>(){

        inner class DataViewHolder(val view: CardhistoryBinding) : RecyclerView.ViewHolder(view.root)

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DataViewHolder {
            return DataViewHolder(
                CardhistoryBinding.inflate(
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
                    tvinfo1.text="Real estate Name: $rname \n Real estate Number: $rnum"
                    tvinfo1.visibility=View.GONE
                    tvinfo.visibility= View.GONE
                    btnfeedback.visibility=View.GONE
                    tvownerdetails.setOnClickListener {
                        tvinfo.visibility= View.VISIBLE
                        Handler().postDelayed({
                            tvinfo.visibility= View.GONE
                        },3500)
                    }

                    tvrealestat.setOnClickListener {
                        tvinfo1.visibility= View.VISIBLE
                        Handler().postDelayed({
                            tvinfo1.visibility= View.GONE
                        },3500)
                    }

                    if(statusPoint=="Completed"){
                        if(feedback==""){
                            btnfeedback.visibility=View.VISIBLE
                        }else{
                            btnfeedback.visibility=View.GONE
                        }
                    }


                    btnfeedback.setOnClickListener {


                        val dd= BottomSheetDialog(context)
                        dd.setContentView(R.layout.cardfeedback)
                        val etfeedback=dd.findViewById<EditText>(R.id.etfeedback)!!
                        val btnsubmit=dd.findViewById<Button>(R.id.btnsubmit)!!
                        val rating=dd.findViewById<RatingBar>(R.id.rating)!!
                        btnsubmit.setOnClickListener {
                            val rate=rating.rating.toString()
                            val feed=etfeedback.text.toString().trim()
                            if(rate=="0.0"){
                                Toast.makeText(context, "give your rating", Toast.LENGTH_SHORT).show()
                            }else if(feed.isEmpty()){
                                Toast.makeText(context, "Enter your Feedback", Toast.LENGTH_SHORT).show()
                            }else {
                                CoroutineScope(Dispatchers.IO).async {
                                    async {
                                        try {
                                            RetrofitClient.instance.updaterating(rate,feed,requesId!!.toInt(),"updaterating")
                                        } catch (e: Exception) {
                                            withContext(Dispatchers.Main) {
                                                Toast.makeText(context, e.message, Toast.LENGTH_SHORT).show()
                                            }
                                            null
                                        }
                                    }.await().let {
                                        withContext(Dispatchers.Main){
                                            it?.body()?.message?.let {
                                                Toast.makeText(context, "$it", Toast.LENGTH_SHORT).show()

                                            }
                                        }
                                    }
                                }.start()
                            }
                        }

                        dd.show()

                    }

                }

            }




        }


        override fun getItemCount() = listdata.size
    }
}

