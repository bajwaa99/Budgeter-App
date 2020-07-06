package com.example.budgeterapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_main2.*

class Main2Activity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main2)

        Start_LogIn.setOnClickListener{
            val intent=(Intent(applicationContext,LoginActivity::class.java))

            startActivity(intent)
        }
        Start_Register.setOnClickListener{
            val intent=(Intent(applicationContext,SignupActivity::class.java))
            startActivity(intent)
        }
    }
}
