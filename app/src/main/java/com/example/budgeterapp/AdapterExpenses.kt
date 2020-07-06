package com.example.budgeterapp

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.Filter
import android.widget.Filterable
import android.widget.TextView
import java.util.*
import kotlin.collections.ArrayList

class AdapterExpenses(var context: Context, var expenses:ArrayList<ExpenseData>):BaseAdapter(),Filterable {
    private var expenseFull:ArrayList<ExpenseData> = expenses.clone() as ArrayList<ExpenseData>

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getCount(): Int {
        return expenses.count()
    }

    override fun getItem(position: Int): Any {
        return expenses.get(position)
    }

    private class ViewHolder(row : View?){
        var name:TextView = row?.findViewById(R.id.expense_title)as TextView
        var amount:TextView = row?.findViewById(R.id.expense_amount) as TextView
    }





    override fun getView(position: Int, Convertview: View?, parent: ViewGroup): View {
        val view:View?
        val viewHolder:ViewHolder
        if(Convertview == null)
        {
            val layout = LayoutInflater.from(context)
            view=layout.inflate(R.layout.expense_single,parent,false)
            viewHolder = ViewHolder(view)
            view.tag = viewHolder
        }
        else
        {
            view= Convertview
            viewHolder=view.tag as ViewHolder
        }

        val expense:ExpenseData = getItem(position) as ExpenseData
        viewHolder.name.text = expense.title.toString()
        viewHolder.amount.text=expense.amoount.toString()

        return view as View


    }

    private val exampleFilter = object : Filter() {
        override fun performFiltering(constraint: CharSequence?): FilterResults {
            val filteredList= ArrayList<ExpenseData>()
            if(constraint==null || constraint.isEmpty()){
                filteredList.addAll(expenseFull)
            }
            else{
                val filterPattern: String = constraint.toString().toLowerCase(Locale.ROOT).trim()
                for(item:ExpenseData in expenseFull){
                    if(item.title?.toLowerCase(Locale.ROOT)?.contains(filterPattern)!!){
                        filteredList.add(item)
                    }
                }
            }
            val results = FilterResults()
            results.values=filteredList
            return results
        }

        override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
            expenses.clear()
            if (results != null) {
                expenses.addAll(results.values as Collection<ExpenseData>)
            }
            notifyDataSetChanged()
        }

    }
    override fun getFilter(): Filter {
        return exampleFilter
    }

}