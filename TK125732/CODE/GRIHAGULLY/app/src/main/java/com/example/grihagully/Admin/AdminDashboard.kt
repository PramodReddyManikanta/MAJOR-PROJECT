package com.example.grihagully.Admin

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.grihagully.R
import com.example.grihagully.databinding.ActivityAdminDashboardBinding
import com.example.grihagully.logout

class AdminDashboard : AppCompatActivity() {
    private val  b by lazy {
        ActivityAdminDashboardBinding.inflate(layoutInflater)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(b.root)

        b.floatingActionButton.setOnClickListener { logout() }
        b.btnadminrealstate.setOnClickListener { startActivity(Intent(this,Adminrealestate::class.java)) }
        b.btnadminuser.setOnClickListener { startActivity(Intent(this,AdminUsers::class.java)) }
    }
}