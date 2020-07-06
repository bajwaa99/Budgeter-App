package com.example.budgeterapp

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import java.util.*
import android.widget.DatePicker
import android.app.DatePickerDialog
import android.content.Intent
import android.view.View
import android.widget.AdapterView
import android.widget.Toast
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_add_expense.*

class AddExpenseActivity : AppCompatActivity() {
    lateinit var phone:String
    lateinit var sp2:String
    private lateinit var update_key:String
    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_expense)
        phone=intent.getSerializableExtra("key") as String
        val type=intent.getSerializableExtra("type") as Int
        if(type==1)
        {
            val obj=intent.getSerializableExtra("object") as ExpenseData
            this.addexpense_title.setText(obj.title)
            this.addexpense_amount.setText(obj.amoount.toString())
            this.addexpense_desc.setText(obj.description)
            this.addexpense_date.setText(obj.date)

            update_key=obj.id.toString()
        }
        if(type==2)
        {
            val obj=intent.getSerializableExtra("object") as DraftData
            this.addexpense_title.setText(obj.title)
            this.addexpense_amount.setText(obj.amoount.toString())
            this.addexpense_desc.setText(obj.description)
            this.addexpense_date.setText(obj.date)

            update_key=obj.id.toString()
        }
        val options_st = arrayOf("Pending", "Paid")


        addexpense_spinner1.adapter= ArrayAdapter(this,android.R.layout.simple_list_item_1,options_st)
        addexpense_spinner1.onItemSelectedListener = object: AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {
                TODO("Not yet implemented")
            }

            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                // Display the selected item text on text view
                sp2 = parent.getItemAtPosition(position) as String
            }
        }

        val c= Calendar.getInstance()
        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)

        addexpense_datepick.setOnClickListener{
            val dpd = DatePickerDialog(this,DatePickerDialog.OnDateSetListener{ _: DatePicker?, myear: Int, mmonth: Int, dayOfMonth: Int ->
                var month :Int = mmonth
                month+=1
                addexpense_date.setText("$dayOfMonth/$month/$myear")
            },year,month,day)

            dpd.show()
        }

        addexpense_save.setOnClickListener{
            val title=addexpense_title.text.toString()
            val amount= addexpense_amount.text.toString()
            val desc = addexpense_desc.text.toString()
            val date = addexpense_date.text.toString()
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
                else if(type==1)
                {
                    val ref = FirebaseDatabase.getInstance().getReference("Expenses")
                    ref.child(update_key).removeValue()
                    add_draft()
                }
                else
                {
                    add_draft()
                }

                val intent = Intent(this,DraftsActivity::class.java)
                intent.putExtra("key",phone)
                startActivity(intent)
            }

        }
        
        addexpense_add.setOnClickListener{
            val title=addexpense_title.text.toString()
            val amount= addexpense_amount.text.toString()
            val desc = addexpense_desc.text.toString()
            val date = addexpense_date.text.toString()
            if(title.trim().isEmpty())
            {
                Toast.makeText(applicationContext,"Enter Title", Toast.LENGTH_SHORT).show()
            }
            else if(amount.trim().isEmpty())
            {
                Toast.makeText(applicationContext,"Enter Amount", Toast.LENGTH_SHORT).show()
            }
            else if(desc.trim().isEmpty())
            {
                Toast.makeText(applicationContext,"Enter Description", Toast.LENGTH_SHORT).show()
            }
            else if(date.trim().isEmpty())
            {
                Toast.makeText(applicationContext,"Enter Date", Toast.LENGTH_SHORT).show()
            }
           else
            {
                if(type==1)
                {
                    update_expense()
                }
                else if(type==2)
                {
                    val ref = FirebaseDatabase.getInstance().getReference("Drafts")
                    ref.child(update_key).removeValue()
                    add_expense()
                }
                else
                {
                    add_expense()
                }

                val intent =Intent(this,ExpensesActivity::class.java)
                intent.putExtra("key",phone)
                startActivity(intent)
            }
        }

    }

    private fun add_expense()
    {
        val ref = FirebaseDatabase.getInstance().getReference("Expenses")
        val title=addexpense_title.text.toString()
        val amount:Int= addexpense_amount.text.toString().toInt()
        val desc = addexpense_desc.text.toString()
        val date = addexpense_date.text.toString()
        val id=ref.push().key.toString()
        val Expense=ExpenseData(id,title,amount,desc,date,sp2,phone)

        ref.child(id).setValue(Expense)

    }
    private fun add_draft()
    {
        val ref = FirebaseDatabase.getInstance().getReference("Drafts")
        val title=addexpense_title.text.toString()
        val amo= addexpense_amount.text.toString()
        val amount:Int
        amount = if(amo.trim().isEmpty()) {
            0
        } else {
            amo.toInt()
        }
        val desc = addexpense_desc.text.toString()
        val date = addexpense_date.text.toString()
        val id =ref.push().key.toString()
        val drafts=DraftData(id,null,title,amount,desc,date,sp2,"Expense",phone)

        ref.child(id).setValue(drafts)
    }
    private fun update_expense()
    {
        val ref = FirebaseDatabase.getInstance().getReference("Expenses")
        val title=addexpense_title.text.toString()
        val amount:Int= addexpense_amount.text.toString().toInt()
        val desc = addexpense_desc.text.toString()
        val date = addexpense_date.text.toString()

        val expense=ExpenseData(update_key,title,amount,desc,date,sp2,phone)
        ref.child(update_key).setValue(expense)
    }

    private fun update_draft()
    {
        val ref = FirebaseDatabase.getInstance().getReference("Drafts")
        val title=addexpense_title.text.toString()
        val amo= addexpense_amount.text.toString()
        val amount:Int
        amount = if(amo.trim().isEmpty()) {
            0
        } else {
            amo.toInt()
        }
        val desc = addexpense_desc.text.toString()
        val date = addexpense_date.text.toString()

        val drafts=DraftData(update_key,null,title,amount,desc,date,sp2,"Expense",phone)
        ref.child(update_key).setValue(drafts)
    }
}
