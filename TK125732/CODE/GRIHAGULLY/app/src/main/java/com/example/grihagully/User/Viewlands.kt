package com.example.grihagully.User

import android.annotation.SuppressLint
import android.app.DatePickerDialog
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
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.material3.BottomSheetScaffold
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.forEach
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.grihagully.R
import com.example.grihagully.Realestate.Viewdetails
import com.example.grihagully.databinding.ActivityViewlandsBinding
import com.example.grihagully.databinding.CarddetaillandBinding
import com.example.grihagully.model.RetrofitClient
import com.example.grihagully.model.land
import com.example.grihagully.model.landresponse
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.ymts0579.model.model.DefaultResponse
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.Calendar

class Viewlands : AppCompatActivity() {
    private val bind by lazy {
        ActivityViewlandsBinding.inflate(layoutInflater)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(bind.root)

        val remail=intent.getStringExtra("email").toString()
        val rname=intent.getStringExtra("name").toString()
        val rnum=intent.getStringExtra("num").toString()


        var email=""
        var num=""
        var name=""

        getSharedPreferences("user", AppCompatActivity.MODE_PRIVATE).apply {
          email=( getString("email","").toString())
          num= (getString("num","").toString())
          name= (getString("name","").toString())
        }

        val p= ProgressDialog(this)
        p.show()
        CoroutineScope(Dispatchers.IO).launch {
            RetrofitClient.instance.viewlands(remail,"viewlands")
                .enqueue(object : Callback<landresponse> {
                    @SuppressLint("SetTextI18n")
                    override fun onResponse(call: Call<landresponse>, response: Response<landresponse>) {

                        bind.listlands.let {
                            response.body()?.user?.let {
                                    it1 ->
                                it.adapter= deatilsuserAdapter(this@Viewlands, it1,remail,
                                        rname,
                                        rnum,email,
                                            num,
                                            name)
                                it.layoutManager= LinearLayoutManager(this@Viewlands)
                                Toast.makeText(this@Viewlands, "success", Toast.LENGTH_SHORT).show()
                            }
                        }
                        p.dismiss()
                    }

                    override fun onFailure(call: Call<landresponse>, t: Throwable) {
                        Toast.makeText(this@Viewlands, "${t.message}", Toast.LENGTH_SHORT).show()

                    }

                })
        }
    }

    class deatilsuserAdapter(var context: Context, var listdata: ArrayList<land>,var remail:String,
                             var rname:String, var rnum:String,var uemail:String,
                            var unum:String,var uname:String,):
        RecyclerView.Adapter<deatilsuserAdapter.DataViewHolder>(){

        inner class DataViewHolder(val view: CarddetaillandBinding) : RecyclerView.ViewHolder(view.root)

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DataViewHolder {
            return DataViewHolder(
                CarddetaillandBinding.inflate(
                    LayoutInflater.from(context),parent,
                    false))
        }

        override fun onBindViewHolder(holder: DataViewHolder, @SuppressLint("RecyclerView") position:Int) {
            with(holder.view){

                listdata[position].apply {
                    if(status=="Pending"){
                        cardinfo.visibility=View.VISIBLE
                    }else{
                        cardinfo.visibility=View.GONE
                    }
                    Glide.with(context).load(Uri.parse(path.trim())).into(image)
                    tvname.text=sname
                    tvcost.text="Cost : $cost"
                    tvstatus.text=status
                    tvlandmark.text=landmark
                    tvaddress.text=address
                    tvdescr.text=description
                    tvinfo.text="Owner name : $oname "
                    tvinfo.visibility= View.GONE
                    tvownerdetails.setOnClickListener {
                        tvinfo.visibility= View.VISIBLE
                        Handler().postDelayed({
                            tvinfo.visibility= View.GONE
                        },3500)
                    }

                    holder.itemView.setOnClickListener {
                        val bb= BottomSheetDialog(context)
                        bb.setContentView(R.layout.cardaddrequest)

                        val etdate=bb.findViewById<EditText>(R.id.etdate)!!
                        val etdescription=bb.findViewById<EditText>(R.id.etdescription)!!
                        etdate.setOnClickListener {
                            val c: Calendar = Calendar.getInstance()
                            DatePickerDialog(context, { view, year, month, dayOfMonth ->
                                val dt = "$dayOfMonth-${month + 1}-$year"
                                etdate.setText(dt)
                            }, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH)).apply {

                                this.datePicker.minDate=System.currentTimeMillis()-1000

                                show()
                            }
                        }


                        bb.findViewById<Button>(R.id.btnaddrequest)!!.setOnClickListener {
                            val date=etdate.text.toString().trim()
                            val desc=etdescription.text.toString().trim()

                            if(date.isEmpty()){etdate.error="choice the proper date"}
                            else if(desc.isEmpty()){etdescription.error="Enter your Description"}
                           else {

                                CoroutineScope(Dispatchers.IO).launch {
                                    RetrofitClient.instance.addrequest(
                                        date = date,
                                        descri = desc,
                                        remail =remail ,
                                        rname = rname,
                                        rnum = rnum,
                                        uemail = uemail,
                                        unum = unum,
                                        uname =uname ,
                                        time = "",
                                        lid=id.toString(),
                                        status="pending",
                                        feedback="",
                                        rating="",
                                        condition = "addrequest"
                                    )
                                        .enqueue(object: Callback<DefaultResponse> {
                                            override fun onFailure(call: Call<DefaultResponse>, t: Throwable) {
                                                Toast.makeText(context, ""+t.message, Toast.LENGTH_SHORT).show()
                                            }
                                            override fun onResponse(call: Call<DefaultResponse>, response: Response<DefaultResponse>) {
                                                Toast.makeText(context, "${response.body()!!.message}", Toast.LENGTH_SHORT).show()
                                                bb.dismiss()
                                            }
                                        })
                                }

                            }

                        }

                        bb.show()
                    }





                }

            }




        }




        override fun getItemCount() = listdata.size
    }
}