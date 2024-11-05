package com.example.grihagully.Admin

import android.annotation.SuppressLint
import android.app.ProgressDialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.RatingBar
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.grihagully.R
import com.example.grihagully.databinding.ActivityAdminrealfeedbackBinding
import com.example.grihagully.model.RetrofitClient
import com.example.grihagully.model.requestresponse
import com.example.grihagully.model.requestsf
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class Adminrealfeedback : AppCompatActivity() {
    private  val b by lazy{
        ActivityAdminrealfeedbackBinding.inflate(layoutInflater)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(b.root)

        val email=intent.getStringExtra("email").toString()

        val p= ProgressDialog(this)
        p.show()
        CoroutineScope(Dispatchers.IO).launch {
            RetrofitClient.instance.realestatehistory(email,"realestatehistory")
                .enqueue(object : Callback<requestresponse> {
                    @SuppressLint("SetTextI18n")
                    override fun onResponse(call: Call<requestresponse>, response: Response<requestresponse>) {

                        b.main.let {
                            response.body()?.user?.let {
                                    it1 ->
                                it.adapter=feedbackadapter(this@Adminrealfeedback,it1)
                                it.layoutManager= LinearLayoutManager(this@Adminrealfeedback)
                                Toast.makeText(this@Adminrealfeedback, "success", Toast.LENGTH_SHORT).show()
                            }
                        }
                        p.dismiss()
                    }

                    override fun onFailure(call: Call<requestresponse>, t: Throwable) {
                        Toast.makeText(this@Adminrealfeedback, "${t.message}", Toast.LENGTH_SHORT).show()

                    }

                })
        }
    }

    class feedbackadapter(var context: Context, var listdata: ArrayList<requestsf>):
        RecyclerView.Adapter<feedbackadapter.DataViewHolder>(){
        var id=0
        class DataViewHolder(view: View) : RecyclerView.ViewHolder(view) {


            val  ratingbar: RatingBar =view.findViewById(R.id.rating)
            val  tvfeedback: TextView =view.findViewById(R.id.tvfeedback)
            val linearfeed: LinearLayout =view.findViewById(R.id.linearfeed)

        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DataViewHolder {
            val view = LayoutInflater.from(context).inflate(R.layout.cardfeed, parent, false)
            return DataViewHolder(view)
        }

        override fun onBindViewHolder(holder: DataViewHolder, @SuppressLint("RecyclerView") position:Int) {
            holder.apply {
                listdata.get(position).apply {

                    tvfeedback.text=feedback

                    var floaft=0.0f
                    rating!!.forEach {
                        if(it!=' '&&it.isDigit()||it=='.'){
                            floaft=it.toFloat()
                        }
                    }
                    ratingbar.isIndeterminate=true
                    ratingbar.rating=floaft

                    if(feedback!!.isEmpty()){
                        linearfeed.visibility= View.GONE
                    }else{
                        linearfeed.visibility= View.VISIBLE
                    }

                }

            }

        }




        override fun getItemCount() = listdata.size
    }
}