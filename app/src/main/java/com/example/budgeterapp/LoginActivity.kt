package com.example.budgeterapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {
     var phone:String?=null
    var users:ArrayList<User> = ArrayList()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val ref = FirebaseDatabase.getInstance().getReference("User")
        ref.addValueEventListener(object:ValueEventListener{
            override fun onCancelled(p0: DatabaseError) {
                TODO("Not yet implemented")
            }

            override fun onDataChange(p0: DataSnapshot) {
                if(p0.exists()) {
                    for(h in p0.children) {
                        val user =h.getValue(User::class.java)
                        if (user != null) {
                            users.add(user)
                        }
                    }
                }
            }
        })

        login_btn.setOnClickListener{
            for( c in users) {
                if (c.email == login_email.text.toString() && c.pass == login_password.text.toString()) {
                    phone = c.phone
                    val intent = Intent(applicationContext, DashboardActivity::class.java)
                    intent.putExtra("key", phone)
                    startActivity(intent)
                }

            }
            if(phone == null)
                Toast.makeText(this,"Wrong Email or Password",Toast.LENGTH_LONG).show()

        }

        LogInToRegister.setOnClickListener {
            startActivity(Intent(applicationContext,SignupActivity::class.java))
        }

    }

}
