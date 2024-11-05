package com.example.grihagully.Realestate

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Base64
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.grihagully.R
import com.example.grihagully.databinding.ActivityAdddetailsBinding
import com.example.grihagully.model.RetrofitClient
import com.ymts0579.model.model.DefaultResponse
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class Adddetails : AppCompatActivity() {
    private val b by lazy {
        ActivityAdddetailsBinding.inflate(layoutInflater)
    }
    var uri: Uri? = null
    var email=""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(b.root)

        getSharedPreferences("user", AppCompatActivity.MODE_PRIVATE).apply {
            email=getString("email","").toString()
        }

        val activity = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            it.data?.data?.let {
                b.addimg.setImageURI(it)
                uri = it
            }
        }


        b.addimg.setOnClickListener {
            Intent(Intent.ACTION_GET_CONTENT).apply {
                type = "image/*"
                activity.launch(this)
            }
        }


        b.btnadddeatils.setOnClickListener {
            val sname=b.etsitename.text.toString().trim()
            val description=b.etdescription.text.toString().trim()
            val landmark=b.etlandmark.text.toString().trim()
            val address=b.etaddress.text.toString().trim()
            val oname=b.etoname.text.toString().trim()
            val onum=b.etonum.text.toString().trim()
            val cost=b.etcost.text.toString().trim()

           if (sname.isEmpty()){b.etsitename.error="Enter site name"}
           else if (description.isEmpty()) { b.etdescription.error="Enter description" }
           else if (landmark.isEmpty()){b.etlandmark.error="Enter land mark "}
           else if (address.isEmpty()){b.etaddress.error="Enter Address"}
           else if (oname.isEmpty()){b.etoname.error="Enter owner name"}
           else if (onum.isEmpty()){b.etonum.error="Enter owner number "}
           else if (cost.isEmpty()){b.etcost.error="Enter cost"}
           else  if (uri == null) {
               Toast.makeText(this, "Please select image from gallery", Toast.LENGTH_SHORT).show()
           }else {
               contentResolver.openInputStream(uri!!)?.readBytes()?.let {


                   CoroutineScope(Dispatchers.IO).launch {
                       RetrofitClient.instance.adddeatils(
                           sname = sname ,
                           description =description ,
                           landmark = landmark,
                           address = address,
                           oname =oname,
                           onum = onum,
                           cost = cost,
                           status = "Pending",
                           path =  Base64.encodeToString(it, Base64.NO_WRAP),
                           email = email,
                           condition="adddetails"
                       )
                           .enqueue(object: Callback<DefaultResponse> {
                               override fun onFailure(call: Call<DefaultResponse>, t: Throwable) {
                                   Toast.makeText(this@Adddetails, ""+t.message, Toast.LENGTH_SHORT).show()
                               }
                               override fun onResponse(call: Call<DefaultResponse>, response: Response<DefaultResponse>) {
                                   Toast.makeText(this@Adddetails, "${response.body()!!.message}", Toast.LENGTH_SHORT).show()
                                   b.etsitename.text.clear()
                                   b.etdescription.text.clear()
                                   b.etlandmark.text.clear()
                                   b.etaddress.text.clear()
                                   b.etoname.text.clear()
                                   b.etonum.text.clear()
                                   b.etcost.text.clear()
                                   finish()
                                   startActivity(Intent(this@Adddetails,Viewdetails::class.java))
                               }
                           })
                   }
               }
           }

        }

    }
}