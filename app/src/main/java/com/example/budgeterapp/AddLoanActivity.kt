package com.example.budgeterapp

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.app.DatePickerDialog
import android.content.Intent
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.DatePicker
import android.widget.Toast
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_add_loan.*
import java.util.*

class AddLoanActivity : AppCompatActivity() {
    lateinit var sp1:String
    lateinit var sp2:String
    lateinit var phone:String
    private lateinit var update_key:String
    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_loan)
        phone=intent.getSerializableExtra("key")as String
        val type=intent.getSerializableExtra("type") as Int
       if(type==1)
       {
           val obj = intent.getSerializableExtra("object") as LoansData
           this.addLoan_title.setText(obj.title)
           this.addLoan_amount.setText(obj.amoount.toString())
           this.addLoan_desc.setText(obj.description)
           this.addLoan_date.setText(obj.date)
           update_key=obj.id.toString()
       }
        else if(type==2)
       {
           val obj=intent.getSerializableExtra("object") as DraftData
           this.addLoan_title.setText(obj.title)
           this.addLoan_amount.setText(obj.amoount.toString())
           this.addLoan_desc.setText(obj.description)
           this.addLoan_date.setText(obj.date)
           update_key=obj.id.toString()
       }


        val options = arrayOf("Borrow","Lend")
        val options_st = arrayOf("Pending", "Paid")

        val adapter1=ArrayAdapter(this,android.R.layout.simple_list_item_1,options)
        adapter1.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line)
        addLoan_spinner.adapter=adapter1
        addLoan_spinner1.adapter= ArrayAdapter(this,android.R.layout.simple_list_item_1,options_st)
        addLoan_spinner.onItemSelectedListener = object: AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {
                TODO("Not yet implemented")
            }

            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                sp1 = parent.getItemAtPosition(position) as String
            }
        }

        addLoan_spinner1.onItemSelectedListener = object: AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {
                TODO("Not yet implemented")
            }

            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                sp2 = parent.getItemAtPosition(position) as String
            }
        }

        val c= Calendar.getInstance()
        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)

        addLoan_datepick.setOnClickListener{
            val dpd = DatePickerDialog(this,
                DatePickerDialog.OnDateSetListener{ _: DatePicker?, myear: Int, mmonth: Int, dayOfMonth: Int ->
                    addLoan_date.setText("$dayOfMonth/$mmonth/$myear")
                },year,month,day)

            dpd.show()
        }


        addLoan_save.setOnClickListener{
            val title=addLoan_title.text.toString()
            val amount= addLoan_amount.text.toString()
            val desc = addLoan_desc.text.toString()
            val date = addLoan_date.text.toString()
            if(title.trim().isEmpty() && amount.trim().isEmpty() && desc.trim().isEmpty() && date.trim().isEmpty())
            {
                Toast.makeText(applicationContext,"All Fields are empty",Toast.LENGTH_LONG).show()
            }
            else
            {
                if(type==2)
                {
                    update_draft()
                }
                else if (type==1)
                {
                    val ref = FirebaseDatabase.getInstance().getReference("Loans")
                    ref.child(update_key).removeValue()
                    add_draft()
                }
                else
                {
                    add_draft()
                }

                val intent= Intent(this,DraftsActivity::class.java)
                intent.putExtra("key",phone)
                startActivity(intent)
            }
        }
        addLoan_add.setOnClickListener{
            val title=addLoan_title.text.toString()
            val amo= addLoan_amount.text.toString()
            val desc = addLoan_desc.text.toString()
            val date = addLoan_date.text.toString()
            if(title.trim().isEmpty())
            {
                Toast.makeText(applicationContext,"Enter Title",Toast.LENGTH_SHORT).show()

            }
            else if(amo.trim().isEmpty())
            {
                Toast.makeText(applicationContext,"Enter Amount",Toast.LENGTH_SHORT).show()

            }
            else if(desc.trim().isEmpty())
            {
                Toast.makeText(applicationContext,"Enter Description",Toast.LENGTH_SHORT).show()

            }
            else if(date.trim().isEmpty())
            {
                Toast.makeText(applicationContext,"Enter Date",Toast.LENGTH_SHORT).show()

            }
            else
            {
                if(type==1)
                {
                    update_loan()
                }
                else if(type==2)
                {
                    val ref = FirebaseDatabase.getInstance().getReference("Drafts")
                    ref.child(update_key).removeValue()
                    add_loan()
                }
                else
                {
                    add_loan()
                }

                val intent =Intent(this,LoansActivity::class.java)
                intent.putExtra("key",phone)
                startActivity(intent)
            }
        }
    }

    private fun add_loan()
    {
        val title=addLoan_title.text.toString()
        val amount= addLoan_amount.text.toString().toInt()
        val desc = addLoan_desc.text.toString()
        val date = addLoan_date.text.toString()

        val ref = FirebaseDatabase.getInstance().getReference("Loans")

        val id= ref.push().key.toString()
        val loan=LoansData(id,sp1,title,amount,desc,date,sp2,phone)

        ref.child(id).setValue(loan)
    }

    private fun add_draft()
    {
        val ref = FirebaseDatabase.getInstance().getReference("Drafts")
        val title=addLoan_title.text.toString()
        val amo= addLoan_amount.text.toString()
        val amount:Int
        amount = if(amo.trim().isEmpty()) {
            0
        } else {
            amo.toInt()
        }
        val desc = addLoan_desc.text.toString()
        val date = addLoan_date.text.toString()
        val id = ref.push().key.toString()
        val drafts=DraftData(id,sp1,title,amount,desc,date,sp2,"Loan",phone)

        ref.child(id).setValue(drafts)
    }

    private fun update_loan()
    {
        val ref = FirebaseDatabase.getInstance().getReference("Loans")
        val title=addLoan_title.text.toString()
        val amount:Int= addLoan_amount.text.toString().toInt()
        val desc = addLoan_desc.text.toString()
        val date = addLoan_date.text.toString()

        val loan=LoansData(update_key,sp1,title,amount,desc,date,sp2,phone)
        ref.child(update_key).setValue(loan)
    }

    private fun update_draft()
    {
        val ref = FirebaseDatabase.getInstance().getReference("Drafts")
        val title=addLoan_title.text.toString()
        val amo= addLoan_amount.text.toString()
        val amount:Int
        amount = if(amo.trim().isEmpty()) {
            0
        } else {
            amo.toInt()
        }
        val desc = addLoan_desc.text.toString()
        val date = addLoan_date.text.toString()

        val drafts=DraftData(update_key,sp1,title,amount,desc,date,sp2,"Loan",phone)
        ref.child(update_key).setValue(drafts)
    }
}
