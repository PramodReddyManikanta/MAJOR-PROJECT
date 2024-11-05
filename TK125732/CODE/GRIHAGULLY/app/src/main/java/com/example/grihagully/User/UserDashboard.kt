package com.example.grihagully.User

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.grihagully.Profile
import com.example.grihagully.R
import com.example.grihagully.databinding.ActivityUserDashboardBinding
import com.example.grihagully.logout

class UserDashboard : AppCompatActivity() {
    private val b by lazy {
        ActivityUserDashboardBinding.inflate(layoutInflater)
    }
    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(b.root)
        getSharedPreferences("user", AppCompatActivity.MODE_PRIVATE).apply {
            b.tvname.setText("Welcome "+getString("name","").toString())
        }


        b.linearrealestate.setOnClickListener { startActivity(Intent(this,viewRealestates::class.java)) }
        b.carduserlogout.setOnClickListener { logout() }
        b.carduserprofile.setOnClickListener { startActivity(Intent(this,Profile::class.java)) }
        b.cardhistory.setOnClickListener { startActivity(Intent(this,History::class.java)) }
    }
}