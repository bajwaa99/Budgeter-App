package com.example.budgeterapp

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.DatePicker
import android.widget.Toast
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_signup.*
import java.util.*

class SignupActivity : AppCompatActivity() {

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)

        signup_btn.setOnClickListener{
            val name=signup_name.text.toString()
            val email=signup_email.text.toString()
            val pass=signup_password.text.toString()
            val phone=signup_phone.text.toString()
            val bod=signup_birthday.text.toString()
            if(name.trim().isEmpty())
            {
                Toast.makeText(applicationContext,"Enter Name",Toast.LENGTH_SHORT).show()
            }
            else if(email.trim().isEmpty())
            {
                Toast.makeText(applicationContext,"Enter Email",Toast.LENGTH_SHORT).show()
            }
            else if(pass.trim().isEmpty())
            {
                Toast.makeText(applicationContext,"Enter Password",Toast.LENGTH_SHORT).show()
            }
            else if(phone.trim().isEmpty())
            {
                Toast.makeText(applicationContext,"Enter Phone Number",Toast.LENGTH_LONG).show()
            }
            else if(bod.trim().isEmpty())
            {
                Toast.makeText(applicationContext,"Enter Birthday",Toast.LENGTH_SHORT).show()
            }
            else if(phone.trim().length != 11)
            {
                Toast.makeText(applicationContext,"Invalid Phone Number",Toast.LENGTH_SHORT).show()
            }
            else if(!email.contains("@",ignoreCase = false))
            {
                Toast.makeText(applicationContext,"Invalid Email",Toast.LENGTH_SHORT).show()
            }
            else if(!email.contains(".com",ignoreCase = false))
            {
                Toast.makeText(applicationContext,"Invalid Email",Toast.LENGTH_SHORT).show()
            }
            else
            {
                adduser()
                startActivity(Intent(applicationContext,LoginActivity::class.java))
            }

        }

        RegisterToLogIn.setOnClickListener {
            startActivity(Intent(applicationContext,LoginActivity::class.java))
        }


        signup_dateBtn.setOnClickListener {

            val c= Calendar.getInstance()
            val year = c.get(Calendar.YEAR)
            val month = c.get(Calendar.MONTH)
            val day = c.get(Calendar.DAY_OF_MONTH)
            val dpd = DatePickerDialog(this,
                DatePickerDialog.OnDateSetListener{ _: DatePicker?, myear: Int, mmonth: Int, dayOfMonth: Int ->
                    var months = mmonth
                    months+=1
                    signup_birthday.setText("$dayOfMonth/$months/$myear")
                },year,month,day)

            dpd.show()
        }
    }

    fun adduser()
    {
        val name=signup_name.text.toString()
        val email=signup_email.text.toString()
        val pass=signup_password.text.toString()
        val phone=signup_phone.text.toString()
        val bod=signup_birthday.text.toString()

        val user=User(name,email,pass,phone,bod)

        val ref =FirebaseDatabase.getInstance().getReference("User")
        ref.child(phone).setValue(user).addOnCompleteListener{
            Toast.makeText(this,"User Registered",Toast.LENGTH_SHORT).show()
        }
    }
}
