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

class AdapterLoans(var context: Context, var loans:ArrayList<LoansData>):BaseAdapter(),Filterable {
    private var loansFull:ArrayList<LoansData> = loans.clone() as ArrayList<LoansData>

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getCount(): Int {
        return loans.count()
    }

    override fun getItem(position: Int): Any {
        return loans[position]
    }

    private class ViewHolder(row : View?){
        var name:TextView = row?.findViewById(R.id.loan_title)as TextView
        var status:TextView = row?.findViewById(R.id.loan_status) as TextView
        var amount:TextView = row?.findViewById(R.id.loan_amount) as TextView
    }

    override fun getView(position: Int, Convertview: View?, parent: ViewGroup): View {
        val view:View?
        val viewHolder:ViewHolder
        if(Convertview == null)
        {
            val layout = LayoutInflater.from(context)
            view=layout.inflate(R.layout.loan_single,parent,false)
            viewHolder = ViewHolder(view)
            view.tag = viewHolder
        }
        else
        {
            view= Convertview
            viewHolder=view.tag as ViewHolder
        }

        val loan:LoansData = getItem(position) as LoansData
        viewHolder.name.text = loan.title.toString()
        viewHolder.amount.text=loan.amoount.toString()
        viewHolder.status.text=loan.status.toString()
        return view as View
    }

    private val exampleFilter = object : Filter() {
        override fun performFiltering(constraint: CharSequence?): FilterResults {
            val filteredList= ArrayList<LoansData>()
            if(constraint==null || constraint.isEmpty()){
                filteredList.addAll(loansFull)
            }
            else{
                val filterPattern: String = constraint.toString().toLowerCase(Locale.ROOT).trim()
                for(item:LoansData in loansFull){
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
            loans.clear()
            if (results != null) {
                loans.addAll(results.values as Collection<LoansData>)
            }
            notifyDataSetChanged()
        }

    }
    override fun getFilter(): Filter {
        return exampleFilter
    }

}