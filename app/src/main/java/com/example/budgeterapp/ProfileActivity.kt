package com.example.budgeterapp

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_profile.*
import kotlinx.android.synthetic.main.nav_header_main.*


class ProfileActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener{

    lateinit var phone:String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)
        phone=intent.getSerializableExtra("key") as String
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout_profile)
        val navView: NavigationView = findViewById(R.id.nav_view_profile)
        val drawerToggle = ActionBarDrawerToggle(this,drawerLayout,toolbar,
            R.string.navigation_drawer_open,R.string.navigation_drawer_close)
        drawerLayout.addDrawerListener(drawerToggle)
        drawerToggle.syncState()

        navView.setNavigationItemSelectedListener(this)

        profile_edit.setOnClickListener {
            val intent = Intent(applicationContext,EditProfileActivity::class.java)
            intent.putExtra("key",phone)
            startActivity(intent)
        }

        val ref = FirebaseDatabase.getInstance().getReference("User")
        ref.addValueEventListener(object: ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
                TODO("Not yet implemented")
            }

            override fun onDataChange(p0: DataSnapshot) {
                for(h in p0.children)
                {
                    val value = h.getValue(User::class.java)
                    if(value?.phone == phone){
                        header_text1.text = value.name
                        header_text2.text = value.email
                        profile_name.text = value.name
                        profile_phone.text = value.phone
                        profile_email.text = value.email
                        profile_birthday.text = value.bod
                    }
                }
            }
        })

        val ref2 = FirebaseDatabase.getInstance().getReference("uploads")
        ref2.addValueEventListener(object: ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
                TODO("Not yet implemented")
            }
            override fun onDataChange(p0: DataSnapshot) {
                for(h in p0.children)
                {
                    val value = h.getValue(Upload::class.java)
                    if(value!!.phone == phone){
                        if(value.mImageUrl == ""){
                            Toast.makeText(applicationContext,"Error", Toast.LENGTH_SHORT).show()
                        }
                        else {
                            Picasso.with(applicationContext).load(value.mImageUrl).into(profile_image)
                            Picasso.with(applicationContext).load(value.mImageUrl).into(header_image)
                        }
                    }
                }
            }
        })
    }

    override fun onBackPressed() {
        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout_profile)
        if(drawerLayout.isDrawerOpen(GravityCompat.START))
            drawerLayout.closeDrawer(GravityCompat.START)
        else
            super.onBackPressed()
    }
    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId){
            R.id.nav_editIncome -> {val intent=Intent(applicationContext,EditIncomeActivity::class.java); intent.putExtra("key",phone) ;startActivity(intent)}

            R.id.nav_dashboard -> {val intent=Intent(applicationContext,DashboardActivity::class.java); intent.putExtra("key",phone) ;startActivity(intent)}

            R.id.nav_profile -> {val intent=Intent(applicationContext,ProfileActivity::class.java); intent.putExtra("key",phone) ;startActivity(intent)}

            R.id.nav_feedback -> {val intent=Intent(applicationContext,FeedbackActivity::class.java); intent.putExtra("key",phone) ;startActivity(intent)}

            R.id.nav_logout -> startActivity(Intent(applicationContext,LoginActivity::class.java))

            R.id.nav_expenses -> {val intent=Intent(applicationContext,ExpensesActivity::class.java); intent.putExtra("key",phone) ;startActivity(intent)}

            R.id.nav_loans -> {val intent=Intent(applicationContext,LoansActivity::class.java); intent.putExtra("key",phone) ;startActivity(intent)}

            R.id.nav_drafts -> {val intent=Intent(applicationContext,DraftsActivity::class.java); intent.putExtra("key",phone) ;startActivity(intent)}
        }

        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout_profile)
        drawerLayout.closeDrawer(GravityCompat.START)

        return true
    }
}
