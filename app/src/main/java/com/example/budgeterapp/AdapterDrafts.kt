package com.example.budgeterapp

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView

class AdapterDrafts(var context: Context, var drafts:ArrayList<DraftData>):BaseAdapter() {

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getCount(): Int {
        return drafts.count()
    }

    override fun getItem(position: Int): Any {
        return drafts[position]
    }

    private class ViewHolder(row : View?){
        var name:TextView = row?.findViewById(R.id.draft_title)as TextView
        var type:TextView= row?.findViewById(R.id.draft_type) as TextView
    }

    override fun getView(position: Int, Convertview: View?, parent: ViewGroup): View {
        val view:View?
        val viewHolder:ViewHolder
        if(Convertview == null)
        {
            val layout = LayoutInflater.from(context)
            view=layout.inflate(R.layout.draft_single,parent,false)
            viewHolder = ViewHolder(view)
            view.tag = viewHolder
        }
        else
        {
            view= Convertview
            viewHolder=view.tag as ViewHolder
        }

        val draft:DraftData = getItem(position) as DraftData
        viewHolder.name.text = draft.title.toString()
        viewHolder.type.text=draft.type.toString()

        return view as View
    }

}