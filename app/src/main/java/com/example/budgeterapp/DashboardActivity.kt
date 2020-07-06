package com.example.budgeterapp


import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_dashboard.*
import kotlinx.android.synthetic.main.nav_header_main.*

class DashboardActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener,
BottomNavigationView.OnNavigationItemSelectedListener{
    lateinit var phone:String
    var isOpen:Boolean = false

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        phone = intent.getSerializableExtra("key")as String

        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout_dashboard)

        val drawerToggle: ActionBarDrawerToggle = ActionBarDrawerToggle(this,drawerLayout,toolbar,
            R.string.navigation_drawer_open,R.string.navigation_drawer_close)
        drawerLayout.addDrawerListener(drawerToggle)
        drawerToggle.syncState()

        val navView: NavigationView = findViewById(R.id.nav_view_dashboard)
        navView.setNavigationItemSelectedListener(this)

        val navViewBottomBar: BottomNavigationView = findViewById(R.id.dashboard_bottomBar)
        navViewBottomBar.setOnNavigationItemSelectedListener(this)

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
                            Picasso.with(applicationContext).load(value.mImageUrl).into(header_image)
                        }
                    }
                }

            }
        })
        floating_button.setOnClickListener{
           animate()
        }

        floating_button1.setOnClickListener {
            val intent = Intent(applicationContext,LoansActivity::class.java)
            intent.putExtra("key",phone)
            startActivity(intent)
        }

        floating_button2.setOnClickListener {
            val intent = Intent(applicationContext,ExpensesActivity::class.java)
            intent.putExtra("key",phone)
            startActivity(intent)
        }

        var expenseTotal = 0
        var incomeTotal = 0
        val ref3 = FirebaseDatabase.getInstance().getReference("Income")
        ref3.addValueEventListener(object: ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
                TODO("Not yet implemented")
            }
            override fun onDataChange(p0: DataSnapshot) {
                for(h in p0.children)
                {
                    val value = h.getValue(IncomeData::class.java)
                    if(value!!.phone == phone && value.month == "January"){

                        incomeTotal=value.amount
                        break
                    }
                }
                dashboard_income.text=incomeTotal.toString()
            }
        })


        val ref4 = FirebaseDatabase.getInstance().getReference("Expenses")
        ref4.addValueEventListener(object: ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
                TODO("Not yet implemented")
            }
            override fun onDataChange(p0: DataSnapshot) {

                for(h in p0.children)
                {
                    val value = h.getValue(ExpenseData::class.java)
                    if(value!!.key == phone && value.date!!.contains("/4/")){
                        expenseTotal+=value.amoount!!.toInt()
                    }
                }
                dashboard_expense.text=expenseTotal.toString()
                incomeTotal-=expenseTotal
                dashboard_netBalance.text=incomeTotal.toString()
            }
        })


        dashboard_previousMonth.setOnClickListener {

            expenseTotal = 0
            incomeTotal = 0
            val re = FirebaseDatabase.getInstance().getReference("Income")
            re.addValueEventListener(object: ValueEventListener {
                override fun onCancelled(p0: DatabaseError) {
                    TODO("Not yet implemented")
                }
                override fun onDataChange(p0: DataSnapshot) {
                    for(h in p0.children)
                    {
                        val value = h.getValue(IncomeData::class.java)
                        if(value!!.phone == phone && value.month == "March"){

                            incomeTotal=value.amount
                            break
                        }
                    }
                    dashboard_income.text=incomeTotal.toString()
                }
            })

            val r = FirebaseDatabase.getInstance().getReference("Expenses")
            r.addValueEventListener(object: ValueEventListener {
                override fun onCancelled(p0: DatabaseError) {
                    TODO("Not yet implemented")
                }
                override fun onDataChange(p0: DataSnapshot) {

                    for(h in p0.children)
                    {
                        val value = h.getValue(ExpenseData::class.java)
                        if(value!!.key == phone && value.date!!.contains("/3/")){
                            expenseTotal+=value.amoount!!.toInt()
                        }
                    }
                    dashboard_expense.text=expenseTotal.toString()
                    incomeTotal-=expenseTotal
                    dashboard_netBalance.text=incomeTotal.toString()
                }
            })
        }

        dashboard_thisMonth.setOnClickListener {

            expenseTotal = 0
            incomeTotal = 0
            val re = FirebaseDatabase.getInstance().getReference("Income")
            re.addValueEventListener(object: ValueEventListener {
                override fun onCancelled(p0: DatabaseError) {
                    TODO("Not yet implemented")
                }
                override fun onDataChange(p0: DataSnapshot) {
                    for(h in p0.children)
                    {
                        val value = h.getValue(IncomeData::class.java)
                        if(value!!.phone == phone && value.month == "April"){

                            incomeTotal=value.amount
                            break
                        }
                    }
                    dashboard_income.text=incomeTotal.toString()

                }
            })

            val r = FirebaseDatabase.getInstance().getReference("Expenses")
            r.addValueEventListener(object: ValueEventListener {
                override fun onCancelled(p0: DatabaseError) {
                    TODO("Not yet implemented")
                }
                override fun onDataChange(p0: DataSnapshot) {

                    for(h in p0.children)
                    {
                        val value = h.getValue(ExpenseData::class.java)
                        if(value!!.key == phone && value.date!!.contains("/4/")){
                            expenseTotal+=value.amoount!!.toInt()
                        }
                    }
                    dashboard_expense.text=expenseTotal.toString()
                    incomeTotal-=expenseTotal
                    dashboard_netBalance.text=incomeTotal.toString()
                }
            })
        }

        dashboard_nextMonth.setOnClickListener {
            expenseTotal = 0
            incomeTotal = 0
            val re = FirebaseDatabase.getInstance().getReference("Income")
            re.addValueEventListener(object: ValueEventListener {
                override fun onCancelled(p0: DatabaseError) {
                    TODO("Not yet implemented")
                }
                override fun onDataChange(p0: DataSnapshot) {
                    for(h in p0.children)
                    {
                        val value = h.getValue(IncomeData::class.java)
                        if(value!!.phone == phone && value.month == "May"){

                            incomeTotal=value.amount
                            break
                        }
                    }
                    dashboard_income.text=incomeTotal.toString()
                }
            })

            val r = FirebaseDatabase.getInstance().getReference("Expenses")
            r.addValueEventListener(object: ValueEventListener {
                override fun onCancelled(p0: DatabaseError) {
                    TODO("Not yet implemented")
                }
                override fun onDataChange(p0: DataSnapshot) {

                    for(h in p0.children)
                    {
                        val value = h.getValue(ExpenseData::class.java)
                        if(value!!.key == phone && value.date!!.contains("/5/")){
                            expenseTotal+=value.amoount!!.toInt()
                        }
                    }
                    dashboard_expense.text=expenseTotal.toString()
                    incomeTotal-=expenseTotal
                    dashboard_netBalance.text=incomeTotal.toString()
                }
            })
        }
    }

    override fun onBackPressed() {
        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout_dashboard)
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

            R.id.bottom_expenses -> {val intent=Intent(applicationContext,ExpensesActivity::class.java); intent.putExtra("key",phone) ;startActivity(intent)}

            R.id.bottom_loans ->{val intent=Intent(applicationContext,LoansActivity::class.java); intent.putExtra("key",phone) ;startActivity(intent)}

            R.id.bottom_profile -> {val intent=Intent(applicationContext,ProfileActivity::class.java); intent.putExtra("key",phone) ;startActivity(intent)}

            R.id.nav_loans -> {val intent=Intent(applicationContext,LoansActivity::class.java); intent.putExtra("key",phone) ;startActivity(intent)}

            R.id.nav_drafts -> {val intent=Intent(applicationContext,DraftsActivity::class.java); intent.putExtra("key",phone) ;startActivity(intent)}
        }

        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout_dashboard)
        drawerLayout.closeDrawer(GravityCompat.START)

        return true
    }

    private fun animate(){
        if(isOpen){
            floating_button1.startAnimation(AnimationUtils.loadAnimation(this,R.anim.fab_close))
            floating_button2.startAnimation(AnimationUtils.loadAnimation(this,R.anim.fab_close))
            floating_button1.isClickable = false
            floating_button2.isClickable = false
            isOpen = false
        }
        else{
            floating_button1.startAnimation(AnimationUtils.loadAnimation(this,R.anim.fab_open))
            floating_button2.startAnimation(AnimationUtils.loadAnimation(this,R.anim.fab_open))
            floating_button1.isClickable = true
            floating_button2.isClickable = true
            isOpen = true
        }
    }

}
